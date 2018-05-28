package com.example.lingluo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lingluo.myapplication.R;

import java.util.List;


/**
 *
 * 通讯录索引适配器
 * Created by lingluo on 2017/12/11.
 */

public class ContactsIndexListAdapter extends BaseAdapter{

    private List<String> mContactsIndexs;
    private Context mContext;

    public ContactsIndexListAdapter(Context context, List<String> contactsIndexs) {
        mContext = context;
        mContactsIndexs = contactsIndexs;
    }

    @Override
    public int getCount() {
        return mContactsIndexs.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactsIndexs.get(position);
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
            v = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts_index_item,null);
            holder = new ViewHolder();
            holder.text = (TextView) v.findViewById(R.id.conctacts_index_item_text);
            v.setTag(holder);
        }
        else{
            v = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(mContactsIndexs.get(position));
        return v;
    }


    public class ViewHolder {
        public TextView text;
    }

}
