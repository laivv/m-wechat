package com.example.lingluo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lingluo.model.MessageDetailItem;
import com.example.lingluo.myapplication.R;
import com.example.lingluo.util.ImageAsyncLoader;
import com.example.lingluo.util.MyTimeHandler;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 与好友聊天界面消息listview 的适配器
 * Created by lingluo on 2017/12/11.
 */

public class MessageDetailListAdapter extends BaseAdapter{

    private List<MessageDetailItem> mMessages;
    private Context mContext;

    public MessageDetailListAdapter(Context context, List<MessageDetailItem> messages){
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
    public int getItemViewType(int position){
        MessageDetailItem item = mMessages.get(position);
        int viewtype = item.getFrom();
        if(viewtype == 0){
            if(item.getMessageType() == MessageDetailItem.TYPE.TEXT){
                return 0;
            }else{
                return 1;
            }
        }else{
            if(item.getMessageType() == MessageDetailItem.TYPE.TEXT){
                return 2;
            }else{
                return 3;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageDetailItem message = mMessages.get(position);
        MessageDetailItem.TYPE messageType = message.getMessageType();
        View v;
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            if(getItemViewType(position)== 0) {
                v = LayoutInflater.from(mContext).inflate(R.layout.layout_chat_message_self, null);
                holder.time = (TextView) v.findViewById(R.id.chat_send_time);
                holder.content = (TextView) v.findViewById(R.id.chat_send_content);
                holder.image = (ImageView) v.findViewById(R.id.chat_send_photo);

            }else if(getItemViewType(position)== 1){
                    v = LayoutInflater.from(mContext).inflate(R.layout.layout_chat_message_self_image,null);
                    holder.time = (TextView) v.findViewById(R.id.chat_send_time_image);
                    holder.content = (ImageView) v.findViewById(R.id.chat_send_content_image);
                    holder.image = (ImageView) v.findViewById(R.id.chat_send_photo_image);
                }
            else if(getItemViewType(position)== 2){
                    v = LayoutInflater.from(mContext).inflate(R.layout.layout_chat_message_friend,null);
                    holder.time = (TextView) v.findViewById(R.id.chat_receive_time);
                    holder.content = (TextView) v.findViewById(R.id.chat_receive_content);
                    holder.image = (ImageView) v.findViewById(R.id.chat_receive_photo);
            }else{
                    v = LayoutInflater.from(mContext).inflate(R.layout.layout_chat_message_friend_image,null);
                    holder.time = (TextView) v.findViewById(R.id.chat_receive_time_image);
                    holder.content = (ImageView) v.findViewById(R.id.chat_receive_content_image);
                    holder.image = (ImageView) v.findViewById(R.id.chat_receive_photo_image);
            }
            v.setTag(holder);
        }
        else{
            v = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        if(messageType == MessageDetailItem.TYPE.TEXT){
            ((TextView)holder.content).setText(message.getContent());
        }
        if(messageType == MessageDetailItem.TYPE.IMAGE){
            new ImageAsyncLoader().loadImg((ImageView)holder.content,message.getContent());//使用多线程加载
            //((ImageView)holder.content).setImageBitmap(message.getImageContent());
        }

        int secondsDiff = 0;
        if(position >= 1){
            secondsDiff =  MyTimeHandler.secondsDifference(((MessageDetailItem)getItem(position - 1)).getTime(),message.getTime());
        }

        if(position < 1 || secondsDiff > 1){
            holder.time.setText(MyTimeHandler.formatTime(message.getTime()));
            holder.time.setVisibility(VISIBLE);
        }else{
            holder.time.setText("");
            holder.time.setVisibility(GONE);
        }

        holder.image.setImageResource(message.getImage());
        return v;
    }




     class ViewHolder{
         public ImageView image;
         public View content;
         public TextView time;
    }


}
