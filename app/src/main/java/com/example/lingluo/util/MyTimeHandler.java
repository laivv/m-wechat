package com.example.lingluo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTimeHandler{
    public static String formatTime(Long timeStamp){
        if(timeStamp ==0){
            return "";
        }
        Date date = new Date(timeStamp);
        String format = "yyyy年MM月dd日 HH:mm";
        Long nowStamp = System.currentTimeMillis();
        if(isSameDay(nowStamp,timeStamp)){
            format = "HH:mm";
        }else{
            int s = (int) Math.floor((nowStamp - timeStamp) /(3600 *24 * 1000));
            if(s == 1){
                format =  "昨天 HH:mm";
            }
            else if(s == 2){
                format =  "前天 HH:mm";
            }
            else{
                format = "MM月dd日 HH:mm";
            }

        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    public static String simpleFormatTime(Long timeStamp){
        if(timeStamp ==0){
            return "";
        }
        Date date = new Date(timeStamp);
        String format = "yyyy年MM月dd日 HH:mm";
        Long nowStamp = System.currentTimeMillis();
        if(isSameDay(nowStamp,timeStamp)){
            format = "HH:mm";
        }else{
            int s = (int) Math.floor((nowStamp - timeStamp) /(3600 *24 * 1000));
            if(s == 1){
                return "昨天";
            }
            if(s == 2){
                return  "前天";
            }
            format = "MM月dd日";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    public static boolean isSameDay(Long d1,Long d2){
        Date date1 =  new Date(d1);
        Date date2 = new Date(d2);
        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date1).equals(simpleDateFormat.format(date2));
    }
    public static boolean isSameYear(Long d1,Long d2){
        Date date1 =  new Date(d1);
        Date date2 = new Date(d2);
        String format = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date1).equals(simpleDateFormat.format(date2));
    }
    public  static int secondsDifference(Long d1,Long d2){
        return (int) Math.floor(Math.abs((d1 - d2) /(1000 * 60)));
    }
}