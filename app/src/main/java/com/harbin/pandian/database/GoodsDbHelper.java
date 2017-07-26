package com.harbin.pandian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Harbin on 7/21/17.
 */
public class GoodsDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "goods.db";

    private static final int DATABASE_VERSION = 1;

    public GoodsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECORD_TABLE = " CREATE TABLE " + GoodsContract.GoodsEntry.TABLE_NAME + " (" +
                GoodsContract.GoodsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GoodsContract.GoodsEntry.COLUMN_ID + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_MERCHANDISE + " TEXT, " +
                GoodsContract.GoodsEntry.COLUMN_DONE + " INTEGER DEFAULT 0, " +
                GoodsContract.GoodsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_QUANTITY + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_LOCNAME + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_CERTIFICATE + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_MODEL + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_LOCCODE + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_ACCEPTENCE_QUANTITY + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_TOTAL_PRICE + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_PRODUCTION_BATCH_NUMBER + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_PRODUCTION_DATE + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_EFFECTIVE_DATE + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_MANUFACTURER + " TEXT NOT NULL, " +
                GoodsContract.GoodsEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GoodsContract.GoodsEntry.TABLE_NAME);
        onCreate(db);
    }
}