package com.example.lingluo.util;


import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class PhotoBookHandler {
    @TargetApi(19)
    public static final String parseKitKat(Intent data, Context mContext){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(mContext,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection,mContext);
            }else if("com.android.provider.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null,mContext);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null,mContext);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        //Bitmap bm= BitmapFactory.decodeFile(imagePath);
        return imagePath;
    }


    public static final  String parseBeforeKitKat(Intent data, Context mContext){
        String imagePath=null;
        Uri uri=data.getData();
        imagePath=getImagePath(uri,null,mContext);
//        Bitmap bm= BitmapFactory.decodeFile(imagePath);
        return imagePath;
    }

    public  static  final  String getStringPath(Intent data, Context mContext){
        String path = null;
        if(Build.VERSION.SDK_INT>=19){
            path = parseKitKat(data,mContext);
        }else{
            path = parseBeforeKitKat(data,mContext);
        }
        return path;
    }

    public static  final  Bitmap getBitmap(String path){
        return BitmapFactory.decodeFile(path);

    }

    public static final String getImagePath(Uri uri,String selection,Context mContext){
        String Path=null;
        Cursor cursor= mContext.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return Path;
    }

}