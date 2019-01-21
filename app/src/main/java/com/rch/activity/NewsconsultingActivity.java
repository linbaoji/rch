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
import com.rch.adatper.NewsConAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.ToastTool;
import com.rch.entity.HomeDateBean;
import com.rch.entity.RchNewsBean;
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

public class NewsconsultingActivity extends SecondBaseActivity{
    @ViewInject(R.id.new_recyclerView)
    private PullToRefreshRecyclerView new_recyclerView;
    private RecyclerView recyclerView;
    private NewsConAdapter adapter;
    private List<RchNewsBean> list;
    private List<RchNewsBean> datalist=new ArrayList<>();
    private int currentPage=1;
    private int pageSize=10;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_news);
    }

    @Override
    public void init(Bundle savedInstanceState) {
         setTopTitle("新闻资讯");
         recyclerView=new_recyclerView.getRefreshableView();
         recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
         adapter=new NewsConAdapter(mContext,datalist);
         recyclerView.setAdapter(adapter);

         getNoteList(currentPage,pageSize);

         new_recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
             @Override
             public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                 currentPage = 1;
                 getNoteList(currentPage,pageSize);
             }

             @Override
             public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                 currentPage++;
                 getNoteList(currentPage,pageSize);
             }
         });
    }

    private void getNoteList(final int currentPage, int pageSize ){
        RequestParam param = new RequestParam();
        param.add("currentPage", currentPage+"");
        param.add("pageSize", pageSize+"");
        OkHttpUtils.post(Config.NOTES_LIST, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                new_recyclerView.onRefreshComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<RchNewsBean>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        if(currentPage==1){
                            datalist.clear();
                        }
                        datalist.addAll(list);
                        adapter.notifyData(datalist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(int code, String error) {
                new_recyclerView.onRefreshComplete();
                ToastTool.show(mContext,error);

            }
        });
    }
}
