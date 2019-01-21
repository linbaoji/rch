package com.rch.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/4/19.
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    TextView tvVersion;
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolBar();
        initControls();
    }

    private void initControls() {
        tvVersion= (TextView) findViewById(R.id.about_version);
        ivBack= (ImageView) findViewById(R.id.about_back);

        try {
            tvVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about_back:
                finish();
                break;
        }
    }
}
