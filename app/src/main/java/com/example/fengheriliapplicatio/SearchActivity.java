package com.example.fengheriliapplicatio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SearchActivity extends AppCompatActivity {

    Button btnreturn, btnsearch, btnlishi;
    EditText edsearch;
    String TAG, searchcity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //读取历史数据
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = openFileInput("data");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            if (line != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final String input = content.toString();
        Log.d(TAG, "=====================.================" + input);


        //搜索传递数据及页面跳转
        btnreturn = findViewById(R.id.btn_return);
        btnsearch = findViewById(R.id.btn_search);
        btnlishi = findViewById(R.id.btn_lishi);
        edsearch = findViewById(R.id.ed_search);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        searchcity = edsearch.getText().toString();
        edsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchcity = edsearch.getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, MainActivity.class);
                    intent.putExtra("searchcity", searchcity);
                    startActivity(intent);
                }
                return false;
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchcity = edsearch.getText().toString();
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, MainActivity.class);
                intent.putExtra("searchcity", searchcity);
                startActivity(intent);
            }
        });

        //历史搜索
        btnlishi.setText(input);
        btnlishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, MainActivity.class);
                intent.putExtra("searchcity", input);
                startActivity(intent);
            }
        });
    }

    //存入历史数据
    @Override
    protected void onPause() {
        super.onPause();
        String text = edsearch.getText().toString();

        BufferedWriter bufferedWriter = null;
        if (!text.isEmpty()) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = openFileOutput("data", Context.MODE_PRIVATE);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(text);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (!(null == bufferedWriter)) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}
