package com.rch.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.networkbench.agent.impl.instrumentation.NBSWebChromeClient;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.SpUtils;
import com.rch.custom.LoadingDialog;
import com.rch.custom.LoadingView;

/**
 * Created by Administrator on 2018/5/31.
 */

public class WebActivity extends BaseActivity implements View.OnClickListener{

    WebView wb;
    ImageView iv_back;
    private LoadingView load_view;
    private String type;
    private TextView tv_title,tv_cancel,tv_agree;
    private String url;

    private LinearLayout ll_bottom;
    private String release_type ;//发布类型（1，发布新车，2 发布二手车）
    private boolean isShowBottom = false;//是否显示底部按钮 发布界面进入不显示
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initToolBar();
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_agree.setOnClickListener(this);
        if (getIntent()!=null) {
            type = getIntent().getExtras().getString("type");
            release_type = getIntent().getStringExtra("release_type");
            isShowBottom = getIntent().getBooleanExtra("isShowBottom",false);
        }
        if(type.equals("1")){
            tv_title.setText("用户协议");
            url=Config.YHXI;
        }else if(type.equals("2")){
            tv_title.setText("关于我们");
            url=Config.ABOUT;
        }else if(type.equals("3")){
            tv_title.setText("检测服务承诺细则");
            url=Config.ABOUT;//地址要换一下
        }else if (type.equals("4")){//发布车辆协议
            tv_title.setText(getResources().getString(R.string.car_agree));
            url=Config.CAR_AGREEMENT_URL;
            if (isShowBottom)
                ll_bottom.setVisibility(View.VISIBLE);
        }
        load_view= (LoadingView) findViewById(R.id.load_view);
        wb= (WebView) findViewById(R.id.wb);
        iv_back= (ImageView) findViewById(R.id.car_history_back);
        initWebViewSettings();
        wb.loadUrl(url);
        Log.i("h5地址",url);
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

        wb.setWebChromeClient(new WebChromeClient(){

            @Override

            public void onProgressChanged(WebView view, int newProgress) {

                NBSWebChromeClient.initJSMonitor(view, newProgress);

                super.onProgressChanged(view, newProgress);

            }

        });


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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_agree:
                //设置当前用户点击过发布协议同意按钮
                SpUtils.saveUserStatus(getApplicationContext(),getUser(),true);
                if (release_type.equals("1")){//新车发布
                    startActivity(new Intent(WebActivity.this,ReleaseNewCarActivity.class));
                }else if (release_type.equals("2")){//二手车发布
                    startActivity(new Intent(WebActivity.this,ReleaseAct.class));
                }
                finish();
                break;
        }
    }
}
