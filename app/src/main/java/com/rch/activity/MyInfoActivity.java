package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;

import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.InfoBean;
import com.rch.entity.ResultBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/4/19.
 */

public class MyInfoActivity extends SecondBaseActivity{
    @ViewInject(R.id.ll_qyxx)
    private LinearLayout ll_qyxx;
    @ViewInject(R.id.ll_ygxx)
    private LinearLayout ll_ygxx;
    @ViewInject(R.id.ll_ssqy)
    private LinearLayout ll_ssqy;
    @ViewInject(R.id.tv_frtitle)
    private TextView tv_frtitle;
    @ViewInject(R.id.iv_fr)
    private ImageView iv_fr;
    @ViewInject(R.id.tv_companynaem)
    private TextView tv_companynaem;
    @ViewInject(R.id.tv_regitdz)
    private TextView tv_regitdz;
    @ViewInject(R.id.tv_lxr)
    private TextView tv_lxr;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_releaname)
    private TextView tv_releaname;
    @ViewInject(R.id.tv_cardnum)
    private TextView tv_cardnum;
    @ViewInject(R.id.tv_ssqy)
    private TextView tv_ssqy;



    private String toDetail;
    private Gson gson;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_info);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toDetail=getIntent().getExtras().getString("type","");
        if(!TextUtils.isEmpty(toDetail)){
            if(toDetail.equals("2")){
                setTopTitle("企业信息");
                tv_frtitle.setText("法定代表人");
                iv_fr.setImageResource(R.mipmap.infofaren);
                ll_ssqy.setVisibility(View.GONE);//所属企业没有
                ll_qyxx.setVisibility(View.VISIBLE);
                ll_ygxx.setVisibility(View.VISIBLE);
            }else if(toDetail.equals("3")){
                setTopTitle("员工信息");
                tv_frtitle.setText("个人信息");
                iv_fr.setImageResource(R.mipmap.infogeren);
                ll_ssqy.setVisibility(View.VISIBLE);
                ll_qyxx.setVisibility(View.GONE);//企业详情不要
                ll_ygxx.setVisibility(View.VISIBLE);
            }else if(toDetail.equals("4")){
                setTopTitle("个人信息");
                tv_frtitle.setText("个人信息");
                iv_fr.setImageResource(R.mipmap.infogeren);
                ll_qyxx.setVisibility(View.GONE);//所属公司不要
                ll_ssqy.setVisibility(View.GONE);//企业详情不要
                ll_ygxx.setVisibility(View.VISIBLE);
            }
        }
        gson=new Gson();
        getInfo();
    }


    /**
     * 获取用户信息
     */
    private void getInfo() {
        RequestParam param = new RequestParam();
        OkHttpUtils.post(Config.MYINFO, param, new OkHttpCallBack(MyInfoActivity.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                gson=new Gson();
                try {
                    JSONObject json=new JSONObject(data);
                    JSONObject result=json.getJSONObject("result");
                    InfoBean bean=gson.fromJson(result.toString(),InfoBean.class);
                    if(bean!=null){
                      tv_companynaem.setText(isNull(bean.getEnterpriseName()));
                      tv_regitdz.setText(isNull(bean.getAddress()));
                      tv_lxr.setText(isNull(bean.getContacts()));
                      tv_phone.setText(isNull(bean.getContactsPhone()));
                      tv_releaname.setText(isNull(bean.getRealName()));
                      if (!TextUtils.isEmpty(bean.getLicenseNo())){
                          String licenseNo = bean.getLicenseNo();
                          String str = String.format("%s**********%s", licenseNo.substring(0, 6), licenseNo.substring(16, 18));
                          tv_cardnum.setText(isNull(str));
                      }else {
                          tv_cardnum.setText(isNull(bean.getLicenseNo()));
                      }
                      tv_ssqy.setText(isNull(bean.getEnterpriseName()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                if(code==1||code==2||code==3){
                    clearAll();
                    SpUtils.clearSp(MyInfoActivity.this);
                    startActivity(new Intent(MyInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(MyInfoActivity.this,error);
            }
        });

    }

    public String isNull(String tex)
    {
        if(TextUtils.isEmpty(tex)){
            return "--";
        }else {
            return tex;
        }
    }


}
