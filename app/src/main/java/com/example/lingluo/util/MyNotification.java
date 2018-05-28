package com.example.lingluo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.lingluo.myapplication.MainActivity;
import com.example.lingluo.myapplication.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MyNotification {

    public static final void setSystemDefaultNotification(Context mContext,String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.app_logo_small);
        builder.setShowWhen(true);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        NotificationManager manager=(NotificationManager) mContext.getSystemService(mContext.getApplicationContext().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    public static final  void setAppNotification(Context mContext){
        setNotification(mContext,"微信","正在后台运行",System.currentTimeMillis(),R.mipmap.app_logo,false,false,false,0);
    }


    public static final  void setMessageNotification(Context mContext,String title,String content,Long time, int icon,int id){
        setNotification(mContext,title,content,time,icon,true,true,true,id);
    }


    public  static  final  void setNotification(Context mContext,String title,String content,Long time,int icon,boolean showSmallIcon,boolean autoCancel,boolean showTicker,int id){
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_my_notification);
        remoteViews.setTextViewText(R.id.mynotification_title,title);
        remoteViews.setTextViewText(R.id.mynotification_content,content);
        remoteViews.setTextViewText(R.id.mynotification_time,MyTimeHandler.simpleFormatTime(time));
        remoteViews.setImageViewResource(R.id.mynotification_icon,icon);
        if(showSmallIcon){
            remoteViews.setViewVisibility(R.id.mynotification_icon_small,VISIBLE);
        }
        else{
            remoteViews.setViewVisibility(R.id.mynotification_icon_small,GONE);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.app_logo_small)
                .setAutoCancel(autoCancel)
                .setOngoing(true);
        if(showTicker){
            builder.setTicker(content);//titlebar 文字提示（类似翻滚效果）

        }


        builder.setContent(remoteViews);
        Notification notification = builder.build();

        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(mContext,MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent pendingIntent=PendingIntent.getActivity(mContext,id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        NotificationManager manager=(NotificationManager) mContext.getSystemService(mContext.getApplicationContext().NOTIFICATION_SERVICE);
        manager.notify(id,notification);
    }

}