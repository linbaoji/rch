package com.rch.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/4/19.
 */

public class InviteCodeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        initToolBar();
    }
}
