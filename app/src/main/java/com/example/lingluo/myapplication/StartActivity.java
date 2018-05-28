package com.example.lingluo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(checkLogin())
        {
            goMain();
        }else{
            goLogin();
        }
    }

    private void goLogin(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent intent = new Intent(mContext,LoginActivity.class);
                mContext.startActivity(intent);
                StartActivity.this.finish();
            }
        }, 2000);
    }

    private void goMain(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("isFromLogin",false);
        startActivity(intent);
        finish();
    }

    private  boolean checkLogin(){
        SharedPreferences preferences =  getSharedPreferences("user",MODE_PRIVATE);
        String username = preferences.getString("username",null);
        String token = preferences.getString("token",null);
        if(username != null && token != null){
            MyApplication app = (MyApplication) getApplication();
            app.setUsername(username);
            app.settoken(token);
           return true;
        }else{
            return false;
        }
    }
}



