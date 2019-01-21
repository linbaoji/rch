package com.rch.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ImageCompressTool {
    static String str="";
    static final int quality=20;
    public static String compressImageToFile(String filePath) {
        try {
            str="";
           /* InputStream is = new FileInputStream(filePath);
            Bitmap bmp= BitmapFactory.decodeStream(is);*/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            File file = new File(filePath);
            if (file.exists()) {
                Bitmap bitmapSize = BitmapFactory.decodeFile(file.getAbsolutePath());
                Bitmap bmp= BitmapFactory.decodeFile(filePath,options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
                FileOutputStream fos = new FileOutputStream(outFile);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
                str=outFile.toString();
               // new File(filePath).delete();
            }
            else {
                str = filePath;
                Log.e("Exception",String.valueOf(file.exists()));
            }
        } catch (Exception e) {
            Log.e("Exception",e.getMessage());
            e.printStackTrace();
        }
        return str;

    }

    public static String compressImageSizeToFile(String filePath,int imageKB) {
        try {
            str="";
           /* BitmapFactory.Options optionss = new BitmapFactory.Options();
            optionss.inSampleSize = 1;
            optionss.inPreferredConfig=Bitmap.Config.RGB_565;
            Bitmap bmp= BitmapFactory.decodeFile(filePath,optionss);*/
            Bitmap bmp= BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            // 0-100 100为不压缩
            int options = 90;
            // 把压缩后的数据存放到baos中
            while (baos.toByteArray().length / 1024 > imageKB) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            str=outFile.toString();
           // new File(filePath).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  str;
    }

    public static List<String> compressImageListPathToFile(List<String> photoList)
    {
       List<String> temp=new ArrayList<>();
        try {
            if(photoList!=null&&photoList.size()>0) {
                for (int i=0;i<photoList.size();i++) {
                    String filePath=photoList.get(i);
                    Bitmap bmp = BitmapFactory.decodeFile(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
                    FileOutputStream fos = new FileOutputStream(outFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    temp.add(outFile.toString());
                 //   new File(filePath).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
