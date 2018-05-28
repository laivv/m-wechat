package com.example.lingluo.model;

/**
 * Created by lingluo on 2017/11/16.
 * 消息预览项
 */

public class MessagePreviewItem {
    private Long time;
    private String title;
    private String content;
    private int image;
    private int uid;//好友id



    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
