package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/18.
 */

public class BookingDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        initToolBar();
    }
}
