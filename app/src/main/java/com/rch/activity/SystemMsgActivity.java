package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rch.R;
import com.rch.adatper.NotifAdapter;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SystemMsgActivity extends BaseActivity {

    ImageView notif_back;
    PullToRefreshRecyclerView notif_recyclerView;
    RecyclerView recyclerView;
    NotifAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        initToolBar();
    }
}
