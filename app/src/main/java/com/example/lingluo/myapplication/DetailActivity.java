package com.example.lingluo.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.example.lingluo.model.FriendItem;
import com.example.lingluo.myapplication.databinding.ActivityContactDetailBinding;


public class DetailActivity extends BaseActivity implements View.OnClickListener {
//    private  int uid;
//    private  int photo;
//    private  String nick;
//    private  String username;
//    private  String province;
//    private  String city;
    public FriendItem userInfo = new FriendItem();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityContactDetailBinding binding =  DataBindingUtil.setContentView(this,R.layout.activity_contact_detail,null);

        Bundle bundle = getIntent().getExtras();
        userInfo.setUid(bundle.getInt("uid"));
        userInfo.setCity(bundle.getString("city"));
        userInfo.setProvince(bundle.getString("province"));
        userInfo.setPhoto(bundle.getInt("photo"));
        userInfo.setNick(bundle.getString("nick"));
        userInfo.setUsername(bundle.getString("username"));
        binding.setUserInfo(userInfo);
        ((ImageView)findViewById(R.id.detail_photo)).setImageResource(bundle.getInt("photo"));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.detail_btn_send_message:
                goChat();
                break;
        }
    }

    private void goChat(){
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("title",userInfo.getNick());
        intent.putExtra("uid",userInfo.getUid());
        intent.putExtra("image",userInfo.getPhoto());
        intent.putExtra("position",1111111);
        startActivity(intent);
    }
}
