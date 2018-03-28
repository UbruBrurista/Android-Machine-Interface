package com.ubru.brurista;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;


import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Vinay on 3/28/18.
 */

public class UARTDriver {

    private static final int BAUD_RATE = 9600;
    private static PeripheralManager mPeripheralManager;
    private static UartDevice mArduino;

    public static class Commands {
        public static byte[] START_FULL_CYCLE = {1, '\n'};
        public static byte[] GO_HOME = {2, '\n'};
        public static byte[] GO_WORK = {3, '\n'};
    }

    public static void init() {
        mPeripheralManager = PeripheralManager.getInstance();

        try {
            mArduino = mPeripheralManager.openUartDevice("UART0");
            mArduino.setBaudrate(BAUD_RATE);
            System.out.println("Connected to " + mArduino.getName());
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
