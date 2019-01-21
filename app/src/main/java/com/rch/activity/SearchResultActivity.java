package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rch.R;
import com.rch.adatper.CarListAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.ToastTool;
import com.rch.entity.CarEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener{

    ImageView ivBack;
    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    RecyclerView recyclerView;

    CarListAdapter adapter;
    List<CarEntity> shopList=new ArrayList<>();

    String titleName="";

    int page=1;
    int pageSize=10;
    String priceType="0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initToolBar();
        titleName=getIntent().getStringExtra("titleName");
        initControls();
        setData();
    }

    private void initControls()
    {
        ivBack= (ImageView) findViewById(R.id.search_result_back);
        pullToRefreshRecyclerView= (PullToRefreshRecyclerView) findViewById(R.id.search_result_recyclerView);

        recyclerView=pullToRefreshRecyclerView.getRefreshableView();

        ivBack.setOnClickListener(this);
    }

    private void setData() {

        onRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CarListAdapter(this,shopList,1);
        recyclerView.setAdapter(adapter);


        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                requestShopListData(true);
            }
        });

    }


    private void onRefresh()
    {
        page=1;
        requestShopListData(false);
    }

    public void requestShopListData(final boolean isLoad)
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("currentPage",String.valueOf(page));
        param.add("pageSize",String.valueOf(pageSize));
        param.add("city", "");
        param.add("brandId", "");
        param.add("salesMinPrice", "");
        param.add("salesMaxPrice", "");
        param.add("orderType", "");
        param.add("source","3");
        param.add("searchName",titleName);
        OkHttpUtils.post(Config.CARLIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                Log.e("data",data);
                priceType= JsonTool.getResultStr(data,"priceType");
                if(isLoad) {
                    List<CarEntity> tempList= JsonTool.getCarListData(data);
                    if ( tempList!= null && tempList.size() > 0)
                    {
                        shopList.addAll(tempList);
//                        adapter.updateShopListData(shopList,priceType,"");
                        adapter.updateShopListData(shopList);
                    }else {
                        page--;
                        ToastTool.show(SearchResultActivity.this, "没有更多信息");
                    }
                }else{
                    shopList=JsonTool.getCarListData(data);
//                    adapter.updateShopListData(shopList,priceType,"");
                    adapter.updateShopListData(shopList);
                }
                pullToRefreshRecyclerView.onRefreshComplete();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SearchResultActivity.this,error);
                pullToRefreshRecyclerView.onRefreshComplete();
            }
        });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.search_result_back:
                finish();
                break;
        }
    }
}
