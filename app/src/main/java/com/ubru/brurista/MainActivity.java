package com.ubru.brurista;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.FileUriExposedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galarzaa.androidthings.Rc522;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;
import com.ubru.brurista.fragments.DrinkListFragment;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         Hide the status bar.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        UARTDriver.init();

        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final String url = "http://buakpsi.com/ubru/drinks/uuid/12345";

        findViewById(R.id.phone_tap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                startUserActivity(UserActivity.DRINK_LIST_FRAGMENT, response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        return;
                    }
                });
                queue.add(stringRequest);

            }
        });

        findViewById(R.id.new_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity(UserActivity.NEW_PHONE_FRAGMENT, null);
            }
        });

        findViewById(R.id.preset_brews_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity(UserActivity.DRINK_LIST_FRAGMENT, null);
            }
        });

        findViewById(R.id.utility_mode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });

        RFIDTask task = new RFIDTask();
        task.execute();
    }

    private void startUserActivity(int page, String json) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_STARTING_PAGE, page);
        intent.putExtra(UserActivity.EXTRA_JSON, json);
        startActivity(intent);
    }

    private class RFIDTask extends AsyncTask <Void, Void, Void> {

        private RFIDDevice mRc522;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PeripheralManager pioService = PeripheralManager.getInstance();
            try {
                PeripheralManager manager = PeripheralManager.getInstance();
                List<String> deviceList = manager.getSpiBusList();
                if (deviceList.isEmpty()) {
                    System.out.println("No SPI bus available on this device.");
                } else {
                    System.out.println("List of available devices: " + deviceList);
                }

            /* Names based on Raspberry Pi 3 */
                SpiDevice spiDevice = pioService.openSpiDevice("SPI0.0");
                System.out.println(spiDevice);

                Gpio resetPin = pioService.openGpio("BCM25");
                mRc522 = new RFIDDevice(spiDevice, resetPin);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("PreExecute complete");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(true){
                boolean success = mRc522.request();
                if(!success){
                    continue;
                }
                success = mRc522.antiCollisionDetect();
                if(!success){
                    continue;
                }
                byte[] uid = mRc522.getUid();
                    System.out.println(RFIDDevice.dataToHexString(uid));
                mRc522.selectTag(uid);
                System.out.println(mRc522.dumpMifare1k());


                byte[] key = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
                byte block = Rc522.getBlockAddress(1,1);
                boolean result = mRc522.authenticateCard(Rc522.AUTH_A, block, key);
                if (!result) {
                    //Authentication failed
                    System.out.println("Authenticaation failed");
                    return null;
                }
                //Buffer to hold read data
                byte[] buffer = new byte[16];
                //Since we're still using the same block, we don't need to authenticate again
                result = mRc522.readBlock(block, buffer);
                if(!result){
                    //Could not read card
                    return null;
                }
                //Stop crypto to allow subsequent readings
                mRc522.stopCrypto();

                break;
            }
            return null;
        }
    }

}
// [80, 102, 68, 30, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
// [-52, 64, 43, -71, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]