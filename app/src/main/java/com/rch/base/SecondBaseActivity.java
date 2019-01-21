package com.rch.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.rch.R;

/**
 * 二级baseactivity
 * Created by Administrator on 2018/9/29.
 */

public abstract class SecondBaseActivity extends BaseActivity {
    public Context mContext;
    private TextView tv_title,tv_right;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setLayout();
        initToolBar();
        ViewUtils.inject(this);
        init(savedInstanceState);

    }
    /**
     * 设置布局
     */
    public abstract void setLayout();

    /**
     * 填充数据
     */
    public abstract void init(Bundle savedInstanceState);

    public void setBackVisiable(boolean backVisiable){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        if (backVisiable){
            iv_back.setVisibility(View.VISIBLE);
        }else {
            iv_back.setVisibility(View.GONE);
        }
    }
    /**
     * 设置顶部标题
     *
     * @param title
     */
    public void setTopTitle(String title) {
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }
    /**
     * 设置右上角
     * @param rightText 文字
     * @param listener 点击十几
     */
    public void setTopRight(String rightText,View.OnClickListener listener){
        tv_right= (TextView) findViewById(R.id.tv_right);
        tv_right.setText(rightText);
        if(listener!=null){
            tv_right.setOnClickListener(listener);
        }
    }
    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 返回
     *
     * @param view
     */
    public void goback(View view) {
        finish();
    }
}
