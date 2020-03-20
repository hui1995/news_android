package com.breathcoder.news.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.breathcoder.news.R;
import com.breathcoder.news.TTAdManagerHolder;
import com.breathcoder.news.adapter.NewsListViewAdapter;
import com.breathcoder.news.adapter.bean.NewsBean;
import com.breathcoder.news.common.HttpCommon;
import com.breathcoder.news.common.ShareCommon;
import com.breathcoder.news.listner.EndlessRecyclerOnScrollListener;
import com.breathcoder.news.model.base.BaseCallbackInterface;
import com.breathcoder.news.utils.HttpUtils;
import com.breathcoder.news.views.DialogShow;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.breathcoder.news.common.ShareCommon.PREFS_NAME;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //广告相关
    private List<TTFeedAd> mData;
    private TTAdNative mTTAdNative;
    private int rate=0;



    private RecyclerView newsList;
    private SwipeRefreshLayout mRefreshLayout;
    private static SharedPreferences mSharedPreferences = null;
    Boolean isOpen = false;
    private NewsListViewAdapter adapter;
    List<TTNativeExpressAd> adsListInfo = new ArrayList<>();

    private List<NewsBean> list = new ArrayList<NewsBean>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent i = this.getActivity().getIntent();  //直接获取传过来的intent
        homeViewModel = new HomeViewModel();
        boolean isFist = i.getBooleanExtra("isFirst", false);

        //广告相关
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(this.getActivity());
        //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());







        //弹出框
        if (isFist) {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("注意")
                    .setMessage("欢迎您第100006位用户，首次注册，赠送您66J币")
                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).create().show();
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        newsList = root.findViewById(R.id.news_list);
        //刷新
        mRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorScheme(android.R.color.white,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        RandminDataAd(2);
                    }
                }, 3000);

            }
        });
        newsList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                RandminDataAd(3)  ;          }
        });



        try {
            mSharedPreferences = this.getActivity().getSharedPreferences(ShareCommon.PREFS_NAME, Context.MODE_PRIVATE);
            isOpen = mSharedPreferences.getBoolean(ShareCommon.Version, false);
            String info = mSharedPreferences.getString(ShareCommon.TOKEN, "");
            rate = mSharedPreferences.getInt(ShareCommon.Rate, 0);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        initData();


        return root;
    }


    public void initData() {
        initListData();
        //
        RandminDataAd(1);



    }

    //获取请求数据类型
    public void RandminDataAd(int type){
        //获取请求广告类型,
        int num=(int)(Math.random()*100);
        if (adsListInfo.isEmpty()){
            loadListAd(type);

        }else{
            if (num<rate){
                loadListAd(type);
            }else{
                getDataList(type,1);
            }
        }
    }

    //初始化列表数据
    private void initListData() {
        newsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));//这里用线性显示 类似于listview
        adapter = new NewsListViewAdapter(this.getActivity(), list, isOpen);
        newsList.setAdapter(adapter);

    }



    //获取后台数据 type 1初始化，2顶部刷新，3底部刷新
    public void getDataList(final int loadType, final int adType) {

        HttpUtils.Get(getActivity(), HttpCommon.ARTICLE_LIST, "load", new BaseCallbackInterface(BaseCallbackInterface.mListener, BaseCallbackInterface.mErrorListener) {
            @Override
            public void onMySuccess(JSONObject result) throws InterruptedException {

                try {

                    List<NewsBean> arraylist = new ArrayList<NewsBean>();
                    int count=0;

                    JSONArray articleList = result.getJSONArray("data");
                    for (int i = 0; i < articleList.length(); i++) {
                        NewsBean newsBean = new NewsBean();
                        String str=articleList.getJSONObject(i).getString("CreateTime");
                        try {

                            String dateStr=str.substring(0,10);
                            newsBean.setF_time(dateStr);
                        }catch (Exception e){
                            continue;
                        }
                        newsBean.setTitle(articleList.getJSONObject(i).getString("Title"));
                        newsBean.setAuthor(articleList.getJSONObject(i).getString("AuthorName"));
                        newsBean.setCover(articleList.getJSONObject(i).getString("Cover"));
                        newsBean.setPoints(articleList.getJSONObject(i).getInt("Price"));
                        newsBean.setRead(articleList.getJSONObject(i).getBoolean("IsRead"));
                        newsBean.setAdType(0);
                        arraylist.add(newsBean);

                        if (rate!=0){
                            if (i%4==0){
                                if (count>=adsListInfo.size()){
                                    continue;
                                }
                                newsBean.setAdType(adType);
                                int num=(int)(Math.random()*adsListInfo.size());

                                newsBean.setTtFeedAd(adsListInfo.get(num));
                                count+=1;
                            }
                        }


                    }
                    if (loadType == 1) {
                        list.addAll(0, arraylist);
                        adapter.notifyDataSetChanged();

                    } else if (loadType == 2) {
                        list.clear();
                        mRefreshLayout.setRefreshing(false);
                        list.addAll(0, arraylist);
                        adapter.notifyDataSetChanged();


                    }else if (loadType==3){
                        list.addAll(arraylist);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMyError(VolleyError error) throws InterruptedException {

            }
        }, true);


    }




    //加载头条广告,类型1

    private void loadListAd(final int type) {

        //feed广告请求类型参数


        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("945099176")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(350, 350)
                .setAdCount(3)
                .build();
        //调用feed广告异步请求接口
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                //0为不加载
                getDataList(type,0);
                //
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                bindAdListener(ads,type);

             }
        });
    }


    private void bindAdListener(final List<TTNativeExpressAd> ads,int type) {
        for (final TTNativeExpressAd ad : ads) {
            final TTNativeExpressAd adTmp = ad;

            adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int type) {
                }

                @Override
                public void onAdShow(View view, int type) {
                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    //返回view的宽高 单位 dp
                    adsListInfo.add(ad);

                }
            });

            getDataList(type,1);

            ad.render();

        }

    }



    //提示框
    public void diaoueBox() {

        DialogShow customDialog = new DialogShow(Objects.requireNonNull(getActivity()));
        customDialog.setTitle("今日还剩余多少次机会,");
        customDialog.setCode("945099185");
        customDialog.setmTTAdNative(mTTAdNative);
        customDialog.setCancel("cancel", new DialogShow.IOnCancelListener() {
            @Override
            public void onCancel(DialogShow dialog) {
            }
        });
        customDialog.setConfirm("confirm", new DialogShow.IOnConfirmListener(){
            @Override
            public void onConfirm(DialogShow dialog) {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId("945099188")
                        .setSupportDeepLink(true)
                        .setAdCount(1)
                        .setImageAcceptedSize(1080, 1920)
                        .setRewardName("金币") //奖励的名称
                        .setRewardAmount(3)   //奖励的数量
                        //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                        //可设置为空字符串
                        .setUserID("user123")
                        .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                        .setMediaExtra("media_extra") //用户透传的信息，可不传
                        .build();
                mTTAdNative.loadRewardVideoAd(adSlot,new TTAdNative.RewardVideoAdListener(){

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                        ttRewardVideoAd.showRewardVideoAd(getActivity());

                    }

                    @Override
                    public void onRewardVideoCached() {

                    }
                });
            }

        });
        customDialog.show();






    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (adsListInfo != null) {
            for (TTNativeExpressAd ad : adsListInfo) {
                if (ad != null) {
                    ad.destroy();
                }
            }
        }
        adsListInfo=null;
    }
}