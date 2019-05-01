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

public class TagRecyclerViewAdapter extends RecyclerView.Adapter<TagRecyclerViewAdapter.TagViewHolder> {
    private final List<String> strings;
    private final Context context;

    public TagRecyclerViewAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_model, viewGroup, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder tagViewHolder, int i) {
        String s = strings.get(i);

        tagViewHolder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
