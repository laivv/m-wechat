package com.example.lingluo.model;

import android.graphics.Bitmap;

/**
 * Created by lingluo on 2017/11/16.
 * 消息项
 */

public class MessageDetailItem {
    private TYPE messageType; //消息类型 文字 or 图片
    private Long time; //发送时间
    private String content; //内容

    private int image; //头像
    private int from;//消息发送方 0自己发的 1对方发的

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    static public enum TYPE{
        TEXT,
        IMAGE
    };



    public TYPE getMessageType() {
        return messageType;
    }

    public void setMessageType(TYPE messageType) {
        this.messageType = messageType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }







    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
