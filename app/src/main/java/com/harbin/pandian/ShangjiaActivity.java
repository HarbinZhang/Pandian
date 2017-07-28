package com.harbin.pandian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShangjiaActivity extends AppCompatActivity {

    private AutoCompleteTextView tv_date, tv_user, tv_provider, tv_keywords;

    private String user, provider, keywords, date;

    private Button btn_submit_chaxun;


    private TextView tv_debug;
    private RequestQueue queue;

    private String[] storageNames;
    private Spinner sp_storages;


    protected Map<String, String> unitMap = new HashMap<>();
    protected Map<String, String> s_code2name_Map = new HashMap<>();

    private ContentLoadingProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangjia);

        setTitle("shangjia");

        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_shangjia);
        loadingBar.show();


        initStorage();

//        getUnit();

        sp_storages = (Spinner) findViewById(R.id.sp_shangjia_storage);


        tv_date = (AutoCompleteTextView) findViewById(R.id.tv_shangjia_date);
        tv_user = (AutoCompleteTextView) findViewById(R.id.tv_shangjia_user);
        tv_provider = (AutoCompleteTextView) findViewById(R.id.tv_shangjia_provider);
        tv_keywords = (AutoCompleteTextView) findViewById(R.id.tv_shangjia_keywords);

        // get curt date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(cal.getTime());
        tv_date.setText(formattedDate);


        btn_submit_chaxun = (Button) findViewById(R.id.btn_shangjia_search);
        btn_submit_chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_Chaxun(v);
            }
        });


        tv_debug = (TextView) findViewById(R.id.tv_shangjia_debug);



    }



    private void submit_Chaxun(final View v){
        date = tv_date.getText().toString();
        user = tv_user.getText().toString();
        keywords = tv_keywords.getText().toString();
        provider = tv_provider.getText().toString();


//        if(date.isEmpty() || user.isEmpty() || keywords.isEmpty() || provider.isEmpty() ){
//            Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
//            return ;
//        }



        queue = Volley.newRequestQueue(this);
        String url ="http://505185679.java.cdnjsp.wang/wms_service/checkIn/getCheckInList.do";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        tv_debug.setText(response);
                        Intent intent = new Intent(v.getContext(), RukuListActivity.class);
                        try{
                            JSONObject jsonObj = new JSONObject(response);
                            String form = jsonObj.getString("result");
                            intent.putExtra("response", form);
//                            Log.d("info:",form);
                            startActivity(intent);
                        }catch (Exception e){

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_debug.setText("That didn't work: "+error);
                    }
        }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String>  params = new HashMap<String, String>();


//                    try {
//                        JSONObject RukuListObj = new JSONObject();
//                        JSONObject RukuObj = new JSONObject();
//                        JSONArray array = new JSONArray();
//                        JSONArray detail_array = new JSONArray();
//
//                        RukuObj.put("ID", "018199");
//                        RukuObj.put("name", "中置器");
//                        detail_array.put(RukuObj);
//                        RukuObj = new JSONObject();
//                        RukuObj.put("ID", "0181200");
//                        RukuObj.put("name", "UC衬垫");
//                        detail_array.put(RukuObj);
//                        RukuListObj.put("ID", "GRK1707");
//                        RukuListObj.put("detail", detail_array);
//
//                        array.put(RukuListObj);
//
//                        RukuObj = new JSONObject();
//                        RukuObj.put("ID", "018299");
//                        RukuObj.put("name", "中置器复制");
//                        detail_array.put(RukuObj);
//                        RukuObj = new JSONObject();
//                        RukuObj.put("ID", "0181201");
//                        RukuObj.put("name", "UC衬垫");
//                        detail_array.put(RukuObj);
//                        RukuListObj = new JSONObject();
//                        RukuListObj.put("ID", "GRK1708");
//                        RukuListObj.put("detail", detail_array);
//
//                        array.put(RukuListObj);


//                        params.put("List", array.toString());
//                    }catch (Exception e){
//
//                    }


                    params.put("user", user);
                    params.put("keywords", keywords);
                    params.put("date", date);
                    params.put("provider", provider);

                    return params;

                }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


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
                            loadingBar.hide();
                        }catch (Exception e){
                            Log.e("Error: ",e.getMessage());
                        }
                        setResults(response);
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

    private void setResults(JSONObject response){
        try{
            JSONArray array = response.getJSONArray("result");
            storageNames = new String[array.length()];
            for (int i = 0; i<array.length(); i++){
                storageNames[i] = array.getJSONObject(i).getString("s_name");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, storageNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_storages.setAdapter(adapter);
            sp_storages.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }

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
                                unitMap.put(array.getJSONObject(i).getString("data_value"),
                                        array.getJSONObject(i).getString("data_name"));
                            }
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

}
