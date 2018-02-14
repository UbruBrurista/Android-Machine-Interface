package com.ubru.brurista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubru.brurista.BluetoothDriver;
import com.ubru.brurista.ConnectTask;
import com.ubru.brurista.R;
import com.ubru.brurista.TcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BrewingFragment extends UserFragment {

    TcpClient mTcpClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewing, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConnectTask().execute("");

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("drink");
                }

                getActivity().finish();
            }
        });

        return rootView;
    }
}
