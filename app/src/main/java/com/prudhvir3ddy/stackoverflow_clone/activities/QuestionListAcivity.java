package com.prudhvir3ddy.stackoverflow_clone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.fragments.HotFragment;
import com.prudhvir3ddy.stackoverflow_clone.fragments.TagFragment;
import com.prudhvir3ddy.stackoverflow_clone.fragments.WeekFragment;
import com.prudhvir3ddy.stackoverflow_clone.utils.SharedPrefs;

import org.json.JSONObject;

public class QuestionListAcivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPrefs sharedPrefs;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefs = new SharedPrefs(getApplicationContext());
        Intent intent = getIntent();
        final String param1 = intent.getStringExtra("param1");
        final String param2 = intent.getStringExtra("param2");
        final String param3 = intent.getStringExtra("param3");
        final String param4 = intent.getStringExtra("param4");

        toolbar.setTitle("Your #");
        loadFragment(new TagFragment());
        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        toolbar.setTitle("Your #");
                        fragment = new TagFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_dashboard:
                        toolbar.setTitle("Hot");
                        fragment = new HotFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_notifications:
                        toolbar.setTitle("Week");
                        fragment = new WeekFragment();
                        loadFragment(fragment);
                        return true;

                }
                return false;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem m1 = menu.findItem(R.id.nav_camera);
        MenuItem m2 = menu.findItem(R.id.nav_gallery);
        MenuItem m3 = menu.findItem(R.id.nav_manage);
        MenuItem m4 = menu.findItem(R.id.nav_slideshow);
        m1.setTitle(param1);
        m2.setTitle(param2);
        m3.setTitle(param3);
        m4.setTitle(param4);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            sharedPrefs.clearLogin();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            getTags(item.getTitle().toString());
        }
        if (id == R.id.nav_gallery) {
            getTags(item.getTitle().toString());
        }
        if (id == R.id.nav_slideshow) {
            getTags(item.getTitle().toString());
        }
        if (id == R.id.nav_manage) {
            getTags(item.getTitle().toString());
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getTags(String tag) {
        AndroidNetworking.get("https://api.stackexchange.com/2.2/tags?order=desc&sort=activity&inname=" + tag + "&site=stackoverflow")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("respomse", "" + response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}
