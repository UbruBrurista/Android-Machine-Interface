package com.ubru.brurista;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.ubru.brurista.fragments.BrewingFragment;
import com.ubru.brurista.fragments.DrinkListFragment;
import com.ubru.brurista.fragments.DrinkParametersFragment;
import com.ubru.brurista.fragments.NewDrinkFragment;
import com.ubru.brurista.fragments.NewPhoneFragment;
import com.ubru.brurista.fragments.PhoneNumberFragment;
import com.ubru.brurista.fragments.UserFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    public static final String BREW_TYPE_ESPRESSO = "ES";
    public static final String BREW_TYPE_AMERICANO = "AM";

    public static final int DRINK_LIST_FRAGMENT = 0;
    public static final int BREWING_FRAGMENT = 1;
    public static final int NEW_DRINK_FRAGMENT = 2;
    public static final int DRINK_PARAMETERS_FRAGMENT = 3;
    public static final int NEW_PHONE_FRAGMENT = 4;
    public static final int PHONE_NUMBER_FRAGMENT = 5;

    public static final String EXTRA_STARTING_PAGE = "com.ubru.brurista.EXTRA_STARTING_PAGE";
    public static final String EXTRA_JSON = "com.ubru.brurista.EXTRA_JSON";
    public static final String EXTRA_UUID = "com.ubru.brurista.EXTRA_UUID";

    private List<JSONObject> brewsObjects;
    private String brewName = "";
    private String brewType = BREW_TYPE_ESPRESSO;
    private String brewSize = "Medium";
    private int brewTemp = 85;
    private String uid = "";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        Bundle extras = getIntent().getExtras();
        int startPage = extras.getInt(EXTRA_STARTING_PAGE, 0);
        String json = extras.getString(EXTRA_JSON, "");
        uid = extras.getString(EXTRA_UUID, "");

        if (json != null && !json.isEmpty()) {
            try {
                brewsObjects = new ArrayList<>();
                JSONArray brews = new JSONArray(json);
                for (int i = 0; i < brews.length(); i++) {
                    try {
                        brewsObjects.add(brews.getJSONObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mViewPager.setCurrentItem(startPage, false);
    }

    public List<JSONObject> getBrews() {
        return brewsObjects;
    }

    public void setBrew(int index) {
        JSONObject brew = brewsObjects.get(index);
        brewType = brew.optString("brew_type", "ES");
        brewSize = brew.optString("brew_size", "Medium");
        brewTemp = brew.optInt("brew_temp", 85);
    }

    public int[] getBrewBytes() {
        int typeByte = 0;
        switch(brewType) {
            case "ES":
                typeByte = 1;
                break;
            case "AM":
                typeByte = 2;
                break;
        }

        int sizeByte = 0;
        if (brewSize.equals("Small")) {
            sizeByte = 1;
        } else if (brewSize.equals("Medium")) {
            sizeByte = 2;
        } else if (brewSize.equals("Large")) {
            sizeByte = 3;
        }

        int tempByte = brewTemp - 79;


        int[] bytesToSend = {typeByte, sizeByte, tempByte};
        return bytesToSend;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getUID() {
        return this.uid;
    }

    public void setBrewType(String s) {
        this.brewType = s;
    }

    public String getBrewType() {
        return this.brewType;
    }

    public void setBrewName(String s) {
        this.brewName = s;
    }

    public String getBrewName() {
        return this.brewName;
    }

    public void pageTo(int item) {
        mViewPager.setCurrentItem(item, false);
        mSectionsPagerAdapter.frags[item].onSlideTo();
    }

    public void setBrewParams(String size, int temp) {
        this.brewSize = size;
        this.brewTemp = temp;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private UserFragment[] frags = {
                new DrinkListFragment(),
                new BrewingFragment(),
                new NewDrinkFragment(),
                new DrinkParametersFragment(),
                new NewPhoneFragment(),
                new PhoneNumberFragment(),
        };

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            for (Fragment frag : frags) {
                ((UserFragment) frag).setParent(UserActivity.this);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }
    }
}
