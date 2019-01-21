package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.SecondBaseActivity;

/**
 * 认证成功页面
 * Created by Administrator on 2018/11/7.
 */

public class VerifySucessActivity extends SecondBaseActivity{
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_suc)
    private TextView tv_suc;
    @ViewInject(R.id.tv_gohome)
    private TextView tv_gohome;
    @ViewInject(R.id.tv_dec)
    private TextView tv_dec;

    private String from;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_verifysucess);
    }

    @Override
    public void init(Bundle savedInstanceState) {
     from=getIntent().getExtras().getString("from","");
     if(from.equals("2")){
         tv_title.setText("分销商认证");
         tv_suc.setText("认证成功");
         tv_gohome.setText("去首页");
         tv_dec.setVisibility(View.GONE);
     }else {
         tv_title.setText("车商认证");
         tv_suc.setText("提交成功,审核中");
         tv_gohome.setText("去逛逛");
         tv_dec.setVisibility(View.VISIBLE);
     }
    }

    @OnClick({R.id.tv_gohome})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_gohome:
                AppManager.getAppManager().finishActivity(DistributorActivity.class);
                AppManager.getAppManager().finishActivity(AuthenticateActivity.class);
                Intent intent = new Intent(mContext, NewMainActivity.class);
                intent.putExtra("from_verifysucess",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
}
