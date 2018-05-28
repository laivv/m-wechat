package com.example.lingluo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatSQLiteHelper extends SQLiteOpenHelper {

    public ChatSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists message("
                        +"id INTEGER PRIMARY KEY AUTOINCREMENT," //主键
                        +"uid INTEGER," //好友id
                        +"type INTEGER,"//消息类型 0文字 1图片  or 2语音
                        +"source INTEGER," //消息来原 0自己还是 1对方
                        +"time INTEGER," //时间戳
                        +"content TEXT)" //消息类容
        );
        db.execSQL(
                "create table if not exists friend("
                        +"id INTEGER PRIMARY KEY AUTOINCREMENT," //主键
                        +"uid INTEGER," //好友id
                        +"username VARCHAR,"//好友账号
                        +"nick VARCHAR,"//好友昵称
                        +"photo VARCHAR," //好友头像
                        +"province VARCHAR,"//省份
                        +"city VARCHAR)" //城市

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}