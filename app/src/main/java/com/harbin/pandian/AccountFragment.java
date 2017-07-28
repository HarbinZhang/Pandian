package com.harbin.pandian;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static com.harbin.pandian.MainActivity.scanDecode;


public class AccountFragment extends Fragment {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Button btn_sign_out;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();



        final View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new IntentIntegrator(getActivity()).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
                scanDecode.starScan();
            }
        };

        btn_sign_out = (Button) view.findViewById(R.id.btn_account_logout);
        if(prefs.getBoolean("login_status", false)){
            btn_sign_out.setText("注销登录");
            btn_sign_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout(v);
                    btn_sign_out.setText("扫码登录");
                    btn_sign_out.setOnClickListener(mClickListener);
//                    btn_sign_out.setText("扫码登录");
                }
            });
        }else{
            btn_sign_out.setText("扫码登录");
            btn_sign_out.setOnClickListener(mClickListener);
        }




        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(prefs.getBoolean("login_status", false)){
//            btn_sign_out.setText("注销登录");
//        }else{
//            btn_sign_out.setText("扫码登录");
//        }
//    }

    public void logout(View view){
        Toast.makeText(getContext(), "注销成功", Toast.LENGTH_SHORT).show();
        editor.putBoolean("login_status",false);
        editor.apply();
    }



}