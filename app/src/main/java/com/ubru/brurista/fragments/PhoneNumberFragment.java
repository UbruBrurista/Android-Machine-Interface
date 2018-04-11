package com.ubru.brurista.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ubru.brurista.MainActivity;
import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberFragment extends UserFragment {

    public PhoneNumberFragment() {}

    private EditText nameView;
    private EditText phoneNumberView;

    private StringRequest stringRequest = new StringRequest(Request.Method.POST,
        "http://ubru.us-east-1.elasticbeanstalk.com/user/",

        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }){

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
            params.put("name", nameView.getText().toString());
            params.put("phone_number", phoneNumberView.getText().toString());
            params.put("uid", getParent().getUID());

            System.out.println(params);

            return params;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        new AlertDialog.Builder(getContext())
            .setMessage("You're all set. Continue to create your first drink!")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                final RequestQueue queue = Volley.newRequestQueue(PhoneNumberFragment.this.getContext());
                queue.add(stringRequest);
                dialogInterface.cancel();
                getParent().pageTo(UserActivity.NEW_DRINK_FRAGMENT);
                }
            })
            .show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phone_number, container, false);

        nameView = rootView.findViewById(R.id.name);
        phoneNumberView = rootView.findViewById(R.id.phone_number);

        rootView.findViewById(R.id.done_button).setOnClickListener(clickListener);

        return rootView;
    }

}
