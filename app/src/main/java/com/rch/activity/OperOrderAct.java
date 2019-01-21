package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;

/**
 * 操作预约
 * Created by acer on 2018/8/13.
 */

public class OperOrderAct extends BaseActivity{
    @ViewInject(R.id.act_order_info_back)
    private ImageView iv_finsh;
    @ViewInject(R.id.lv_info)
    private ListView lv_info;
    @ViewInject(R.id.tv_cancle)
    private TextView tv_cancle;

    private ArrayList<String> list = new ArrayList<>();

    private String remark;
    private String id;

    private int clickTemp;
    private Myadpter myadpter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_oper_order);
        initToolBar();
        ViewUtils.inject(this);
        list.add("客户电话打不通");
        list.add("客户没有看车意向");
        list.add("其他原因");
        id=getIntent().getExtras().getString("id","");

        if(list!=null&&list.size()>0){
            remark=list.get(0);
            myadpter=new Myadpter();
            lv_info.setAdapter(myadpter);
        }



        lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             remark=list.get(i);
             clickTemp=i+1;
             myadpter.notifyDataSetChanged();

            }
        });

    }

    @OnClick({R.id.act_order_info_back,R.id.tv_cancle})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.act_order_info_back:
                finish();
                break;

            case R.id.tv_cancle:
                goCancle();
                break;
        }
    }

    private void goCancle() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        if (!TextUtils.isEmpty(id))
            param.add("id",id);
        param.add("type","2");
        param.add("cacelReason",clickTemp+"");

        LogUtils.e("id:"+id+"===reson:"+clickTemp);
        OkHttpUtils.post(Config.OPER_ORDER_URL, param, new OkHttpCallBack(OperOrderAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                ToastTool.show(OperOrderAct.this,"已取消");
                sendBroadcast(new Intent(CustomerDetailActivity.CUSTOMER_DETAIL));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(OperOrderAct.this,error);
            }
        });

    }

    private class Myadpter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=View.inflate(OperOrderAct.this,R.layout.item_canorder,null);
            TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_select= (ImageView) view.findViewById(R.id.iv_select);
            tv_name.setText(list.get(i));
            if((clickTemp-1)==i){
//               iv_select.setVisibility(View.VISIBLE);
                 iv_select.setImageResource(R.mipmap.tijiao);
            }else {
                 iv_select.setImageResource(R.mipmap.ordercancle);
            }
            return view;
        }
    }
}
