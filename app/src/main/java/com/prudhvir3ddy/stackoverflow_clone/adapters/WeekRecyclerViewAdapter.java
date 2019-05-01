package com.prudhvir3ddy.stackoverflow_clone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prudhvir3ddy.stackoverflow_clone.R;

import java.util.List;

public class WeekRecyclerViewAdapter extends RecyclerView.Adapter<WeekRecyclerViewAdapter.WeekHolder> {

    private final List<String> strings;
    private final Context context;

    public WeekRecyclerViewAdapter(List<String> strings, Context context) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public WeekHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_model, viewGroup, false);
        return new WeekHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekHolder weekHolder, int i) {
        String s = strings.get(i);
        weekHolder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class WeekHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public WeekHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
