package com.example.fengheriliapplicatio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SettingActivity extends AppCompatActivity {

    Button btreturn;
    TextView tvhelp, tvconnection;
    EditText edmoren;
    //Button btnsmall,btnmedium,btnbig;
    //float size = 1.0f;
    //String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tvhelp = findViewById(R.id.tv_connection);
        tvconnection = findViewById(R.id.tv_help);
        btreturn = findViewById(R.id.bt_return);
        edmoren = findViewById(R.id.ed_moren);
//        btnsmall = findViewById(R.id.btn_small);
//        btnmedium = findViewById(R.id.btn_medium);
//        btnbig = findViewById(R.id.btn_big);
        btreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tvhelp.setText("邮箱：Li21_li21@163.com" + "\n联系电话：15805428615" + "\nQQ:253705386");
        tvconnection.setText("如返回空白首页，则获取天气信息失败，请检查网络连接或重新尝试输入城市\n可以通过到设置界面再返回，获取默认位置天气信息\n需要Android4.4版本及以上，为了提供更好的使用体验，以及隐私保护，本APP只会申请必要的手机权限");
        Intent intent = getIntent();
        String morencity = intent.getStringExtra("morencity");
        edmoren.setHint("当前默认城市：" + morencity);
        if (morencity == null) {
            Toast toast = Toast.makeText(SettingActivity.this, "欢迎来到风和日丽天气，请设置您的默认城市", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        edmoren.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String morencity = edmoren.getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, MainActivity.class);
                    intent.putExtra("morencity", morencity);
                    startActivity(intent);
                }
                return false;
            }
        });
//        btnsmall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                size = 0.8f;
//            }
//        });
//        btnmedium.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                size = 1.0f;
//            }
//        });
//        btnbig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                size = 1.2f;
//            }
//        });
//    }
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration configuration = res.getConfiguration();
//        configuration.fontScale = size;
//        res.updateConfiguration(configuration,res.getDisplayMetrics());
//        return res;
    }

}
