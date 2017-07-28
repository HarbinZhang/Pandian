package com.harbin.pandian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Harbin on 7/26/17.
 */

public class RukuListDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rukudan.db";

    private static final int DATABASE_VERSION = 8;

    public RukuListDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECORD_TABLE = " CREATE TABLE " + RukuListContract.RukuListEntry.TABLE_NAME + " (" +
                RukuListContract.RukuListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RukuListContract.RukuListEntry.COLUMN_ID + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_SERIAL_NUMBER + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_STORE_NAME + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_CREATOR_NAME + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_CREATE_TIME + " TEXT NOT NULL, " +
                RukuListContract.RukuListEntry.COLUMN_DONE + " INTEGER DEFAULT 0, " +
                RukuListContract.RukuListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

//                RukuListContract.RukuListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                RukuListContract.RukuListEntry.COLUMN_ID + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_SERIAL_NUMBER + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_SUPPLIER_NAME + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_STORE_NAME + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_CREATOR_NAME + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_CREATE_TIME + " TEXT, " +
//                RukuListContract.RukuListEntry.COLUMN_DONE + " INTEGER DEFAULT 0, " +
//                RukuListContract.RukuListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
//                "); ";



        db.execSQL(SQL_CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RukuListContract.RukuListEntry.TABLE_NAME);
        onCreate(db);
    }
}