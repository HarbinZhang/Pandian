package com.harbin.pandian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private ContentLoadingProgressBar loadingBar;

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        loadingBar = (ContentLoadingProgressBar) findViewById(R.id.loading_main);
        loadingBar.hide();
        queue = Volley.newRequestQueue(this);



    }


    public void toStart(View view){
        Intent intent = new Intent(this, BasicInfo.class);
        startActivity(intent);
    }

    public void toLogin(View view){
        new IntentIntegrator(this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
    }

    public void toShangjia(View view){
        if(prefs.getBoolean("login_status", false)){
            Intent intent = new Intent(this, RukuListActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "请先登录", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.show();
                Toast.makeText(this, "扫描成功：" + result.getContents(), Toast.LENGTH_LONG).show();

                String url = "http://505185679.java.cdnjsp.wang/wms_service/user/getUser.do";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                try {
                                    JSONObject obj = new JSONObject(response);
//                                    int re = res.getInt("code");
//                                    JSONObject obj = res.getJSONObject("result");
                                    if(obj.getInt("code")==0){
                                        loadingBar.hide();
                                        Toast.makeText(MainActivity.this, "登录失败: "+obj.getString("result"), Toast.LENGTH_SHORT).show();
                                    }else if(obj.getInt("code")==200){
                                        JSONObject userObj = new JSONObject(obj.getString("result"));
                                        editor.putString("account_info", userObj.toString());
                                        editor.putBoolean("login_status", true);
                                        editor.apply();
                                        loadingBar.hide();
                                        Toast.makeText(MainActivity.this, "登录成功： 欢迎 " + userObj.getString("user_name"), Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    Log.e("Error", e.getMessage());
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
                    protected Map<String, String > getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
//                        params.put("employee_code",result.getContents());
                        params.put("employee_code", String.valueOf(1610024));
                        Log.d("params: ", params.toString());
                        return params;
                    }
                };
                queue.add(postRequest);

                editor.putString("account", result.getContents());
                editor.apply();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    
}
