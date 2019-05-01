package com.prudhvir3ddy.stackoverflow_clone.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.adapters.WeekRecyclerViewAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment {

    List<String> strings;
    WeekRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    LoadToast loadToast;

    public WeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        strings = new ArrayList<>();
        loadToast = new LoadToast(getContext());
        loadToast.setText("Loading..");
        loadToast.show();
        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        recyclerView = rootView.findViewById(R.id.week_recyclerview);
        recyclerViewAdapter = new WeekRecyclerViewAdapter(strings, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
        AndroidNetworking.get("https://api.stackexchange.com/2.2/questions?order=desc&sort=week&site=stackoverflow")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test", "" + response);
                        JSONArray items = response.optJSONArray("items");

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.optJSONObject(i);
                            strings.add(object.optString("title"));
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        loadToast.success();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ers", "" + anError + anError.getResponse());
                        loadToast.error();
                    }
                });

        return rootView;
    }

}
