package com.breathcoder.news.model.base;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public abstract class BaseCallbackInterface {
    public static  Response.Listener<JSONObject> mListener;
    public static Response.ErrorListener mErrorListener;

    public BaseCallbackInterface( Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener) {
        this.mErrorListener = errorListener;
        this.mListener = listener;
    }

    // 请求成功时的回调函数
    public abstract void onMySuccess(JSONObject result) throws InterruptedException;

    // 请求失败时的回调函数
    public abstract void onMyError(VolleyError error) throws InterruptedException;

    // 创建请求的事件监听
    public Response.Listener<JSONObject> responseListener() {
        mListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject s) {
                Log.e("Volley Response", "response == " + s);
                try {
                    onMySuccess(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return mListener;
    }

    // 创建请求失败的事件监听
    public Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                try {
                    onMyError(volleyError);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("Volley Response", "response == " + volleyError);

            }
        };
        return mErrorListener;
    }
}
