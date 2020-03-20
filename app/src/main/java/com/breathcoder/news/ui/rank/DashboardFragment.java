package com.breathcoder.news.ui.rank;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.breathcoder.news.R;
import com.breathcoder.news.adapter.NewsListViewAdapter;
import com.breathcoder.news.adapter.RankListViewAdapter;
import com.breathcoder.news.adapter.bean.RankBean;
import com.breathcoder.news.common.HttpCommon;
import com.breathcoder.news.model.base.BaseCallbackInterface;
import com.breathcoder.news.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button today_btn;
    private Button yes_btn;
    private RecyclerView newsList;

    Boolean isOpen = false;
    private NewsListViewAdapter adapter;

    private List<RankBean> list = new ArrayList<RankBean>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        today_btn=root.findViewById(R.id.today_btn);
        yes_btn=root.findViewById(R.id.yes_btn);
        getDataList(0);
        click();

        return root;
    }


    //初始化列表数据
    private void initListData() {
        newsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));//这里用线性显示 类似于listview
        adapter = new RankListViewAdapter(this.getActivity(), list, isOpen);
        newsList.setAdapter(adapter);

    }

    public void getDataList(final int loadType) {
        String url="";
        if (loadType==0){
            url=HttpCommon.TODAY;
        }else{
            url=HttpCommon.YESTODAY;
        }

        HttpUtils.Get(getActivity(), url, "load", new BaseCallbackInterface(BaseCallbackInterface.mListener, BaseCallbackInterface.mErrorListener) {


            @Override
            public void onMySuccess(JSONObject result) throws InterruptedException {
                System.out.println(result);
                List<RankBean> resultlst= new ArrayList<>();

                try {
                    JSONArray articleList = result.getJSONArray("data");
                    for (int i=0;i<articleList.length();i++){
                        RankBean rankBean= new RankBean();
                        rankBean.setPosition(articleList.getJSONObject(i).getString("Position"));
                        rankBean.setUserName(articleList.getJSONObject(i).getString("UserName"));
                        rankBean.setValue(articleList.getJSONObject(i).getString("Score"));
                        rankBean.setMe(articleList.getJSONObject(i).getBoolean("IsMe"));
                        resultlst.add(rankBean);

                    }

                    list=resultlst;
                    adapter.notifyDataSetChanged();


                }catch (JSONException e){
                    e.printStackTrace();

                }


            }

            @Override
            public void onMyError(VolleyError error) throws InterruptedException {

            }
        },true);}

        public void click(){
        today_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_rank2));
                yes_btn.setTextColor(Color.parseColor("#666666"));
                today_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_rank));
                today_btn.setTextColor(Color.parseColor("#ffffff"));

            }
        });
            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    today_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_rank2));
                    today_btn.setTextColor(Color.parseColor("#666666"));
                    yes_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_rank));
                    yes_btn.setTextColor(Color.parseColor("#ffffff"));

                }
            });
        }


}

