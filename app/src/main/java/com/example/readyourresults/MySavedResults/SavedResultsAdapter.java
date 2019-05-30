package com.example.readyourresults.MySavedResults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.readyourresults.R;

import java.util.List;

public class SavedResultsAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    LayoutInflater inflater;

    public SavedResultsAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_saved_results_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_saved_results_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        SavedResultsListParent title = (SavedResultsListParent)o;
        titleParentViewHolder.textView.setText(title.getTitle());
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        SavedResultsListChild child = (SavedResultsListChild)o;
        titleChildViewHolder.testType.setText(child.getTestType());
        titleChildViewHolder.testOutcome.setText(child.getTestOutcome());
        titleChildViewHolder.testDate.setText(child.getTestDate());
    }
}