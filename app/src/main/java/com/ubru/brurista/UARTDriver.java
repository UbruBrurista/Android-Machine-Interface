package com.ubru.brurista;

import android.util.Log;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UARTDriver {

    private static final int BAUD_RATE = 9600;
    private static UartDevice mArduino;

    public static class Commands {
        public static byte[] START_FULL_CYCLE = {1, 1, 2, 85};
        static byte[] GO_HOME = {2};
        static byte[] GO_WORK = {3};
        static byte[] GRIND = {4};
        static byte[] PUMP = {5};
        static byte[] DISABLE_ALL = {100};
    }

    static void init() {
        PeripheralManager mPeripheralManager = PeripheralManager.getInstance();

        try {
            mArduino = mPeripheralManager.openUartDevice("MINIUART");
            mArduino.setBaudrate(BAUD_RATE);
            mArduino.registerUartDeviceCallback(mUartCallback);
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
            System.err.println("FAILED to write....");
        }
    }
    private static void readUartBuffer(UartDevice uart) throws IOException {
        // Maximum amount of data to read at one time
        final int maxCount = 1;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            System.out.println("Read " + count + " bytes from peripheral");
        }
    }


    private static UartDeviceCallback mUartCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            // Read available data from the UART device
            try {
                readUartBuffer(uart);
            } catch (IOException e) {
                System.out.println("Unable to access UART device");
                e.printStackTrace();
            }


            // Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            System.err.println(uart + ": Error event " + error);
        }
    };

}
