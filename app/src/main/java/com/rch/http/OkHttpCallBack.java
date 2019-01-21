package com.rch.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;


import com.rch.NewMainActivity;
import com.rch.R;

import org.json.JSONException;

import java.io.IOException;

import com.rch.activity.LoginActivity;
import com.rch.activity.SetActivity;
import com.rch.base.AppManager;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingDialog;
import com.rch.fragment.BusinessCircleFragment;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/7/24.
 */

public abstract class OkHttpCallBack implements Callback {

    Activity activity;
    LoadingDialog dialog;
    Call call;

    Handler handler=new Handler(Looper.getMainLooper());
    public OkHttpCallBack(Context context, String msg) {
        this.activity= (Activity) context;
//        if(!msg.isEmpty()) {
//            showDialog(context, msg);
//            onBefore();
//        }
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
            errorStr="请求超时！";
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             onError(0,errorStr);
                         }});
                onAfter();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        this.call=call;
        final String bodyString = response.body().string();
        final int code = response.code();
        Log.i("返回结果", bodyString);
         if(code==200) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(JsonTool.getHttpCode(bodyString)==200)
                            onSuccess(bodyString);
                        else
                            onError(JsonTool.getHttpCode(bodyString),JsonTool.getResultMsg(bodyString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (code == 1 || code == 2 || code == 3) {

//                    clearUser();

                        SpUtils.clearAll(activity);
                        SpUtils.saveUser(activity,"","");

                        SpUtils.setToken(activity,"");
                        SpUtils.setIsLogin(activity,false);

                        AppManager.getAppManager().finishOtherActivity();//结束除主界面之外的所有activity
                        activity.sendBroadcast(new Intent(BusinessCircleFragment.TEXT_RECEIVER));

                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        activity.startActivity(intent);
//                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        ToastTool.show(activity, JsonTool.getResultMsg(bodyString));
                    }else {
                        onError(code, JsonTool.getResultMsg(bodyString));
                    }
                }
            });
        }
        onAfter();
    }


    public abstract void onSuccess(String data);
    public abstract void onError(int code,String error);



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
        dialog.setText(msg.isEmpty()?"":msg);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
               /* if(keyCode== KeyEvent.KEYCODE_BACK&&event.getKeyCode()==KeyEvent.ACTION_DOWN)*/
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
