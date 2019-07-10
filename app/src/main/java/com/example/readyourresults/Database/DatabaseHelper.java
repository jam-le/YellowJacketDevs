package com.example.readyourresults.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "results.db";
    private static final String TABLE_NAME = "results_table";
    private static final String COL_1 = "TEST_TYPE";
    private static final String COL_2 = "OUTCOME";
    private static final String COL_3 = "DATE";
    private static final String COL_4 = "IMAGE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL("create table " + TABLE_NAME + " (TEST_TYPE TEXT,OUTCOME TEXT,DATE TEXT,IMAGE TEXT)");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean insertData(String testType, String testOutcome, String testDate, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, testType);
        contentValues.put(COL_2, testOutcome);
        contentValues.put(COL_3, testDate);
        contentValues.put(COL_4, image);
        Long insertResult = db.insert(TABLE_NAME, null, contentValues);
        if (insertResult == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,
                null);
        return res;
    }
}
