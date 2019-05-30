package com.example.readyourresults.MySavedResults;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.readyourresults.R;

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView testType, testOutcome, testDate;
    public TitleChildViewHolder(View itemView) {
        super(itemView);
        testType = (TextView)itemView.findViewById(R.id.test_type);
        testOutcome = (TextView)itemView.findViewById(R.id.test_outcome);
        testDate = (TextView)itemView.findViewById(R.id.test_date);
    }
}