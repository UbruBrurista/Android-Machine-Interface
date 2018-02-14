package com.ubru.brurista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubru.brurista.BluetoothDriver;
import com.ubru.brurista.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BrewingFragment extends UserFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewing, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getActivity().finish();
            }
        });

        return rootView;
    }
}
