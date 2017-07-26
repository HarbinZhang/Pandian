package com.harbin.pandian;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RukuListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String debug_lists = "[{\"ID\":\"GRK1707\",\"detail\":[{\"ID\":\"018199\",\"name\":\"中置器\"},{\"ID\":\"0181200\",\"name\":\"UC衬垫\"},{\"ID\":\"018299\",\"name\":\"中置器复制\"},{\"ID\":\"0181201\",\"name\":\"UC衬垫\"}]},{\"ID\":\"GRK1708\",\"detail\":[{\"ID\":\"018199\",\"name\":\"中置器\"},{\"ID\":\"0181200\",\"name\":\"UC衬垫\"},{\"ID\":\"018299\",\"name\":\"中置器复制\"},{\"ID\":\"0181201\",\"name\":\"UC衬垫\"}]}]";

    private RequestQueue queue;

    private ContentLoadingProgressBar loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruku_list);

        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_rukulist);
        loadingBar.show();

        queue = Volley.newRequestQueue(this);

        setTitle("入库单列表");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ruku_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        getRukuList();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }


        }).attachToRecyclerView(mRecyclerView);

    }



    private void getRukuList(){
        String url ="http://505185679.java.cdnjsp.wang/wms_service/checkIn/getCheckInList.do";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray RukuArray = new JSONArray(jsonObj.getString("result"));
                            mAdapter = new RukuListAdapter(RukuArray);
                            mRecyclerView.setAdapter(mAdapter);
                            loadingBar.hide();
                        }catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getLocalizedMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();


                return params;

            }
        };

        queue.add(stringRequest);



    }



}

