package com.example.lingluo.myapplication;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingluo on 2017/11/17.
 */

public class MyApplication extends Application {
    private List<Activity> activities = new ArrayList<Activity>();
    private String username;
    private String token;

    public String getUsername (){
        return  username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public void addActivity(Activity activity){
        if(!activities.contains(activity)){
            activities.add(activity);
        }
    }

    public void removeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
            activity.finish();
        }
    }

    public void loginOut(){
        SharedPreferences preferences =  getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        exitApplication();
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void exitApplication(){
       for(Activity activity : activities){
           activity.finish();
       }
       //结束程序时清除通知栏
        NotificationManager manager=(NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public Activity getLastActivity(){
        return activities.get(activities.size() - 1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String gettoken() {
        return token;
    }

    public void settoken(String token) {
        this.token = token;
    }
}
