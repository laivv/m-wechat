package com.example.lingluo.myapplication;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lingluo.adapter.MessageDetailListAdapter;
import com.example.lingluo.model.MessageDetailItem;
import com.example.lingluo.service.MyService;
import com.example.lingluo.util.MyNotification;
import com.example.lingluo.util.PhotoBookHandler;
import com.example.lingluo.view.TitleBar;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;
import com.example.lingluo.helper.ChatSQLiteHelper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ChatActivity extends BaseActivity{
    private EditText editText;
    private Context mContext = this;
    private ListView listView;
    private List<MessageDetailItem> messages;
    private MessageDetailListAdapter messageDetailListAdapter;
    private TitleBar titleBar;
    private Button btn_send;
    private ImageButton btn_add_picture;
    private ImageButton btn_chat_converter;
    private Button btn_record_voice;
    private int position;
    private int inputStatus = 0;//当前切换到了语音输入还是文字 0文字 1语音

    private int image;//好友头像
    private int uid;//好友id
    private String title;
    private IBinder binder = null;
    MyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = View.inflate(this,R.layout.activity_chat,null);
        setContentView(mainView);
        initView();
        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        position = intent.getExtras().getInt("position");
        image = intent.getExtras().getInt("image");
        uid = intent.getExtras().getInt("uid");
        titleBar = (TitleBar) findViewById(R.id.title_bar_1);

        titleBar.setTitleText(title);
        titleBar.setLeftBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishBack();
            }
        });

        titleBar.setRightBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatInfoActivity.class);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s == null || "".equals(s.toString())){
                    setButtonStatus(0);
                }else{
                    setButtonStatus(1);
                }
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                String content = editText.getText().toString();
               if(content == null || "".equals(content)){
                   Toast.makeText(mContext,"请输入消息",Toast.LENGTH_SHORT).show();
                   return;
               }

              MessageDetailItem message = new MessageDetailItem();
               message.setTime(System.currentTimeMillis());
               message.setContent(content);
               message.setMessageType(MessageDetailItem.TYPE.TEXT);
               message.setFrom(0);
               message.setImage(R.drawable.item2);
               messages.add(message);

//               MessageDetailItem message2 = new MessageDetailItem();
//               message2.setTime(System.currentTimeMillis());
//               message2.setContent("你发送的消息内容为:"+editText.getText().toString());
//               message2.setFrom(1);
//               message2.setImage(image);
//               messages.add(message2);

               messageDetailListAdapter.notifyDataSetChanged();
               editText.setText("");
//               setButtonStatus(0);
               listView.setSelection(messageDetailListAdapter.getCount() - 1);

               Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
               long[] pattern = {0, 200, 300, 200}; // OFF/ON/OFF/ON......
               vibrator.vibrate(pattern, -1);

               MyNotification.setMessageNotification(mContext,title,content,System.currentTimeMillis(),image,uid);


               ChatSQLiteHelper helper = new ChatSQLiteHelper(mContext,"chat.db",null,1);
               SQLiteDatabase db =  helper.getReadableDatabase();
               ContentValues values = new ContentValues();
               values.put("uid",uid);
               values.put("type",0);
               values.put("time",System.currentTimeMillis());
               values.put("content",content);
               db.insert("message","id",values);
               service.sendMessage(uid,MyService.TEXT,content);


           }
       });




        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });



        btn_chat_converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputStatus = inputStatus == 0 ? 1 : 0;
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(inputStatus == 0){

                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            editText.requestFocus();
                        }
                    });
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    btn_chat_converter.setImageResource(R.mipmap.btn_chat_voice);
                    editText.setVisibility(VISIBLE);
                    btn_record_voice.setVisibility(GONE);
                }else{
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    btn_chat_converter.setImageResource(R.mipmap.btn_chat_keyboard);
                    editText.setVisibility(GONE);
                    btn_record_voice.setVisibility(VISIBLE);
                }
            }
        });




        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
               binder = service;
                ChatActivity.this.service = ((MyService.MyBinder)binder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        };
        Intent serviceIntent = new Intent(this,MyService.class);
        bindService(serviceIntent,connection,BIND_AUTO_CREATE);




    }







    private void openAblum(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType( "image/*");
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
       if(data != null || data.getExtras() != null){
           String imagePath = PhotoBookHandler.getStringPath(data,this);
           MessageDetailItem message = new MessageDetailItem();
           message.setMessageType(MessageDetailItem.TYPE.IMAGE);
           message.setImage(R.drawable.item2);
           message.setContent(imagePath);
           message.setTime(System.currentTimeMillis());
           messages.add(message);
           messageDetailListAdapter.notifyDataSetChanged();
           listView.setSelection(messageDetailListAdapter.getCount() - 1);

           Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
           long[] pattern = {0, 200, 300, 200}; // OFF/ON/OFF/ON......
           vibrator.vibrate(pattern, -1);
           MyNotification.setMessageNotification(mContext,title,"[图片]",System.currentTimeMillis(),image,uid);
       }

    }

    private void initView(){
        btn_record_voice = (Button) findViewById(R.id.btn_record_voice);
        btn_chat_converter = (ImageButton) findViewById(R.id.btn_chat_converter);
        btn_send = (Button) findViewById(R.id.btn_send_message);
        btn_add_picture = (ImageButton) findViewById(R.id.btn_add_picture);
        editText = (EditText) findViewById(R.id.chat_edit_text);
        messages = new ArrayList<MessageDetailItem>();
        listView = (ListView) findViewById(R.id.chat_message_listview);
        messageDetailListAdapter = new MessageDetailListAdapter(mContext,messages);
        listView.setAdapter(messageDetailListAdapter);
        getMessageList();

    }

    private void setButtonStatus(int code){
        if(code == 0){
            btn_add_picture.setVisibility(View.VISIBLE);
            btn_send.setVisibility(GONE);
        }else{
            btn_add_picture.setVisibility(GONE);
            btn_send.setVisibility(View.VISIBLE);
        }
    }

    private void finishBack() {

        Intent intent = new Intent();
        String content = "";
        Long time = new Long((long)0);

        if(messageDetailListAdapter.getCount() >= 1){
            MessageDetailItem message = (MessageDetailItem) messageDetailListAdapter.getItem(messageDetailListAdapter.getCount() - 1);
            content = message.getContent();
            time = message.getTime();
            if(message.getMessageType() == MessageDetailItem.TYPE.IMAGE){
                content="[图片]";
            }
        }

        intent.putExtra("content",content);
        intent.putExtra("position",position);
        intent.putExtra("time",time);
        setResult(1,intent);
        finish();



    }

    @Override
    public void onBackPressed() {
        finishBack();
    }


    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
             }
             else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }else{
            openAblum();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openAblum();
    }



    private void getMessageList(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db =  new ChatSQLiteHelper(mContext,"chat.db",null,1).getReadableDatabase();
                Cursor cursor = db.query("message",new String[]{"uid","type","time","content","source"},"uid=?",new String[]{uid + ""},null,null,"time ASC","10");
                while (cursor.moveToNext()){
                    int uid = cursor.getInt(0);
                    int type = cursor.getInt(1);
                    long time = cursor.getLong(2);
                    String content = cursor.getString(3);
                    int source = cursor.getInt(4);

                    MessageDetailItem item = new MessageDetailItem();
                    item.setContent(content);
                    item.setMessageType(type == 0?MessageDetailItem.TYPE.TEXT:MessageDetailItem.TYPE.IMAGE);
                    item.setTime(time);
                    item.setFrom(source);
                    messages.add(item);

                }
                cursor.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        messageDetailListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();


    }





}
