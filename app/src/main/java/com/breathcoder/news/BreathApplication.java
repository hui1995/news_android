package com.breathcoder.news;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class BreathApplication extends Application {

    public static RequestQueue queue;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        // 主要是添加下面这句代码
        queue= Volley.newRequestQueue(getApplicationContext());





        //初始化今日头条广告
        TTAdManagerHolder.init(this);





    }



    public static RequestQueue getQueue() {
        return queue;
    }
}
