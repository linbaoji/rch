package com.rch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.base.MyApplication;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GlideUtils;
import com.rch.common.GsonUtils;
import com.rch.common.ToastTool;

import com.rch.custom.PromptDialog;
import com.rch.custom.StepView;
import com.rch.entity.CarEntity;
import com.rch.entity.OrderProcessBean;
import com.rch.entity.UserOrderEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的预约
 * Created by Administrator on 2018/5/23.
 */

public class MyAptAct extends BaseActivity{
    @ViewInject(R.id.iv_logo)
    private ImageView iv_logo;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_color)
    private TextView tv_color;
    @ViewInject(R.id.iv_state)
    private ImageView iv_state;
    @ViewInject(R.id.tv_zy)
    private TextView tv_zy;
    @ViewInject(R.id.tv_dm)
    private TextView tv_dm;
    @ViewInject(R.id.tv_adress)
    private TextView tv_adress;
    private PromptDialog dialog;

    @ViewInject(R.id.stv_yy)
    private StepView stv_yy;


    private String phone="";//电话号码
    private String orderId;
    private List<OrderProcessBean>list;
    private UserOrderEntity userentity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_myapt);
        initToolBar();
        ViewUtils.inject(this);
        orderId=getIntent().getExtras().getString("userOrder","");
//       entity= (UserOrderEntity) getIntent().getSerializableExtra("userOrder");
//       setUi();//设置界面
        getDate(orderId);//获取数据

    }

    private void getDate(String orderId) {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("orderId", orderId);
        OkHttpUtils.post(Config.ORDERDETAIL_URL, param, new OkHttpCallBack(MyAptAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson=new Gson();
                    JSONObject json=new JSONObject(data.toString());
                    JSONObject result=json.getJSONObject("result");
                    JSONArray array=result.getJSONArray("orderProcess");
                    JSONObject user=result.getJSONObject("order");
                    list= gson.fromJson(array.toString(),new TypeToken<List<OrderProcessBean>>(){}.getType());
                    userentity=GsonUtils.gsonToBean(user.toString(),UserOrderEntity.class);
                    setUi();//设置界面
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(MyAptAct.this,error);
            }
        });
    }

    private void setUi() {
        stv_yy.initDate(list);
        String province,city,address;
        if(userentity!=null){
            if(!TextUtils.isEmpty(userentity.getVehicleFullName())){
                tv_name.setText(userentity.getVehicleFullName()+"");//车名
            }else {
                tv_name.setText("");//车名
            }

            if(!TextUtils.isEmpty(userentity.getEnterpriseName())){
                tv_dm.setText(userentity.getEnterpriseName()+"");
            }else {
                tv_dm.setText("");
            }

            if(!TextUtils.isEmpty(userentity.getEnterpriseProvince())){
                province=userentity.getEnterpriseProvince();
            }else {
                province="";
            }

            if(!TextUtils.isEmpty(userentity.getEnterpriseCity())){
                city=userentity.getEnterpriseCity();
            }else {
                city="";
            }

            if(!TextUtils.isEmpty(userentity.getEnterpriseAddress())){
                address=userentity.getEnterpriseAddress();
            }else {
                address="";
            }

            tv_adress.setText(province+city+address);//地址
            if(!TextUtils.isEmpty(userentity.getIsGoup())) {
                if (userentity.getIsGoup().equals("1")) {
                    tv_zy.setText("直营");
                } else {
                    tv_zy.setText("其他");
                }

            }
//            if(entity.getOrderState().equals("1")||entity.getOrderState().equals("2")||entity.getOrderState().equals("3")){
//                iv_state.setImageResource(R.mipmap.myaptno);
//            }else {//4
//                iv_state.setImageResource(R.mipmap.myapt);
//            }
            if(!TextUtils.isEmpty(userentity.getVehicleColorName())) {
                tv_color.setText(userentity.getVehicleColorName());//
            }else {
                tv_color.setText("未知");
            }
            GlideUtils.showImg(MyAptAct.this,userentity.getVehicleImagePath(),R.mipmap.banner,DiskCacheStrategy.ALL,iv_logo);
//            Glide.with(MyAptAct.this).load(userentity.getVehicleImagePath()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.banner).into(iv_logo);
            if(!TextUtils.isEmpty(userentity.getTelphone())){
                phone=userentity.getTelphone();
            }


        }
    }


    @OnClick({R.id.tv_phone,R.id.car_history_back,R.id.ll_goinfo})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.tv_phone:
                if(TextUtils.isEmpty(phone)){
                    ToastTool.show(MyAptAct.this,"暂无座机号码");
                }else {
                    showPhoneDialog();
                }
                break;

            case R.id.car_history_back:
                finish();
                break;

            case R.id.ll_goinfo:
                startActivity(new Intent(MyAptAct.this,CarDetailActivity.class).putExtra("id",userentity.getVehicleId()).putExtra("version",userentity.getVersion()).putExtra("orderid",userentity.getId()));
                break;
        }
    }

    /**
     * 弹出dialog
     */
    private void showPhoneDialog() {
        dialog=new PromptDialog(MyAptAct.this);
        dialog.setText(phone);

        dialog.setLeftButtonText("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        dialog.setRightButtonText("呼叫", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                String pare="tel:"+phone;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse(pare);
                intent.setData(data);
                startActivity(intent);

            }
        });

        dialog.show();
    }
}
