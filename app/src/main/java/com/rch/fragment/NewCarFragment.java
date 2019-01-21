package com.rch.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.activity.CityLocationActivity;
import com.rch.activity.CitySelectActivity;
import com.rch.base.BaseFrag;
import com.rch.base.BaseFragment;
import com.rch.common.GsonUtils;
import com.rch.common.SpUtils;
import com.rch.entity.CityInfoEntity;
import com.rch.entity.ComparatorImpl;
import com.rch.entity.SearchEntity;

import org.json.JSONException;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */

public class NewCarFragment extends BaseFrag {
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
    @ViewInject(R.id.car_fragment_location)
    private TextView tvLocation;

    private FragmentManager fragmentManager;
    private OneCarFragment onefra;
    private CarFragment twofra;
    private String gpsCity="";
    private int index=0;//用来记录在哪个子fragment上默认为0

    @Override
    public View initView(LayoutInflater inflater) {
        view=inflater.inflate(R.layout.fra_newcar,null);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        SpUtils.getLoacationCity(getActivity());
        tvLocation.setText(SpUtils.getLoacationCity(getActivity()).isEmpty()?"全国":SpUtils.getLoacationCity(getActivity()));
        showFrag(0);
    }

    //对外提供的显示当前frg的方法
    public void showCurrentFrg(int pos){
//        index = pos;
//        if (isAdded())
//            showFrag(index);
        showFrag(pos);
    }
    private void showFrag(int i) {
        setTopColor(i);
        index=i;
        fragmentManager=getChildFragmentManager();//申明
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideFragment(transaction);//影长fragment
        switch (i){
            case 0:
                if(onefra==null){
                    onefra=new OneCarFragment();
                    transaction.add(R.id.fr_car,onefra);
                    twofra=new CarFragment();
                    transaction.add(R.id.fr_car,twofra);
                    transaction.hide(twofra);
                }else {
                    transaction.show(onefra);
                }
                break;

            case 1:
                if(twofra==null){
                    twofra=new CarFragment();
                    transaction.add(R.id.fr_car,twofra);
                }else {
                    transaction.show(twofra);
                }
                break;
        }
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (onefra != null) {
            transaction.hide(onefra);
        }
        if(twofra!=null){
            transaction.hide(twofra);
        }
    }


    private void setTopColor(int i) {
        tv_newcar.setTextColor(NewCarFragment.this.getResources().getColor(R.color.black_2));
        tv_seccar.setTextColor(NewCarFragment.this.getResources().getColor(R.color.black_2));
        tv_newcar.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.dip_13));
        tv_seccar.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.dip_13));
        tv_newcar .setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tv_seccar .setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        view_newcar.setBackgroundColor(NewCarFragment.this.getResources().getColor(R.color.white));
        view_seccar.setBackgroundColor(NewCarFragment.this.getResources().getColor(R.color.white));
        if(i==0){
            tv_newcar.setTextColor(NewCarFragment.this.getResources().getColor(R.color.black_2));
            view_newcar.setBackgroundColor(NewCarFragment.this.getResources().getColor(R.color.btn_org1));
            tv_newcar.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.dip_20));
            tv_newcar .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else if(i==1){
            tv_seccar.setTextColor(NewCarFragment.this.getResources().getColor(R.color.black_2));
            view_seccar.setBackgroundColor(NewCarFragment.this.getResources().getColor(R.color.btn_org1));
            tv_seccar.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.dip_20));
            tv_seccar .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

    }

    int BRANDREQUESTCODE =10101;//品牌
    int CITYREQUESTCODE=10102;//城市
    final int REQUESTGPSCODE=10103;//GPS

    @OnClick({R.id.rl_newcar,R.id.rl_seccar,R.id.home_area_layout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_newcar:
                showFrag(0);
                break;
            case R.id.rl_seccar:
                showFrag(1);
                break;
            case R.id.home_area_layout:
//                startActivityForResult(new Intent(getActivity(), CityLocationActivity.class).putExtra("request",CITYREQUESTCODE), CITYREQUESTCODE);
                startActivityForResult(new Intent(getActivity(), CitySelectActivity.class).putExtra("request",CITYREQUESTCODE), CITYREQUESTCODE);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }else {
            if(requestCode==CITYREQUESTCODE){
                int type = data.getExtras().getInt("type");
                if(type==1) {
                    List<CityInfoEntity> cityList = data.getExtras().getParcelableArrayList("CityInfoEntity");
                    if(cityList!=null&&cityList.size()>0) {
                        //对获取到的城市列表排序
                        Collections.sort(cityList, new ComparatorImpl());
                        Log.e("=====list=", cityList.toString());
                        gpsCity = cityList.get(0).getCityName();
                        if (cityList.size() > 1) {
                            gpsCity = gpsCity + "等";
                        }

                        StringBuilder builder=new StringBuilder();
                        for(int i=0;i<cityList.size();i++){
                            if(i==cityList.size()-1){
                                builder.append(cityList.get(i).getCity());
                            }else {
                                builder.append(cityList.get(i).getCity()+",");
                            }
                        }

                        SpUtils.setCityid(mActivity,builder.toString());

                    }else {//如果不选
                        gpsCity="全国";
                        SpUtils.setCityid(mActivity,"");
                    }
                    try {
                        SpUtils.setLoacationCityList(mActivity,GsonUtils.List2Json(cityList).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //保存所选城市列表
                    SpUtils.setLoacationCity(getActivity(),gpsCity);
                    tvLocation.setText(gpsCity.isEmpty()?"全国":gpsCity);
                    if(index==0){
                        onefra.upDateUi();
                    }else {
                        twofra.upDateUi();
                    }
                }
            }
        }

    }


//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(!hidden){
//            if (index == 0) {
//                onefra.upDateUi();
//            } else {
//                twofra.upDateUi();
//            }
//        }
//    }

    /**
     * 从搜索页面结果进来两个标签都要更新table
     * 带品牌结果的
     */
    public void fromSerchBrand(SearchEntity entity) {
            showFrag(0);
            onefra.upTable(entity);
            twofra.upTable(entity);
    }

    /**
     * 从首页里面来的点击明星车而来
     */
    public void fromStartClick() {
        onefra.upDateIsRec();
        twofra.upDateIsRec();
    }
}