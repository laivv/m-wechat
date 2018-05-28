package com.example.lingluo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lingluo.myapplication.R;
import com.example.lingluo.myapplication.SettingActivity;


/**
 * Created by lingluo on 2017/12/7.
 */

public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_me, container, false);
        ((LinearLayout)view.findViewById(R.id.btn_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext().getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }


}