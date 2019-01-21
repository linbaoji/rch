package com.rch.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
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
import java.util.List;

/**
 * Created by acer on 2018/8/13.
 */

public class CanceOrderAct extends BaseActivity{
    @ViewInject(R.id.act_order_info_back)
    private ImageView iv_finsh;
    @ViewInject(R.id.lv_info)
    private ListView lv_info;
    @ViewInject(R.id.tv_cancle)
    private TextView tv_cancle;

    private ArrayList<String>list;

    private String remark;
    private String orderId;

    private int clickTemp;
    private Myadpter myadpter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cancleorder);
        initToolBar();
        ViewUtils.inject(this);
        list=getIntent().getStringArrayListExtra("list");
        orderId=getIntent().getExtras().getString("orderId","");

        if(list!=null&&list.size()>0){
            remark=list.get(0);
            myadpter=new Myadpter();
            lv_info.setAdapter(myadpter);
        }



        lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             remark=list.get(i);
             clickTemp=i;
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
        param.add("orderId",orderId);
        param.add("remark",remark);
        OkHttpUtils.post(Config.CANCELVEHICLE_URL, param, new OkHttpCallBack(CanceOrderAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                ToastTool.show(CanceOrderAct.this,"订单已取消");
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(CanceOrderAct.this,error);
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
            view=View.inflate(CanceOrderAct.this,R.layout.item_canorder,null);
            TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
            ImageView iv_select= (ImageView) view.findViewById(R.id.iv_select);
            tv_name.setText(list.get(i));
            if(clickTemp==i){
//               iv_select.setVisibility(View.VISIBLE);
                 iv_select.setImageResource(R.mipmap.tijiao);
            }else {
                 iv_select.setImageResource(R.mipmap.ordercancle);
            }
            return view;
        }
    }
}
