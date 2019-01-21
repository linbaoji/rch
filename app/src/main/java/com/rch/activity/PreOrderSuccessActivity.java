package com.rch.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/11.
 */

public class PreOrderSuccessActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivBack;
    TextView tvTitle;
    String desc="";
    private TextView tv_jt;

    private RelativeLayout rl_order_success;
    private Button btn_continue,btn_look_order;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_order_success);
        initToolBar();
        desc=getIntent().getStringExtra("desc");
        initControls();
    }

    private void initControls() {
        rl_order_success = (RelativeLayout) findViewById(R.id.rl_order_success);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_look_order = (Button) findViewById(R.id.btn_look_order);
        btn_continue.setOnClickListener(this);
        btn_look_order.setOnClickListener(this);
        ivBack= (ImageView) findViewById(R.id.pre_order_success_back);
        tvTitle= (TextView) findViewById(R.id.pre_order_success_title);
        tv_jt= (TextView) findViewById(R.id.tv_jt);
        tv_jt.setVisibility(View.GONE);
        if(desc.equals("1")) {
//            tvTitle.setText("恭喜您，已经预约成功");
            rl_order_success.setBackgroundResource(R.mipmap.success);
        }else {
//            tv_jt.setVisibility(View.VISIBLE);
//            tvTitle.setText("您今日已预约成功,客服将跟您联系,");
//            tv_jt.setText("请留意接听哦");
            rl_order_success.setBackgroundResource(R.mipmap.successed);
        }
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pre_order_success_back:
                finish();
                break;
            case R.id.btn_continue:
                Intent intent = new Intent(PreOrderSuccessActivity.this, NewMainActivity.class);
                intent.putExtra("from_pre_order",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_look_order:
                startActivity(new Intent(PreOrderSuccessActivity.this,CarHistoryActivity.class));
                break;
        }
    }
}
