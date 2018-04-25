package com.ubru.brurista;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class UtilityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
    }

    public void runFullCycle(View v) {
        GPIODriver.write(GPIODriver.Commands.START_FULL_CYCLE);
    }

    public void goHome(View v) {
        GPIODriver.write(GPIODriver.Commands.GO_HOME);
    }

    public void goWork(View v) {
        GPIODriver.write(GPIODriver.Commands.GO_WORK);
    }

    public void runGrinder(View v) {
        GPIODriver.write(GPIODriver.Commands.GRIND);

    }

    public void runPump(View v) {
        GPIODriver.write(GPIODriver.Commands.PUMP);
    }

    public void disable(View v) {
        GPIODriver.write(GPIODriver.Commands.DISABLE_ALL);
    }

    public void cleanWater(View v) {
        GPIODriver.write(GPIODriver.Commands.CLEAN_WATER);
    }

    public void finish(View v) { this.finish(); }

}
