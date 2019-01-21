package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
import com.rch.adatper.CarDealerAdapter;
import com.rch.adatper.CustomerDetailAdapter;
import com.rch.adatper.CustomerManageAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.entity.CustomerManagerEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 车商
 * Created by Administrator on 2018/10/11.
 */

public class CarDealerActivity extends SecondBaseActivity{
    @ViewInject(R.id.plv_yg)
    private PullToRefreshRecyclerView plv_yg;
    @ViewInject(R.id.ld_view)
    LoadingView ld_view;
    List<CustomerManagerEntity.EnterListBean> dataList = new ArrayList<>();
    List<CustomerManagerEntity.EnterListBean> enterList = new ArrayList<>();

    private RecyclerView recyclerView;
    private CarDealerAdapter adapter;

    private int currentPage = 1;
    private int pageSize = 10 ;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_select_car_dealer);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("选择车商");



        recyclerView=plv_yg.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        getCustomerManagerList("",currentPage,pageSize);
        plv_yg.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage = 1;
                getCustomerManagerList("",currentPage,pageSize);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage++;
                getCustomerManagerList("",currentPage,pageSize);
            }
        });
    }

    //获取客户管理列表
    public void getCustomerManagerList(String searchName, final int currentPage, int pageSize) {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        if (!TextUtils.isEmpty(searchName))
            param.add("searchName", searchName);
        param.add("currentPage", currentPage+"");
        param.add("pageSize", pageSize+"");
        OkHttpUtils.post(Config.CUSTOMER_MANAGER_LIST, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                plv_yg.onRefreshComplete();
                ld_view.loadComplete();
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject json = jsonObject.getJSONObject("result");
                        CustomerManagerEntity customerManagerEntity = GsonUtils.gsonToBean(json.toString(), CustomerManagerEntity.class);

                        if (customerManagerEntity!=null){
                            enterList = customerManagerEntity.getEnterList();
                            if (currentPage==1){
                                dataList.clear();
                            }
                            dataList.addAll(enterList);

                            if (dataList!=null && dataList.size()>0){
                                if (adapter==null) {
                                    adapter = new CarDealerAdapter(mContext, dataList);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    adapter.updateData(dataList);
                                }
                                adapter.setOnItemClickListener(new CarDealerAdapter.OnItemClick() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        startActivity(new Intent(mContext,CustomerManagementActivity.class).putExtra("enterpriseId",enterList.get(position).getEnterpriseId()));
                                    }
                                });
                            }else {
                                if (currentPage==1){
                                    ld_view.loadComplete();
                                    ld_view.noContent();
                                    ld_view.setNoContentIco(R.mipmap.wukehu);
                                    ld_view.setNoContentTxt("您还没有任何客户噢~");
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ld_view.loadComplete();
                        plv_yg.onRefreshComplete();
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
                plv_yg.onRefreshComplete();
                ld_view.loadComplete();
            }
        });
    }
}
