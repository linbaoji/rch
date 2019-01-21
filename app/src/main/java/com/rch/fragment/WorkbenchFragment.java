package com.rch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.activity.CarDealerActivity;
import com.rch.activity.CarManagerNewActivity;
import com.rch.activity.CustomerDetailActivity;
import com.rch.activity.CustomerManagementActivity;
import com.rch.activity.EmployeesAct;
import com.rch.activity.LoginActivity;
import com.rch.activity.OperOrderAct;
import com.rch.activity.VehicleMnageAct;
import com.rch.base.BaseFrag;
import com.rch.base.BaseFragment;

/**
 * Created by Administrator on 2018/4/13.
 */

public class WorkbenchFragment extends BaseFrag {
    boolean isVisible = true;
    View view;

    @ViewInject(R.id.view_cus)
    private View view_cus;
    @ViewInject(R.id.ll_new_car_manage)
    private LinearLayout ll_new_car_manage;
    @ViewInject(R.id.ll_old_car_manage)
    private LinearLayout ll_old_car_manage;
    @ViewInject(R.id.ll_member_manage)
    private LinearLayout ll_member_manage;
    @ViewInject(R.id.ll_customer_manage)
    private LinearLayout ll_customer_manage;

    @Override
    public View initView(LayoutInflater inflater) {
        if(view==null)
            view=inflater.inflate(R.layout.workbench_fragment,null);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        init();
    }


    private void init() {
        if(isVisible) {
            if(view==null)
                return;
            initControl();
        }
    }

    private void initControl() {
        setPermission();
    }


    @Override
    protected void onVisible() {
        isVisible =true;
        init();
    }

    @Override
    protected void onInvisible() {
        isVisible =false;
    }


    @OnClick({R.id.ll_new_car_manage,R.id.ll_old_car_manage,R.id.ll_member_manage,R.id.ll_customer_manage})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_new_car_manage://新车管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), CarManagerNewActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_old_car_manage://二手车管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), VehicleMnageAct.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_member_manage://员工管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), EmployeesAct.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_customer_manage://客户管理
                if(isLogin()) {
                //判断是车商（进入客户管理）还是日常管理人（进入选择车商）
                    if(getUserInfo().getIfDailyadmin().equals("1")){//日常管理人
                        startActivity(new Intent(getActivity(), CarDealerActivity.class));//选择车商
                    }else if (getUserInfo().getUserRoleType().equals("201")) {
                        startActivity(new Intent(getActivity(), CustomerManagementActivity.class));//客户管理
                    }

                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }

    }
//用户角色类型   101-自营车商管理员，102-自营车商员工，201-全网车商管理员，202-全网车商员工 ,301-企业分销商管理员,302-企业分销商员工,401-个人分销商,501-普通用户
    private void setPermission(){
        setGone();
        //判断是否是日常管理人，如果是，显示客户管理
        String roleType = getUserInfo().getUserRoleType();
        if (TextUtils.isEmpty(roleType))
            return;
        switch (roleType){
            case "101":
                setSelfEmployed();
                break;
            case "201":
                setAllNetEmployed();
                break;
            case "301":
                setCorporateDistributors();
                break;
            default://日常管理员
                setDailyManager();
                break;
        }

    }

    private void setGone(){
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.GONE);
        ll_customer_manage.setVisibility(View.GONE);
    }
    //自营车商  管理员：新车，二手车，员工管理
    private void setSelfEmployed(){
        ll_new_car_manage.setVisibility(View.VISIBLE);
        ll_old_car_manage.setVisibility(View.VISIBLE);
        ll_member_manage.setVisibility(View.VISIBLE);
        if (getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            view_cus.setVisibility(View.VISIBLE);
            ll_customer_manage.setVisibility(View.VISIBLE);
        }else {
            ll_customer_manage.setVisibility(View.GONE);
        }
    }
    //全网车商  管理员：新车，二手车，员工管理，客户管理
    private void setAllNetEmployed(){
        ll_new_car_manage.setVisibility(View.VISIBLE);
        ll_old_car_manage.setVisibility(View.VISIBLE);
        ll_member_manage.setVisibility(View.VISIBLE);
        ll_customer_manage.setVisibility(View.VISIBLE);
    }
    //企业分销商 管理员：员工管理
    private void setCorporateDistributors(){
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.VISIBLE);
        if (getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            view_cus.setVisibility(View.VISIBLE);
            ll_customer_manage.setVisibility(View.VISIBLE);
        }else {
            ll_customer_manage.setVisibility(View.GONE);
        }
    }
    //日常管理人
    private void setDailyManager(){
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.GONE);
        view_cus.setVisibility(View.GONE);
        ll_customer_manage.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setPermission();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setPermission();
    }
}
