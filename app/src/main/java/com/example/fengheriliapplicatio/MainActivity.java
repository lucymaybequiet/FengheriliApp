package com.example.fengheriliapplicatio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.air.Air;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity {

    TextView tvLocation, tvqing, tvwendu, tvdetail, tvquality, tvlife;
    ImageView imqing;
    Button btsousuo, btshezhi;
    String TAG;
    List<ForecastBase> forecastBases;
    String location = "beijing";
    String morenlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.mipmap.sunny);
        //声明权限
        HeConfig.init("HE2001241201141079", "ec874e285d42423f97f8571d3739731a");
        HeConfig.switchToFreeServerNode();

        //获取默认城市
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = openFileInput("data2");
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
        String input = content.toString();
        Log.d(TAG, "=====================.================" + input);
        //第一次进入程序,设置默认城市，跳转到setting
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingActivity.class);
            intent.putExtra("kongcheng", location);
            startActivity(intent);
        }
        Intent intent = getIntent();
        String morencity = intent.getStringExtra("morencity");
        if (morencity == null) {
            location = input;
            morenlocation = input;
        } else {
            location = morencity;
            morenlocation = morencity;
        }

        //点击事件
        btsousuo = findViewById(R.id.btn_sousuo);
        btsousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        btshezhi = findViewById(R.id.btm_shezhi);
        btshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                intent.putExtra("morencity", morenlocation);
                startActivity(intent);
            }
        });

        //搜索城市
        Intent intentl = getIntent();
        String searchcity = intentl.getStringExtra("searchcity");
        if (!(searchcity == null)) {
            location = searchcity;
        }
//        Log.d(TAG, "------------------------------------" + searchcity);
//        Log.d(TAG, "==============================================" + location);

        //获取常规天气数据
        tvLocation = findViewById(R.id.tv_location);
        tvqing = findViewById(R.id.tv_qing);
        tvwendu = findViewById(R.id.tv_wendu);
        tvdetail = findViewById(R.id.tv_detail);
        imqing = findViewById(R.id.im_weather);
        final HeWeather.OnResultWeatherNowBeanListener listener = new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Looper.prepare();
                Toast toast = Toast.makeText(MainActivity.this, "找不到该城市，请重试", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Log.i(TAG, "cuole", throwable);
                Looper.loop();
            }

            @Override
            public void onSuccess(Now now) {
                Log.i(TAG, "chenggong" + new Gson().toJson(now));
                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                    NowBase nowBase = now.getNow();
                    location = now.getBasic().getLocation();
                    tvLocation.setText(location);
                    tvqing.setText(nowBase.getCond_txt());
                    tvwendu.setText(nowBase.getTmp() + "℃");
                    tvdetail.setText(nowBase.getWind_dir()
                            + "\u2000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + nowBase.getWind_sc() + "级\n"
                            + "风\u3000\u3000速\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + nowBase.getWind_spd() + "km/h\n"
                            + "相对湿度\u2000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + nowBase.getHum() + "%\n"
                            + "大气压强\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + nowBase.getPres() + "pa\n"
                            + "能见度\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + nowBase.getVis() + "m");
                    imqing.setImageResource(tubiaohuoqu.getDayIcon(nowBase.getCond_code()));

                    //设置背景
                    if (nowBase.getCond_txt().equals("雨")) {
                        getWindow().setBackgroundDrawableResource(R.mipmap.rainy);
                    } else if (nowBase.getCond_txt().equals("阴")) {
                        getWindow().setBackgroundDrawableResource(R.mipmap.yintian2);
                    } else if (nowBase.getCond_txt().equals("多云")) {
                        getWindow().setBackgroundDrawableResource(R.mipmap.yintian2);
                    }

                } else {
                    String status = now.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code:" + code);
                    tvqing.setText(null);
                }

            }
        };
        class Threadweathernow extends Thread {
            @Override
            public void run() {
                HeWeather.getWeatherNow(MainActivity.this, location, listener);
            }
        }
        final Threadweathernow threadweathernow = new Threadweathernow();
        threadweathernow.start();

        //天气预报
        class Threadweather extends Thread {
            @Override
            public void run() {
                HeWeather.getWeatherForecast(MainActivity.this, location, new HeWeather.OnResultWeatherForecastBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "cuole", e);
                    }

                    @Override
                    public void onSuccess(Forecast forecastdata) {
                        Log.i(TAG, "chenggong" + new Gson().toJson(forecastdata));
                        if (Code.OK.getCode().equalsIgnoreCase(forecastdata.getStatus())) {
                            forecastBases = forecastdata.getDaily_forecast();
                            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(forecastBases);
                            final RecyclerView rvyubao = findViewById(R.id.rv_yubao);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                            rvyubao.setLayoutManager(linearLayoutManager);
                            rvyubao.setAdapter(myAdapter);
                        } else {
                            String status = forecastdata.getStatus();
                            Code code = Code.toEnum(status);
                            Log.i(TAG, "failed code:" + code);
                        }
                    }
                });

            }
        }
        final Threadweather threadweather = new Threadweather();
        threadweather.start();


        //get空气质量
        tvquality = findViewById(R.id.tv_quality);
        class Threadquality extends Thread {
            @Override
            public void run() {
                HeWeather.getAirNow(MainActivity.this, location, new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "cuole", e);
                    }

                    @Override
                    public void onSuccess(AirNow airNow) {
                        Log.i(TAG, "chenggong" + new Gson().toJson(airNow));
                        if (Code.OK.getCode().equalsIgnoreCase(airNow.getStatus())) {
                            AirNowCity airNowCity = airNow.getAir_now_city();
                            tvquality.setText("空气质量指数\u2000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + airNowCity.getAqi()
                                    + "\n空气质量\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + airNowCity.getQlty()
                                    + "\n主要污染物\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + airNowCity.getMain());
                        } else {
                            String status = airNow.getStatus();
                            Code code = Code.toEnum(status);
                            Log.i(TAG, "failed code:" + code);
                        }

                    }
                });
            }
        }
        final Threadquality threadquality = new Threadquality();
        threadquality.start();


        //生活指数
        tvlife = findViewById(R.id.tv_style);
        class ThreadLifeStyle extends Thread {
            @Override
            public void run() {
                HeWeather.getWeatherLifeStyle(MainActivity.this, location, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(Lifestyle lifestyle) {
                        Log.i(TAG, "chenggong" + new Gson().toJson(lifestyle));
                        if (Code.OK.getCode().equalsIgnoreCase(lifestyle.getStatus())) {
                            List<LifestyleBase> lifestyleBases = lifestyle.getLifestyle();
                            String comf = lifestyleBases.get(0).getTxt();
                            String flu = lifestyleBases.get(3).getTxt();
                            tvlife.setText(comf + flu);
                        }
                    }
                });
            }
        }
        final ThreadLifeStyle threadLifeStyle = new ThreadLifeStyle();
        threadLifeStyle.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //储存默认城市
        String text = morenlocation;
        BufferedWriter bufferedWriter = null;
        if (!text.isEmpty()) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = openFileOutput("data2", Context.MODE_PRIVATE);
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


}
