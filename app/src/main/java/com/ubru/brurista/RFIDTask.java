package com.ubru.brurista;

import android.os.AsyncTask;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;

abstract class RFIDTask extends AsyncTask<Void, Void, String> {

    private RFIDDevice mDevice;
    private Gpio resetPin;
    private SpiDevice spiDevice;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
    }

    @Override
    protected String doInBackground(Void... voids) {
        while(true){
            boolean success = mDevice.request();
            if(!success){
                continue;
            }

            success = mDevice.antiCollisionDetect();
            if(!success){
                continue;
            }

            byte[] uid = mDevice.getUid();
            return RFIDDevice.dataToHexString(uid);


//                mDevice.selectTag(uid);
//                System.out.println(mDevice.dumpMifare1k());
//                byte[] key = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
//                byte block = Rc522.getBlockAddress(1,1);
//                boolean result = mDevice.authenticateCard(Rc522.AUTH_A, block, key);
//                if (!result) {
//                    //Authentication failed
//                    System.out.println("Authenticaation failed");
//                    return null;
//                }
//                //Buffer to hold read data
//                byte[] buffer = new byte[16];
//                //Since we're still using the same block, we don't need to authenticate again
//                result = mDevice.readBlock(block, buffer);
//                if(!result){
//                    //Could not read card
//                    return null;
//                }
//                //Stop crypto to allow subsequent readings
//                mDevice.stopCrypto();
        }
    }

    @Override
    protected void onPostExecute(String uid) {
        super.onPostExecute(uid);

        try {
            spiDevice.close();
//            resetPin.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println(uid);
        if (uid != null) {
            postExecuteCallback(uid);
        }

    }

    abstract void postExecuteCallback(String uid);
}