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
import com.prudhvir3ddy.stackoverflow_clone.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuestionListAcivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPrefs sharedPrefs;
    private BottomNavigationView bottomNavigationView;
    String tagurl;
    String tags[];
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        tags = new String[5];
        setSupportActionBar(toolbar);
        sharedPrefs = new SharedPrefs(getApplicationContext());
        Intent intent = getIntent();
        final String param1 = intent.getStringExtra("param1");
        final String param2 = intent.getStringExtra("param2");
        final String param3 = intent.getStringExtra("param3");
        final String param4 = intent.getStringExtra("param4");
        tagurl = Urls.tagged + param1 + ";" + param2 + ";" + param3 + ";" + param4 + "&site=stackoverflow";
        toolbar.setTitle("Your #");
        Bundle bundle = new Bundle();
        bundle.putString("url", tagurl);
        Fragment fragment = new TagFragment();
        fragment.setArguments(bundle);
        loadFragment(fragment);
        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Bundle bundle = new Bundle();
                        bundle.putString("url", tagurl);
                        toolbar.setTitle("Your #");
                        fragment = new TagFragment();
                        fragment.setArguments(bundle);
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

        navigationView = findViewById(R.id.nav_view);
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
        tagurl = Urls.tagged + item.getTitle().toString() + "&site=stackoverflow";
        Bundle bundle = new Bundle();
        bundle.putString("url", tagurl);
        Fragment fragment = new TagFragment();
        fragment.setArguments(bundle);
        loadFragment(fragment);
        if (id == R.id.nav_camera) {
            getTags(item.getTitle().toString(), "camera");
        }
        if (id == R.id.nav_gallery) {
            getTags(item.getTitle().toString(), "gallery");

        }
        if (id == R.id.nav_slideshow) {
            getTags(item.getTitle().toString(), "slide");
        }
        if (id == R.id.nav_manage) {
            getTags(item.getTitle().toString(), "manage");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getTags(String tag, final String id) {
        AndroidNetworking.get("https://api.stackexchange.com/2.2/tags?order=desc&sort=activity&inname=" + tag + "&site=stackoverflow")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray items = response.optJSONArray("items");

                        for (int i = 0; i < 5; i++) {
                            JSONObject object = items.optJSONObject(i);
                            tags[i] = object.optString("name");
                        }
                        if (id.equals("camera")) {
                            navigationView.getMenu().setGroupVisible(R.id.one, true);
                            navigationView.getMenu().setGroupVisible(R.id.SetupGroup, false);
                            navigationView.getMenu().setGroupVisible(R.id.Setup, false);
                            navigationView.getMenu().setGroupVisible(R.id.Setupfd, false);
                            navigationView.getMenu().findItem(R.id.two).setTitle("# " + tags[0]);
                            navigationView.getMenu().findItem(R.id.three).setTitle("# " + tags[1]);
                            navigationView.getMenu().findItem(R.id.four).setTitle("# " + tags[2]);
                            navigationView.getMenu().findItem(R.id.twenty).setTitle("# " + tags[3]);
                            navigationView.getMenu().findItem(R.id.twenty_one).setTitle("# " + tags[4]);
                        } else if (id.equals("gallery")) {
                            navigationView.getMenu().setGroupVisible(R.id.SetupGroup, true);
                            navigationView.getMenu().setGroupVisible(R.id.one, false);
                            navigationView.getMenu().setGroupVisible(R.id.Setup, false);
                            navigationView.getMenu().setGroupVisible(R.id.Setupfd, false);
                            navigationView.getMenu().findItem(R.id.five).setTitle("# " + tags[0]);
                            navigationView.getMenu().findItem(R.id.six).setTitle("# " + tags[1]);
                            navigationView.getMenu().findItem(R.id.seven).setTitle("# " + tags[2]);
                            navigationView.getMenu().findItem(R.id.eighteen).setTitle("# " + tags[3]);
                            navigationView.getMenu().findItem(R.id.nineteen).setTitle("# " + tags[4]);
                        } else if (id.equals("slide")) {
                            navigationView.getMenu().setGroupVisible(R.id.Setup, true);
                            navigationView.getMenu().setGroupVisible(R.id.Setupfd, false);
                            navigationView.getMenu().setGroupVisible(R.id.SetupGroup, false);
                            navigationView.getMenu().setGroupVisible(R.id.one, false);
                            navigationView.getMenu().findItem(R.id.eight).setTitle("# " + tags[0]);
                            navigationView.getMenu().findItem(R.id.nine).setTitle("# " + tags[1]);
                            navigationView.getMenu().findItem(R.id.ten).setTitle("# " + tags[2]);
                            navigationView.getMenu().findItem(R.id.sixteen).setTitle("# " + tags[3]);
                            navigationView.getMenu().findItem(R.id.seventeen).setTitle("# " + tags[4]);
                        } else if (id.equals("manage")) {
                            navigationView.getMenu().setGroupVisible(R.id.Setupfd, true);
                            navigationView.getMenu().setGroupVisible(R.id.SetupGroup, false);
                            navigationView.getMenu().setGroupVisible(R.id.one, false);
                            navigationView.getMenu().setGroupVisible(R.id.Setup, false);
                            navigationView.getMenu().findItem(R.id.eleven).setTitle("# " + tags[0]);
                            navigationView.getMenu().findItem(R.id.tw).setTitle("# " + tags[1]);
                            navigationView.getMenu().findItem(R.id.thi).setTitle("# " + tags[2]);
                            navigationView.getMenu().findItem(R.id.fourteen).setTitle("# " + tags[3]);
                            navigationView.getMenu().findItem(R.id.fifteen).setTitle("# " + tags[4]);
                        }



                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}
