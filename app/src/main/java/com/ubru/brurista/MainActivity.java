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

    final String BASE_URL = "http://buakpsi.com/ubru/drinks/uuid/";
    RFIDTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         Hide the status bar.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        UARTDriver.init();

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

        task = new RFIDTask();
        task.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null)
            task.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (task != null)
            task.cancel(true);

        task = new RFIDTask();
        task.execute();

    }

    private void startUserActivity(int page, String json) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_STARTING_PAGE, page);
        intent.putExtra(UserActivity.EXTRA_JSON, json);
        startActivity(intent);
    }

    private void getUserDrinks(String uid) {
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = BASE_URL + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startUserActivity(UserActivity.DRINK_LIST_FRAGMENT, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(stringRequest);
    }

    private class RFIDTask extends AsyncTask <Void, Void, String> {

        private RFIDDevice mDevice;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PeripheralManager pioService = PeripheralManager.getInstance();
            try {
                SpiDevice spiDevice = pioService.openSpiDevice("SPI0.0");
                System.out.println(spiDevice);

                Gpio resetPin = pioService.openGpio("BCM25");
                mDevice = new RFIDDevice(spiDevice, resetPin);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            while(true){
                boolean success = mDevice.request();
                if(!success){
                    continue;
                }

                success = mDevice.antiCollisionDetect();
                if(!success){
                    continue;
                }

                byte[] uid = mDevice.getUid();
                return RFIDDevice.dataToHexString(uid);


//                mDevice.selectTag(uid);
//                System.out.println(mDevice.dumpMifare1k());
//                byte[] key = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
//                byte block = Rc522.getBlockAddress(1,1);
//                boolean result = mDevice.authenticateCard(Rc522.AUTH_A, block, key);
//                if (!result) {
//                    //Authentication failed
//                    System.out.println("Authenticaation failed");
//                    return null;
//                }
//                //Buffer to hold read data
//                byte[] buffer = new byte[16];
//                //Since we're still using the same block, we don't need to authenticate again
//                result = mDevice.readBlock(block, buffer);
//                if(!result){
//                    //Could not read card
//                    return null;
//                }
//                //Stop crypto to allow subsequent readings
//                mDevice.stopCrypto();
            }
        }

        @Override
        protected void onPostExecute(String uid) {
            super.onPostExecute(uid);
            MainActivity.this.getUserDrinks(uid);
        }
    }

}
// [80, 102, 68, 30, 108, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
// [-52, 64, 43, -71, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]