package com.ubru.brurista;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ubru.brurista.fragments.DrinkListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the status bar.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        findViewById(R.id.phone_tap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity(UserActivity.DRINK_LIST_FRAGMENT);
            }
        });

        findViewById(R.id.new_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity(UserActivity.NEW_PHONE_FRAGMENT);
            }
        });

        findViewById(R.id.preset_brews_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity(UserActivity.DRINK_LIST_FRAGMENT);
            }
        });
    }

    private void startUserActivity(int page) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_STARTING_PAGE, page);
        startActivity(intent);
    }
}
