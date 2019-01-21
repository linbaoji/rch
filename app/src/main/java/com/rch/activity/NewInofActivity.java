package com.rch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.custom.LoadingView;

/**
 * Created by Administrator on 2018/12/18.
 */

public class NewInofActivity extends SecondBaseActivity{

    @ViewInject(R.id.wb)
    private WebView wb;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    public static final String NOTE_URL = "news_url";//外部链接新闻地址
    public static final String ID = "id";//新闻主键
    private String id ;//新闻主键
    private String url ;//新闻地址
    private String from="1";
    @Override
    public void setLayout() {
        setContentView(R.layout.act_newinfo);
    }

    @Override
    public void init(Bundle savedInstanceState) {

        load_view.loading();
        if (getIntent()!=null){
            url = getIntent().getStringExtra(NOTE_URL);
            id = getIntent().getStringExtra(ID);
            from=getIntent().getExtras().getString("from","1");
        }

        if(from.equals("1")){
            setTopTitle("详情");
        }else {
            setTopTitle("新闻详情");
        }

        initWebViewSettings();

        if (TextUtils.isEmpty(url)) {
            url = Config.NOTES_DETAIL_URL + id;
        }

        Log.e("===url",url);
        if (!TextUtils.isEmpty(url))
            wb.loadUrl(url);

        wb.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Log.i("---开始",url);
//                load_view.loading();
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view.loadUrl(request.getUrl().toString());
//                } else {
//                    view.loadUrl(request.toString());
//                }
//                return true;
//            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    return true;

                }else {
                    view.stopLoading();
                    view.goBack();
                    return false;
                }


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                dialog.dismiss();
                load_view.loadComplete();
            }


//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                Log.e("---错误",error.getDescription().toString());
//                load_view.loadError();
//            }
        });


        wb.setWebChromeClient(new WebChromeClient(){
        });


        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                wb.loadUrl(url);
            }
        });

    }

    /**
     *设置webview
     */
    private void initWebViewSettings() {
        WebSettings webSettings=wb.getSettings();
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

//        WebSettings webSettings = wb.getSettings();
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAppCacheMaxSize(1024*1024*8);
//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        webSettings.setAppCachePath(appCachePath);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
    }
}
