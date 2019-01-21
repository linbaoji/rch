package com.rch.common;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rch.R;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/8/4.
 */

public class AddphotoUtil {

    private Context context;
    private Uri uir;
    private ImageView iv_photo;
    private LinearLayout ll_pro;
    private ProgressBar pb;
    private TextView tv_jd;
    private ImageView iv_red;
    private String type;
    private String namefile;
    private String token;


//    private static AddphotoUtil addphotoUtil = null;
//    static {
//        if (addphotoUtil == null) {
//            addphotoUtil = new AddphotoUtil();
//        }
//    }


    public AddphotoUtil(Context context, Uri uir, ImageView iv_photo, LinearLayout ll_pro, ProgressBar pb, TextView tv_jd,
                        ImageView iv_red, String type, String token) {
        this.context = context;
        this.uir = uir;
        this.iv_photo = iv_photo;
        this.ll_pro = ll_pro;
        this.pb = pb;
        this.tv_jd = tv_jd;
        this.iv_red = iv_red;
        this.type=type;
        this.token=token;
        for(int i=0;i<14;i++){
            if(type.equals(i+"")){
                namefile="ubc"+i+"."+"jpeg";
            }
        }
    }

    public void upload() {

        Bitmap bit = null;//将bitmap尺寸压缩，然后质量压缩其实裁剪之后可以不用压缩
        try {
            bit = MyBitmapUtils.getBitmapFormUri(context, uir);
            iv_photo.setImageBitmap(bit);
            File file = MyBitmapUtils.saveBitmap(bit, namefile);//把mitmap转成file文件保存在本地
            ll_pro.setVisibility(View.VISIBLE);//先隐掉
            pb.setProgress(10);
            tv_jd.setText("已经上传0%");
            new Thread(new Runnable() {
                int initial = 0;//初始下载进度

                @Override
                public void run() {
                    while (initial < 80) {
                        pb.setProgress(initial += 10);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

            uploadPhoto(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


      public void uploadPhoto(File file) {
        iv_photo.setEnabled(false);
          tv_jd.setText("已上传80%");
        EncryptionTools.initEncrypMD5(token);
        RequestParam param=new RequestParam();
        param.add("token", token);
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.addFile("f1",file);

        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(context,"上传中...") {
            @Override
            public void onSuccess(String data) {
                iv_red.setVisibility(View.VISIBLE);
                pb.setProgress(100);
                tv_jd.setText("已上传100%");
                ll_pro.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray array=object.getJSONArray("result");
                    JSONObject obj= (JSONObject) array.get(0);
                    String imagepath=obj.getString("imageUrlPath");
                    if(type.equals("0")){
                        PhotoFaUtils.setfmur(context,imagepath);
                    }
                    if(type.equals("1")){
                        PhotoFaUtils.setzcur(context,imagepath);
                    }
                    if(type.equals("2")){
                        PhotoFaUtils.setzhur(context,imagepath);
                    }
                    if(type.equals("3")){
                        PhotoFaUtils.setjsczyur(context,imagepath);
                    }
                    if(type.equals("4")){
                        PhotoFaUtils.setybpur(context,imagepath);
                    }
                    if(type.equals("5")){
                        PhotoFaUtils.setfdjcur(context,imagepath);
                    }
                    if(type.equals("6")){
                        PhotoFaUtils.setctur(context,imagepath);
                    }
                    if(type.equals("7")){
                        PhotoFaUtils.setcwur(context,imagepath);
                    }
                    if(type.equals("8")){
                        PhotoFaUtils.setycur(context,imagepath);
                    }
                    if(type.equals("9")){
                        PhotoFaUtils.setyhcur(context,imagepath);
                    }
                    if(type.equals("10")){
                        PhotoFaUtils.setyqcur(context,imagepath);
                    }
                    if(type.equals("11")){
                        PhotoFaUtils.setryrtur(context,imagepath);
                    }
                    if(type.equals("12")){
                        PhotoFaUtils.sethbzyur(context,imagepath);
                    }
                    if(type.equals("13")){
                        PhotoFaUtils.sethbxur(context,imagepath);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(int code,String error) {
                iv_red.setVisibility(View.GONE);
                ll_pro.setVisibility(View.GONE);
                iv_photo.setImageResource(R.mipmap.photofm);
                iv_photo.setEnabled(true);
                ToastTool.show(context,error);
            }
        });
    }

}
