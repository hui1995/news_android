package com.breathcoder.news.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.breathcoder.news.R;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

public class DialogShow extends Dialog implements View.OnClickListener {

    //声明xml文件里的组件
    private Button bt_cancel, bt_confirm;

    //声明xml文件中组件中的text变量，为string类，方便之后改
    private String title, code;
    private String cancel, confirm;
    private TTAdNative mTTAdNative;

    //声明两个点击事件，等会一定要为取消和确定这两个按钮也点击事件
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;

    //设置四个组件的内容
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setmTTAdNative(TTAdNative mTTAdNative) {
        this.mTTAdNative = mTTAdNative;
    }

    public void setCancel(String cancel, IOnCancelListener cancelListener) {
        this.cancel = cancel;
        this.cancelListener = cancelListener;
    }

    public void setConfirm(String confirm, IOnConfirmListener confirmListener) {
        this.confirm = confirm;
        this.confirmListener = confirmListener;
    }

    //CustomDialog类的构造方法
    public DialogShow(@NonNull Context context) {
        super(context);
    }

    public DialogShow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    //在app上以对象的形式把xml里面的东西呈现出来的方法！
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了锁定app界面的东西是来自哪个xml文件
        setContentView(R.layout.alert_tip);

        final FrameLayout mExpressContainer = findViewById(R.id.adInfo3);
//
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(code) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(400, 200) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                System.out.println(s);

            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                if (list == null || list.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = list.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);//设置轮播间隔 ms,不调用则不进行轮播展示}
                mTTAd.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {

                    }

                    @Override
                    public void onAdShow(View view, int i) {

                    }

                    @Override
                    public void onRenderFail(View view, String s, int i) {

                    }

                    @Override
                    public void onRenderSuccess(View view, float v, float v1) {
                        mExpressContainer.removeAllViews();
                        mExpressContainer.addView(view);
                    }
                });
                mTTAd.render();//调用render开始渲染广告

            }
        });

        //设置弹窗的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * 0.8);//是dialog的宽度为app界面的80%
        getWindow().setAttributes(p);


        bt_cancel = findViewById(R.id.closeDia);
        bt_confirm = findViewById(R.id.getReward);

        if (!TextUtils.isEmpty(cancel)) {
            bt_cancel.setText(cancel);
        }

        //为两个按钮添加点击事件
        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    //重写onClick方法
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeDia:
                if (cancelListener != null) {
                    cancelListener.onCancel(this);
                }
                dismiss();
                break;
            case R.id.getReward:
                if (confirmListener != null) {
                    confirmListener.onConfirm(this);
                }
                dismiss();//按钮按之后会消失
                break;
        }
    }

    //写两个接口，当要创建一个CustomDialog对象的时候，必须要实现这两个接口
    //也就是说，当要弹出一个自定义dialog的时候，取消和确定这两个按钮的点击事件，一定要重写！
    public interface IOnCancelListener {
        void onCancel(DialogShow dialog);
    }

    public interface IOnConfirmListener {
        void onConfirm(DialogShow dialog);
    }

}
