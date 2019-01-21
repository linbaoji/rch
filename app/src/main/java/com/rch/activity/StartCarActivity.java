package com.rch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.CarlibAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.entity.CarEntity;
import com.rch.fragment.NewCarFragment;
import com.rch.fragment.OneCarFragment;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/12/10.
 */

public class StartCarActivity extends SecondBaseActivity{
    @ViewInject(R.id.home_recyclerView)
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    @ViewInject(R.id.tv_newcar)
    private TextView tv_newcar;
    @ViewInject(R.id.tv_seccar)
    private TextView tv_seccar;
    @ViewInject(R.id.view_newcar)
    private View view_newcar;
    @ViewInject(R.id.view_seccar)
    private View view_seccar;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    @ViewInject(R.id.tv_totle)
    private TextView tv_totle;

    private RecyclerView recyclerView;
    int page=1;
    int pageSize=10;

    private Gson gson;
    List<CarEntity> shopList = new ArrayList<>();
    List<CarEntity> tempList;
    private CarlibAdapter adapter;

    private int type=1;//1新车 0 二手车
    private int adptertype=0;//适配器 0 新车 1二手车

    private boolean isStarNewCar = true;
    private String totalSize;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_startcar);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("明星车款");
        if (getIntent()!=null)
            isStarNewCar = getIntent().getBooleanExtra("isStarNewCar",true);
        gson=new Gson();
        recyclerView=pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (isStarNewCar){
            setTopColor(1);
        }else {
            setTopColor(0);
        }
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page=1;
                requestShopListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                requestShopListData();
            }
        });

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                page=1;
                requestShopListData();
                load_view.loading();
            }
        });
    }

    private void requestShopListData() {
        RequestParam param=new RequestParam();
        param.add("currentPage",String.valueOf(page));
        param.add("pageSize",String.valueOf(pageSize));
        param.add("ifNew",type+"");
        OkHttpUtils.post(Config.STARTCAR_URL, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                pullToRefreshRecyclerView.onRefreshComplete();
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    if(!result.isNull("totalSize")){
                        totalSize=result.getString("totalSize");
                    }
                    JSONArray array = result.getJSONArray("list");
                    tempList = gson.fromJson(array.toString(), new TypeToken<List<CarEntity>>() {
                    }.getType());
                    if (tempList != null && tempList.size() > 0) {
                        if (page == 1) {
                            shopList.clear();
                        }
                        shopList.addAll(tempList);
                        if(type==1){
                            adptertype=0;
                        }else {
                            adptertype=1;
                        }
                        if (adapter == null) {
                            adapter = new CarlibAdapter(mContext, shopList, adptertype);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateShopListData(shopList,adptertype);
                        }
                    } else {
                        if (page == 1) {
                            load_view.noContent();
                            load_view.setNoContentIco(R.mipmap.no_car_data);
                            load_view.setNoContentTxt("暂无数据");
                        }
                    }

                    if(page==1){//只需要下拉时候才显示
                        if(tv_totle.getVisibility()==View.GONE) {
                            tv_totle.setVisibility(View.VISIBLE);
                        }
                        i=3;
                        tv_totle.setText("共为您找到" + totalSize + "辆车");
                        timer = new Timer();
                        myTask = new MyTimerTask();
                        timer.schedule(myTask, 0, 1000);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
                pullToRefreshRecyclerView.onRefreshComplete();
                load_view.loadError();
            }
        });
    }



    private int i = 3;
    private Timer timer;
    private MyTimerTask myTask;




    /**
     * 倒计时
     *
     * @author wangbin
     *
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(i--);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                tv_btyzm.setEnabled(true);
//                tv_btyzm.setText("重新发送");
                tv_totle.setVisibility(View.GONE);
                timer.cancel();
                myTask.cancel();
            }
        }

    };

    @OnClick({R.id.rl_newcar,R.id.rl_seccar})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.rl_newcar:
                setTopColor(1);
                break;

            case R.id.rl_seccar:
                setTopColor(0);
                break;
        }
    }

    private void setTopColor(int i) {
        type = i;
        tv_newcar.setTextColor(getResources().getColor(R.color.gray_3));
        tv_seccar.setTextColor(getResources().getColor(R.color.gray_3));
        view_newcar.setBackgroundColor(getResources().getColor(R.color.white));
        view_seccar.setBackgroundColor(getResources().getColor(R.color.white));
        if(i==1){
            tv_newcar.setTextColor(getResources().getColor(R.color.btn_org1));
            view_newcar.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        }else if(i==0){
            tv_seccar.setTextColor(getResources().getColor(R.color.btn_org1));
            view_seccar.setBackgroundColor(getResources().getColor(R.color.btn_org1));
            view_seccar.setVisibility(View.VISIBLE);
        }

        page=1;
        load_view.loading();
        requestShopListData();

    }
}
