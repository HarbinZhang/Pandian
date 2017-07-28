package com.harbin.pandian;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
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
import com.harbin.pandian.database.RukuListContract;
import com.harbin.pandian.database.RukuListDbHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RukuListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RukuListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String debug_lists = "[{\"ID\":\"GRK1707\",\"detail\":[{\"ID\":\"018199\",\"name\":\"中置器\"},{\"ID\":\"0181200\",\"name\":\"UC衬垫\"},{\"ID\":\"018299\",\"name\":\"中置器复制\"},{\"ID\":\"0181201\",\"name\":\"UC衬垫\"}]},{\"ID\":\"GRK1708\",\"detail\":[{\"ID\":\"018199\",\"name\":\"中置器\"},{\"ID\":\"0181200\",\"name\":\"UC衬垫\"},{\"ID\":\"018299\",\"name\":\"中置器复制\"},{\"ID\":\"0181201\",\"name\":\"UC衬垫\"}]}]";

    private RequestQueue queue;

    private ContentLoadingProgressBar loadingBar;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruku_list);

        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_rukulist);
        loadingBar.show();


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_rukulist);


        queue = Volley.newRequestQueue(this);



        RukuListDbHelper dbHelper = new RukuListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        mDb.delete(RukuListContract.RukuListEntry.TABLE_NAME, null, null);

        setTitle("入库单列表");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ruku_list);



        mAdapter = new RukuListAdapter(this, getAllRukudan());
        mRecyclerView.setAdapter(mAdapter);


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


    @Override
    protected void onRestart() {
        super.onRestart();

        mAdapter.swapCursor(getAllRukudan());
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


                            for(int i = 0; i<RukuArray.length(); i++){
                                JSONObject it = RukuArray.getJSONObject(i);
                                addNewRukudan(it.getString("id"),
                                        it.getString("serial_number"),
                                        it.getString("supplier_name"),
                                        it.getString("store_name"),
                                        it.getString("creator_name"),
                                        it.getString("create_time"));
                            }

                            mAdapter.swapCursor(getAllRukudan());

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
                params.put("firstRow", "0");
                params.put("rowCount", "10");

                return params;

            }
        };

        queue.add(stringRequest);

    }



    private Long addNewRukudan(String id, String serial_number, String supplier_name,
                               String store_name, String creator_name, String create_time){
        ContentValues cv = new ContentValues();
        cv.put(RukuListContract.RukuListEntry.COLUMN_CREATE_TIME, create_time);
        cv.put(RukuListContract.RukuListEntry.COLUMN_CREATOR_NAME, creator_name);
        cv.put(RukuListContract.RukuListEntry.COLUMN_ID, id);
        cv.put(RukuListContract.RukuListEntry.COLUMN_SERIAL_NUMBER, serial_number);
        cv.put(RukuListContract.RukuListEntry.COLUMN_STORE_NAME, store_name);
        cv.put(RukuListContract.RukuListEntry.COLUMN_SUPPLIER_NAME, supplier_name);
        cv.put(RukuListContract.RukuListEntry.COLUMN_DONE, 0);

        return mDb.insert(RukuListContract.RukuListEntry.TABLE_NAME, null, cv);
    }

    private Cursor getAllRukudan(){
        return mDb.query(
                RukuListContract.RukuListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


}

