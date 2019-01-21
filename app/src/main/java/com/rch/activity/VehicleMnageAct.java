package com.rch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.CarListAdapter;
import com.rch.adatper.VehicleManAdapter;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.entity.CarEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class VehicleMnageAct  extends SecondBaseActivity {

    public static final String UPDATEUI ="FROM_COMSUCESS";//二手车辆发布成功后然后退到这个界面要刷新界面
    @ViewInject(R.id.view_dsj)
    View view_dsj;
    @ViewInject(R.id.view_zsz)
    View view_zsz;
    @ViewInject(R.id.view_ysq)
    View view_ysq;
    @ViewInject(R.id.view_yxj)
    View view_yxj;
    @ViewInject(R.id.ld_view)
    LoadingView ld_view;
    @ViewInject(R.id.home_recyclerView)
    private PullToRefreshRecyclerView home_recyclerView;



    int page = 1;
    int pageSize = 10;
    private String shelvesStatus="0";//0-待上架，1-在售中，2-下架 3-已售罄
    private List<CarEntity>list;
    private List<CarEntity>pulllist=new ArrayList<>();
    private Gson gson;
    private CarListAdapter adapter;
    private RecyclerView recyclerView;
    private int from;//0是新车 1是二手车(点击item要跳转的地方不一样的)

    private boolean userStatus;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_newcar_manage);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("二手车管理");
        gson=new Gson();
        recyclerView=home_recyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        setTopRight("发布二手车", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userStatus = SpUtils.getUserStatus(mContext, getUser());//获取当前用户是否点击过发布协议同意按钮
                //判断当前用户是否点击过发布协议同意按钮
                if (userStatus){
                    startActivity(new Intent(mContext,ReleaseAct.class).putExtra("fromtype","1"));
                }else {
                    startActivity(new Intent(mContext, WebActivity.class).putExtra("type", "4").putExtra("release_type", "2").putExtra("isShowBottom", true));
//                startActivity(new Intent(mContext,ReleaseAct.class).putExtra("fromtype","1"));
                }


            }
        });
        selectCol(0);

        home_recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
               page=1;
               getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                getData();
            }
        });

    }

    @OnClick({R.id.ll_dsj,R.id.ll_zsz,R.id.ll_ysq,R.id.ll_yxj,R.id.tv_right,R.id.car_go_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_dsj://待上架
                selectCol(3);
                break;
            case R.id.ll_zsz://在售中
                selectCol(0);
                break;
            case R.id.ll_ysq://已售罄
                selectCol(1);
                break;
            case R.id.ll_yxj://已下架
                selectCol(2);
                break;
            case R.id.tv_right://发布二手车
                break;
            case R.id.car_go_back://返回
                finish();
                break;
        }

    }
    private void selectCol(int i) {
        view_dsj.setBackgroundColor(getResources().getColor(R.color.white));
        view_zsz.setBackgroundColor(getResources().getColor(R.color.white));
        view_ysq.setBackgroundColor(getResources().getColor(R.color.white));
        view_yxj.setBackgroundColor(getResources().getColor(R.color.white));

        if(i==0){
            view_zsz.setBackgroundColor(getResources().getColor(R.color.orange_2));
            shelvesStatus="1";
        }else if(i==1){
            view_ysq.setBackgroundColor(getResources().getColor(R.color.orange_2));
            shelvesStatus="3";
        }else if(i==2){
            view_yxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
            shelvesStatus="2";
        }else if (i==3){
            view_dsj.setBackgroundColor(getResources().getColor(R.color.orange_2));
            shelvesStatus="0";
        }
        onRefresh();
    }

    private void onRefresh() {
        page=1;
        ld_view.loading();
        home_recyclerView.setVisibility(View.GONE);
        getData();

    }

    private void getData() {
        RequestParam param=new RequestParam();
        param.add("shelvesStatus",shelvesStatus);
        param.add("currentPage",page+"");
        param.add("pageSize",pageSize+"");
        OkHttpUtils.post(Config.OLDCARGULB_URL, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                ld_view.loadComplete();
                home_recyclerView.onRefreshComplete();
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<CarEntity>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        if(page==1){
                            pulllist.clear();
                        }
                        pulllist.addAll(list);
                        if(adapter==null){
                            adapter=new CarListAdapter(mContext,pulllist,1);
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.updateShopListData(pulllist);
                        }
                        home_recyclerView.setVisibility(View.VISIBLE);
                    }else {
                        if(page==1) {
                            ld_view.loadComplete();
                            ld_view.noContent();
                            ld_view.setNoContentIco(R.mipmap.no_car_data);
                            ld_view.setNoContentTxt("请点击“发布二手车”,上传车辆信息");
                            //        如果没有数据执行以下语句
                            ld_view.clickButton("发布二手车", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    userStatus = SpUtils.getUserStatus(mContext, getUser());//获取当前用户是否点击过发布协议同意按钮
                                    //判断当前用户是否点击过发布协议同意按钮
                                    if (userStatus){
                                        startActivity(new Intent(mContext,ReleaseAct.class).putExtra("fromtype","1"));
                                    }else {
                                        startActivity(new Intent(mContext, WebActivity.class).putExtra("type", "4").putExtra("release_type", "2").putExtra("isShowBottom", true));
//                startActivity(new Intent(mContext,ReleaseAct.class).putExtra("fromtype","1"));
                                    }
                                }
                            });
                            home_recyclerView.setVisibility(View.GONE);
                        }else {//加载没数据界面还是显示原始数据

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ld_view.loadError();
                ToastTool.show(mContext,error);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    //    private Myreceiver receiver;
//    public class Myreceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals(UPDATEUI)){
//                onRefresh();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(receiver==null){
//            receiver=new Myreceiver();
//            registerReceiver(receiver,new IntentFilter(UPDATEUI));
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(receiver!=null){
//            unregisterReceiver(receiver);
//            receiver=null;
//        }
//    }
}
