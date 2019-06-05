package com.example.readyourresults.MySavedResults;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readyourresults.R;

public class SavedResultsViewHolder extends RecyclerView.ViewHolder {
    public TextView title, testType, testOutcome, testDate;
    public ImageView arrow;
    private CardView testInfo;

    public SavedResultsViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.parent_title);
        testType = (TextView)itemView.findViewById(R.id.test_type);
        testOutcome = (TextView)itemView.findViewById(R.id.test_outcome);
        testDate = (TextView)itemView.findViewById(R.id.test_date);
        arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
        testInfo = (CardView) itemView.findViewById(R.id.test_info);
    }

    public void bind(SavedResultsItem savedResultsItem) {
        boolean expanded = savedResultsItem.isExpanded();

        if(expanded) {
            testInfo.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_down_arrow);
        } else {
            testInfo.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_help_question_arrow);
        }
        title.setText(savedResultsItem.getTitle());
        testType.setText(savedResultsItem.getTestType());
        testOutcome.setText(savedResultsItem.getTestOutcome());
        testDate.setText(savedResultsItem.getTestDate());
    }
}