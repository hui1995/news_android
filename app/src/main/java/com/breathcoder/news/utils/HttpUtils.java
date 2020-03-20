package com.breathcoder.news.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.breathcoder.news.BreathApplication;
import com.breathcoder.news.common.HttpCommon;
import com.breathcoder.news.model.base.BaseCallbackInterface;
import com.breathcoder.news.ui.screenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HttpUtils {

    public static HeaderJSONRequest stringRequest;
    public static Context context;
    public static int sTimeOut=3000;

    /*
     * 获取GET请求内容
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * timeOutDefaultFlg：是否使用Volley默认连接超时；
     * */
    public static void Get(Context context,String url, String tag,
                                  BaseCallbackInterface baseCallbackInterface,
                                  boolean timeOutDefaultFlg){
        // 清除请求队列中的tag标记请求
        BreathApplication.getQueue().cancelAll(tag);
        // 创建当前的请求，获取字符串内容
        stringRequest = new HeaderJSONRequest(context,Request.Method.GET, HttpCommon.BASE_HTTP + url,
                baseCallbackInterface.responseListener(), baseCallbackInterface.errorListener());
//        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 默认超时时间以及重连次数
        int myTimeOut = timeOutDefaultFlg ? DefaultRetryPolicy.DEFAULT_TIMEOUT_MS : sTimeOut;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(myTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // 将当前请求添加到请求队列中
        BreathApplication.getQueue().add(stringRequest);

        // 重启当前请求队
        System.out.println(HttpCommon.BASE_HTTP + url);


    }
    /*
     * 获取POST请求内容（请求的代码为Map）
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * params：POST请求内容；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * timeOutDefaultFlg：是否使用Volley默认连接超时；
     * */
    public static void Post(Context context,String url, String tag,
                                   final Map<String, String> params,
                            BaseCallbackInterface baseCallbackInterface,
                                   boolean timeOutDefaultFlg) {
        // 清除请求队列中的tag标记请求
        BreathApplication.getQueue().cancelAll(tag);

        JSONObject jsonBody =new JSONObject(params);


        // 创建当前的POST请求，并将请求内容写入Map中

        stringRequest = new HeaderJSONRequest(context,Request.Method.POST, HttpCommon.BASE_HTTP + url, jsonBody, baseCallbackInterface.responseListener(),baseCallbackInterface.errorListener());

        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 默认超时时间以及重连次数
        int myTimeOut = timeOutDefaultFlg ? DefaultRetryPolicy.DEFAULT_TIMEOUT_MS : sTimeOut;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(myTimeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将当前请求添加到请求队列中
        BreathApplication.getQueue().add(stringRequest);
        // 重启当前请求队列
//        BreathApplication.getQueue().start();
    }


}


