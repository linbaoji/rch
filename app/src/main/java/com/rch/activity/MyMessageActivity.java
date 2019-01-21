package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/17.
 */

public class MyMessageActivity extends BaseActivity implements View.OnClickListener {
    ImageView ivBack;
    LinearLayout lListOne,lListTwo,lListThree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initToolBar();
        initContros();
    }

    private void initContros()
    {
        ivBack= (ImageView) findViewById(R.id.message_back);
        lListOne= (LinearLayout) findViewById(R.id.msg_list_one);
        lListTwo= (LinearLayout) findViewById(R.id.msg_list_two);
        lListThree= (LinearLayout) findViewById(R.id.msg_list_three);

        //101-自有车商，1021-集团所属分销商，1022-非集团所属分销商，103-入驻车商
        String state=getUserInfo().getUserRoleType();
        if(state.equals("101")||state.equals("1021")||state.equals("1022")||state.equals("103"))
            lListOne.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(this);
        lListOne.setOnClickListener(this);
        lListTwo.setOnClickListener(this);
        lListThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.message_back:
                finish();
                break;
            case R.id.msg_list_one:
                startActivity(new Intent(this, BookingorderAct.class));
                break;
            case R.id.msg_list_two:
                break;
            case R.id.msg_list_three:
                startActivity(new Intent(this, CustomerAct.class));
                break;
        }
    }
}
