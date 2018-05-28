package com.example.lingluo.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


public class ImageAsyncLoader {
    private ImageView imageView;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                imageView.setImageBitmap((Bitmap) msg.obj);
        }
    };


    public void loadImg(ImageView imageView, final String path){
        this.imageView = imageView;
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message message = Message.obtain();
                message.obj = PhotoBookHandler.getBitmap(path);
                handler.sendMessage(message);
            }
        }.start();
    }
}