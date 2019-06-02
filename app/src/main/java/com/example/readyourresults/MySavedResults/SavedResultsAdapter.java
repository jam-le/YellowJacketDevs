package com.example.readyourresults.MySavedResults;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.readyourresults.R;

import java.util.List;

public class SavedResultsAdapter extends RecyclerView.Adapter<SavedResultsViewHolder> {

    private List<SavedResultsItem> savedResultsItems;

    public SavedResultsAdapter(List<SavedResultsItem> savedResultsItems) {
        this.savedResultsItems = savedResultsItems;
    }

    @Override
    public SavedResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_saved_result,
                        parent,
                        false);
        return new SavedResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedResultsViewHolder savedResultsViewHolder, final int position) {
        final SavedResultsItem savedResultsItem = savedResultsItems.get(position);

        savedResultsViewHolder.bind(savedResultsItem);

        savedResultsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = savedResultsItem.isExpanded();
                savedResultsItem.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedResultsItems == null ? 0 : savedResultsItems.size();
    }
}