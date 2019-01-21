package com.rch.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.rch.R;
import com.rch.view.statusbar.BarUtils;
import com.rch.view.statusbar.StatusBarCompat;

/**
 * Created by bitch-1 on 2017/3/16.
 */

public abstract class BaseFrag extends BaseFragment {

    /**
     * 返回的view对象
     */
    protected View view;
    //用此Activity，防止getContext为空
    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = initView(inflater);
        // 注入控件
        ViewUtils.inject(this, view);
        initToolBar();
        return view;
    }
    protected void initToolBar() {
        StatusBarCompat.setStatusBarColor(mActivity, mActivity.getResources().getColor(R.color.transparent), true);
        StatusBarCompat.setTranslucent(mActivity.getWindow(), true);
        if (Build.VERSION.SDK_INT >= 19 && view.findViewById(R.id.toolbar) != null) {
            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolbar);
            int statusBarHeight = BarUtils.getStatusBarHeight();
            toolBar.setPadding(0, statusBarHeight, 0, 0);
            toolBar.getLayoutParams().height = statusBarHeight + (int) TypedValue.applyDimension(1, 45.0F, this.getResources().getDisplayMetrics());
        }else if (Build.VERSION.SDK_INT < 19 && view.findViewById(R.id.view_top)!=null){
            view.findViewById(R.id.view_top).setVisibility(View.GONE);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(Activity)context;

    }

    /**
     * 初始化View 对象
     *
     * @param inflater
     *            view填充器 需要布局文件
     * @return 返回view 对象
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    public abstract void initData(Bundle savedInstanceState);

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
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }
}
