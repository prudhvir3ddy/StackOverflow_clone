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
import com.prudhvir3ddy.stackoverflow_clone.adapters.TagRecyclerViewAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment {

    List<String> strings;
    TagRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    LoadToast loadToast;
    String url;
    public TagFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        strings = new ArrayList<>();
        url = getArguments().getString("url");
        Log.d("ser", url);
        View rootView = inflater.inflate(R.layout.fragment_tag, container, false);
        recyclerView = rootView.findViewById(R.id.tag_recyclerview);
        recyclerViewAdapter = new TagRecyclerViewAdapter(getContext(), strings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
        loadToast = new LoadToast(getContext());
        loadToast.setText("Loading..");
        loadToast.show();

        AndroidNetworking.get(url)
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
