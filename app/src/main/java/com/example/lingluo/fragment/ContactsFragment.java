package com.example.lingluo.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lingluo.adapter.ContactsFriendListAdapter;
import com.example.lingluo.adapter.ContactsIndexListAdapter;
import com.example.lingluo.model.FriendItem;
import com.example.lingluo.myapplication.DetailActivity;
import com.example.lingluo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingluo on 2017/12/7.
 */

public class ContactsFragment extends Fragment {
    private ListView listViewIndex;
    private ListView listViewFriend;
    private ContactsIndexListAdapter indexListadapter;
    private ContactsFriendListAdapter friendListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_contacts, container, false);

        listViewFriend = (ListView) view.findViewById(R.id.conctacts_friend_list);
        friendListAdapter = new ContactsFriendListAdapter(getContext().getApplicationContext(),getFriendList());
        listViewFriend.setAdapter(friendListAdapter);

        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendItem item = (FriendItem) friendListAdapter.getItem(position);
                Intent intent = new Intent(getContext().getApplicationContext(),DetailActivity.class);
                intent.putExtra("uid",item.getUid());
                intent.putExtra("username",item.getUsername());
                intent.putExtra("photo",item.getPhoto());
                intent.putExtra("nick",item.getNick());
                intent.putExtra("province",item.getProvince());
                intent.putExtra("city",item.getCity());
                startActivity(intent);
            }
        });

        listViewIndex = (ListView) view.findViewById(R.id.conctacts_index_items);
        indexListadapter = new ContactsIndexListAdapter(getContext().getApplicationContext(),getDemoIndexs());
        listViewIndex.setAdapter(indexListadapter);

        return view;
    }


    private  List<String> getDemoIndexs(){
        List<String> indexs = new ArrayList<String>();
        String[] contacts_indexs = new String[]{
                "A", "B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
        };
        for(String item:contacts_indexs){
            indexs.add(item);
        }
        return indexs;
    }


    private  List<FriendItem> getFriendList(){
        List<FriendItem> list = new ArrayList<FriendItem>();
        String [] nickList = new String[]{
                "零落",
                "jack马",
                "pony马化腾",
                "雷军",
                "李彦宏"
        };
        Resources resources = getResources();
        for(int i = 0 ; i < nickList.length ;++i){
            int image = resources.getIdentifier("item"+String.valueOf(i),"drawable",getContext().getPackageName());
            FriendItem item = new FriendItem();
            item.setNick(nickList[i]);
            item.setUsername("wechat_00"+i);
            item.setPhoto(image);
            item.setUid(i);
            item.setProvince("四川");
            item.setCity("成都");
            list.add(item);
        }
        return list;
    }

}