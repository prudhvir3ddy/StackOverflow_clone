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

public class HotRecyclerViewAdapter extends RecyclerView.Adapter<HotRecyclerViewAdapter.HotViewHolder> {

    private final List<String> strings;
    private final Context context;

    public HotRecyclerViewAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public HotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_model, viewGroup, false);
        return new HotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotViewHolder hotViewHolder, int i) {
        String s = strings.get(i);

        hotViewHolder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class HotViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
