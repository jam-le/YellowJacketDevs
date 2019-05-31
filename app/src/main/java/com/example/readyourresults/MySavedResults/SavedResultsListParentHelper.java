package com.example.readyourresults.MySavedResults;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedResultsListParentHelper {
    static SavedResultsListParentHelper savedResultsListParentHelper;
    List<SavedResultsListParent> savedResultsListParents;

    public SavedResultsListParentHelper(Context context) {
        savedResultsListParents = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            SavedResultsListParent title = new SavedResultsListParent(
                    "Test " + i);
            savedResultsListParents.add(title);
        }
    }

    public static SavedResultsListParentHelper get(Context context) {
        if(savedResultsListParentHelper == null)
            savedResultsListParentHelper = new SavedResultsListParentHelper(context);
        return savedResultsListParentHelper;
    }

    public List<SavedResultsListParent> getAll() {
        return savedResultsListParents;
    }
}