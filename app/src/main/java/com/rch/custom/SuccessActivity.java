package com.rch.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/27.
 */

public class SuccessActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivBack;
    TextView tvTitle,tvDesc,tvBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);
        initToolBar();
        init();
    }

    private void init() {


        ivBack= (ImageView) findViewById(R.id.success_back);

        tvTitle= (TextView) findViewById(R.id.success_title);

        tvDesc= (TextView) findViewById(R.id.success_desc);

        tvBtn= (TextView) findViewById(R.id.success_ok);

        ivBack.setOnClickListener(this);
        tvBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.success_back:
                finish();
                break;
            case R.id.success_ok:
                break;
        }
    }


  /*  public void setAll(String titleText,String descText,View.OnClickListener backLinster,View.OnClickListener okLinster)
    {
        tvTitle.setText(titleText);
        tvDesc.setText(descText);
        ivBack.setOnClickListener(backLinster);
        tvBtn.setOnClickListener(okLinster);
    }

    public void setBackListener(View.OnClickListener mlistener)
    {
        ivBack.setOnClickListener(mlistener);
    }

    public void setTitile(String titleText)
    {
        tvTitle.setText(titleText);
    }

    public void setDesc(String descText)
    {
        tvDesc.setText(descText);
    }*/
}
