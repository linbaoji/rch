package com.rch.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;

/**
 * Created by Administrator on 2018/11/9.
 */

public class MorNewCarActivity extends SecondBaseActivity{
    @ViewInject(R.id.wb)
    private WebView wb;

    private String id;
    private String shareUrl;

    @Override
    public void setLayout() {
        setContentView(R.layout.act_mornewcar);
    }

    @Override
    public void init(Bundle savedInstanceState) {
      setTopTitle("车辆配置");
      id=getIntent().getExtras().getString("id","");
      if(!TextUtils.isEmpty(id)){
          shareUrl=Config.NEWCARMOR_URL+id+"&type=app";
      }

        WebSettings webSettings=wb.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);



        wb.loadUrl(shareUrl);
        Log.i("详情地址",shareUrl);
        wb.setWebViewClient(new WebViewClient(){

//          @Override
//          public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//              return super.shouldOverrideUrlLoading(view, request.getUrl().toString());
//          }
//      });


          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
//              return super.shouldOverrideUrlLoading(view, url);
              view.loadUrl(url);
              return true;
          }
      });
      }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wb!=null){
            wb=null;
        }
    }
}
