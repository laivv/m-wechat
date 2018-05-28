package com.example.lingluo.myapplication;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class BaseActivity extends FragmentActivity {
    private MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getApplication();
        application.addActivity(this);
    }

    protected  void exitApp(){
        application.exitApplication();
    }

    @Override
    protected void onDestroy() {
        application.removeActivity(this);
        super.onDestroy();
    }



}
