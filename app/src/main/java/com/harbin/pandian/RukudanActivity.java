package com.harbin.pandian;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.harbin.pandian.database.GoodsContract;
import com.harbin.pandian.database.GoodsDbHelper;
import com.harbin.pandian.database.RukuListContract;
import com.harbin.pandian.database.RukuListDbHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RukudanActivity extends AppCompatActivity {


    private TextView tv_debug,tv_loc_code;

    private RecyclerView mRecyclerView;
    private RukudanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SQLiteDatabase mDb;
    private RequestQueue queue;

    private Button btn_submit, btn_scan;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    protected Map<String, String> s_code2name_Map = new HashMap<>();


    private ContentLoadingProgressBar loadingBar;


    private SQLiteDatabase rukulistDb;
    private RukuListAdapter listAdapter;


    private Context context;

    String ticket_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukudan);

//        setTitle("入库单明细");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_rukudan);


        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_rukudan);
        loadingBar.show();

        queue = Volley.newRequestQueue(this);

        ticket_id = getIntent().getStringExtra("rukudan");
        getDetail(ticket_id);

        tv_loc_code = (TextView) findViewById(R.id.tv_rukudan_loc_code);

        context = this;


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_rukudan_container);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // sql
        GoodsDbHelper dbHelper = new GoodsDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        mDb.delete(GoodsContract.GoodsEntry.TABLE_NAME, null, null);


        RukuListDbHelper dbHelper1 = new RukuListDbHelper(this);
        rukulistDb = dbHelper1.getWritableDatabase();

//
//        try{
//
//
//            String details = getIntent().getStringExtra("rukudan").toString();
//            JSONArray detailsArray = new JSONArray(details);
//
//            for(int i=0; i<detailsArray.length(); i++){
//                JSONObject obj = detailsArray.getJSONObject(i);
//                addNewGoods(obj.getString("ID"), "ceshi", obj.getString("name"));
//            }
//
////            mAdapter = new RukudanAdapter(detailsArray);
////            mRecyclerView.setAdapter(mAdapter);
//
//        }catch (Exception e){
//            LogFragment.e("Error in Json parse:", e.getMessage());
//        }

        Cursor cursor = getAllGoods();

        mAdapter = new RukudanAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);


        btn_submit = (Button) findViewById(R.id.btn_rukudan_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            rukudan_submit();
            }
        });




    }


    @Override
    protected void onRestart() {
        super.onRestart();

        mAdapter.swapCursor(getAllGoods());
    }

    protected long addNewGoods(String id, String merchandise, String name, String quantity, String unit, String loc_name, String certificate,
                               String model, String loc_code, String acceptence_quanitity, String total_price, String production_batch_number,
                               String production_date, String effective_date, String manufacturer){

        ContentValues cv = new ContentValues();
        cv.put(GoodsContract.GoodsEntry.COLUMN_ID, id);
        cv.put(GoodsContract.GoodsEntry.COLUMN_MERCHANDISE, merchandise);
        cv.put(GoodsContract.GoodsEntry.COLUMN_NAME, name);
        cv.put(GoodsContract.GoodsEntry.COLUMN_QUANTITY, quantity);
        cv.put(GoodsContract.GoodsEntry.COLUMN_UNIT, unit);
        cv.put(GoodsContract.GoodsEntry.COLUMN_LOCNAME, loc_name);
        cv.put(GoodsContract.GoodsEntry.COLUMN_CERTIFICATE, certificate);
        cv.put(GoodsContract.GoodsEntry.COLUMN_MODEL, model);
        cv.put(GoodsContract.GoodsEntry.COLUMN_LOCCODE, loc_code);
        cv.put(GoodsContract.GoodsEntry.COLUMN_ACCEPTENCE_QUANTITY, quantity);
        cv.put(GoodsContract.GoodsEntry.COLUMN_TOTAL_PRICE, total_price);
        cv.put(GoodsContract.GoodsEntry.COLUMN_PRODUCTION_BATCH_NUMBER, production_batch_number);
        cv.put(GoodsContract.GoodsEntry.COLUMN_PRODUCTION_DATE, production_date);
        cv.put(GoodsContract.GoodsEntry.COLUMN_EFFECTIVE_DATE, effective_date);
        cv.put(GoodsContract.GoodsEntry.COLUMN_MANUFACTURER, manufacturer);

        return mDb.insert(GoodsContract.GoodsEntry.TABLE_NAME, null, cv);

    }

    private Cursor getAllGoods(){
        return mDb.query(
                GoodsContract.GoodsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    private void getDetail(final String id){

        final String url = "http://505185679.java.cdnjsp.wang/wms_service/checkInDetail/getCheckInDetailList.do";

// prepare the Request
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);


//                        String tmp = "{\n" +
//                                "\"result\":[{\n" +
//                                "\"checkin_ticket\":\"2a5eabd7-bb77-40f9-95be-fbb98ed8000f\",\n" +
//                                "\"merchandise\":\"000fa6ec-2915-47b1-9994-af64b422b0b7\",\n" +
//                                "\"m_name\":\"泌乳激素校准品 PRL\",\n" +
//                                "\"quantity\":\"100\",\n" +
//                                "\"unit\":\"JLDW0026\",\n" +
//                                "\"loc_name\":\"器械货位\",\n" +
//                                "\"certificate_code\":\"国食药监械(进)字2014第2404436号\"\n" +
//                                "}],\n" +
//                                "\"code\":200 \n" +
//                                "}";


                        try{
                            JSONObject resultObj = new JSONObject(response);
                            JSONArray array = resultObj.getJSONArray("result");
                            for(int i = 0; i<array.length(); i++){
                                JSONObject it = array.getJSONObject(i);
                                if(!isExist(it.getString("checkin_ticket"))) {
                                    addNewGoods(it.getString("checkin_ticket"),
                                            it.getString("merchandise"),
                                            it.getString("m_name"),
                                            it.getString("quantity"),
                                            it.getString("unit"),
                                            it.getString("loc_name"),
                                            it.getString("certificate_code"),
                                            it.getString("model"),
                                            it.getString("loc_code"),
                                            it.getString("acceptence_quantity"),
                                            it.getString("total_price"),
                                            it.getString("production_batch_number"),
                                            it.getString("production_date"),
                                            it.getString("effective_date"),
                                            it.getString("manufacturer")
                                    );
                                }
                            }

                            mAdapter.swapCursor(getAllGoods());

                            loadingBar.hide();
                        }catch (Exception e){
                            Log.e("Error:",e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ticket_id", id);


                return params;
            }
        };
        queue.add(postRequest);
    }


    public void startScan(View view){
//            new IntentIntegrator(this).setOrientationLocked(false).initiateScan();
        new IntentIntegrator(this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
    }


    public void inputBarcode(View view){

    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String loc_code = result.getContents();
                if (!s_code2name_Map.containsKey(loc_code)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putString("scanned_loc_code", result.getContents());
                            editor.putString("curt_loc_name", null);
                            editor.apply();
                            tv_loc_code.setText("货架编码："+result.getContents());
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return ;
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setTitle("货架编码不存在");
                    dialog.setMessage("确定添加当前货架编码吗？");
                    dialog.show();
                }else{
                    editor.putString("scanned_loc_code", result.getContents());
                    editor.putString("curt_loc_name", s_code2name_Map.get(loc_code));
                    editor.apply();
                    tv_loc_code.setText("货架编码："+result.getContents());
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void initStorage(){
        queue = Volley.newRequestQueue(this);
        String url ="http://505185679.java.cdnjsp.wang/wms_service/drop/getStoreData.do";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONArray array = response.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++){
                                s_code2name_Map.put(array.getJSONObject(i).getString("s_code"),array.getJSONObject(i).getString("s_name"));
                            }
                        }catch (Exception e){
                            Log.e("Error: ",e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                });
        queue.add(getRequest);
    }


    public void rukudan_submit(){
        if(!allThingsDone()){
            Toast.makeText(context, "请更新完每个入库单明细", Toast.LENGTH_LONG).show();
        }else{
            Long sqlId = getIntent().getLongExtra("rukudanSqlId", 0);
            ContentValues cv = new ContentValues();
            cv.put("done", 1);
            rukulistDb.update(RukuListContract.RukuListEntry.TABLE_NAME,
                    cv,
                    RukuListContract.RukuListEntry._ID + " = " + sqlId,
                    null);
            Toast.makeText(context, "提交成功", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean allThingsDone(){
        Cursor undone_cursor = mDb.query(GoodsContract.GoodsEntry.TABLE_NAME,
                null,
                GoodsContract.GoodsEntry.COLUMN_DONE + " = 0 ",
                null,
                null,
                null,
                null);
        int cnt = undone_cursor.getCount();
        return (cnt==0);
    }

    private boolean isExist(String id){
//        Cursor cursor = mDb.query(
//                GoodsContract.GoodsEntry.TABLE_NAME,
//                null,
//                GoodsContract.GoodsEntry.COLUMN_ID + " = " + id,
//                null,
//                null,
//                null,
//                null);
//
//        int cnt = cursor.getCount();
//        return (cnt == 1);


        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("系统提示")
                    .setMessage("仍有未提交的明细，确定直接退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }


}
