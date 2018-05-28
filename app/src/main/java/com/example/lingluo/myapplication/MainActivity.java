package com.example.lingluo.myapplication;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lingluo.fragment.ContactsFragment;
import com.example.lingluo.fragment.DiscoverFragment;
import com.example.lingluo.fragment.MeFragment;
import com.example.lingluo.fragment.WechatFragment;
import com.example.lingluo.service.MyService;
import com.example.lingluo.util.MyNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext = this;
    private Date date;
    private Toast toast;
    private ViewPager viewpager;
    private List<Fragment> views = new ArrayList<Fragment>();
    private int[][] btnSources = {
            {R.id.b1,R.id.b1_text,R.mipmap.btn_wechat,R.mipmap.btn_wechat_active,R.id.tab1},
            {R.id.b2,R.id.b2_text,R.mipmap.btn_contacts,R.mipmap.btn_contacts_active,R.id.tab2},
            {R.id.b3,R.id.b3_text,R.mipmap.btn_discover,R.mipmap.btn_discover_active,R.id.tab3},
            {R.id.b4,R.id.b4_text,R.mipmap.btn_me,R.mipmap.btn_me_active,R.id.tab4}
    };

    private  IBinder binder = null;







    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyNotification.setAppNotification(mContext);

        views = getViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }
        };
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBtnStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        ((ImageButton)findViewById(R.id.btn_bar_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_add_popwindow,null,false);
                PopupWindow window = new PopupWindow(mView,400,400,true);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setOutsideTouchable(true);
                window.setTouchable(true);
                window.showAsDropDown((ImageButton)findViewById(R.id.btn_bar_add), 0, 30);
            }
        });



        ServiceConnection conn = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;

            }
        };
        Intent  intent = new Intent(this, MyService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
        checkLogin();

    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            JSONObject json = null;
            try
            {json = new JSONObject((String) message.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(json != null){
                int errcode = 1;
                String msg = "";

                try {
                    errcode =  json.getInt("errcode");
                    msg = json.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(errcode == 2)
                {
                    SharedPreferences preferences =  mContext.getSharedPreferences("user",mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    MyApplication app = (MyApplication) getApplication();
                    app.setUsername(null);
                    app.settoken(null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                                    {
                                        return true;
                                    }
                                    else
                                    {
                                        return false;
                                    }
                                }
                            })
                            .setTitle("提示")
                            .setMessage(msg)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                    MainActivity.this.finish();
                                }
                            });
                    dialog.show();
                }
            }
        }
    };
    private void checkLogin(){
        Intent intent = getIntent();
        boolean isFromLogin = intent.getExtras().getBoolean("isFromLogin",false);
        if(!isFromLogin){
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String result = "";
                        HttpURLConnection connection = null;
                        try{
                            URL url = new URL("http://10.0.2.2:5000/login");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Charset","utf-8");
                            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                            connection.setConnectTimeout(5000);
                            OutputStream outputStream = connection.getOutputStream();
                            MyApplication app = (MyApplication) getApplication();
                            byte[] requestBody = new String("username="+app.getUsername()+"&token="+app.gettoken()).getBytes("utf-8");
                            outputStream.write(requestBody);
                            outputStream.flush();
                            outputStream.close();
                            InputStream inputStream =  connection.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String data = null;
                            StringBuffer stringBuffer = new StringBuffer();
                            while ((data = bufferedReader.readLine()) != null){
                                stringBuffer.append(data);
                            }
                            result = stringBuffer.toString();
                            inputStreamReader.close();

                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            if(connection != null){
                                connection.disconnect();
                            }
                        }
                        Message message = new Message();
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                }.start();
        }
    }


    private  List<Fragment> getViews(){
        List<Fragment> views = new ArrayList<>();
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        views.add(new WechatFragment());
        views.add(new ContactsFragment());
        views.add(new DiscoverFragment());
        views.add(new MeFragment());
        return  views;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);

    }

    private void setBtnStatus(int index){
        for(int i = 0 ,len = btnSources.length ;i < len ;++i){
            int[] item = btnSources[i];
            int image,color;
            if(i == index){
                image = item[3];
                color = Color.rgb(62,185,78);
            }else{
                image = item[2];
                color = Color.rgb(119,119,119);
            }
            ((ImageButton)findViewById(item[0])).setImageResource(image);
            ((TextView)findViewById(item[1])).setTextColor(color);
        }
    }


    @Override
    public void onClick(View v) {

        int index = 0;

        for(int i = 0 ,len = btnSources.length ;i < len ;++i){
                if(btnSources[i][4] == v.getId()){
                    index = i;
                    break;
                }
            }
        viewpager.setCurrentItem(index);
        setBtnStatus(index);
    }








}
