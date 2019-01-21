package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.SpUtils;

/**
 * Created by Administrator on 2018/6/4.
 */

public class SplashAct extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉Activity上面
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        setContentView(R.layout.act_splash);
        initToolBar();
        SpUtils.setScreenWith(getApplicationContext(),SpUtils.getScreenWidth(this));//获取屏幕宽度
        initDate();

        Log.e("===","0");

    }

    private void initDate() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SpUtils.getIsFirst(SplashAct.this)) {
                    SpUtils.setIsFirst(getApplicationContext(), false);
                    startActivity(new Intent(SplashAct.this,GuideActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAct.this,NewMainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
