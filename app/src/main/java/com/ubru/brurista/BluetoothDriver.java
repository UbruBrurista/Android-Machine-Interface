package com.ubru.brurista;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Vinay on 11/15/17.
 */

public class BluetoothDriver {

    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothSocket mSocket;
    private static BluetoothDevice mDevice = null;

    static Handler handler;

    public static void init(Activity activity) {
        if (mBluetoothAdapter != null) {
            return;
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();

        if(mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices != null && pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("BlueZ 5.47"))
                {
                    Log.e("Ubru",device.getName());
                    mDevice = device;
                    break;
                }
            }
        }
    }

    public static void sendMessage(String msg) {
        (new Thread(new BluetoothMessage(msg))).start();
    }

    public static class BluetoothMessage implements Runnable {

        private String msg;

        final byte delimiter = 33; // delimiter is an exclamation point (!)
        int readBufferPosition = 0;

        private BluetoothMessage(String msg) {
            this.msg = msg;
        }

        private int sendBluetooth(){
            System.out.println("Sending message: " + this.msg);

            UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e492929"); //Standard SerialPortService ID
            try {
                if(mDevice != null) {
                    mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
                    if (!mSocket.isConnected()) {
                        mSocket.connect();
                    }

                    OutputStream mmOutputStream = mSocket.getOutputStream();
                    mmOutputStream.write(this.msg.getBytes());
                    return 0;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;

        }

        public void run()
        {
            if(sendBluetooth() == 0) {
                while (!Thread.currentThread().isInterrupted()) {
                    int bytesAvailable;
                    boolean workDone = false;

                    try {


                        final InputStream mmInputStream;
                        mmInputStream = mSocket.getInputStream();
                        bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {

                            byte[] packetBytes = new byte[bytesAvailable];
                            Log.e("Ubru", "bytes available");
                            byte[] readBuffer = new byte[1024];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    Log.e("Ubru", "msg: " + data);

                                    //The variable data now contains our full command
                                    handler.post(new Runnable() {
                                        public void run() {
                                            System.out.println(data);
                                        }
                                    });

                                    workDone = true;
                                    break;


                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }

                            if (workDone) {
                                Log.e("Ubru", "socket closed");
                                mSocket.close();
                                break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    };

}
