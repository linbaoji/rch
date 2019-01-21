package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.adatper.CarHistoryAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.SpUtils;
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
 * 历史预约
 * Created by Administrator on 2018/5/21.
 */

public class CarHistoryActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivBack;
    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    RecyclerView recyclerView;

    CarHistoryAdapter adapter;

    int page=1;
    int pageSize=10;
    private List<UserOrderEntity>list=new ArrayList<>();
    private Gson gson;
    int type=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initToolBar();
        type=getIntent().getIntExtra("type",0);
        initControl();
        setData();
    }

    private void initControl() {
        ivBack = (ImageView) findViewById(R.id.car_history_back);
        ivBack.setOnClickListener(this);
        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.car_history_recyclerView);
        recyclerView=pullToRefreshRecyclerView.getRefreshableView();
        if(!isLogin()&&type==1)
        {
            startActivity(new Intent(this, NewMainActivity.class));
            finish();
        }
    }

    private void setData() {

        onRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarHistoryAdapter(this,list);
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


    /**
     * 获取数据
     * @param b
     */
    private void requestShopListData(boolean b) {

        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("currentPage",String.valueOf(page));
        param.add("pageSize",String.valueOf(pageSize));
        OkHttpUtils.post(Config.LISTORDERBYUSER, param, new OkHttpCallBack(CarHistoryActivity.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                pullToRefreshRecyclerView.onRefreshComplete();
                try {
                    gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray result=object.getJSONArray("result");
                    List<UserOrderEntity>datelist=gson.fromJson(result.toString(),new TypeToken<List<UserOrderEntity>>(){}.getType());
                    if(datelist!=null&&datelist.size()>0){
                        if(page==1){
                            list.clear();
                        }
                        list.addAll(datelist);
                        adapter.updateShopListData(list);
                    }else {
//                        page--;
                        ToastTool.show(CarHistoryActivity.this, "没有更多信息");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                pullToRefreshRecyclerView.onRefreshComplete();
                if(code==1||code==2||code==3){
                    clearAll();
                    SpUtils.clearSp(CarHistoryActivity.this);
                    startActivity(new Intent(CarHistoryActivity.this,LoginActivity.class));
                }
                ToastTool.show(CarHistoryActivity.this,error);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.car_history_back:
                finish();
                break;
        }

    }
}
