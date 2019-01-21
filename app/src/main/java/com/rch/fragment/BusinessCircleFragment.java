package com.rch.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.activity.PublishCircleAct;
import com.rch.adatper.CircleAdapter;
import com.rch.base.BaseFrag;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.SpUtils;
import com.rch.custom.LoadingView;
import com.rch.entity.BussBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class BusinessCircleFragment extends BaseFrag {
    @ViewInject(R.id.circle_recyclerView)
    private PullToRefreshRecyclerView circle_recyclerView;

    @ViewInject(R.id.load_view)
    private LoadingView load_view;

    private RecyclerView recyclerView;
    private CircleAdapter adapter;

    @ViewInject(R.id.tv_right)
    private TextView tv_right;//发布按钮未登录不显示

    private int currentPage=1;
    private int pageSize=20;

    private Gson gson;
    private List<BussBean>list;
    private List<BussBean>addlist=new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fra_businesscircle,null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        gson=new Gson();
        recyclerView=circle_recyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        setRightVis();

        circle_recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage=1;
                getCircleList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
               currentPage++;
                getCircleList();
            }
        });

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                currentPage=1;
                load_view.loading();
                getCircleList();
            }
        });

        load_view.loading();
        getCircleList();
    }

    private void getCircleList() {
        RequestParam param=new RequestParam();
        param.add("currentPage",currentPage+"");
        param.add("pageSize",pageSize+"");
        OkHttpUtils.post(Config.GETCIRCLELIST_URL, param, new OkHttpCallBack(mActivity,"") {
            @Override
            public void onSuccess(String data) {
                circle_recyclerView.onRefreshComplete();
                load_view.loadComplete();
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    int totalSize=result.getInt("totalSize");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<BussBean>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        if(currentPage==1){
                            addlist.clear();
                        }
                        addlist.addAll(list);
                        if(adapter==null){
                            adapter=new CircleAdapter(getActivity(),addlist);
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.notifyDateChange(addlist);
                        }
                    }else {
                        if(currentPage==1){
                           load_view.noContent();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
             circle_recyclerView.onRefreshComplete();
             load_view.loadError();
            }
        });
    }

    //用户角色类型   101-自营车商管理员，102-自营车商员工，201-全网车商管理员，202-全网车商员工 ,301-企业分销商管理员,302-企业分销商员工,401-个人分销商,501-普通用户
    private void setRightVis(){
        if (!SpUtils.getIsLogin(mActivity)){
            tv_right.setVisibility(View.GONE);
        }else {
            String rlyType=getUserInfo().getUserRoleType();
            if(rlyType.equals("101")||rlyType.equals("201")||rlyType.equals("301")||rlyType.equals("401")) {
                tv_right.setVisibility(View.VISIBLE);
            }else {
                if(!TextUtils.isEmpty(getUserInfo().getIfDailyadmin())&&getUserInfo().getIfDailyadmin().equals("1")) {
                    tv_right.setVisibility(View.VISIBLE);
                }else {
                    tv_right.setVisibility(View.GONE);
                }
            }
        }
    }
    @OnClick({R.id.tv_right})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_right:
                if(!ButtonUtils.isFastDoubleClick(view.getId())) {
                    startActivity(new Intent(mActivity, PublishCircleAct.class));
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (receiver ==null){
            receiver = new TextBroadcastReceiver();
            mActivity.registerReceiver(receiver,new IntentFilter(TEXT_RECEIVER));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            mActivity.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private TextBroadcastReceiver receiver;
    public static final String TEXT_RECEIVER = "text_receiver";



    private class TextBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setRightVis();
        }
    }

    /**
     * 如果为暂无数据再点击就刷新
     */
    public void upDate() {
        if(addlist.size()==0){
            load_view.loading();
            currentPage=1;
            getCircleList();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            setRightVis();
        }
    }
}
