package com.prudhvir3ddy.stackoverflow_clone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.utils.Connection;
import com.prudhvir3ddy.stackoverflow_clone.utils.SharedPrefs;
import com.prudhvir3ddy.stackoverflow_clone.utils.Urls;

public class LoginActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button2);
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        if (sharedPrefs.getLogedInKey() != null)
            startActivity(new Intent(getApplicationContext(), UserIntrestActivity.class));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection(getApplicationContext());
                if (connection.isInternet()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.login));
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");


    }

}
