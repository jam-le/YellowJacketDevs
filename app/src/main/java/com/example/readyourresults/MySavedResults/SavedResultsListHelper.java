package com.example.readyourresults.MySavedResults;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedResultsListHelper {
    static SavedResultsListHelper savedResultsListHelper;
    List<SavedResultsItem> savedResultsItems;

    public SavedResultsListHelper(Context context) {
        savedResultsItems = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            SavedResultsItem title = new SavedResultsItem(
                    "Test " + i,
                    "Test Type: ",
                    "Test Outcome: ",
                    "Test Date: ");
            savedResultsItems.add(title);

        }
    }

    public static SavedResultsListHelper get(Context context) {
        if(savedResultsListHelper == null)
            savedResultsListHelper = new SavedResultsListHelper(context);
        return savedResultsListHelper;
    }

    public List<SavedResultsItem> getAll() {
        return savedResultsItems;
    }
}