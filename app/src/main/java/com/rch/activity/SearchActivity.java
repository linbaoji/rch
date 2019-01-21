package com.rch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.adatper.SearchAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.ClearEditText;
import com.rch.custom.SerchLableView;
import com.rch.entity.SearchEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/22.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    TextView tvEsc,tvCountDesc;
    ClearEditText clearEditText;
    RecyclerView recyclerView;

    LinearLayout ll_serchresult,ll_serchcace;//搜索结果，搜索缓存
    TextView tv_deleat;

    SearchAdapter adapter;


    private TagFlowLayout id_flowlayout;

    List<SearchEntity> list=new ArrayList<>();
    List<SearchEntity>cacelist=new ArrayList<>();

    boolean ishaveCace=false;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolBar();
        initControls();
        gson=new Gson();
        showResultOrCace();//显示搜索结果还是历史记录
        setData();
    }

    /**
     * 搜索结果历史记录
     */
    private void showResultOrCace() {
        if(!TextUtils.isEmpty(SpUtils.getCaceSearchList(SearchActivity.this))){
            ishaveCace=true;
            ll_serchcace.setVisibility(View.VISIBLE);
            id_flowlayout.setVisibility(View.VISIBLE);
            ll_serchresult.setVisibility(View.GONE);
            cacelist=gson.fromJson(SpUtils.getCaceSearchList(SearchActivity.this).toString(),new TypeToken<List<SearchEntity>>(){}.getType());

            id_flowlayout.setAdapter(new TagAdapter(cacelist) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    View textview=View.inflate(SearchActivity.this,R.layout.item_serchlable,null);
                    TextView tv_lab= (TextView) textview.findViewById(R.id.tv_lable);
//                    if(!TextUtils.isEmpty(cacelist.get(position).getSeriesName())) {
//                        tv_lab.setText(cacelist.get(position).getSeriesName());
//                    }else {
//                        tv_lab.setText(cacelist.get(position).getBrandName());
//                    }
                    tv_lab.setText(cacelist.get(position).getShowName());
                    return textview;
                }
            });

        }else {
            ishaveCace=false;
            ll_serchcace.setVisibility(View.GONE);
            id_flowlayout.setVisibility(View.GONE);
            ll_serchresult.setVisibility(View.VISIBLE);

        }

        id_flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                SpUtils.setCaceSearchList(SearchActivity.this, gson.toJson(cacelist));
                Intent intent = new Intent(SearchActivity.this, NewMainActivity.class);
                intent.putExtra("from_SearchActivity",true);
                intent.putExtra("serchentity",cacelist.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    private void initControls() {
        tvEsc= (TextView) findViewById(R.id.search_esc);
        tvCountDesc= (TextView) findViewById(R.id.search_count);
        recyclerView= (RecyclerView) findViewById(R.id.search_recycler_view);
        clearEditText= (ClearEditText) findViewById(R.id.search_et);
        clearEditText.addTextChangedListener(watcher);
        tvEsc.setOnClickListener(this);
        ll_serchresult= (LinearLayout) findViewById(R.id.ll_serchresult);
        ll_serchcace= (LinearLayout) findViewById(R.id.ll_serchcace);
        tv_deleat= (TextView) findViewById(R.id.tv_deleat);
        tv_deleat.setOnClickListener(this);

        id_flowlayout= (TagFlowLayout) findViewById(R.id.id_flowlayout);

        //弹出软键盘
        clearEditText.setFocusable(true);
        clearEditText.setFocusableInTouchMode(true);
        clearEditText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)clearEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager!=null)
                    inputManager.showSoftInput(clearEditText, 0);
            }
        }, 500);
    }

    private void setData()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SearchAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setSearchTitleOnClickLister(new SearchAdapter.SearchTitleOnClickLister() {
            @Override
            public void onItem(SearchEntity entity) {
//                startSearchResultActivity(titleName);
                if(cacelist.size()>0){
                    for(int i=0;i<cacelist.size();i++){
                        if(entity.getSearchId().equals(cacelist.get(i).getSearchId())){
                            cacelist.remove(i);
                        }
                    }
                }
                cacelist.add(0,entity);

                if(cacelist.size()>10){
                    cacelist = cacelist.subList(0,10);//截取前10条
                }

                SpUtils.setCaceSearchList(SearchActivity.this, gson.toJson(cacelist));
                Intent intent = new Intent(SearchActivity.this, NewMainActivity.class);
                intent.putExtra("from_SearchActivity",true);
                intent.putExtra("serchentity",entity);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

//        clearEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == event.KEYCODE_SEARCH) {
//                    String titleName=clearEditText.getText().toString().trim();
//                    startSearchResultActivity(titleName);
//                    return true;
//                }
//                return false;
//            }
//        });
    }


    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           if(!TextUtils.isEmpty(s.toString())){//不为空
             ll_serchcace.setVisibility(View.GONE);
             ll_serchresult.setVisibility(View.VISIBLE);
           }else {//为空时候
               if(ishaveCace){
                   ll_serchcace.setVisibility(View.VISIBLE);
                   ll_serchresult.setVisibility(View.GONE);
               }else {
                   ll_serchcace.setVisibility(View.GONE);
                   ll_serchresult.setVisibility(View.VISIBLE);
               }
           }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String searchContent=clearEditText.getText().toString().trim();
            if(searchContent.isEmpty()) {
                tvCountDesc.setVisibility(View.GONE);
                adapter.updateSearchData(new ArrayList<SearchEntity>());
                return;
            }
            httpKeyResult(searchContent);
        }
    };

    /*搜索页-模糊匹配车辆名称*/
    private void httpKeyResult(String searchContent)
    {
        RequestParam param = new RequestParam();
        param.add("currentPage","");
        param.add("pageSize", "");
        param.add("searchName",searchContent);
        OkHttpUtils.post(Config.SEARCH, param, new OkHttpCallBack(this,"") {

            @Override
            public void onSuccess(String data) {
                String totalSize=JsonTool.getResultStr(data,"totalSize");
                list=JsonTool.getSearchData(data);
                adapter.updateSearchData(list);
                if(totalSize.isEmpty())
                    tvCountDesc.setVisibility(View.GONE);
                else
                {
                    tvCountDesc.setVisibility(View.VISIBLE);
                    tvCountDesc.setText("搜索结果：共为您找到"+totalSize+"条数据");
                }
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SearchActivity.this,error);
            }
        });
    }

    private void startSearchResultActivity(String titleName)
    {
        startActivity(new Intent(this,SearchResultActivity.class).putExtra("titleName",titleName));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.search_esc:
                finish();
                break;

            case R.id.tv_deleat:
                SpUtils.setCaceSearchList(SearchActivity.this,"");
                cacelist.clear();
                showResultOrCace();
                break;
        }
    }
}
