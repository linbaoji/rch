package com.rch.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;


import com.rch.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.rch.common.JsonTool;
import com.rch.custom.LoadingDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/22.
 */

public abstract class OKDownLoadCallback implements Callback {

    Activity activity;
    LoadingDialog dialog;
    Call call;
    String path;
    Handler handler=new Handler(Looper.getMainLooper());
    public OKDownLoadCallback(Context context, String msg) {
        this.activity= (Activity) context;
        showDialog(context,msg);
        onBefore();
    }

    public void setFilePath(String path)
    {
        this.path=path;
    }

    /**
     * 请求前
     *
     */
    public void onBefore() {
        if (dialog != null&&!dialog.isShowing()) {
            dialog.show();
        }
    }


    /**
     * 请求完成
     */
    public void onAfter() {
        if (activity!=null&&activity.isFinishing()==false
                &&dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onFailure(Call call, IOException e) {
        this.call=call;
        final String errorStr;
        if(isNetConn())
            errorStr="网络不可用！";
        else
            errorStr="请检查网络！";
        handler.post(new Runnable() {
            @Override
            public void run() {
                onError(0,errorStr);
            }});
        onAfter();
    }

    @Override
    public void onResponse(Call call, Response response) {
        this.call=call;
        int len=0;
        int total=0;
        if(call.isCanceled()) return ;
        if(response.isSuccessful()){
            InputStream inputStream = response.body().byteStream();
            OutputStream outputStream= null;
            try {
                outputStream = new FileOutputStream(path);
                total=inputStream.available();
                byte[] buffer=new byte[1024];
                while((len=inputStream.read(buffer))!=-1)
                {
                    outputStream.write(buffer,0,len);
                    publishProgress(total,len);
                }
                outputStream.flush();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(path);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onError(0,"下载失败！");
                    }
                });
            }finally {
                if(inputStream!=null)
                {
                    try {
                        inputStream.close();
                        inputStream=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(outputStream!=null)
                {
                    try {
                        outputStream.close();
                        outputStream=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onError(0, JsonTool.getResultMsg("下载失败！"));
                }
            });
        }
        onAfter();
    }



    private void publishProgress(final long total , final long current){
        handler.post(new Runnable() {
            public void run() {
                int progress = (int) (current / (float) total * 100);
                onProgress(progress);
            }
        });
    }

    public abstract void onSuccess(String data);
    public abstract void onError(int code,String error);
    public abstract void onProgress(int current);



    private boolean isNetConn()
    {
        ConnectivityManager manager= (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isAvailable())
            return true;
        else
            return false;
    }

    private void showDialog(Context context,String msg)
    {
        dialog=new LoadingDialog(context, R.style.LoadingDialogStyle);
        dialog.setText(msg);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_BACK)
                {
                    dialog.dismiss();
                    if(call!=null)
                        call.cancel();
                    return true;
                }
                return false;
            }
        });
    }

}
