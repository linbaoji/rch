package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
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
 * Created by Administrator on 2018/10/11.
 */

public class CustomerManagementActivity extends SecondBaseActivity {

    @ViewInject(R.id.rl_search)
    RelativeLayout rl_search;
    @ViewInject(R.id.et_customer_name)
    private EditText et_customer_name;//搜索内容
    private String str_customer_name="";
    @ViewInject(R.id.plv_yg)
    private PullToRefreshRecyclerView plv_yg;
    @ViewInject(R.id.ld_view)
    LoadingView ld_view;
    private List<CustomerManagerEntity.UserListBean> dataList = new ArrayList<>();
    private List<CustomerManagerEntity.UserListBean> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private CustomerManageAdapter adapter;

    private String enterpriseId="";//车商Id   当用户类型为日常管理人，查询用户列表时必输，全网车商不需要输入
    private int currentPage = 1;
    private int pageSize = 10 ;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_customer_management);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("客户管理");
        if (getIntent()!=null)
            enterpriseId = getIntent().getStringExtra("enterpriseId");
        recyclerView=plv_yg.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        getCustomerManagerList("",currentPage,pageSize);

        plv_yg.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                str_customer_name = et_customer_name.getText().toString().trim();
                currentPage = 1;
                getCustomerManagerList(str_customer_name,currentPage,pageSize);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                str_customer_name = et_customer_name.getText().toString().trim();
                currentPage++;
                getCustomerManagerList(str_customer_name,currentPage,pageSize);
            }
        });
        et_customer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentPage =1;
                getCustomerManagerList(charSequence.toString().trim(),currentPage,pageSize);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        if (!TextUtils.isEmpty(enterpriseId))
            param.add("enterpriseId", enterpriseId);
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

                        if (customerManagerEntity!=null)
                            list = customerManagerEntity.getUserList();
                        if (currentPage==1)
                            dataList.clear();
                        dataList.addAll(list);
                        if (dataList!=null && dataList.size()>0) {
//                            rl_search.setVisibility(View.VISIBLE);
                            if (adapter == null) {
                                adapter = new CustomerManageAdapter(mContext, dataList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.updateData(dataList);
                            }

                            adapter.setOnItemClickListener(new CustomerManageAdapter.OnItemClick() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(mContext, CustomerDetailActivity.class);
                                    intent.putExtra("enterpriseId",enterpriseId);
                                    intent.putExtra("userId", dataList.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                        }else {
                            if (currentPage==1){
//                                rl_search.setVisibility(View.GONE);
                                ld_view.loadComplete();
                                ld_view.noContent();
                                ld_view.setNoContentIco(R.mipmap.wukehu);
                                ld_view.setNoContentTxt("您还没有任何客户噢~");
                            }else {
//                                rl_search.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
