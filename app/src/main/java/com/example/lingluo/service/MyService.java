package com.example.lingluo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lingluo.myapplication.MyApplication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by lingluo on 2017/12/12.
 */

public class MyService extends Service {
    Socket socket;
    PrintWriter pw;
    InputStream in;
    byte[] buff = new byte[4096];
    DataInputStream dis;
    public static final int TEXT = 0x001;
    public static final int IMAGE = 0x002;
    public static final int VOICE = 0x003;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service", "onCreate: ");
        initSocket();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("service", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);

    }



    public void sendMessage(int uid,int type,String content){
            String msg_type = "text";
            switch (type){
                case TEXT:
                    msg_type = "text";
                    break;

                case IMAGE:
                    msg_type = "image";
                    break;

                case VOICE:
                    msg_type = "voice";
                    break;
                default:
                    msg_type = "text";
            }
        final String data = "{\"uid\":\"" + uid  + "\",\"type\":\"" + msg_type + "\",\"content\":\""+ content + "\"}";
        new Thread(new Runnable() {
            @Override
            public void run() {
                pw.print(data);
                pw.flush();
            }
        }).start();

    }



    public void initSocket(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("10.0.2.2",5001);
                    pw = new PrintWriter(socket.getOutputStream(),true);
                    OutputStream outputStream = socket.getOutputStream();
                    MyApplication app = (MyApplication)getApplication();
                    outputStream.write(("{\"type\":\"connect\",\"uid\":\"" + app.getUsername() + "\",\"token\":\""+ app.gettoken() +"\"}").getBytes("utf-8"));
                    outputStream.flush();
                    in = socket.getInputStream();
                    dis = new DataInputStream(in);


                    while (true){

                        int rcvLen = dis.read(buff);
                        String text = new String(buff,0,rcvLen,"utf-8");
                        Log.i("mysocket",text);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service", "onDestroy: ");
    }


   public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }


}
