package com.harbin.pandian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Harbin on 7/3/17.
 */

public class RecordDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "records.db";

    private static final int DATABASE_VERSION = 1;

    public RecordDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECORD_TABLE = " CREATE TABLE " + RecordContract.RecordEntry.TABLE_NAME + " (" +
                RecordContract.RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecordContract.RecordEntry.COLUMN_BARCODE + " TEXT NOT NULL, " +
                RecordContract.RecordEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecordContract.RecordEntry.TABLE_NAME);
        onCreate(db);
    }
}
