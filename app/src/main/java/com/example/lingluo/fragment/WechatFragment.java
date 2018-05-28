package com.example.lingluo.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lingluo.adapter.MessagePreviewListAdapter;
import com.example.lingluo.helper.ChatSQLiteHelper;
import com.example.lingluo.model.MessagePreviewItem;
import com.example.lingluo.myapplication.ChatActivity;
import com.example.lingluo.myapplication.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by lingluo on 2017/12/7.
 */

public class WechatFragment extends Fragment {

    private List<MessagePreviewItem> messages;
    private ListView listview;
    private int currentListViewPosition;
    private MessagePreviewListAdapter lvAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_wechat, container, false);
        listview = (ListView) view.findViewById(R.id.wechat_list_view);

        initListView();
        registerListViewListener();

        return view;
    }


    private void initListView(){
        messages = getDemoMessageList();
        lvAdapter = new MessagePreviewListAdapter(getContext().getApplicationContext(), messages);
        listview.setAdapter(lvAdapter);
    }

    private void registerListViewListener(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext().getApplicationContext(),ChatActivity.class);
                MessagePreviewItem item = (MessagePreviewItem)lvAdapter.getItem(position);
                String title = item.getTitle();
                int image = item.getImage();
                int uid = item.getUid();
                intent.putExtra("title",title);
                intent.putExtra("image",image);
                intent.putExtra("position",position);
                intent.putExtra("uid",uid);
                startActivityForResult(intent,1);
            }
        });

        listview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                currentListViewPosition = info.position;
                menu.add(0, 0, 0, "标为未读");
                menu.add(0, 1, 0, "置顶聊天");
                menu.add(0, 2, 0, "删除该聊天 ");

            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 2){
            messages.remove(currentListViewPosition);
            lvAdapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }



//    private List<MessagePreviewItem> getDemoMessageList(){
//        List<MessagePreviewItem> list = new ArrayList<MessagePreviewItem>();
//        Resources resources = getResources();
//        for(int i = 0 ; i <= 23 ; ++i){
//            int image = resources.getIdentifier("item"+String.valueOf(i),"drawable",getContext().getPackageName());
//            Long time = new Long((long)0);
//            String title = "标题"+ i;
//            String content = "这是内容"+i;
//            MessagePreviewItem message = new MessagePreviewItem();
//            message.setImage(image);
//            message.setContent(content);
//            message.setTime(time);
//            message.setTitle(title);
//            message.setUid( i + 1);
//            list.add(message);
//        }
//        return list;
//    }

    private List<MessagePreviewItem> getDemoMessageList(){
        List<MessagePreviewItem> list = new ArrayList<MessagePreviewItem>();
        Resources resources = getResources();
        SQLiteDatabase db =  new ChatSQLiteHelper(getContext(),"chat.db",null,1).getReadableDatabase();
        Cursor cursor = db.query("message",new String[]{"uid,type,time,content"},null,null,"uid",null,"time DESC");
        int i = 0;
        while (cursor.moveToNext()){
            int image = resources.getIdentifier("item"+String.valueOf(i),"drawable",getContext().getPackageName());

            Long time = cursor.getLong(2);
            String content = cursor.getString(3);
            int uid = cursor.getInt(0);
            int type = cursor.getInt(1);
            content = type == 0?content:(type == 1?"[图片]":"[语音]");

            String title = "标题"+ i;

            MessagePreviewItem message = new MessagePreviewItem();
            message.setImage(image);
            message.setContent(content);
            message.setTime(time);
            message.setTitle(title);
            message.setUid( uid);
            list.add(message);
            i++;
        }

        return  list;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = data.getExtras().getInt("position");
        String content = data.getExtras().getString("content");
        Long time = data.getExtras().getLong("time");
        MessagePreviewItem item = (MessagePreviewItem) lvAdapter.getItem(position);
        item.setContent(content);
        item.setTime(time);
        lvAdapter.notifyDataSetChanged();

    }
}