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

    private Timer timer;
    private String message = "test1";
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            BluetoothDriver.sendMessage(message);
            if (message.equals("test1")) {
                message = "test2";
            } else {
                message = "test1";
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewing, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null) {
                    timer.cancel();
                }
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onSlideTo() {
        BluetoothDriver.init(getActivity());

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 500);
    }
}
