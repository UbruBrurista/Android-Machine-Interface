package com.ubru.brurista.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubru.brurista.R;
import com.ubru.brurista.UARTDriver;

import java.util.Arrays;

public class BrewingFragment extends UserFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewing, container, false);

        byte[] bytesReceived = getParent().getBrewBytes();

        byte[] bytesToSend = new byte[3];
        bytesToSend[0] = UARTDriver.Commands.START_FULL_CYCLE[0];
        bytesToSend[1] = bytesReceived[0];
        bytesToSend[2] = bytesReceived[1];

        System.out.println("Sending Arduino: " + Arrays.toString(bytesToSend));
//        UARTDriver.sendCommand(bytesToSend);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return rootView;
    }


}
