package com.ubru.brurista.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;
import com.ubru.brurista.R;
import com.ubru.brurista.RFIDDevice;
import com.ubru.brurista.RFIDTask;
import com.ubru.brurista.UserActivity;

import java.io.IOException;
import java.util.Arrays;

public class NewPhoneFragment extends UserFragment {

    static RFIDTask task;

    public NewPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPause() {
        super.onPause();
        if (task != null)
            task.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (task != null)
            task.cancel(true);


        task = new RFIDTask();
        task.setFragment(this);
        task.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_phone, container, false);
    }

    static class RFIDTask extends AsyncTask<Void, Void, String> {

        private RFIDDevice mDevice;
        private Gpio resetPin;
        private SpiDevice spiDevice;
        private UserFragment frag;
        private boolean isRunning = true;

        void setFragment(UserFragment f) {
            this.frag = f;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String uid = "";
            if (mDevice == null) {
                PeripheralManager pioService = PeripheralManager.getInstance();
                try {
                    spiDevice = pioService.openSpiDevice("SPI0.0");

                    resetPin = pioService.openGpio("BCM25");
                    mDevice = new RFIDDevice(spiDevice, resetPin);

                    resetPin.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            while(isRunning){
                boolean success = mDevice.request();
                if (!success) {
                    continue;
                }

                success = mDevice.antiCollisionDetect();
                if (!success) {
                    continue;
                }

                byte[] uidBytes = mDevice.getUid();
                System.out.println(Arrays.toString(uidBytes));
                uid = RFIDDevice.dataToHexString(uidBytes);
                break;
            }

            try {
                spiDevice.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uid;
        }

        @Override
        protected void onPostExecute(String uid) {
            super.onPostExecute(uid);
            System.out.println(uid);


            this.frag.getParent().setUID(uid);
            this.frag.getParent().pageTo(UserActivity.PHONE_NUMBER_FRAGMENT);

        }
    }

}
