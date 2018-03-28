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

    public void test1(View v) { UARTDriver.sendCommand(UARTDriver.Commands.START_FULL_CYCLE); }

    public void test2(View v) { UARTDriver.sendCommand(UARTDriver.Commands.GO_HOME); }

    public void test3(View v) { UARTDriver.sendCommand(UARTDriver.Commands.GO_WORK); }

    public void test4(View v) { }

    public void test5(View v) { }

    public void test6(View v) { }

    public void test7(View v) { }

    public void test8(View v) { }

    public void test9(View v) { }

}
