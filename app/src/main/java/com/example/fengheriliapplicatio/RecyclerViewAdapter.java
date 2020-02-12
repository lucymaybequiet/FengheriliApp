package com.example.fengheriliapplicatio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    private List<ForecastBase> yubaoData;
    private Context context;

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView tvriqi, tvmim, tvmax;
        ImageView imyubao;

        public MyHolder(@NonNull View view) {
            super(view);
            tvriqi = view.findViewById(R.id.tv_riqi);
            tvmax = view.findViewById(R.id.tv_max);
            tvmim = view.findViewById(R.id.tv_min);
            imyubao = view.findViewById(R.id.im_tuli);
        }
    }

    public RecyclerViewAdapter(List<ForecastBase> forecastBases) {
        this.yubaoData = forecastBases;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int p) {
        ForecastBase forecastBase = yubaoData.get(p);
        String riqi = forecastBase.getDate();
        String max = forecastBase.getTmp_max();
        String min = forecastBase.getTmp_min();
        String fcode = forecastBase.getCond_code_d();
        holder.tvriqi.setText(riqi);
        holder.tvmax.setText(max + "");
        holder.tvmim.setText(min + "");
        holder.imyubao.setImageResource(tubiaohuoqu.getDayIcon(fcode));
    }


    @Override
    public int getItemCount() {
        return yubaoData.size();
    }

}
