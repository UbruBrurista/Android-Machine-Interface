package com.ubru.brurista;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.List;


public class GPIODriver {

    private static String PULSE_PIN = "BCM20";
    private static String START_STOP_PIN = "BCM21";

    private static Gpio PULSE_PGIO = null;
    private static Gpio START_STOP_GPIO = null;

    public static class Commands {

        static int[] DISABLE_ALL = {1};
        public static int[] START_FULL_CYCLE = {2, 1, 3, 1};  // type, size, temp
        static int[] GO_HOME = {3};
        static int[] GO_WORK = {4};
        static int[] GRIND = {5};
        static int[] PUMP = {6};
        static int[] CLEAN_WATER = {7};
    }

    private static ToggleHandler timerHandler = new ToggleHandler();

    static void init() {
        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> portList = manager.getGpioList();
        if (portList.contains(START_STOP_PIN) && portList.contains(PULSE_PIN)) {
            try {
                PULSE_PGIO = manager.openGpio(START_STOP_PIN);
                START_STOP_GPIO = manager.openGpio(PULSE_PIN);

                PULSE_PGIO.setActiveType(Gpio.ACTIVE_HIGH);
                START_STOP_GPIO.setActiveType(Gpio.ACTIVE_HIGH);

                System.out.println("Setting to high");

                //write(50);
            } catch (IOException e) {
                System.err.println("OPENING GPIO ERROR");
                e.printStackTrace();
            }
        }

    }

    public static void setupCallback(GpioCallback callback) {
        try {
            // Initialize the pin as an input
            START_STOP_GPIO.setDirection(Gpio.DIRECTION_IN);
            // Low voltage is considered active
            START_STOP_GPIO.setActiveType(Gpio.ACTIVE_LOW);

            // Register for all state changes
            START_STOP_GPIO.setEdgeTriggerType(Gpio.EDGE_BOTH);
            START_STOP_GPIO.registerGpioCallback(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write (int[] nums, final GpioCallback callback) {
        timerHandler.startToggle(nums, new HandlerCallback(){
            @Override
            public void onSendFinish() {
                try {
                    // Initialize the pin as an input
                    START_STOP_GPIO.setDirection(Gpio.DIRECTION_IN);
                    // Low voltage is considered active
                    START_STOP_GPIO.setActiveType(Gpio.ACTIVE_HIGH);

                    // Register for all state changes
                    START_STOP_GPIO.setEdgeTriggerType(Gpio.EDGE_BOTH);
                    START_STOP_GPIO.registerGpioCallback(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void write(int[] nums) {
        timerHandler.startToggle(nums, null);
    }

    static class ToggleHandler extends Handler {

        private int[] nums = {};
        private int index = 0;
        private int currentCount = 0;
        private boolean isEnabled = false;
        private boolean isStartStopEnabled = false;
        private HandlerCallback callback;

        private Runnable toggleRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isEnabled) {
                        PULSE_PGIO.setValue(false);
                        currentCount++;

                        if (currentCount >= nums[index]) {
                            // Toggle start/stop pin!
                            System.out.println(currentCount);
                            ToggleHandler.this.postDelayed(toggleStartStop, 20);
                            return;
                        }
                    } else {
                        PULSE_PGIO.setValue(true);
                    }
                    isEnabled = !isEnabled;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ToggleHandler.this.postDelayed(this, 20);
            }
        };

        private Runnable toggleStartStop = new Runnable() {
            @Override
            public void run() {
                try {
                    if(isStartStopEnabled) {
                        START_STOP_GPIO.setValue(false);
                        isStartStopEnabled = false;
                        if (currentCount == nums[index]) {
                            index++;
                            if (index < nums.length) {
                                ToggleHandler.this.postDelayed(sendByte, 20);
                            } else {
                                if (callback != null) {
                                    callback.onSendFinish();
                                }
                            }
                        }
                        return;
                    } else {
                        START_STOP_GPIO.setValue(true);
                        if (currentCount == 0) {
                            System.out.println("STARTING");
                        }
                    }
                    isStartStopEnabled = true;
                    ToggleHandler.this.postDelayed(this, 20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        private Runnable sendByte = new Runnable() {
            @Override
            public void run() {
                currentCount = 0;
                isEnabled = false;

                try {
                    PULSE_PGIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                    START_STOP_GPIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // START PIN:
                // Toggle START/STOP pin
                ToggleHandler.this.post(toggleStartStop);
                ToggleHandler.this.postDelayed(toggleRunnable, 20);
            }
        };

        void startToggle(int[] nums, HandlerCallback callback) {
            this.nums = nums;
            this.index = 0;
            this.callback = callback;

            this.post(sendByte);

        }

    }

    public static abstract class HandlerCallback {

        public abstract void onSendFinish();

    }

}
