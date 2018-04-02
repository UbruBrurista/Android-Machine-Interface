package com.ubru.brurista;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbDevice;


import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vinay on 3/28/18.
 */

public class UARTDriver {

    private static final int BAUD_RATE = 9600;
    private static PeripheralManager mPeripheralManager;
    private static UartDevice mArduino;

    public static class Commands {
        public static byte[] START_FULL_CYCLE = {1};
        public static byte[] GO_HOME = {2};
        public static byte[] GO_WORK = {3};
        public static byte[] GRIND = {4};
        public static byte[] PUMP = {5};
        public static byte[] DISABLE_ALL = {100};
    }

    public static void init() {
        mPeripheralManager = PeripheralManager.getInstance();

       /* See available devices */
      /* List<String> deviceList = mPeripheralManager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            System.out.println("No device");
        } else {
            System.out.println("------> Available devices: ");
          for (String s : deviceList) {
                System.out.println(s);
             }
        }*/


        try {
            mArduino = mPeripheralManager.openUartDevice("MINIUART");
            mArduino.setBaudrate(BAUD_RATE);
            System.out.println("------> Connected to " + mArduino.getName());
        } catch (IOException e) {
            System.err.println("Could not connect!");
            e.printStackTrace();
        }
    }

    public static void sendCommand(byte[] command) {
        try {
            mArduino.write(command, command.length);
            System.out.println("Writing to arduino: " + Arrays.toString(command));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED to write....");
        }
    }
}
