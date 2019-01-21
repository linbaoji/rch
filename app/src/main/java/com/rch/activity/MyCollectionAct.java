package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.SpUtils;
import com.rch.fragment.CollNewCarFragment;
import com.rch.fragment.CollOldCarFragment;

/**
 * Created by Administrator on 2018/10/17.
 */

public class MyCollectionAct extends SecondBaseActivity{
    @ViewInject(R.id.fr_car)
    private FrameLayout fra_car;
    @ViewInject(R.id.rl_newcar)
    private RelativeLayout rl_newcar;
    @ViewInject(R.id.rl_seccar)
    private RelativeLayout rl_seccar;
    @ViewInject(R.id.tv_newcar)
    private TextView tv_newcar;
    @ViewInject(R.id.tv_seccar)
    private TextView tv_seccar;
    @ViewInject(R.id.view_newcar)
    private View view_newcar;
    @ViewInject(R.id.view_seccar)
    private View view_seccar;


    private FragmentManager fragmentManager;
    private CollNewCarFragment newfra;
    private CollOldCarFragment oldfra;

    private int dex;


    @Override
    public void setLayout() {
        setContentView(R.layout.act_collection);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("我的收藏");
        showFrag(0);
    }

    private void showFrag(int i) {
        setTopColor(i);
        fragmentManager=getSupportFragmentManager();//申明
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideFragment(transaction);//影长fragment
        dex=i;
        switch (i){
            case 0:
                if(newfra==null){
                    newfra=new CollNewCarFragment();
                    transaction.add(R.id.fr_car,newfra);
                }else {
                    transaction.show(newfra);
                }
                break;

            case 1:
                if(oldfra==null){
                    oldfra=new CollOldCarFragment();
                    transaction.add(R.id.fr_car,oldfra);
                }else {
                    transaction.show(oldfra);
                }
                break;


        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (newfra != null) {
            transaction.hide(newfra);
        }
        if(oldfra!=null){
            transaction.hide(oldfra);
        }
    }

    private void setTopColor(int i) {
        tv_newcar.setTextColor(getResources().getColor(R.color.gray_3));
        tv_seccar.setTextColor(getResources().getColor(R.color.gray_3));
        view_newcar.setBackgroundColor(getResources().getColor(R.color.gray_line));
        view_seccar.setBackgroundColor(getResources().getColor(R.color.gray_line));
        if(i==0){
            tv_newcar.setTextColor(getResources().getColor(R.color.btn_org1));
            view_newcar.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        }else if(i==1){
            tv_seccar.setTextColor(getResources().getColor(R.color.btn_org1));
            view_seccar.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        }
    }

    @OnClick({R.id.rl_newcar,R.id.rl_seccar,R.id.car_detail_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_newcar:
                showFrag(0);
                break;
            case R.id.rl_seccar:
                showFrag(1);
                break;
            case R.id.car_detail_back:
                finish();
                break;
        }
    }
}
