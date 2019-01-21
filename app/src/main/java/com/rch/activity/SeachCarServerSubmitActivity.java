package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.DensityUtil;
import com.rch.common.EncryptionTools;
import com.rch.common.ReceiverTool;
import com.rch.common.ToastTool;
import com.rch.custom.HomeCarLayout;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by Administrator on 2018/7/19.
 */

public class SeachCarServerSubmitActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivBack;
    TextView serach_car_submit_sell,serach_car_submit_shop,content_one;

    LinearLayout serach_car_submit_more,ll_tj;
    HomeCarLayout serach_car_submit_layout;

    private TextView tv_decc;

    int type=1;//1卖车提交，2找车提交，汽车检测提交,3认证成功,4提交认证分销商成功,5,发布新车成功

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_server_submit);
        type=getIntent().getIntExtra("type",1);
        initControl();
        setData();
    }

    public void initControl()
    {
        ll_tj = (LinearLayout) findViewById(R.id.ll_tj);
        ivBack= (ImageView) findViewById(R.id.serach_car_submit_back);

        serach_car_submit_sell= (TextView) findViewById(R.id.serach_car_submit_sell);
        serach_car_submit_shop= (TextView) findViewById(R.id.serach_car_submit_shop);
        content_one= (TextView) findViewById(R.id.content_one);
        serach_car_submit_more= (LinearLayout) findViewById(R.id.serach_car_submit_more);
        serach_car_submit_layout= (HomeCarLayout) findViewById(R.id.serach_car_submit_layout);
        tv_decc= (TextView) findViewById(R.id.tv_decc);

        if(type==1) {
            content_one.setText("车源发布成功");
            tv_decc.setText("温馨提示：客服会尽快联系您，请留意来电");
//            serach_car_submit_shop.setText("继续逛逛");
            serach_car_submit_sell.setVisibility(View.VISIBLE);
        }else if(type==2){
            content_one.setText("订单提交成功");
            tv_decc.setText("温馨提示：客服会尽快联系您，请留意来电");
//            serach_car_submit_shop.setText("去首页逛逛");
            DensityUtil densityUtil=new DensityUtil(this);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) serach_car_submit_shop.getLayoutParams();
            params.width=densityUtil.dip2px(240);
            serach_car_submit_shop.setLayoutParams(params);
            serach_car_submit_sell.setVisibility(View.GONE);

        }else if(type==3){
            content_one.setText("提交成功,审核中...");
            tv_decc.setText("我们正在快马加鞭为您审核，请您耐心等待");
            DensityUtil densityUtil=new DensityUtil(this);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) serach_car_submit_shop.getLayoutParams();
            params.width=densityUtil.dip2px(240);
            serach_car_submit_shop.setText("去首页逛逛");
            serach_car_submit_shop.setLayoutParams(params);
            serach_car_submit_sell.setVisibility(View.GONE);
        }else if (type == 4){//提交认证分销商成功
            content_one.setText("提交成功,审核中...");
            tv_decc.setText("我们正在快马加鞭为您审核，请您耐心等待");
            DensityUtil densityUtil=new DensityUtil(this);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) serach_car_submit_shop.getLayoutParams();
            params.width=densityUtil.dip2px(240);
            serach_car_submit_shop.setText("先逛逛");
            serach_car_submit_shop.setLayoutParams(params);
            serach_car_submit_sell.setVisibility(View.GONE);
            ll_tj.setVisibility(View.GONE);
            serach_car_submit_layout.setVisibility(View.GONE);
            serach_car_submit_more.setVisibility(View.GONE);
        }else if (type == 5){//发布新车成功
            content_one.setText("提交成功,审核中...");
            tv_decc.setText("我们正在快马加鞭为您审核，请您耐心等待");
            serach_car_submit_shop.setText("继续发布车辆");
            serach_car_submit_sell.setText("去车辆管理");
            serach_car_submit_sell.setVisibility(View.VISIBLE);
            ll_tj.setVisibility(View.GONE);
            serach_car_submit_layout.setVisibility(View.GONE);
            serach_car_submit_more.setVisibility(View.GONE);
        }


        ivBack.setOnClickListener(this);
        serach_car_submit_sell.setOnClickListener(this);
        serach_car_submit_shop.setOnClickListener(this);
        serach_car_submit_more.setOnClickListener(this);
    }

    private void setData()
    {
        requestShopListData();
    }

    public void requestShopListData()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("currentPage",String.valueOf(1));
        param.add("pageSize",String.valueOf(4));
        param.add("orderType", "4");
        param.add("source","3");
        OkHttpUtils.post(Config.CARLIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
//                String priceType=JsonTool.getResultStr(data,"priceType");
//                List<CarEntity> tempList= JsonTool.getCarListData(data);
//                serach_car_submit_layout.initData(tempList,1);
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SeachCarServerSubmitActivity.this,error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.serach_car_submit_back:
                finish();
                break;
            case R.id.serach_car_submit_more:
                sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                 if(type==2){
                    AppManager.getAppManager().finishActivity(SeachCarServerActivity.class);
                }
                finish();
                break;
            case R.id.serach_car_submit_shop:
                if(type==2) {
                    AppManager.getAppManager().finishActivity(SeachCarServerActivity.class);
                }
                sendBroadcast(new Intent(ReceiverTool.REFRESHHOMEFRAGMENTMODULE));
                if (type == 5) {//发布新车成功 继续发布车辆
                    AppManager.getAppManager().finishActivity(ReleaseNewCarActivity.class);
                    startActivity(new Intent(SeachCarServerSubmitActivity.this, ReleaseNewCarActivity.class));
                }
                finish();

                break;
            case R.id.serach_car_submit_sell:
                sendBroadcast(new Intent(ReceiverTool.REFRESHHOMEFRAGMENTMODULE));
                if (type == 5) {//发布新车成功 去车辆管理
                    //关闭之前的界面重新打开新车管理界面
                    AppManager.getAppManager().finishActivity(CarManagerNewActivity.class);
                    AppManager.getAppManager().finishActivity(ReleaseNewCarActivity.class);
                    startActivity(new Intent(SeachCarServerSubmitActivity.this, CarManagerNewActivity.class));
                }
                finish();

                break;
        }
    }
}
