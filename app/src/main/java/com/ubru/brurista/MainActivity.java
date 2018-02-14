package com.ubru.brurista;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ubru.brurista.fragments.DrinkListFragment;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the status bar.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final String url ="http://buakpsi.com/ubru/drinks/uuid/12345";


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
    }

    private void startUserActivity(int page, String json) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_STARTING_PAGE, page);
        intent.putExtra(UserActivity.EXTRA_JSON, json);
        startActivity(intent);
    }

}
