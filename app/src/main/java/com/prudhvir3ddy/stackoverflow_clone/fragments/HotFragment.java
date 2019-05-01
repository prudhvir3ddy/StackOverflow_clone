package com.prudhvir3ddy.stackoverflow_clone.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prudhvir3ddy.stackoverflow_clone.R;
import com.prudhvir3ddy.stackoverflow_clone.adapters.HotRecyclerViewAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends Fragment {

    List<String> strings;
    LoadToast loadToast;
    HotRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;

    public HotFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_hot, container, false);
        recyclerView = rootView.findViewById(R.id.hot_recyclerview);
        recyclerViewAdapter = new HotRecyclerViewAdapter(getContext(), strings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
        Log.v("Im here ", "view");
        AndroidNetworking.get("https://api.stackexchange.com/2.2/questions?order=desc&sort=hot&site=stackoverflow")
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
                        Toast.makeText(getContext(), "Error in connection", Toast.LENGTH_SHORT).show();
                        loadToast.error();
                    }
                });

        return rootView;
    }

}
