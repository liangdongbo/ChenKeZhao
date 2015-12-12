package com.ckz.crawler.entity;

/**
 * Created by win7 on 2015/11/17.
 * 文章信息
 */
public class Article {
    private String id;//文章ID
    private String title;//标题
    private String href;//标题链接
    private String snapshoot;//内容快照
    private String cover;//封面
    private String time;//发布时间

    public Article(String id, String title, String href, String snapshoot, String cover, String time) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.snapshoot = snapshoot;
        this.cover = cover;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSnapshoot() {
        return snapshoot;
    }

    public void setSnapshoot(String snapshoot) {
        this.snapshoot = snapshoot;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
