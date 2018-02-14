package com.ubru.brurista.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;
import com.ubru.brurista.TcpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkParametersFragment extends UserFragment {

    private int[] parameterValues = {93, 9, 30};
    private int[] parameterDeltas = {1, 1, 1};
    private int[] parameterUnits = {
            R.string.unit_degrees,
            R.string.unit_bars,
            R.string.unit_seconds
    };

    private int[][] parametersViews = {
            { R.id.temp_increment, R.id.temp_decrement, R.id.temp_text } ,
            { R.id.pressure_increment, R.id.pressure_decrement, R.id.pressure_text },
            { R.id.time_increment, R.id.time_decrement, R.id.time_text }
    };

    public DrinkParametersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drink_parameters, container, false);

        for (int i = 0; i < parameterValues.length; i++) {
            initializeParameter(rootView, i);
        }

        rootView.findViewById(R.id.start_brew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCP communication to RPi
                getParent().pageTo(UserActivity.BREWING_FRAGMENT);
            }
        });

        return rootView;
    }
    
    private void initializeParameter(final View root, final int parametersIndex) {
        final int[] views = parametersViews[parametersIndex];
        final int unit = parameterUnits[parametersIndex];

        // Initial Value
        String paramText = getString(unit, parameterValues[parametersIndex]);
        ((TextView) root.findViewById(views[2])).setText(paramText);

        // Increment Listener
        root.findViewById(views[0]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameterValues[parametersIndex] += parameterDeltas[parametersIndex];
                String paramText = getString(unit, parameterValues[parametersIndex]);
                ((TextView) root.findViewById(views[2])).setText(paramText);
            }
        });

        // Decrement Listener
        root.findViewById(views[1]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameterValues[parametersIndex] -= parameterDeltas[parametersIndex];
                String paramText = getString(unit, parameterValues[parametersIndex]);
                ((TextView) root.findViewById(views[2])).setText(paramText);
            }
        });
    }



}
