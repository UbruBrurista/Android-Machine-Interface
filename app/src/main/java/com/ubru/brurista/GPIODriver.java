package com.ubru.brurista;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.List;


public class GPIODriver {

    private static String PULSE_PIN = "BCM20";
    private static String START_STOP_PIN = "BCM21";

    private static Gpio PULSE_PGIO = null;
    private static Gpio START_STOP_GPIO = null;

    private static ToggleHandler timerHandler = new ToggleHandler();

    static void init() {
        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> portList = manager.getGpioList();
        if (portList.contains(START_STOP_PIN) && portList.contains(PULSE_PIN)) {
            try {
                PULSE_PGIO = manager.openGpio(START_STOP_PIN);
                START_STOP_GPIO = manager.openGpio(PULSE_PIN);

                PULSE_PGIO.setActiveType(Gpio.ACTIVE_HIGH);
                PULSE_PGIO.setActiveType(Gpio.ACTIVE_HIGH);

                System.out.println("Setting to high");

                write(100);
            } catch (IOException e) {
                System.err.println("OPENING GPIO ERROR");
                e.printStackTrace();
            }
        }

    }

    static void write(int count) {
        timerHandler.startToggle(count);
    }

    static class ToggleHandler extends Handler {

        private int count = 0;
        private int currentCount = 0;
        private boolean isEnabled = false;

        private Runnable toggleRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isEnabled) {
                        PULSE_PGIO.setValue(false);
                        if (currentCount >= count) {
                            return;
                        }
                    } else {
                        PULSE_PGIO.setValue(true);
                        System.out.println(currentCount);
                        currentCount++;
                    }
                    isEnabled = !isEnabled;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ToggleHandler.this.postDelayed(this, 20);
            }
        };

        void startToggle(int count) {
            this.count = count;
            this.currentCount = 0;
            this.isEnabled = false;

            try {
                PULSE_PGIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                START_STOP_GPIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            } catch (Exception e) {
                e.printStackTrace();
            }


            this.post(toggleRunnable);


        }

    }

}
