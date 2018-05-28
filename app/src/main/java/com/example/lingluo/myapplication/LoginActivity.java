package com.example.lingluo.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.databinding.DataBindingUtil;
import android.widget.Toast;

import com.example.lingluo.model.LoginForm;
import com.example.lingluo.myapplication.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;


import static android.view.View.*;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private  ProgressDialog pd;
    private  Context context = this;
    private LoginForm form = new LoginForm();
    private ActivityLoginBinding binding;
    private boolean isLogining = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setForm(form);
   

    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                    doLogin();
                    break;

                case R.id.btn_go_register:
                    goRegister();
                    break;
            }
    }

    private  void doLogin(){
        if(checkForm()){
            isLogining = true;
            showProgressDialog();
            login();
        }
    }

    private void goRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    private void showProgressDialog() {
        pd = ProgressDialog.show(LoginActivity.this, "", "登录中...");
    }

    private boolean checkForm(){
        if(form.getUserName().equals(null) || form.getUserName().equals("")){
            Toast.makeText(context,"请输入账号",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(form.getPassWord().equals(null) || form.getPassWord().equals("")){
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    private Handler handler = new Handler() {
        @Override
       public void handleMessage(Message message){
                super.handleMessage(message);
                pd.dismiss();
                isLogining = false;
                JSONObject json = null;
                try {
                    json = new JSONObject((String) message.obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(json != null){
                    int errcode = 1;
                    String msg = "";
                    String token = null;
                    try {
                       errcode =  json.getInt("errcode");
                        msg = json.getString("msg");
                        token = json.getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(errcode == 0)
                    {
                        SharedPreferences preferences =  context.getSharedPreferences("user",context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        if(token != null){
                            MyApplication app = (MyApplication) getApplication();
                            app.setUsername(form.getUserName());
                            app.settoken(token);
                            editor.putString("username",form.getUserName());
                            editor.putString("token",token);
                            editor.commit();
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.putExtra("isFromLogin",true);
                            context.startActivity(intent);
                        }else{
                            editor.clear();
                        }


                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("提示")
                                .setMessage(msg)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                });
                        dialog.show();
                    }


            }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("提示")
                            .setMessage("网络错误！")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    dialog.show();
            }
        }


//            Timer timer = new Timer();
//
//            timer.schedule(new TimerTask() {
//                public void run() {
//                    pd.dismiss();
//                    Intent intent = new Intent();
//                    intent.setClass(context,MainActivity.class);
//                    intent.putExtra("userName",binding.getForm().getUserName());
//                    intent.putExtra("passWord",binding.getForm().getPassWord());
//                    intent.putExtra("checked",binding.getForm().getChecked());
//                    context.startActivity(intent);
//                    LoginActivity.this.finish();
//                }
//            }, 1000);
  //     }
    };


    private void login(){

        new Thread(){
            @Override
            public void run(){
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

                    byte[] requestBody = new String("username="+form.getUserName()+"&password="+form.getPassWord()).getBytes("utf-8");
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


    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
