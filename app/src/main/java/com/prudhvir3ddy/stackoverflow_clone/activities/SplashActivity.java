package com.prudhvir3ddy.stackoverflow_clone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {
    private SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPrefs = new SharedPrefs(this);
        if (sharedPrefs.getLogedInKey() != null)
            startActivity(new Intent(getApplicationContext(), UserIntrestActivity.class));
        else
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
