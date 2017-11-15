package com.ubru.brurista.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

public class NewPhoneFragment extends UserFragment {

    public NewPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_phone, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParent().pageTo(UserActivity.PHONE_NUMBER_FRAGMENT);
            }
        });

        return rootView;
    }

}
