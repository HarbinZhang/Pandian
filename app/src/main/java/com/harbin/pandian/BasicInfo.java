package com.harbin.pandian;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.harbin.pandian.database.RecordContract;
import com.harbin.pandian.database.RecordDbHelper;
import com.harbin.pandian.database.RecordDisplay;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BasicInfo extends AppCompatActivity {

    private ArrayAdapter<String> adapter = null;

    private EditText et_applicat, et_time, et_pandianren, et_keyword, et_state;
    private String storageName;

    private static final String [] storageNames = {"常温库","阴凉库","冷藏库","冷冻库","不合格库"};
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);



        // SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();


        // bind
        et_applicat = (EditText) findViewById(R.id.et_basic_applicat);
        et_keyword = (EditText) findViewById(R.id.et_basic_keyword);
        et_pandianren = (EditText) findViewById(R.id.et_basic_pandianren);
        et_state = (EditText) findViewById(R.id.et_basic_state);
        et_time = (EditText) findViewById(R.id.et_basic_time);



        // get curt date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(cal.getTime());
        et_time.setText(formattedDate);

        initBasicInfo();


        // sql
        RecordDbHelper dbHelper = new RecordDbHelper(this);
        mDb = dbHelper.getWritableDatabase();




        Spinner sp_storageNames = (Spinner) findViewById(R.id.sp_basic_storage_name);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, storageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_storageNames.setAdapter(adapter);
        sp_storageNames.setVisibility(View.VISIBLE);
        sp_storageNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initBasicInfo(){
        String pandianren = prefs.getString("pandianren", null);
        String time = prefs.getString("time",null);
        String applicat = prefs.getString("applicat", null);
        String state = prefs.getString("state", null);
        String keyword = prefs.getString("keyword", null);

        if (time != null){
            et_time.setText(time);
        }
        et_pandianren.setText(pandianren);
        et_keyword.setText(keyword);
        et_applicat.setText(applicat);
        et_state.setText(state);
    }

    public boolean saveBasicInfo(){
        if(et_pandianren.getText().length() == 0 ||
                et_time.getText().length() == 0 ||
                et_applicat.getText().length() == 0 ||
                et_state.getText().length() == 0 ||
                et_keyword.getText().length() == 0){
            return false;
        }
        editor.putString("pandianren", et_pandianren.getText().toString());
        editor.putString("time", et_time.getText().toString());
        editor.putString("applicat", et_applicat.getText().toString());
        editor.putString("state", et_state.getText().toString());
        editor.putString("keyword", et_keyword.getText().toString());

        editor.commit();
        return true;
    }

    public void startScan(View view){
        if(saveBasicInfo()) {
            new IntentIntegrator(this).initiateScan();
        }else{
            Toast.makeText(view.getContext(), "请输入基本信息", Toast.LENGTH_SHORT).show();
        }
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                addNewRecord(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected long addNewRecord(String key){
        ContentValues cv = new ContentValues();
        cv.put(RecordContract.RecordEntry.COLUMN_BARCODE, key);
        return mDb.insert(RecordContract.RecordEntry.TABLE_NAME, null, cv);
    }

    public void startCScan(View view){
        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
        startActivity(intent);
    }

    public void goSql(View view){
        Intent intent = new Intent(this, RecordDisplay.class);
        startActivity(intent);
    }

}
