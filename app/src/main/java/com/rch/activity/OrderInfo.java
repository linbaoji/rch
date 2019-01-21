package com.rch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.CalculationTool;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.custom.MyAlertDialog;
import com.rch.custom.MySelfSheetDialog;
import com.rch.custom.OrderStatusInfo;
import com.rch.custom.StepOwnView;
import com.rch.entity.OderListEntity;
import com.rch.entity.OrderInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单详情界面
 * Created by Administrator on 2018/7/31.
 */

public class OrderInfo extends BaseActivity implements View.OnClickListener{

    ImageView act_order_info_back,act_order_info_img,act_order_info_icon;
    TextView act_order_info_name,act_order_info_phone,act_order_info_title,act_order_info_desc,act_order_info_one,act_order_info_two,act_order_info_one_money,act_order_info_two_money,
            tv_deleat,act_order_info_left,act_order_info_right;
    LinearLayout act_order_info_one_layout,act_order_info_two_layout,ll_order;
    private ImageView iv_more;

    OrderStatusInfo orderStatusInfo;
    private LinearLayout ll_bottom;
    TextView tv_title;


    String orderId="";
    String priceType="";
    private Gson gson;
    OrderInfoEntity entity;
    private JSONArray array;
    private String type;//0从预约审核列表过来，1从订单管理列表过来
    private String httpurl;
    private List<String>strlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_orderinfo);
        initToolBar();
        AppManager.getAppManager().addActivity(this);

        orderId=getIntent().getExtras().getString("orderId","");
        type=getIntent().getExtras().getString("type","1");
        initContors();

        if(type.equals("0")){
            tv_title.setText("预约详情");
            iv_more.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            act_order_info_left.setText("客户未看车");
            act_order_info_right.setText("确认客户已看车");
            httpurl=Config.AUDITORDERDETAILBYENT;

        }else {//订单管理里面进来
            tv_title.setText("订单详情");
            iv_more.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);
            act_order_info_left.setText("提交客户看车信息");
            act_order_info_right.setText("拨打客户电话");
            httpurl=Config.GETORDERDETAILBYENT;

        }

        httpOrderInfo();



    }

    private void initContors()
    {
        act_order_info_back= (ImageView) findViewById(R.id.act_order_info_back);
        act_order_info_back.setOnClickListener(this);
        act_order_info_img= (ImageView) findViewById(R.id.act_order_info_img);
        act_order_info_icon= (ImageView) findViewById(R.id.act_order_info_icon);

        act_order_info_name= (TextView) findViewById(R.id.act_order_info_name);
        act_order_info_phone= (TextView) findViewById(R.id.act_order_info_phone);
        act_order_info_title= (TextView) findViewById(R.id.act_order_info_title);
        act_order_info_desc= (TextView) findViewById(R.id.act_order_info_desc);
        act_order_info_one= (TextView) findViewById(R.id.act_order_info_one);
        act_order_info_two= (TextView) findViewById(R.id.act_order_info_two);
        act_order_info_one_money= (TextView) findViewById(R.id.act_order_info_one_money);
        act_order_info_two_money= (TextView) findViewById(R.id.act_order_info_two_money);
        act_order_info_left= (TextView) findViewById(R.id.act_order_info_left);
        act_order_info_right= (TextView) findViewById(R.id.act_order_info_right);

        orderStatusInfo= (OrderStatusInfo) findViewById(R.id.act_order_info_order_status);

        act_order_info_one_layout= (LinearLayout) findViewById(R.id.act_order_info_one_layout);
        act_order_info_two_layout= (LinearLayout) findViewById(R.id.act_order_info_two_layout);
        ll_order= (LinearLayout) findViewById(R.id.shop_list_adapter_layout);
        tv_title= (TextView) findViewById(R.id.tv_title);
        ll_order.setOnClickListener(this);
        act_order_info_left.setOnClickListener(this);
        act_order_info_right.setOnClickListener(this);
        ll_bottom= (LinearLayout) findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.GONE);
        iv_more= (ImageView) findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);




    }


    private void httpOrderInfo()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("orderId",orderId);

        OkHttpUtils.post(httpurl, param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                try {
                    gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject resultObject=object.getJSONObject("result");
                    priceType=resultObject.getString("priceType");
                    JSONObject orderDetail=resultObject.getJSONObject("orderDetail");
                    entity=gson.fromJson(orderDetail.toString(), OrderInfoEntity.class);
                    array=resultObject.getJSONArray("flowchart");


                    if(!resultObject.isNull("cancelOrderCause")){
                        JSONArray strarray=resultObject.getJSONArray("cancelOrderCause");
                        strlist=gson.fromJson(strarray.toString(),new TypeToken<List<String>>(){}.getType());
                    }
                    init();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(OrderInfo.this,error);
            }
        });
    }

    private void init()
    {
        if(entity!=null)
        {

            act_order_info_name.setText(entity.getOrderUserName());
            act_order_info_phone.setText(entity.getOrderUserMobileEncryption());
            if (entity.getOrderUserSex().equals("1")){//男
                act_order_info_img.setImageResource(R.mipmap.img_boy);
            }else if (entity.getOrderUserSex().equals("2")){//女
                act_order_info_img.setImageResource(R.mipmap.img_girl);
            }else {//未知
                act_order_info_img.setImageResource(R.mipmap.img_moren);
            }

            act_order_info_title.setText(entity.getVehicleFullName());
            Glide.with(this).load(entity.getVehicleImage()).into(act_order_info_icon);

            String sMoney=CalculationTool.getCaluculationUnit(entity.getSalesPrice());//.isEmpty()?"":carEntity.getSalesPrice()+"万";
            act_order_info_one_money.setText(sMoney);

            String sDiscountMoney=CalculationTool.getCaluculationUnit(entity.getRatePrice());//carEntity.getRatePrice()+"万";
            act_order_info_two_money.setText(sDiscountMoney);


            if(priceType.equals("0"))
            {
                act_order_info_two_layout.setVisibility(View.GONE);
                act_order_info_one.setTextSize(12f);
                act_order_info_one_money.setTextSize(15f);
                act_order_info_one.setText("门店价:");
            }
            else if(priceType.equals("1"))
            {
                act_order_info_two_layout.setVisibility(View.VISIBLE);
//                    tvStore.setText("企业价:");
                act_order_info_two.setText("批发价:");
                if (sMoney.length()>7) {
                    act_order_info_one_money.setTextSize(6f);
                }else {
                    act_order_info_one_money.setTextSize(11f);
                }
                if (sDiscountMoney.length()>7){
                    act_order_info_two_money.setTextSize(6f);
                }else {
                    act_order_info_two_money.setTextSize(11f);
                }
            }else
            {
                act_order_info_two_layout.setVisibility(View.VISIBLE);
                act_order_info_two.setText("分销价:");
                if (sMoney.length()>7) {
                    act_order_info_one_money.setTextSize(6f);
                }else {
                    act_order_info_one_money.setTextSize(11f);
                }
                if (sDiscountMoney.length()>7){
                    act_order_info_two_money.setTextSize(6f);
                }else {
                    act_order_info_two_money.setTextSize(11f);
                }
            }
        }
        if(array!=null)
        {
            orderStatusInfo.init(array);
        }

        if(type.equals("0")){//从预约单过来
            if(entity.getOrderState().equals("2")) {
                ll_bottom.setVisibility(View.VISIBLE);
            }else {
                ll_bottom.setVisibility(View.GONE);

            }
        }else {//从订单管理过来
            ll_bottom.setVisibility(View.VISIBLE);

            //预约单状态：1待回访  2客户到店看车3门店已确认看车  4客户已买车5门店确认失败6已取消
            if (entity.getOrderState().equals("2")){//只能拨打电话  2客户到店看车
                iv_more.setVisibility(View.GONE);
                act_order_info_left.setVisibility(View.GONE);
                act_order_info_right.setVisibility(View.VISIBLE);
            }else if (entity.getOrderState().equals("3")){//只能取消  3门店已确认看车
                iv_more.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.GONE);
            }else if (entity.getOrderState().equals("4") ||entity.getOrderState().equals("6")){//不能操作 4客户已买车 6已取消
                iv_more.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shop_list_adapter_layout:
                startActivity(new Intent(OrderInfo.this,CarDetailActivity.class).putExtra("id",entity.getVehicleId()).putExtra("version","").putExtra("orderid",entity.getId()));
                break;
            case R.id.act_order_info_left:
                if(type.equals("1")){//订单管理过来
                    Intent intent=new Intent(OrderInfo.this,FillInfoAct.class);
                    intent.putExtra("orderId",entity.getId());
                    startActivity(intent);
                }else {
                  Intent intent=new Intent(OrderInfo.this,DisMissAct.class);
                  intent.putExtra("orderId",entity.getId());
                  intent.putExtra("auditType","2");
                  startActivity(intent);
                }

                break;

            case R.id.act_order_info_right:
                if(type.equals("1")){
                    showAlertDialog();
                }else {
                    MyAlertDialog dialog=new MyAlertDialog(OrderInfo.this).builder();
                    dialog.setMsg("确认客户已经看车");
                    dialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            upload();

                        }
                    });
                    dialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    dialog.show();
//                    Intent intent=new Intent(OrderInfo.this,DisMissAct.class);
//                    intent.putExtra("orderId",entity.getId());
//                    intent.putExtra("auditType","1");
//                    startActivity(intent);
                }



                break;

            case R.id.iv_more:
                MySelfSheetDialog dialog=new MySelfSheetDialog(OrderInfo.this);
                dialog.builder().addSheetItem("取消此订单", MySelfSheetDialog.SheetItemColor.Gray, new MySelfSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent=new Intent(OrderInfo.this,CanceOrderAct.class);
                        intent.putStringArrayListExtra("list", (ArrayList<String>) strlist);
                        intent.putExtra("orderId",entity.getId());
                        startActivity(intent);

                    }
                }).show();
                break;

            case R.id.act_order_info_back:
                finish();
                break;
        }
    }

    private void upload() {
            EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
            RequestParam param = new RequestParam();
            param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
            param.add("timestamp", EncryptionTools.TIMESTAMP);
            param.add("nonce", EncryptionTools.NONCE);
            param.add("signature", EncryptionTools.SIGNATURE);
            param.add("orderId",orderId);
//            param.add("remark",mark);
            param.add("auditType","1");
            OkHttpUtils.post(Config.AUDITVEHICLE, param, new OkHttpCallBack(OrderInfo.this,"加载中...") {
                @Override
                public void onSuccess(String data) {
                    httpOrderInfo();

                }

                @Override
                public void onError(int code, String error) {

                }
            });
        }


    private void showAlertDialog() {
        MyAlertDialog dialog = new MyAlertDialog(OrderInfo.this);
        dialog.builder().setTitle("").setMsg(entity.getOrderUserMobile()).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pare="tel:"+entity.getOrderUserMobile();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse(pare);
                intent.setData(data);
                startActivity(intent);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpOrderInfo();
    }
}
