package com.example.lingluo.myapplication;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lingluo.view.TitleBar;


public class SettingActivity extends BaseActivity{
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((LinearLayout)findViewById(R.id.btn_exit_application)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ((TitleBar)findViewById(R.id.bar_title_setting)).setLeftBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void showDialog(){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(SettingActivity.this)
        .setIcon(R.mipmap.app_logo)
        .setTitle("微信")
        .setMessage("选择退出方式")
                        .setNeutralButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
        .setNegativeButton("关闭微信",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitApp();
                    }
                })
        .setPositiveButton("退出登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication app = (MyApplication) getApplication();
                        app.loginOut();
                    }
                });
        dialog.show();
    }
}
