package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.adatper.BookorderAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.PickTimeUtil;
import com.rch.common.ToastTool;
import com.rch.entity.OderListEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单管理列表
 * Created by Administrator on 2018/7/31.
 */

public class BookingorderAct extends BaseActivity {
    @ViewInject(R.id.plv_bookorder)
    private PullToRefreshRecyclerView plv_bookorder;

    @ViewInject(R.id.act_order_time_layout)
    private LinearLayout act_order_time_layout;
    @ViewInject(R.id.act_order_status_layout)
    private LinearLayout act_order_status_layout;
    @ViewInject(R.id.act_order_time)
    private TextView act_order_time;
    @ViewInject(R.id.act_order_status)
    private TextView act_order_status;
    @ViewInject(R.id.act_order_back)
    private ImageView act_order_back;


    private RecyclerView rcy;
    private BookorderAdapter adapter;//车辆管理适配器
    private List<OderListEntity> list = new ArrayList<>();

    String time = "", status = "1";
    int page = 1;
    int pageSize = 10;
    private Gson gson;

    String priceType = "0";
    private boolean isLoad;

    int type=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bookorder);
        initToolBar();
        ViewUtils.inject(this);
        type=getIntent().getIntExtra("type",0);
        if(!isLogin()&&type==1)
        {
//            startActivity(new Intent(this, MainActivity.class));
            startActivity(new Intent(this, NewMainActivity.class));
            finish();
        }
        Date date = new Date(System.currentTimeMillis());
        time = PickTimeUtil.getTimeYM(date);
        act_order_time.setText(time);
        rcy = plv_bookorder.getRefreshableView();
        rcy.setLayoutManager(new LinearLayoutManager(BookingorderAct.this));

        adapter = new BookorderAdapter(BookingorderAct.this, list, priceType);
        rcy.setAdapter(adapter);

        plv_bookorder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                onRefresh();
                isLoad = true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                isLoad = true;
                requestShopListData();
            }
        });

        onRefresh();
    }


    private void onRefresh() {
        isLoad = false;
        page = 1;
        requestShopListData();

    }


    @OnClick({R.id.act_order_time_layout, R.id.act_order_status_layout, R.id.act_order_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.act_order_time_layout:
                PickTimeUtil.pickShowYM(this, act_order_time, new PickTimeUtil.timeListener() {
                    @Override
                    public void time(String date) {
                        time = date;
                        onRefresh();
                    }
                });
                break;

            case R.id.act_order_status_layout:
                PickTimeUtil.ShowStatusView(this, act_order_status, new PickTimeUtil.statusListener() {
                    @Override
                    public void statusItem(String statusLable) {
                        status = statusLable;
                        onRefresh();
                    }
                });
                break;

            case R.id.act_order_back:
                finish();
                break;
        }
    }

    private void requestShopListData() {

        EncryptionTools.initEncrypMD5(getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("currentPage", String.valueOf(page));
        param.add("pageSize", String.valueOf(pageSize));
        param.add("conventionYearMonth", time);
        param.add("orderState", status);
        OkHttpUtils.post(Config.LISTORDERBYENT, param, new OkHttpCallBack(this, "加载中...") {
            @Override
            public void onSuccess(String data) {
                plv_bookorder.onRefreshComplete();
                try {
                    Log.e("data", data);
                    gson = new Gson();
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject resultObject = object.getJSONObject("result");
                    priceType = resultObject.getString("priceType");
                    JSONArray result = resultObject.getJSONArray("list");
                    List<OderListEntity> datelist = gson.fromJson(result.toString(), new TypeToken<List<OderListEntity>>() {
                    }.getType());
                    if (datelist != null && datelist.size() > 0) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(datelist);
                        adapter.updata(list, priceType);
                    } else {
                        if(isLoad){//说明是下拉或者加载无数据不变

                        }else {
                            adapter.updata(new ArrayList<OderListEntity>(), priceType);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                plv_bookorder.onRefreshComplete();
                ToastTool.show(BookingorderAct.this, error);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
