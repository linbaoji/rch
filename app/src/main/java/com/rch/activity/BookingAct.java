package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.VehicleManAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.entity.UserOrderEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约单审核列表
 * Created by Administrator on 2018/7/31.
 */

public class BookingAct extends BaseActivity{
    @ViewInject(R.id.plv_book)
    private PullToRefreshRecyclerView plv_book;
    private RecyclerView rcy;

    @ViewInject(R.id.view_zsz)
    private View view_zsz;
    @ViewInject(R.id.view_ysq)
    private View view_ysq;
    @ViewInject(R.id.view_yxj)
    private View view_yxj;

    private int currentPage=1;

    private int pageSize=10;
    private boolean isshow;
    private String priceType;

    private VehicleManAdapter adapter;
    private String orderState="2";//3.5
    private List<UserOrderEntity>list;
    private List<UserOrderEntity>datelist=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_booking);
        initToolBar();
        ViewUtils.inject(this);

        rcy=plv_book.getRefreshableView();
        rcy.setLayoutManager(new LinearLayoutManager(this));
        adapter=new VehicleManAdapter(BookingAct.this,datelist);
        rcy.setAdapter(adapter);
        selectCol(0);
        getDate();

        plv_book.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refresh();
                isshow=true;
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage++;
                isshow=true;
                getDate();
            }
        });

    }

    private void getDate() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("currentPage",currentPage+"");
        param.add("pageSize",pageSize+"");
        param.add("orderState",orderState);

        OkHttpUtils.post(Config.AUDITORDER, param, new OkHttpCallBack(BookingAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                plv_book.onRefreshComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                    priceType=result.getString("priceType");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<UserOrderEntity>>(){}.getType());

                    if(list!=null&&list.size()>0){
                        if(currentPage==1){
                            datelist.clear();
                        }
                        datelist.addAll(list);
                        adapter.notifyDate(datelist,priceType);
                    }else {
                        if(isshow){//刷新什么都不变

                        }else {//切换条件
                            adapter.notifyDate(new ArrayList<UserOrderEntity>(),priceType);
                            ToastTool.show(BookingAct.this,"暂无内容");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                plv_book.onRefreshComplete();

            }
        });
    }



    private void refresh() {
        currentPage=1;
        isshow=false;
        getDate();
    }

    @OnClick({R.id.iv_back,R.id.ll_zsz,R.id.ll_ysq,R.id.ll_yxj})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_zsz:
                selectCol(0);
                break;

            case R.id.ll_ysq:
                selectCol(1);
                break;

            case R.id.ll_yxj:
                selectCol(2);
                break;
        }
    }

    private void selectCol(int i) {
        view_zsz.setBackgroundColor(getResources().getColor(R.color.white));
        view_ysq.setBackgroundColor(getResources().getColor(R.color.white));
        view_yxj.setBackgroundColor(getResources().getColor(R.color.white));

        if(i==0){
            orderState="2";
            view_zsz.setBackgroundColor(getResources().getColor(R.color.orange_2));

        }else if(i==1){
            view_ysq.setBackgroundColor(getResources().getColor(R.color.orange_2));
            orderState="3";
        }else if(i==2){
            orderState="5";
            view_yxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
        }

        refresh();

    }

}
