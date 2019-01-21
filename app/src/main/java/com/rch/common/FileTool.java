package com.rch.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/7/3.
 */

public class FileTool {

    public static final String path= Environment.getExternalStorageDirectory()+"/rch/";


    public static void copyFile(Context context,String oldFile,String newFile)
    {
        File file2=createFile(path,newFile);
       int code= CopySdcardFile(oldFile,file2.toString());
       if(code==-1)
           ToastTool.show(context,"读取照片出错！");
    }

    /**
     * 创建文件
     * @param path
     * @param newFile
     * @return
     */
    public static File createFile(String path,String newFile)
    {
        File file1=new File(path);
        if(!file1.exists())
            file1.mkdirs();
        File file2=new File(path,newFile);
        if(!file2.exists())
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.e("fasdfasdf",file2.getName());
        return file2;
    }



    /***
     *  //文件拷贝
     //要复制的目录下的所有非子目录(文件夹)文件拷贝
     * @param fromFile
     * @param toFile
     * @return
     */
    public static int CopySdcardFile(String fromFile, String toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex)
        {
            return -1;
        }
    }


    /***
     * //删除文件夹跟文件
     * @param file
     */
    public static  void delete(File file) {
        if (file.isFile()) {
            Log.e("File", file.toString());
            file.delete();
            return;
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            Log.e("File", file.toString());
            file.delete();
        }
    }
}
