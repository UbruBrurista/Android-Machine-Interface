package com.ubru.brurista.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkListFragment extends UserFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drink_list, container, false);
        ListView list = rootView.findViewById(R.id.drink_list_view);
        List<JSONObject> brews = getParent().getBrews();
        list.setAdapter(new DrinkListAdapter(
                this.getContext(), android.R.layout.simple_list_item_1, brews));

        Button customDrink = rootView.findViewById(R.id.custom_drink_button);
        customDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParent().pageTo(UserActivity.NEW_DRINK_FRAGMENT);
            }
        });


        return rootView;
    }

    private class DrinkListAdapter extends ArrayAdapter<JSONObject> {

        List<JSONObject> drinks;

        public DrinkListAdapter(Context context, int resource, List<JSONObject> objects) {
            super(context, resource, objects);
            this.drinks = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = (TextView) vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            v.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT));
            v.setText(this.drinks.get(position).optString("name"));
            v.setTextColor(Color.WHITE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getParent().pageTo(UserActivity.BREWING_FRAGMENT);
                }
            });

            return v;
        }

    }
}
