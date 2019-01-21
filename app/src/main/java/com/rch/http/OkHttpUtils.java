package com.rch.http;

import android.content.Context;
import android.util.Log;


import java.io.File;
import java.util.concurrent.TimeUnit;

import com.rch.base.MyApplication;
import com.rch.common.EncryptionTools;
import com.rch.common.FileTool;
import com.rch.common.GsonUtils;
import com.rch.common.RoncheUtil;
import com.rch.common.SpUtils;
import com.umeng.message.PushAgent;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2017/7/24.
 */

public class OkHttpUtils {

    static Context context;
    static int timeout=60;

    /**
     * application/x-www-form-urlencoded 数据是个普通表单
     multipart/form-data 数据里有文件
     application/json 数据是个json
     但是好像以上的普通表单并没有指定Content-Type，这是因为FormBody继承了RequestBody，它已经指定了数据类型为application/x-www-form-urlencoded。
     */
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    //流，具体请看http://www.w3school.com.cn/media/media_mimeref.asp
    private static final MediaType STRAM_TYPE=MediaType.parse("application/octet-stream");

    public static void get(String url, Callback callback)
    {
        Request.Builder request=new Request.Builder();
        request.url(url);
        execute(request.build(),callback);
    }

    //RequestParam 自定类
    public static void post(String url,RequestParam params, Callback callback)
    {
        EncryptionTools.initEncrypMD5(SpUtils.getToken(MyApplication.getInstance()));
        params.add("token",SpUtils.getToken(MyApplication.getInstance()) );
        params.add("timestamp", EncryptionTools.TIMESTAMP);
        params.add("nonce", EncryptionTools.NONCE);
        params.add("signature", EncryptionTools.SIGNATURE);
        params.add("source","3");
        params.add("versioninfo","OperationSyste:"+"Android"+RoncheUtil.getSystemVersion()+"ApplicationVersion:"+RoncheUtil.getVersionName(MyApplication.getInstance())+"("+RoncheUtil.getVersionCode(MyApplication.getInstance())+")"+"PhoneModels:"+RoncheUtil.getSystemModel());
        params.add("deviceToken", PushAgent.getInstance(MyApplication.getInstance()).getRegistrationId()+"");//友盟注册的token
        Log.i("请求路径", url);
        Log.i("请求参数", GsonUtils.bean2Json(params));
        Request request=(params!=null&&!params.hasFiles())?getFilePostReques(url,params):getStringPostReques(url,params);
        execute(request,callback);
    }

    public static void download(String url,String filePath,String fileName,OKDownLoadCallback callback)
    {
        if(filePath!=null&&!filePath.equals(""))
        {
            FileTool.delete(new File(filePath,fileName));
            File file=FileTool.createFile(filePath,fileName);
            callback.setFilePath(file.toString());
            Request request=new Request.Builder().url(url).get().build();
            execute(request,callback);
        }
    }


    private static void execute(Request request, Callback callback)
    {
        new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout,TimeUnit.SECONDS)
                .build()
                .newCall(request)
                .enqueue(callback);
    }

    /**
     * 提交图片、还有Key、Value参数
     * @param url
     * @param params
     * @return
     */
    private static Request getFilePostReques(String url, RequestParam params)
    {
        final MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        params.IteratorString(new RequestParam.KeyValueIteratorListener() {
            @Override
            public void onIterator(String key, String value) {
                builder.addFormDataPart(key,value);
            }
        });

        params.IteratorFile(new RequestParam.KeyFilesIteratorListener() {
            @Override
            public void onIterator(String key, File file) {
                builder.addFormDataPart(key,file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),file));
            }
        });

        return new Request.Builder().url(url).post(builder.build()).build();
    }


    //提交Key、Value参数
    private static Request getStringPostReques(String url, RequestParam params) {
        final FormBody.Builder builder=new FormBody.Builder();
        params.IteratorString(new RequestParam.KeyValueIteratorListener() {
            @Override
            public void onIterator(String key, String value) {
                builder.add(key,value);
            }
        });

        return new Request.Builder().url(url).post(builder.build()).build();
    }

   /* private void addParams(RequestParam params) {
        final MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key.replaceAll("\\[\\d+\\]", "") + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }*/
}
