package com.example.readyourresults.MySavedResults;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readyourresults.Database.DatabaseHelper;
import com.example.readyourresults.R;

public class SavedResultsViewHolder extends RecyclerView.ViewHolder {
    public TextView title, testType, testOutcome, testDate;
    public ImageView arrow;
    public ImageView testImage;
    private CardView testInfo;
    DatabaseHelper database;

    public SavedResultsViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.parent_title);
        testType = (TextView)itemView.findViewById(R.id.test_type);
        testOutcome = (TextView)itemView.findViewById(R.id.test_outcome);
        testDate = (TextView)itemView.findViewById(R.id.test_date);
        arrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
        testInfo = (CardView) itemView.findViewById(R.id.test_info);
        testImage = (ImageView) itemView.findViewById(R.id.test_image);
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

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;

        Bitmap bitmapImage = BitmapFactory.decodeFile(savedResultsItem.getTestImage(), opts);
        if (bitmapImage!= null) {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapImage, 0, 0, (bitmapImage.getWidth()),
                    (bitmapImage.getHeight()), matrix, true);

            testImage.setImageBitmap(rotatedBitmap);
        } else {
            testImage.setImageBitmap(bitmapImage);
        }

        title.setText(savedResultsItem.getTitle());
        testType.setText(savedResultsItem.getTestType());
        testOutcome.setText(savedResultsItem.getTestOutcome());
        testDate.setText(savedResultsItem.getTestDate());
    }
}