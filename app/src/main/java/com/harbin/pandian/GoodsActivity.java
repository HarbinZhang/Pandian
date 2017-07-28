package com.harbin.pandian;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.harbin.pandian.database.GoodsContract;
import com.harbin.pandian.database.GoodsDbHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GoodsActivity extends ShangjiaActivity {

    private Button btn_ok;
    private EditText et_quantity, et_loc_name, et_certificate, et_id, et_name;
    private Spinner sp_unit, sp_storages, sp_rack_name, sp_rack_code;

    private String[] units;
    private String[] rackName;
    private String[] rackCode;

    SQLiteDatabase mDatabase;

    private long sqlId;

    private TextView tv_id;


    protected Map<String, String> s_code2name_Map = new HashMap<>();
    protected Map<String, String> s_name2code_Map = new HashMap<>();
    protected Map<String, String> unit_value2name_Map = new HashMap<>();
    protected Map<String, String> unit_name2value_Map = new HashMap<>();
    private RequestQueue queue;

    private ContentLoadingProgressBar loadingBar;

    private String unit_value;
    private String rack_name, rack_code;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        setTitle("商品明细");

        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_goods);
        loadingBar.show();

        context = this;

        et_quantity = (EditText) findViewById(R.id.et_goods_quantity);
        et_quantity.setText(getIntent().getStringExtra("quantity"));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_goods);



        queue = Volley.newRequestQueue(this);

        initStorage();
        getUnit();


        sp_unit = (Spinner) findViewById(R.id.sp_goods_unit);
        sp_rack_name = (Spinner) findViewById(R.id.sp_goods_loc_name);
        sp_rack_code = (Spinner) findViewById(R.id.sp_goods_rack);
//        sp_register_number = (Spinner) findViewById(R.id.sp_goods_register_number);


        GoodsDbHelper dbHelper = new GoodsDbHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        sqlId = getIntent().getLongExtra("sqlId", 0);





        et_id = (EditText) findViewById(R.id.et_goods_id);
        et_name = (EditText) findViewById(R.id.et_goods_name);

        String id = getIntent().getStringExtra("id");
        et_id.setText(id);
        et_id.setKeyListener(null);

        String name = getIntent().getStringExtra("name");
        et_name.setText(name);
        et_name.setKeyListener(null);




        String loc_name = getIntent().getStringExtra("loc_name");
//        et_loc_name = (EditText) findViewById(R.id.et_goods_loc_name);
//        if (loc_name.equals("")){
//            et_loc_name.setHint("请输入货架名称");
//        }else{
//            et_loc_name.setText(loc_name);
//            et_loc_name.setKeyListener(null);
//        }


        String certificate = getIntent().getStringExtra("certificate");
        et_certificate = (EditText) findViewById(R.id.tv_goods_certificate);
        et_certificate.setText(certificate);
        et_certificate.setKeyListener(null);


        btn_ok = (Button) findViewById(R.id.btn_goods_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGoods();
            }
        });

    }


    private void sendGoods(){
        // null judge

        final String id = et_id.getText().toString();
        final String quantity = et_quantity.getText().toString();
//        final String loc_code = getIntent().getStringExtra("loc_code");
//        final String loc_code = "1234556677890";
//        final String loc_name = sp_rack_name.getText().toString();
        final String certificate = et_certificate.getText().toString();

        if(id.isEmpty() || quantity.isEmpty() || rack_name.isEmpty() || rack_code.isEmpty() || certificate.isEmpty()){
            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
            return ;
        }

        loadingBar.show();
        // http
        String url = "http://505185679.java.cdnjsp.wang/wms_service/checkIn/saveCheckInDetail.do";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        loadingBar.hide();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("code") == 200) {
                                Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                // ?
                                Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.e("Error: ", e.getMessage());
                        }
//                        finish();
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
                params.put("id", id);
                params.put("quantity", quantity);
                params.put("unit", unit_value);
                params.put("loc_code", rack_code);
                params.put("loc_name", rack_name);
                params.put("certificate", certificate);

                return params;
            }
        };
        queue.add(postRequest);




        // update
        ContentValues cv = new ContentValues();
        cv.put("done", 1);
        mDatabase.update(GoodsContract.GoodsEntry.TABLE_NAME,
                cv,
                "_id="+sqlId,
                null);


    }


    private Cursor getCursor(long id){
        String whereClause = GoodsContract.GoodsEntry._ID + " = '" + id + "' ";
        return mDatabase.query(
                GoodsContract.GoodsEntry.TABLE_NAME,
                null,
                whereClause,
                null,
                null,
                null,
                null
        );
    }





    private void getUnit(){
        final String url = "http://505185679.java.cdnjsp.wang/wms_service/drop/getDictDataData.do?class_code=UNIT&exclude=JLDW0055";

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());

                        try{
                            JSONArray array = response.getJSONArray("result");
                            for (int i = 0; i<array.length(); i++){
                                unit_value2name_Map.put(array.getJSONObject(i).getString("data_value"),
                                        array.getJSONObject(i).getString("data_name"));
                                unit_name2value_Map.put(array.getJSONObject(i).getString("data_name"),
                                        array.getJSONObject(i).getString("data_value"));
                            }
                            setUnits();
//                            loadingBar.hide();
                        }catch (Exception e){
                            Log.e("Error: ", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }


    private void setUnits(){
        units = unit_name2value_Map.keySet().toArray(new String[unit_name2value_Map.size()]);
        ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_unit.setAdapter(unit_adapter);
        sp_unit.setVisibility(View.VISIBLE);
        String unit = getIntent().getStringExtra("unit");
        if(unit_name2value_Map.containsKey(unit)){
            sp_unit.setSelection(Arrays.asList(units).indexOf(unit));
        }
        sp_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit_value = unit_name2value_Map.get(units[position]);
                Log.d("unit_value", unit_value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                                s_code2name_Map.put(array.getJSONObject(i).getString("s_code"), array.getJSONObject(i).getString("s_name"));
                                s_name2code_Map.put(array.getJSONObject(i).getString("s_name"), array.getJSONObject(i).getString("s_code"));
                            }
                            rackName = s_name2code_Map.keySet().toArray(new String[s_name2code_Map.size()]);
                            rackCode = s_code2name_Map.keySet().toArray(new String[s_code2name_Map.size()]);
                            setRacksName();
                            setRacksCode();

                            String scanned_loc_code = getIntent().getStringExtra("loc_code");
                            if(s_code2name_Map.containsKey(scanned_loc_code)){
                                sp_rack_code.setSelection(Arrays.asList(rackCode).indexOf(scanned_loc_code));
                            }

                            loadingBar.hide();
                        }catch (Exception e){
                            Log.e("Error: ",e.getMessage());
                        }

//                        setResults(response);
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

    private void setRacksCode(){
        ArrayAdapter<String> rack_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rackCode);
        rack_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_rack_code.setAdapter(rack_adapter);
        sp_rack_code.setVisibility(View.VISIBLE);
        sp_rack_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rack_name = s_code2name_Map.get(rackCode[position]);
                sp_rack_name.setSelection(Arrays.asList(rackName).indexOf(rack_name));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void setRacksName(){
        ArrayAdapter<String> rack_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rackName);
        rack_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_rack_name.setAdapter(rack_adapter);
        sp_rack_name.setVisibility(View.VISIBLE);
        sp_rack_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rack_code = s_name2code_Map.get(rackName[position]);
                sp_rack_code.setSelection(Arrays.asList(rackCode).indexOf(rack_code));
//                if (position == 1){
//                    LogFragment.d("info: ", "alert changed loc_code");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // change to a new
//                            et_loc_code.setText("等待后端更新货位编码");
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            sp_rack_name.setSelection(0);
//                            return;
//                        }
//                    });
//
//                    AlertDialog dialog = builder.create();
//                    dialog.setTitle("改变货位编码");
//                    dialog.setMessage("确定改变当前货位编码为服务器数据吗？");
//                    dialog.show();
//                }else{
//                    et_loc_code.setText(getIntent().getStringExtra("loc_code"));
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    .setMessage("所有更改将会丢失，确定直接退出吗？")
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
