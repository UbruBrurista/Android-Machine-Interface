package com.ubru.brurista.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkParametersFragment extends UserFragment {

    private int[] parameterValues = {2, 80};
    private int[] parameterDeltas = {1, 1};
    private int[] parameterUnits = {
            0,
            R.string.unit_degrees,
    };

    private int[][] parametersViews = {
            { R.id.size_increment, R.id.size_decrement, R.id.size_text },
            { R.id.temp_increment, R.id.temp_decrement, R.id.temp_text } ,
    };

    public DrinkParametersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_drink_parameters, container, false);

        for (int i = 0; i < parameterValues.length; i++) {
            initializeParameter(rootView, i);
        }

        rootView.findViewById(R.id.start_brew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TCP communication to RPi
                TextView tempText = (TextView) rootView.findViewById(R.id.temp_text);
                final int temp = Integer.valueOf(tempText.getText().subSequence(0,2).toString());

                TextView sizeText = (TextView) rootView.findViewById(R.id.size_text);
                final String size = sizeText.getText().toString();

                getParent().setBrewParams(size, temp);

                StringRequest postRequest = new StringRequest(Request.Method.POST,
                        "http://ubru.us-east-1.elasticbeanstalk.com/drinks/",

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                System.out.println("Success");
                                getParent().pageTo(UserActivity.BREWING_FRAGMENT);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}

                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("uuid", getParent().getUID());
                        params.put("name", getParent().getBrewName());
                        params.put("brew_type", getParent().getBrewType());
                        params.put("brew_temp", String.valueOf(temp));
                        params.put("brew_size", size);

                        System.out.println(params);

                        return params;
                    }
                };

                final RequestQueue queue = Volley.newRequestQueue(DrinkParametersFragment.this.getContext());
                queue.add(postRequest);

            }
        });

        return rootView;
    }
    
    private void initializeParameter(final View root, final int parametersIndex) {
        final int[] views = parametersViews[parametersIndex];
        final int unit = parameterUnits[parametersIndex];

        String paramText = "";
        if (parametersIndex == 0) {
            paramText = "Medium";
        } else {
            paramText = getString(unit, parameterValues[parametersIndex]);
        }
        ((TextView) root.findViewById(views[2])).setText(paramText);

        // Increment Listener
        root.findViewById(views[0]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((parametersIndex == 0 && parameterValues[parametersIndex] == 3)
                        || (parametersIndex == 1 && parameterValues[parametersIndex] == 90))
                    return;

                parameterValues[parametersIndex] += parameterDeltas[parametersIndex];
                String paramText = "";
                if (parametersIndex == 0) {
                    switch (parameterValues[parametersIndex]) {
                        case 1:
                            paramText = "Small";
                            break;
                        case 2:
                            paramText = "Medium";
                            break;
                        case 3:
                            paramText = "Large";
                            break;
                    }
                } else {
                    paramText = getString(unit, parameterValues[parametersIndex]);
                }
                ((TextView) root.findViewById(views[2])).setText(paramText);
            }
        });

        // Decrement Listener
        root.findViewById(views[1]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((parametersIndex == 0 && parameterValues[parametersIndex] == 1)
                        || (parametersIndex == 1 && parameterValues[parametersIndex] == 80))
                    return;


                parameterValues[parametersIndex] -= parameterDeltas[parametersIndex];
                String paramText = "";
                if (parametersIndex == 0) {
                    switch (parameterValues[parametersIndex]) {
                        case 1:
                            paramText = "Small";
                            break;
                        case 2:
                            paramText = "Medium";
                            break;
                        case 3:
                            paramText = "Large";
                            break;
                    }
                } else {
                    paramText = getString(unit, parameterValues[parametersIndex]);
                }
                ((TextView) root.findViewById(views[2])).setText(paramText);
            }
        });
    }



}
