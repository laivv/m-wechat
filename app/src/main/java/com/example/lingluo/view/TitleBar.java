package com.example.lingluo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lingluo.myapplication.R;

/**
 * Created by lingluo on 2017/12/12.
 */

public class TitleBar extends RelativeLayout {
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private TextView tv;
    private View devider;

    private String text;
    private float textSize;
    private int textColor;
    private int leftIcon;
    private int rightIcon;



    public TitleBar(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_main_title_bar,this,true);

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.TitleBar);
        text = a.getString(R.styleable.TitleBar_titleText);
        textSize = a.getDimension(R.styleable.TitleBar_titleTextSize,18f);
        textColor = a.getColor(R.styleable.TitleBar_titleTextColor, Color.WHITE);
        leftIcon = a.getResourceId(R.styleable.TitleBar_titleLeftBtnIcon,0);
        rightIcon = a.getResourceId(R.styleable.TitleBar_titleRightBtnIcon,0);
        a.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftBtn = (ImageButton) findViewById(R.id.btn_bar_back);
        rightBtn = (ImageButton) findViewById(R.id.btn_bar_right);
        tv = (TextView) findViewById(R.id.bar_title);
        devider = findViewById(R.id.bar_devider);
        setAttrs();
    }

    protected void setAttrs(){
        tv.setText(text);
        tv.setTextSize(textSize);
        tv.setTextColor(textColor);
        if(leftIcon == 0){
            leftBtn.setVisibility(GONE);
            devider.setVisibility(GONE);

        }else{
            leftBtn.setImageResource(leftIcon);
        }

        if(rightIcon == 0){
            rightBtn.setVisibility(GONE);
        }else{
            rightBtn.setImageResource(rightIcon);

        }
    }

    public void setLeftBtnOnClickListener(OnClickListener listener){
        leftBtn.setOnClickListener(listener);
    }

    public void setRightBtnOnClickListener(OnClickListener listener){
        rightBtn.setOnClickListener(listener);
    }
    public void setTitleText(String text){
        tv.setText(text);
    }
}
