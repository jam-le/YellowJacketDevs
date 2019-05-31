package com.example.readyourresults.MySavedResults;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.readyourresults.R;

public class TitleParentViewHolder extends ParentViewHolder {
    public TextView textView;
    public ImageButton imageButton;

    public TitleParentViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.parent_title);
        imageButton = (ImageButton) itemView.findViewById(R.id.expand_arrow);
    }
}