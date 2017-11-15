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

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

public class PhoneNumberFragment extends UserFragment {

    public PhoneNumberFragment() {}

    private View phoneNumberView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phone_number, container, false);

        phoneNumberView = rootView.findViewById(R.id.phone_number);

        rootView.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("You're all set. Look out for a text message with " +
                                "instructions to logging onto out web interface!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                getParent().pageTo(UserActivity.NEW_DRINK_FRAGMENT);
                            }
                        })
                        .show();
            }
        });

        return rootView;
    }

}
