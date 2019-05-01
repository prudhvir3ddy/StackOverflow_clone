package com.prudhvir3ddy.stackoverflow_clone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.adapters.RecyclerViewAdapter;
import com.prudhvir3ddy.stackoverflow_clone.utils.Connection;
import com.prudhvir3ddy.stackoverflow_clone.utils.RecyclerTouchListener;
import com.prudhvir3ddy.stackoverflow_clone.utils.SharedPrefs;
import com.prudhvir3ddy.stackoverflow_clone.utils.Urls;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserIntrestActivity extends AppCompatActivity {
    private List<String> strings;
    private Set<String> selected;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private Button button, param1, param2, param3, param4;
    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_intrest);
        SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());
        param1 = findViewById(R.id.param1);
        param2 = findViewById(R.id.param2);
        param3 = findViewById(R.id.param3);
        param4 = findViewById(R.id.param4);
        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");

        if (sharedPrefs.getLogedInKey() == null) {
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();
            sharedPrefs.savePref("" + appLinkData);
        }
        button = findViewById(R.id.button);
        selected = new HashSet<>();
        strings = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new RecyclerViewAdapter(getApplicationContext(), strings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.size() == 4) {
                    Intent intent = new Intent(getApplicationContext(), QuestionListAcivity.class);
                    Object params[] = selected.toArray();
                    intent.putExtra("param1", params[0].toString().replace("#", "%23"));
                    intent.putExtra("param2", params[1].toString().replace("#", "%23"));
                    intent.putExtra("param3", params[2].toString().replace("#", "%23"));
                    intent.putExtra("param4", params[3].toString().replace("#", "%23"));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select 4 options", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Connection connection = new Connection(getApplicationContext());
        if (connection.isInternet()) {
            loadToast.show();
            AndroidNetworking.get(Urls.tags)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray items = response.optJSONArray("items");

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject object = items.optJSONObject(i);
                                strings.add(object.optString("name"));
                            }

                            adapter.notifyDataSetChanged();
                            loadToast.success();
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadToast.error();
                            Log.d("error", "" + anError.getResponse());
                            Toast.makeText(getApplicationContext(), "some error in getting data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (selected.size() < 4) {
                    if (!selected.contains(strings.get(position))) {
                        if (param1.getVisibility() == View.GONE) {

                            selected.add(strings.get(position));
                            param1.setText(strings.get(position));
                            param1.setVisibility(View.VISIBLE);
                        } else if (param2.getVisibility() == View.GONE) {

                            selected.add(strings.get(position));
                            param2.setText(strings.get(position));
                            param2.setVisibility(View.VISIBLE);
                        } else if (param3.getVisibility() == View.GONE) {

                            selected.add(strings.get(position));
                            param3.setText(strings.get(position));
                            param3.setVisibility(View.VISIBLE);
                        } else if (param4.getVisibility() == View.GONE) {
                            selected.add(strings.get(position));
                            param4.setText(strings.get(position));
                            param4.setVisibility(View.VISIBLE);
                        }
                    }
                } else
                    Toast.makeText(getApplicationContext(), "you can only selected four tags", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void param1click(View view) {
        if (param1.getVisibility() == View.VISIBLE) {

            selected.remove(param1.getText().toString());
            param1.setVisibility(View.GONE);
        }
    }

    public void param2click(View view) {

        if (param2.getVisibility() == View.VISIBLE) {
            selected.remove(param2.getText().toString());
            param2.setVisibility(View.GONE);
        }
    }

    public void param3click(View view) {

        if (param3.getVisibility() == View.VISIBLE) {
            selected.remove(param3.getText().toString());
            param3.setVisibility(View.GONE);
        }
    }

    public void param4click(View view) {

        if (param4.getVisibility() == View.VISIBLE) {
            selected.remove(param4.getText().toString());
            param4.setVisibility(View.GONE);
        }
    }
}
