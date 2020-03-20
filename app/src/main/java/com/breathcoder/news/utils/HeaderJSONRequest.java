package com.breathcoder.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.breathcoder.news.common.ShareCommon;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HeaderJSONRequest extends JsonObjectRequest {
    private static SharedPreferences mSharedPreferences = null;
    private Context context;




    public HeaderJSONRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public HeaderJSONRequest(Context context,int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context=context;
    }

    public HeaderJSONRequest(Context contxt,int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context=contxt;
    }

    public HeaderJSONRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    public HeaderJSONRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        mSharedPreferences = this.context.getSharedPreferences(ShareCommon.PREFS_NAME, Context.MODE_PRIVATE);
        String token=mSharedPreferences.getString(ShareCommon.TOKEN, "");
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        return params;
    }
}
