package com.breathcoder.news.adapter.bean;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

public class NewsBean {
    private int type;//排版类型
    private String title;//标题
    private String f_time;//发布时间
    private String author;//作者
    private String Cover;
    private int points;
    private boolean isRead;
    private int adType; //1:今日头条,0:无
    private TTNativeExpressAd ttFeedAd;

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public TTNativeExpressAd getTtFeedAd() {
        return ttFeedAd;
    }

    public void setTtFeedAd(TTNativeExpressAd ttFeedAd) {
        this.ttFeedAd = ttFeedAd;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getF_time() {
        return f_time;
    }

    public void setF_time(String f_time) {
        this.f_time = f_time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
