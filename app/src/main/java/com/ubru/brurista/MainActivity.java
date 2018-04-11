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

    final String BASE_URL = "http://ubru.us-east-1.elasticbeanstalk.com/drinks/uuid/";
    RFIDTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         Hide the status bar.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

//        UARTDriver.init();
        GPIODriver.init();

        findViewById(R.id.new_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.stop();
                task.cancel(false);
                startUserActivity(UserActivity.NEW_PHONE_FRAGMENT, null, null);
            }
        });

        findViewById(R.id.preset_brews_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.stop();
                task.cancel(false);
                startUserActivity(UserActivity.DRINK_LIST_FRAGMENT, null, null);
            }
        });

        findViewById(R.id.utility_mode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.stop();
                task.cancel(false);
                Intent intent = new Intent(MainActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.stop();
            task.cancel(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (task != null) {
            task.stop();
            task.cancel(false);
        }

        task = new RFIDTask() {
            @Override
            public void postExecuteCallback(String uid) {
                getUserDrinks(uid);
            }
        };
        task.execute();

    }

    private void startUserActivity(int page, String json, String uuid) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_STARTING_PAGE, page);
        intent.putExtra(UserActivity.EXTRA_JSON, json);
        intent.putExtra(UserActivity.EXTRA_UUID, uuid);
        startActivity(intent);
    }

    private void getUserDrinks(final String uid) {
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = BASE_URL + uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        startUserActivity(UserActivity.DRINK_LIST_FRAGMENT, response, uid);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(stringRequest);
    }

}