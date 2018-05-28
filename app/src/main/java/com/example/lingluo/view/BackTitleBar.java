package com.example.lingluo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.lingluo.myapplication.R;

/**
 * Created by lingluo on 2017/12/12.
 */

public class BackTitleBar extends TitleBar {
    private Context mContext;
    public BackTitleBar(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_main_back_title_bar,this,true);
        mContext = context;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLeftBtnOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
