package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.SpUtils;

/**
 * Created by Administrator on 2018/6/11.
 */

public class CustomerAct extends BaseActivity implements View.OnClickListener{

    private WebView webView;
    private ImageView car_history_back;
    private String pjurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_customer);
        initToolBar();
        webView = (WebView) findViewById(R.id.wb);
        car_history_back= (ImageView) findViewById(R.id.car_history_back);
        car_history_back.setOnClickListener(this);
        if(!TextUtils.isEmpty(SpUtils.getHxurl(CustomerAct.this))) {
//            pjurl = Config.KFURL;
            pjurl=SpUtils.getHxurl(CustomerAct.this);
            initWebViewSettings();
        }
    }

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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);



            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);


            }
        });

        webView.loadUrl(pjurl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.car_history_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            webView=null;
        }
    }
}
