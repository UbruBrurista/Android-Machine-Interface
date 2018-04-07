package com.ubru.brurista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

public class NewDrinkFragment extends UserFragment {

    public NewDrinkFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_drink, container, false);

        Button espressoButton = rootView.findViewById(R.id.brew_button_espresso);
        espressoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getParent().setBrewType(UserActivity.BREW_TYPE_ESPRESSO);
                getParent().pageTo(UserActivity.DRINK_PARAMETERS_FRAGMENT);
            }
        });

        Button americanoButton = rootView.findViewById(R.id.brew_button_americano);
        americanoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getParent().setBrewType(UserActivity.BREW_TYPE_AMERICANO);
                getParent().pageTo(UserActivity.DRINK_PARAMETERS_FRAGMENT);
            }
        });

        return rootView;
    }
}
