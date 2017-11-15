package com.ubru.brurista.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubru.brurista.R;
import com.ubru.brurista.UserActivity;

public class UserFragment extends Fragment {

    public UserFragment() {}

    protected UserActivity parentAdapter;
    public void setParent(UserActivity parent) {
        this.parentAdapter = parent;
    }
    protected UserActivity getParent() {
        return this.parentAdapter;
    }

    public void onSlideTo() {
        return;
    }

}
