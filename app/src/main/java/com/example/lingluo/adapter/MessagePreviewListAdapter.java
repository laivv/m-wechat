package com.example.lingluo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lingluo.model.MessagePreviewItem;
import com.example.lingluo.myapplication.R;
import com.example.lingluo.util.MyTimeHandler;

import java.util.List;



/**
 * 主界面消息列表适配器
 * Created by lingluo on 2017/12/11.
 */

public class MessagePreviewListAdapter extends BaseAdapter{

    private List<MessagePreviewItem> mMessages;
    private Context mContext;

    public MessagePreviewListAdapter(Context context, List<MessagePreviewItem> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
       ViewHolder holder;
        if (convertView == null) {
            v = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_wechat_item, null);
            holder = new ViewHolder();
            holder.photo = (ImageView) v.findViewById(R.id.wechat_item_photo);
            holder.title = (TextView) v.findViewById(R.id.wechat_item_title);
            holder.content = (TextView) v.findViewById(R.id.wechat_item_content);
            holder.time = (TextView) v.findViewById(R.id.wechat_item_time);
            v.setTag(holder);

        } else {
            v = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        MessagePreviewItem message = mMessages.get(position);
        holder.photo.setImageResource(message.getImage());
        holder.title.setText(message.getTitle());
        holder.content.setText(message.getContent());
        holder.time.setText(MyTimeHandler.simpleFormatTime(message.getTime()));
        return v;
    }


    public class ViewHolder{
        public ImageView photo;
        public TextView title;
        public TextView content;
        public TextView time;
    }
}
