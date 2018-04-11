package com.ubru.brurista;

import android.os.AsyncTask;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;
import java.util.Arrays;

public abstract class RFIDTask extends AsyncTask<Void, Void, String> {

    private RFIDDevice mDevice;
    private Gpio resetPin;
    private SpiDevice spiDevice;
    private boolean isRunning = true;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
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

        String uid = "";

        while(isRunning){
            boolean success = mDevice.request();
            if(!success){
                continue;
            }

            success = mDevice.antiCollisionDetect();
            if(!success){
                continue;
            }

            byte[] uidBytes = mDevice.getUid();
            System.out.println(Arrays.toString(uidBytes));
            uid = RFIDDevice.dataToHexString(uidBytes);
            break;
        }

        try {
            this.spiDevice.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    @Override
    protected void onPostExecute(String uid) {
        super.onPostExecute(uid);

        System.out.println("MainActivity: " + uid);
        if (uid != null) {
            postExecuteCallback(uid);
        }

    }

    public void stop() {
        this.isRunning = false;
    }

    public abstract void postExecuteCallback(String uid);
}