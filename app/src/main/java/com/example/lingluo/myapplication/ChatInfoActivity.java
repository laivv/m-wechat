package com.example.lingluo.myapplication;


import android.os.Bundle;
import android.view.View;

import com.example.lingluo.view.TitleBar;


public class ChatInfoActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);


        ((TitleBar)findViewById(R.id.bar_title_chatinfo)).setLeftBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}
