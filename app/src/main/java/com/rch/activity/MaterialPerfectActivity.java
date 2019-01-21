package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ReceiverTool;

/**
 * Created by Administrator on 2018/4/12.
 */

public class MaterialPerfectActivity extends BaseActivity implements View.OnClickListener{

    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        initToolBar();
        initControls();
    }

    private void initControls() {
        ivBack= (ImageView) findViewById(R.id.material_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.material_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
        removeALLActivity();
    }
}
