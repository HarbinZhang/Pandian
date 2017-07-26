package com.harbin.pandian.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.harbin.pandian.R;

import java.util.ArrayList;

public class RecordDisplay extends AppCompatActivity {

    private final ArrayList<String> itemSelected = new ArrayList<>();

    private RecordListAdapter recordListAdapter;

    private SQLiteDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_display);

        RecyclerView recordRecyclerView;
        recordRecyclerView = (RecyclerView) this.findViewById(R.id.record_all_list_view);

        recordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecordDbHelper dbHelper = new RecordDbHelper(this);

        mDb = dbHelper.getWritableDatabase();


        Cursor cursor = getAllRecords();

        recordListAdapter = new RecordListAdapter(this, cursor);

        recordRecyclerView.setAdapter(recordListAdapter);
    }

    private Cursor getAllRecords(){
        return mDb.query(
                RecordContract.RecordEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RecordContract.RecordEntry.COLUMN_TIMESTAMP
        );
    }


}
