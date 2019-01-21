package com.rch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rch.R;
import com.rch.activity.SoldcarAct;
import com.rch.adatper.SoldcarAdapter;
import com.rch.base.BaseFragment;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.entity.OwnerBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class WmcFragment extends BaseFragment{
    private PullToRefreshRecyclerView plv_mc;
    private RecyclerView recyclerView;
    private SoldcarAdapter adapter;
    private int currentPage=1;
    private int pageSize=10;
    private List<OwnerBean> list;
    private List<OwnerBean>datelist=new ArrayList<>();
    private LoadingView load_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fra_soldcar,null);
        plv_mc = (PullToRefreshRecyclerView)view.findViewById(R.id.plv_mc);
        load_view= (LoadingView) view.findViewById(R.id.load_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
        load_view.loading();
        getDate();//获取数据
    }

    private void getDate() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("mobile",getUserInfo().getMobile());
        param.add("ptype","2");
        param.add("currentPage",currentPage+"");
        param.add("pageSize",pageSize+"");

        OkHttpUtils.post(Config.QUERTOWNERVEHICLE, param, new OkHttpCallBack(getActivity(),"加载中...") {
            @Override
            public void onSuccess(String data) {
                plv_mc.onRefreshComplete();
                load_view.loadComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<OwnerBean>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        if(currentPage==1){
                            datelist.clear();
                        }
                        datelist.addAll(list);
                        adapter.change(datelist);
                    }else {
                        if(currentPage==1){
//                            ToastTool.show(getActivity(),"暂无数据");
                            load_view.noContent();
                            plv_mc.setVisibility(View.GONE);
                        }else {
                            ToastTool.show(getActivity(),"无更多数据");
                            plv_mc.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                plv_mc.onRefreshComplete();
                load_view.loadError();
                plv_mc.setVisibility(View.GONE);
                ToastTool.show(getActivity(),error);
            }
        });


    }

    private void initDate() {
        recyclerView=plv_mc.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new SoldcarAdapter(getActivity(),datelist);
        recyclerView.setAdapter(adapter);

        plv_mc.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage=1;
                getDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage++;
                getDate();
            }
        });

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                currentPage=1;
                getDate();
                load_view.loading();
            }
        });
    }


    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }
}