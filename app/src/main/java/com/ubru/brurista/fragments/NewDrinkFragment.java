package com.ubru.brurista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

public class NewDrinkFragment extends UserFragment {

    public NewDrinkFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_drink, container, false);

        final EditText brewName = rootView.findViewById(R.id.name);

//        InputMethodManager inputMethodManager =
//                (InputMethodManager) getParent().getSystemService(Context.INPUT_METHOD_SERVICE);
        brewName.performClick();
//        inputMethodManager.toggleSoftInputFromWindow(
//                brewName.getApplicationWindowToken(),
//                InputMethodManager.SHOW_FORCED, 0);

        Button espressoButton = rootView.findViewById(R.id.brew_button_espresso);
        espressoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParent().setBrewName(brewName.getText().toString());
                getParent().setBrewType(UserActivity.BREW_TYPE_ESPRESSO);
                getParent().pageTo(UserActivity.DRINK_PARAMETERS_FRAGMENT);
            }
        });

        Button americanoButton = rootView.findViewById(R.id.brew_button_americano);
        americanoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParent().setBrewName(brewName.getText().toString());
                getParent().setBrewType(UserActivity.BREW_TYPE_AMERICANO);
                getParent().pageTo(UserActivity.DRINK_PARAMETERS_FRAGMENT);
            }
        });

        return rootView;
    }
}
