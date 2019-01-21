package com.rch.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ReceiverTool;
import com.rch.custom.LoadingDialog;
import com.rch.custom.LoadingView;


/**
 * Created by Administrator on 2018/6/4.
 */

public class TaskcenterActivity extends BaseActivity implements View.OnClickListener{

    private WebView webView;
    private ImageView car_history_back;
    private String pjurl;

    private LoadingView load_view;
    private TextView tv_title;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_task);
        initToolBar();
        load_view= (LoadingView) findViewById(R.id.load_view);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我要成为分销商");
        getPjUrl();//获取url

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                webView.loadUrl(pjurl);
            }
        });

    }

    private void getPjUrl() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        String token="&token="+getUserInfo().getToken();
        String timestamp="&timestamp="+EncryptionTools.TIMESTAMP;
        String nonce="&nonce="+EncryptionTools.NONCE;
        String signature="&signature="+EncryptionTools.SIGNATURE;
        pjurl=Config.TASKURL+token+timestamp+nonce+signature;

        webView = (WebView) findViewById(R.id.wb);
        car_history_back= (ImageView) findViewById(R.id.car_history_back);
        car_history_back.setOnClickListener(this);
        initWebViewSettings();
    }

    /**
     * 设置webview
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.addJavascriptInterface(new JsInteraction(),"AppModel");
        webView.addJavascriptInterface(new JsInteraction(),"AppModel");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                  load_view.loadComplete();

            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                load_view.loadComplete();
            }
        });

        webView.loadUrl(pjurl);
        Log.i("URL",pjurl);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.car_history_back:
                if(webView.canGoBack()){
                    webView.goBack();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_title.setText("我要成为分销商");
                        }
                    });

                }else{
//                    onBackPressed();
                    finish();
                }

                break;
        }
    }


    public class JsInteraction{
        @JavascriptInterface
        public void taskShare() {
            sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
            finish();
        }
        @JavascriptInterface
        public void taskSignIn(){
//            sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
//            startActivity(new Intent(TaskcenterActivity.this,SigninAct.class));
//            finish();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_title.setText("我的签到");
                }
            });
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_title.setText("我要成为分销商");
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            webView=null;
        }
    }
}
