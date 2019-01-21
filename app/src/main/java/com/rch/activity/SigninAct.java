package com.rch.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
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
import com.rch.custom.LoadingView;

/**
 * Created by Administrator on 2018/6/11.
 */

public class SigninAct extends BaseActivity{
    WebView wb;
    ImageView iv_back;
    private LoadingView load_view;
    private TextView tv_title;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的签到");
        getPjurl();
//        url=Config.SINGIN;//签到
        load_view= (LoadingView) findViewById(R.id.load_view);
        wb= (WebView) findViewById(R.id.wb);
        iv_back= (ImageView) findViewById(R.id.car_history_back);
        initWebViewSettings();
        wb.loadUrl(url);
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                dialog.dismiss();
                load_view.loadComplete();
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                dialog.dismiss();
                load_view.loadError();
            }



        });


        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                wb.loadUrl(url);
            }
        });




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    private void getPjurl() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        String token="&token="+getUserInfo().getToken();
        String timestamp="&timestamp="+EncryptionTools.TIMESTAMP;
        String nonce="&nonce="+EncryptionTools.NONCE;
        String signature="&signature="+EncryptionTools.SIGNATURE;
        url=Config.SINGIN+token+timestamp+nonce+signature;
    }

    /**
     *设置webview
     */
    private void initWebViewSettings() {
        WebSettings webSettings=wb.getSettings();
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

    }


}
