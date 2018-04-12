package com.ubru.brurista.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.ubru.brurista.GPIODriver;
import com.ubru.brurista.GPIODriver.HandlerCallback;
import com.ubru.brurista.R;
import com.ubru.brurista.UARTDriver;
import com.ubru.brurista.UserActivity;

import java.util.Arrays;

public class BrewingFragment extends UserFragment {

    private String[] STAGES = {
            "Preheating",
            "Grinding",
            "Brewing",
            "Cleaning Up"
    }
    private int stageIndex = 0;
    private TextView stageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewing, container, false);

        stageView = rootView.findViewById(R.id.brewing_stage);

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

                stageIndex++;
                if (stageIndex >= STAGES.length) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Enjoy your drink!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    BrewingFragment.this.getActivity().finish();
                                }
                            })
                            .show();
                } else {
                    stageView.setText(STAGES[stageIndex] + "...");
                }


                return false;
            }
        });
    }
}
