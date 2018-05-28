package com.example.lingluo.model;

/**
 * Created by lingluo on 2017/11/16.
 * 登录表单
 */

public class LoginForm {
    private String userName = "";
    private String passWord = "";
    private boolean checked = false;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
