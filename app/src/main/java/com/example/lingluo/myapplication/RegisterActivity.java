package com.example.lingluo.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.lingluo.model.RegisterForm;
import com.example.lingluo.myapplication.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.OnClickListener;

public class RegisterActivity extends BaseActivity implements OnClickListener {
    private  ProgressDialog pd;
    private  Context context = this;
    private RegisterForm form = new RegisterForm();
    private ActivityRegisterBinding binding;
    private boolean isRegistering = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setForm(form);
   

    }


    @Override
    public void onClick(View v) {
        switch ( v.getId()){

            case R.id.btn_register:
                doRegister();
                break;

            case R.id.btn_go_login:
                goLogin();
                break;
        }
    }


    private void doRegister(){
        if(checkForm()){
            isRegistering = true;
            showProgressDialog();
            register();
        }
    }

    private void goLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }



    private void showProgressDialog() {
        pd = ProgressDialog.show(RegisterActivity.this, "", "注册中...");
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

        if(form.getRePassWord().equals(null) || form.getRePassWord().equals("")){
            Toast.makeText(context,"请输入确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(form.getRePassWord().equals(form.getPassWord()))){
            Toast.makeText(context,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    private Handler handler = new Handler() {
        @Override
       public void handleMessage(Message message){
                super.handleMessage(message);
                pd.dismiss();
            isRegistering = false;
                JSONObject json = null;
                try {
                    json = new JSONObject((String) message.obj);
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
                    final int errcode2 =  errcode;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("提示")
                                .setMessage(msg)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                if(errcode2 == 0){
                                                    goLogin();
                                                }
                                            }
                                });
                        dialog.show();



            }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this)
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


    };


    private void register(){

        new Thread(){
            @Override
            public void run(){
                super.run();
                String result = "";
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://10.0.2.2:5000/register");
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
