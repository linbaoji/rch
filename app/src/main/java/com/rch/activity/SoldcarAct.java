package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.SoldcarAdapter;
import com.rch.base.BaseActivity;
import com.rch.fragment.BmcFragment;
import com.rch.fragment.WmcFragment;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SoldcarAct extends BaseActivity{
    @ViewInject(R.id.tv_bmc)
    private TextView tv_bmc;
    @ViewInject(R.id.tv_wmc)
    private TextView tv_wmc;
    @ViewInject(R.id.view_bmc)
    private View view_bmc;
    @ViewInject(R.id.view_wmc)
    private View view_wmc;
    @ViewInject(R.id.fr_mian)
    private FrameLayout fr_mian;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private BmcFragment bmcfra;
    private WmcFragment wmcfra;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_soldcar);
        ViewUtils.inject(this);
        fragmentManager = getSupportFragmentManager();
        selectCol(0);//设置颜色
        selectFra(0);
    }

    @OnClick({R.id.ll_bmc,R.id.ll_wmc,R.id.iv_finsh})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_bmc:
                selectCol(0);//设置颜色
                selectFra(0);
                break;

            case R.id.ll_wmc:
                selectCol(1);//设置颜色
                selectFra(1);
                break;

            case R.id.iv_finsh:
                finish();
                break;
        }
    }

    /**
     * 选择fragment
     */
    private void selectFra(int i) {
        //获取事物
         transaction = fragmentManager.beginTransaction();
         hideFragment(transaction);
         switch (i){
             case 0:
                 if(bmcfra==null){
                     bmcfra=new BmcFragment();
                     transaction.add(R.id.fr_mian,bmcfra);
                 }else {
                     transaction.show(bmcfra);
                 }
                 break;

             case 1:
                 if(wmcfra==null){
                     wmcfra=new WmcFragment();
                     transaction.add(R.id.fr_mian,wmcfra);
                 }else {
                     transaction.show(wmcfra);
                 }
                 break;
         }
        transaction.commit();
    }

    /**
     * 隐藏fragment
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (bmcfra != null) {
            transaction.hide(bmcfra);
        }
        if (wmcfra != null) {
            transaction.hide(wmcfra);
        }

    }

    private void selectCol(int i) {
        tv_bmc.setTextColor(getResources().getColor(R.color.black_1));
        view_bmc.setBackgroundColor(getResources().getColor(R.color.white));
        tv_wmc.setTextColor(getResources().getColor(R.color.black_1));
        view_wmc.setBackgroundColor(getResources().getColor(R.color.white));

        if(i==0){
            tv_bmc.setTextColor(getResources().getColor(R.color.orange_2));
            view_bmc.setBackgroundColor(getResources().getColor(R.color.orange_2));
        }else {
            tv_wmc.setTextColor(getResources().getColor(R.color.orange_2));
            view_wmc.setBackgroundColor(getResources().getColor(R.color.orange_2));
        }
    }
}
