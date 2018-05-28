package com.example.lingluo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lingluo.model.FriendItem;
import com.example.lingluo.myapplication.R;

import java.util.List;


/**
 *
 * 通讯录联系人列表适配器
 * Created by lingluo on 2017/12/11.
 */

public class ContactsFriendListAdapter extends BaseAdapter{

    private List<FriendItem> mContactsFriendList;
    private Context mContext;

    public ContactsFriendListAdapter(Context context, List<FriendItem> contactsFriendList) {
        mContext = context;
        mContactsFriendList = contactsFriendList;
    }

    @Override
    public int getCount() {
        return mContactsFriendList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactsFriendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        ViewHolder holder;
        if(convertView == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts_friend_item,null);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.contact_friend_photo);
            holder.text = (TextView) v.findViewById(R.id.contact_friend_text);
            v.setTag(holder);
        }
        else{
            v = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        FriendItem item = mContactsFriendList.get(position);
        holder.image.setImageResource(item.getPhoto());
        holder.text.setText(item.getNick());
        return v;
    }


    public class ViewHolder {
        public ImageView image;
        public TextView text;
    }

}
