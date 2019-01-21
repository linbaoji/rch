package com.rch.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
import com.rch.adatper.FeedAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.entity.FeedbackEntity;
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

public class FeedbackListActivity extends SecondBaseActivity{
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    @ViewInject(R.id.feed_pulltorefresh)
    private PullToRefreshRecyclerView feed_pulltorefresh;
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private List<FeedbackEntity> list;
    private List<FeedbackEntity> datalist=new ArrayList<>();
    private int currentPage=1;
    private int pageSize=10;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_feedbacklist);
    }

    @Override
    public void init(Bundle savedInstanceState) {
         setTopTitle("我的反馈");
         recyclerView=feed_pulltorefresh.getRefreshableView();
         recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
         adapter=new FeedAdapter(mContext,datalist);
         recyclerView.setAdapter(adapter);
         getFeedbackList(currentPage,pageSize);

        feed_pulltorefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage=1;
                getFeedbackList(currentPage,pageSize);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                currentPage++;
                getFeedbackList(currentPage,pageSize);
            }
        });
    }

    private void getFeedbackList(final int currentPage, int pageSize ){
        RequestParam param = new RequestParam();
        param.add("currentPage", currentPage+"");
        param.add("pageSize", pageSize+"");
        OkHttpUtils.post(Config.FEEDBACK_LIST, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                feed_pulltorefresh.onRefreshComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<FeedbackEntity>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        if(currentPage==1){
                            datalist.clear();
                        }
                        datalist.addAll(list);
                        adapter.notifyData(datalist);
                    }else {
                        if(currentPage==1){
                            load_view.noContent();
                            load_view.setNoContentTxt("暂无数据");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(int code, String error) {
                load_view.loadError();
                feed_pulltorefresh.onRefreshComplete();
                ToastTool.show(mContext,error);

            }
        });
    }
}
