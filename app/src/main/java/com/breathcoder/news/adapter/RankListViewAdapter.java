package com.breathcoder.news.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.breathcoder.news.R;
import com.breathcoder.news.adapter.bean.NewsBean;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;
import java.util.Random;


public class RankListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_NORMAL = 0;
    private static final int ITEM_VIEW_TYPE_GROUP_PIC_AD = 1;
    private static final int ITEM_VIEW_TYPE_SMALL_PIC_AD = 2;
    private static final int ITEM_VIEW_TYPE_LARGE_PIC_AD = 3;
    private static final int ITEM_VIEW_TYPE_VIDEO = 4;
    private static final int ITEM_VIEW_TYPE_VERTICAL_IMG = 5;//竖版图片
    private static final int ITEM_VIEW_TYPE_VIDEO_VERTICAL = 6;//竖版视频
    private Context context;
    private List<NewsBean> list;
    private boolean isOpen;
    int[] coverlst = {
            R.mipmap.cover_1,
            R.mipmap.cover_2,
            R.mipmap.cover_3,
            R.mipmap.cover_4,
            R.mipmap.cover_5,
            R.mipmap.cover_6,
            R.mipmap.cover_7,
            R.mipmap.cover_8,
            R.mipmap.cover_9,
            R.mipmap.cover_10};


    public RankListViewAdapter(Activity context, List<NewsBean> list, boolean isOpen) {
        this.context = context;
        this.list = list;
        this.isOpen = isOpen;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_VIEW_TYPE_NORMAL) {
            return new NewsPhotoViewHolder(inflater.inflate(R.layout.list_item_news, parent, false));

        } else if (viewType==ITEM_VIEW_TYPE_GROUP_PIC_AD ||viewType==ITEM_VIEW_TYPE_SMALL_PIC_AD){

            return new AdImageHolder(inflater.inflate(R.layout.list_item_ad2, parent, false));

        }else{
            return new AdImageHolder(inflater.inflate(R.layout.list_item_ad, parent, false));

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_GROUP_PIC_AD:
                getVideoVIew2(holder, position);
                break;
            case ITEM_VIEW_TYPE_SMALL_PIC_AD:
                getVideoVIew2(holder, position);
                break;
            case ITEM_VIEW_TYPE_LARGE_PIC_AD:
                getVideoVIew(holder, position);
                break;
            case ITEM_VIEW_TYPE_VERTICAL_IMG:
                getVideoVIew(holder, position);
                break;
            case ITEM_VIEW_TYPE_VIDEO:
                getVideoVIew(holder, position);
                break;
            case ITEM_VIEW_TYPE_VIDEO_VERTICAL:
                getVideoVIew(holder, position);
                break;
            default:
                getNormalView(holder, position);
                break;

        }


    }

    private void getVideoVIew(@NonNull RecyclerView.ViewHolder holder, int position) {
        TTNativeExpressAd ttNativeExpressAd = list.get(position).getTtFeedAd();
        View video = ttNativeExpressAd.getExpressAdView();
        if (video != null) {
            if (video.getParent()==null){
                ((AdImageHolder) holder).imageView.removeView(video);

                ((AdImageHolder) holder).imageView.addView(video);
            }

        }



    }
    private void getVideoVIew2(@NonNull RecyclerView.ViewHolder holder, int position) {
        TTNativeExpressAd ttNativeExpressAd = list.get(position).getTtFeedAd();
        View video = ttNativeExpressAd.getExpressAdView();
        if (video != null) {
            if (video.getParent()==null){
                ((AdImageHolder2) holder).imageView.removeView(video);

                ((AdImageHolder2) holder).imageView.addView(video);
            }

        }



    }

    //获取普通的新闻列表信息
    private void getNormalView(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = list.get(position).getTitle();
        String time = list.get(position).getF_time();
        String author = list.get(position).getAuthor();
        int adType = list.get(position).getAdType();
        boolean read = list.get(position).isRead();
        int points = list.get(position).getPoints();

        ((NewsPhotoViewHolder) holder).tx_news_simple_photos_title.setText(title);
        ((NewsPhotoViewHolder) holder).tx_news_simple_photos_time.setText(time);
        ((NewsPhotoViewHolder) holder).tx_news_simple_photos_author.setText(author);
        if (read) {
            ((NewsPhotoViewHolder) holder).can_read.setImageResource(R.drawable.ead);
            ((NewsPhotoViewHolder) holder).moeny_show.setImageResource(R.drawable.not_show_money);
        } else {
            ((NewsPhotoViewHolder) holder).can_read.setImageResource(R.drawable.not_read);
            ((NewsPhotoViewHolder) holder).moeny_show.setImageResource(R.drawable.money);
            ((NewsPhotoViewHolder) holder).money.setText(String.valueOf(points));

        }

        Random random = new Random();
        int index = random.nextInt(10);

        ((NewsPhotoViewHolder) holder).img_news_simple_photos_01.setImageResource(coverlst[index]);
    }

    @Override
    public int getItemViewType(int position) {
        int adType = list.get(position).getAdType();
        if (adType == 0) {
            return ITEM_VIEW_TYPE_NORMAL;
        }
        TTNativeExpressAd ad = list.get(position).getTtFeedAd();
        if (ad == null) {
            return ITEM_VIEW_TYPE_NORMAL;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
            return ITEM_VIEW_TYPE_SMALL_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
            return ITEM_VIEW_TYPE_LARGE_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_GROUP_IMG) {
            return ITEM_VIEW_TYPE_GROUP_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO) {
            return ITEM_VIEW_TYPE_VIDEO;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VERTICAL_IMG) {
            return ITEM_VIEW_TYPE_VERTICAL_IMG;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL) {
            return ITEM_VIEW_TYPE_VIDEO_VERTICAL;
        } else {
            return ITEM_VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdImageHolder extends RecyclerView.ViewHolder {
        FrameLayout imageView;

        public AdImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.adInfo);
        }
    }
    class AdImageHolder2 extends RecyclerView.ViewHolder {
        FrameLayout imageView;

        public AdImageHolder2(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.adInfo2);
        }
    }

    class NewsPhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView tx_news_simple_photos_title;//标题
        private ImageView img_news_simple_photos_01;//单图文模式的唯一一张图
        private TextView tx_news_simple_photos_time;//单图文模式的更新时间
        private TextView tx_news_simple_photos_author;//单图文模式的新闻作者
        private ImageView moeny_show;//单图文模式的新闻作者
        private ImageView can_read;//单图文模式的新闻作者
        private TextView money;//单图文模式的新闻作者

        public NewsPhotoViewHolder(View itemView) {
            super(itemView);
            tx_news_simple_photos_title = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_title);//标题
            img_news_simple_photos_01 = (ImageView) itemView.findViewById(R.id.tx_news_simple_photos_01);//单图文模式的唯一一张图
            tx_news_simple_photos_time = (TextView) itemView.findViewById(R.id.tx_news_simple_photos_time);//单图文模式的更新时间
            tx_news_simple_photos_author = (TextView) itemView.findViewById(R.id.img_news_simple_photos_author);//单图文模式的新闻作者
            moeny_show = (ImageView) itemView.findViewById(R.id.moeny_icon);//单图文模式的新闻作者
            can_read = (ImageView) itemView.findViewById(R.id.can_read);//单图文模式的新闻作者
            money = (TextView) itemView.findViewById(R.id.money_points);//单图文模式的新闻作者

        }
    }








}
