package com.example.readyourresults.MySavedResults;

import android.content.Context;
import android.database.Cursor;

import com.example.readyourresults.Database.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedResultsListHelper {
    static SavedResultsListHelper savedResultsListHelper;
    List<SavedResultsItem> savedResultsItems;
    DatabaseHelper database;

    public SavedResultsListHelper(Context context) {

        database = new DatabaseHelper(context);

        Cursor res = database.getAllData();

        savedResultsItems = new ArrayList<>();
        int count = 0;
        while (res.moveToNext()) {
            count++;
            StringBuffer testTypeBuffer = new StringBuffer();
            StringBuffer testOutcomeBuffer = new StringBuffer();
            StringBuffer testDateBuffer = new StringBuffer();
            StringBuffer testImagePathBuffer = new StringBuffer();

            testTypeBuffer.append(res.getString(0));
            testOutcomeBuffer.append(res.getString(1));
            testDateBuffer.append(res.getString(2));
            testImagePathBuffer.append(res.getString(3));

            SavedResultsItem savedResultsItem = new SavedResultsItem(
                    "Test " + count,
                    "Test Type: " + testTypeBuffer.toString(),
                    "Test Outcome: " + testOutcomeBuffer.toString(),
                    "Test Date: " + testDateBuffer.toString(),
                    testImagePathBuffer.toString()
            );
            savedResultsItems.add(savedResultsItem);
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