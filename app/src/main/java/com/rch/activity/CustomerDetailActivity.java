package com.rch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.CustomerDetailAdapter;
import com.rch.adatper.CustomerManageAdapter;
import com.rch.adatper.InquiryDetailAdapter;
import com.rch.adatper.OrderDetailAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.RoncheUtil;
import com.rch.common.ToastTool;
import com.rch.custom.CommonView;
import com.rch.custom.LoadingView;
import com.rch.entity.CustomerDetailEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */

public class CustomerDetailActivity extends SecondBaseActivity {
    @ViewInject(R.id.mCollapsingToolbarLayout)
    private CoordinatorLayout mCollapsingToolbarLayout;
    @ViewInject(R.id.app_bar)
    private AppBarLayout app_bar;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;//数据条数
    @ViewInject(R.id.ll_car_type)
    private LinearLayout ll_car_type;//车类型（新车、二手车、全部）
    @ViewInject(R.id.tv_car_all)
    private TextView tv_car_all;//全部
    @ViewInject(R.id.tv_car_new)
    private TextView tv_car_new;//新车
    @ViewInject(R.id.tv_car_old)
    private TextView tv_car_old;//二手车
    @ViewInject(R.id.tv_name)
    private TextView tv_name;//客户姓名
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;//手机号
    @ViewInject(R.id.tv_tj_name)
    private TextView tv_tj_name;//推荐人
    @ViewInject(R.id.tv_gjjl)
    private TextView tv_gjjl;//跟进记录
    @ViewInject(R.id.tv_yyd)
    private TextView tv_yyd;//预约单
    @ViewInject(R.id.tv_khxj)
    private TextView tv_khxj;//客户询价
    @ViewInject(R.id.view_gjjl)
    private View view_gjjl;
    @ViewInject(R.id.view_yyd)
    private View view_yyd;
    @ViewInject(R.id.view_khxj)
    private View view_khxj;
   @ViewInject(R.id.cv)
    CommonView cv;


    @ViewInject(R.id.plv_yg)
    private RecyclerView recyclerView;
//    @ViewInject(R.id.ld_view)
//    LoadingView ld_view;

    private CustomerDetailAdapter adapter;
    private OrderDetailAdapter orderDetailAdapter;//预约单适配器
    private InquiryDetailAdapter inquiryDetailAdapter;//客户询价适配器

    private int type =0;//选中类型
    private int yyd_pos = 0;//预约单的选项位置
    private int khxj_pos = 0;//客户询价的选项位置

    private String enterpriseId;//车商id
    private String userId;//客户id
    private List<CustomerDetailEntity.FollowListBean> followList = new ArrayList<>();//跟进记录
    private List<CustomerDetailEntity.OrderListBean> orderList = new ArrayList<>();//预约单
    private List<CustomerDetailEntity.QuerySaleListBean> queryList = new ArrayList<>();//客户询价

    private int followNum = 0;//跟进记录数目
    private int orderNum = 0;//预约单数目
    private int queryNum = 0;//客户询价数目
    @Override
    public void setLayout() {
        setContentView(R.layout.act_customer_detail_change);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("客户详情");
        cv.setIvArrow(R.mipmap.down_arrow,true);

        if (getIntent()!=null) {
            enterpriseId = getIntent().getStringExtra("enterpriseId");
            userId = getIntent().getStringExtra("userId");
        }

//        userId = "5bdfd6aae075d2e2ec73a42e";

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //去掉上滑时顶部阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            app_bar.setOutlineProvider(null);
            mCollapsingToolbarLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
        getCustomerManagerList(userId);
    }
    private CustomerDetailEntity customerDetailEntity;
    //获取客户详情
    public void getCustomerManagerList(String userId) {
        upLoadingShow();
        EncryptionTools.initEncrypMD5(getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        if (!TextUtils.isEmpty(userId))
            param.add("userId", userId);
        if (!TextUtils.isEmpty(enterpriseId))
            param.add("enterpriseId",enterpriseId);
        OkHttpUtils.post(Config.CUSTOMER_DETAIL, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                upLoadingClose();
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject json = jsonObject.getJSONObject("result");
                        customerDetailEntity = GsonUtils.gsonToBean(json.toString(), CustomerDetailEntity.class);
                        if (customerDetailEntity!=null)
                            setData(customerDetailEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                upLoadingClose();
                ToastTool.show(mContext, error);
            }
        });
    }

    //设置数据
    private void setData(CustomerDetailEntity entity){
        tv_name.setText(RoncheUtil.getSelfString(entity.getUserName()));
        tv_phone.setText(RoncheUtil.getSelfString(entity.getMobile()));
        tv_tj_name.setText(RoncheUtil.getSelfString(entity.getInviteName()));
        followList = entity.getFollowList();
        orderList = entity.getOrderList();
        queryList = entity.getQuerySaleList();
        followNum = followList.size();
        orderNum = orderList.size();
        queryNum = queryList.size();
        if (adapter == null) {
            adapter = new CustomerDetailAdapter(mContext, followList);
            recyclerView.setAdapter(adapter);
        }else {
            adapter.notify(followList);
        }

        if (orderDetailAdapter == null) {
            orderDetailAdapter = new OrderDetailAdapter(mContext, orderList);
            recyclerView.setAdapter( orderDetailAdapter);
        }else {
            orderDetailAdapter.notify(orderList);
        }

        if (inquiryDetailAdapter == null) {
            inquiryDetailAdapter = new InquiryDetailAdapter(mContext, queryList);
            recyclerView.setAdapter(inquiryDetailAdapter);
        }else {
            inquiryDetailAdapter.notify(queryList);
        }
        selectCol(0);
        getOrderList();
        getQueryList();
    }
    @OnClick({R.id.ll_gjjl,R.id.ll_yyd,R.id.ll_khxj,R.id.tv_write,R.id.tv_car_all,R.id.tv_car_new,R.id.tv_car_old})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_gjjl:
                selectCol(0);
                break;
            case R.id.ll_yyd:
                selectCol(1);
                break;
            case R.id.ll_khxj:
                selectCol(2);
                break;
            case R.id.tv_write:
                startActivity(new Intent(mContext,FollowupActivity.class).putExtra("userId",userId).putExtra("enterpriseId",enterpriseId));
                break;
            case R.id.tv_car_all:
                setBg(0);
                break;
            case R.id.tv_car_new:
                setBg(1);
                break;
            case R.id.tv_car_old:
                setBg(2);
                break;
        }
    }
    private List<CustomerDetailEntity.OrderListBean> orderNewList = new ArrayList<>();//预约单新车
    private List<CustomerDetailEntity.OrderListBean> orderOldList = new ArrayList<>();//预约单二手车
    //预约单列表
    private void getOrderList(){
        orderNewList.clear();
        orderOldList.clear();
        //全部
        orderList = customerDetailEntity.getOrderList();
        for (int i=0;i<orderList.size();i++){
            if (orderList.get(i).getIfNew().equals("1")){ //新车
                orderNewList.add(orderList.get(i));
            }else {
                orderOldList.add(orderList.get(i));//二手车
            }
        }
    }
    private List<CustomerDetailEntity.QuerySaleListBean> queryNewList = new ArrayList<>();//客户询价新车
    private List<CustomerDetailEntity.QuerySaleListBean> queryOldList = new ArrayList<>();//客户询价二手车
    //客户询价列表
    private void getQueryList(){
        queryNewList.clear();
        queryOldList.clear();
        //全部
        queryList = customerDetailEntity.getQuerySaleList();
        for (int i=0;i<queryList.size();i++){
            if (queryList.get(i).getIfNew().equals("1")){ //新车
                queryNewList.add(queryList.get(i));
            }else {
                queryOldList.add(queryList.get(i));//二手车
            }
        }
    }
    private void selectCol(int i) {
        type = i;
        tv_gjjl.setTextColor(getResources().getColor(R.color.gray_3));
        tv_yyd.setTextColor(getResources().getColor(R.color.gray_3));
        tv_khxj.setTextColor(getResources().getColor(R.color.gray_3));
        view_gjjl.setBackgroundColor(getResources().getColor(R.color.white));
        view_yyd.setBackgroundColor(getResources().getColor(R.color.white));
        view_khxj.setBackgroundColor(getResources().getColor(R.color.white));

        if(i==0){
            tv_gjjl.setTextColor(getResources().getColor(R.color.black));
            tv_yyd.setTextColor(getResources().getColor(R.color.gray_3));
            tv_khxj.setTextColor(getResources().getColor(R.color.gray_3));
            view_gjjl.setBackgroundColor(getResources().getColor(R.color.orange_2));
            tv_num.setText("共"+followNum+"条跟进记录");
            ll_car_type.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new CustomerDetailAdapter(mContext, followList);
                recyclerView.setAdapter(adapter);
            }else {
                recyclerView.setAdapter(adapter);
            }
        }else if(i==1){
            tv_gjjl.setTextColor(getResources().getColor(R.color.gray_3));
            tv_yyd.setTextColor(getResources().getColor(R.color.black));
            tv_khxj.setTextColor(getResources().getColor(R.color.gray_3));
            view_yyd.setBackgroundColor(getResources().getColor(R.color.orange_2));
            tv_num.setText("共"+orderNum+"条预约信息");
            ll_car_type.setVisibility(View.VISIBLE);
            setBg(yyd_pos);
            if (orderDetailAdapter == null) {
                orderDetailAdapter = new OrderDetailAdapter(mContext, orderList);
                recyclerView.setAdapter(orderDetailAdapter);
            }else {
                recyclerView.setAdapter(orderDetailAdapter);
            }
        }else if(i==2){
            tv_gjjl.setTextColor(getResources().getColor(R.color.gray_3));
            tv_yyd.setTextColor(getResources().getColor(R.color.gray_3));
            tv_khxj.setTextColor(getResources().getColor(R.color.black));
            view_khxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
            tv_num.setText("共"+queryNum+"条询价信息");
            ll_car_type.setVisibility(View.VISIBLE);
            setBg(khxj_pos);
            if (inquiryDetailAdapter == null) {
                inquiryDetailAdapter = new InquiryDetailAdapter(mContext, queryList);
                recyclerView.setAdapter(inquiryDetailAdapter);
            }else {
                recyclerView.setAdapter(inquiryDetailAdapter);
            }
        }
    }


    //设置文本背景色及字体颜色
    private void setBg(int pos){
        switch (pos){
            case 0://全部
                tv_car_all.setTextColor(getResources().getColor(R.color.white));
                tv_car_all.setBackground(getResources().getDrawable(R.drawable.button));

                tv_car_new.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_new.setBackground(getResources().getDrawable(R.drawable.history_bg));

                tv_car_old.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_old.setBackground(getResources().getDrawable(R.drawable.history_bg));

                if (type == 0){

                }else if (type == 1) {
                    yyd_pos = 0;
                }else if (type == 2) {
                    khxj_pos = 0;
                }
                if (type == 1){
                    orderDetailAdapter.notify(orderList);
                    recyclerView.setAdapter(orderDetailAdapter);
                }else if (type == 2){
                    inquiryDetailAdapter.notify(queryList);
                    recyclerView.setAdapter(inquiryDetailAdapter);
                }
                break;
            case 1://新车
                tv_car_all.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_all.setBackground(getResources().getDrawable(R.drawable.history_bg));

                tv_car_new.setTextColor(getResources().getColor(R.color.white));
                tv_car_new.setBackground(getResources().getDrawable(R.drawable.button));

                tv_car_old.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_old.setBackground(getResources().getDrawable(R.drawable.history_bg));

                if (type == 0){

                }else if (type == 1) {
                    yyd_pos = 1;
                }else if (type == 2) {
                    khxj_pos = 1;
                }
                if (type == 1){
                    orderDetailAdapter.notify(orderNewList);
                    recyclerView.setAdapter(orderDetailAdapter);
                }else if (type == 2){
                    inquiryDetailAdapter.notify(queryNewList);
                    recyclerView.setAdapter(inquiryDetailAdapter);
                }
                break;
            case 2://二手车
                tv_car_all.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_all.setBackground(getResources().getDrawable(R.drawable.history_bg));

                tv_car_new.setTextColor(getResources().getColor(R.color.btn_org1));
                tv_car_new.setBackground(getResources().getDrawable(R.drawable.history_bg));

                tv_car_old.setTextColor(getResources().getColor(R.color.white));
                tv_car_old.setBackground(getResources().getDrawable(R.drawable.button));

                if (type == 0){

                }else if (type == 1) {//当前位置预约单tab
                    yyd_pos = 2;
                }else if (type == 2) {//当前位置客户询价tab
                    khxj_pos = 2;
                }
                if (type == 1){
                    orderDetailAdapter.notify(orderOldList);
                    recyclerView.setAdapter(orderDetailAdapter);
                }else if (type == 2){
                    inquiryDetailAdapter.notify(queryOldList);
                    recyclerView.setAdapter(inquiryDetailAdapter);
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(receiver==null){
            receiver=new MyBrodcastreceive();
            registerReceiver(receiver,new IntentFilter(CUSTOMER_DETAIL));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public static String CUSTOMER_DETAIL = "customer_detail";
    private MyBrodcastreceive receiver;
    private class MyBrodcastreceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCustomerManagerList(userId);
        }
    }
}
