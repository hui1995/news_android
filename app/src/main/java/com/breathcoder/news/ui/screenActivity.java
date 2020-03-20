package com.breathcoder.news.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.android.volley.VolleyError;
import com.breathcoder.news.R;
import com.breathcoder.news.common.ShareCommon;
import com.breathcoder.news.model.base.BaseCallbackInterface;
import com.breathcoder.news.utils.HttpUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class screenActivity extends Activity {
    private Boolean isOpen = false;
    private FrameLayout mSplashContainer;


    private static SharedPreferences mSharedPreferences = null;
    private static final int AD_TIME_OUT = 4000;
    private String mCodeId = "887309186";
    private boolean mIsExpress = true; //是否请求模板广告
    private TTAdNative mTTAdNative;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSharedPreferences = getSharedPreferences(ShareCommon.PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_screen);

        mSplashContainer = (FrameLayout) findViewById(R.id.splash_container);

        TTAdManager ttAdManager= TTAdSdk.getAdManager();
        ttAdManager.requestPermissionIfNecessary(getApplicationContext());
        mTTAdNative=ttAdManager.createAdNative(this);

        loadSplashAd();










    }
    private void loadConfig(){
            String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String serial = Build.SERIAL;
            String platform = Build.BRAND;
            String MODEL = Build.MODEL;
            System.out.println(m_szAndroidID);
            System.out.println(platform);
            Map<String, String> merchant = new HashMap<String, String>();

            merchant.put("deviceId",serial);
            merchant.put("platform",platform);
            merchant.put("manufacturer",platform);
            merchant.put("model",MODEL);
            HttpUtils.Post(screenActivity.this,"/load/config", "load", merchant, new BaseCallbackInterface(BaseCallbackInterface.mListener, BaseCallbackInterface.mErrorListener) {
                @Override
                public void onMySuccess(JSONObject result) throws InterruptedException {
                    SharedPreferences.Editor edit = mSharedPreferences.edit();
                    Intent intent=new Intent(screenActivity.this, MainActivity.class);

                    int Rate=0;

                    try{
                        JSONObject jsonObject=result.getJSONObject("data");
                        isOpen=jsonObject.getBoolean("IsOpen");
                        boolean isFirst=jsonObject.getBoolean("IsFirst");
                        String token=jsonObject.getString("Token");
                        String Message=jsonObject.getString("Message");
                        edit.putString(ShareCommon.TOKEN,  token);                   // 存入名为 key , 值为 value (类型为 boolean ) 的数据.

                         Rate=jsonObject.getInt("Rate");
                        int Limit=jsonObject.getInt("Limit");
                        int Interval=jsonObject.getInt("Interval");
                        if (isFirst){
                            String UserId=jsonObject.getString("UserId");
                            intent.putExtra("message",Message);
                            intent.putExtra("isFirst",true);
                        }else{
                            intent.putExtra("isFirst",false);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        isOpen=false;
                        intent.putExtra("isFirst",false);

                    }
                    edit.putBoolean(ShareCommon.Version,  isOpen);                   // 存入名为 key , 值为 value (类型为 boolean ) 的数据.
                    edit.putInt(ShareCommon.Rate,  Rate);                   // 存入名为 key , 值为 value (类型为 boolean ) 的数据.
                    edit.apply();
                    startActivity(intent);
                    finish();


                }

                @Override
                public void onMyError(VolleyError error) throws InterruptedException {
                    SharedPreferences.Editor edit = mSharedPreferences.edit();
                    isOpen=false;
                    edit.putBoolean(ShareCommon.Version,  isOpen);

                    edit.apply();
                    Intent intent=new Intent(screenActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();



                }
            },true);


    }



    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档

        AdSlot  adSlot = new AdSlot.Builder()
                    .setCodeId(mCodeId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .build();




        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, String.valueOf(message));
                showToast(message);
            }

            @Override
            @MainThread
            public void onTimeout() {
loadConfig();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null && mSplashContainer != null && !screenActivity.this.isFinishing()) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
//                    ad.setNotAllowSdkCountdown();
                }else {
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        loadConfig();

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
        loadConfig();

                    }
                });
                if(ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);
//
    }

    private void showToast(String message){
        Toast t = Toast.makeText(this,message, Toast.LENGTH_LONG);
        t.show();
    }


}
