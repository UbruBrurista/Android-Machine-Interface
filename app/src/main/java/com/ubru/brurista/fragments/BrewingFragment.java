package com.ubru.brurista.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.ubru.brurista.GPIODriver;
import com.ubru.brurista.GPIODriver.HandlerCallback;
import com.ubru.brurista.R;
import com.ubru.brurista.UARTDriver;

import java.util.Arrays;

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

    @Override
    public void onSlideTo() {
        super.onSlideTo();

        int[] bytesReceived = getParent().getBrewBytes();

        int[] bytesToSend = new int[4];
        bytesToSend[0] = GPIODriver.Commands.START_FULL_CYCLE[0];
        bytesToSend[1] = bytesReceived[0];
        bytesToSend[2] = bytesReceived[1];
        bytesToSend[3] = bytesReceived[2];

        System.out.println("Sending Arduino: " + Arrays.toString(bytesToSend));

        GPIODriver.write(bytesToSend, new GpioCallback() {
            @Override
            public boolean onGpioEdge(Gpio gpio) {
                System.out.println("CALLBACK------");
                BrewingFragment.this.getActivity().finish();
                return false;
            }
        });
    }
}
