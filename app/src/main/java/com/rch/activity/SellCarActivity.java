package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;
import com.rch.custom.SellProblemLayout;
import com.rch.entity.BrandEntity;
import com.rch.entity.CityInfoEntity;
import com.rch.entity.LableEntity;
import com.rch.entity.SellCatEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class SellCarActivity extends BaseActivity implements View.OnClickListener{

    ImageView sell_back;
    TextView sell_cat_btn,sell_cat_btn1,sell_cat_num,sell_cat_prompt,sell_cat_area_text;
    EditText sell_cat_phone;
    SellProblemLayout sell_cat_problem;
    LinearLayout sell_cat_area;

    int REQUESTCODE=10103;


    List<SellCatEntity> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        initToolBar();
        initContorl();
        setData();
    }

    public void initContorl()
    {
        sell_back= (ImageView) findViewById(R.id.sell_back);
        sell_cat_btn= (TextView) findViewById(R.id.sell_cat_btn);
        sell_cat_btn1= (TextView) findViewById(R.id.sell_cat_btn1);
        sell_cat_num= (TextView) findViewById(R.id.sell_cat_num);
        sell_cat_prompt= (TextView) findViewById(R.id.sell_cat_prompt);
        sell_cat_area_text= (TextView) findViewById(R.id.sell_cat_area_text);

        sell_cat_problem= (SellProblemLayout) findViewById(R.id.sell_cat_problem);

        sell_cat_area= (LinearLayout) findViewById(R.id.sell_cat_area);

        sell_cat_phone= (EditText) findViewById(R.id.sell_cat_phone);

        if(isLogin()) {
            sell_cat_phone.setText(getUserInfo().getMobile());
            sell_cat_phone.setSelection(getUserInfo().getMobile().length());
            sell_cat_btn.setEnabled(true);
            sell_cat_btn1.setEnabled(true);
            sell_cat_btn.setBackground(getResources().getDrawable(R.drawable.button));
            sell_cat_btn1.setBackground(getResources().getDrawable(R.drawable.button));
        }else {
            sell_cat_btn.setEnabled(false);
            sell_cat_btn1.setEnabled(false);
            sell_cat_btn.setBackgroundColor(getResources().getColor(R.color.gray_3));
            sell_cat_btn1.setBackgroundColor(getResources().getColor(R.color.gray_3));
        }



        sell_cat_phone.addTextChangedListener(textWatcher);
        sell_cat_area.setOnClickListener(this);
        sell_cat_btn.setOnClickListener(this);
        sell_cat_btn1.setOnClickListener(this);
        sell_cat_prompt.setOnClickListener(this);
        sell_back.setOnClickListener(this);
    }

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(sell_cat_phone.getText().toString().trim().length()>0) {
                sell_cat_btn.setEnabled(true);
                sell_cat_btn.setBackground(getResources().getDrawable(R.drawable.button));
                sell_cat_btn1.setEnabled(true);
                sell_cat_btn1.setBackground(getResources().getDrawable(R.drawable.button));
            }else
            {
                sell_cat_btn.setEnabled(false);
                sell_cat_btn.setBackgroundColor(getResources().getColor(R.color.gray_3));
                sell_cat_btn1.setEnabled(false);
                sell_cat_btn1.setBackgroundColor(getResources().getColor(R.color.gray_3));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void setData()
    {
        requestData();
    }



    private void requestData()
    {
        RequestParam param = new RequestParam();
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        OkHttpUtils.post(Config.SELLCARPROBLEM, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
//                list = JsonTool.getSellCatReuslt(data);
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    String totalNum=result.getString("totalNum");
                    if(!TextUtils.isEmpty(totalNum)){
                        sell_cat_num.setText(totalNum);
                    }
                    JSONArray array=result.getJSONArray("list");
                    if(array!=null&&array.length()>0) {
                        sell_cat_problem.initData(array);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SellCarActivity.this,error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sell_cat_btn:
            case R.id.sell_cat_btn1:
                submit();
                break;
            case R.id.sell_cat_area:
                startActivityForResult(new Intent(this,CityLocationActivity.class).putExtra("requestType",REQUESTCODE),REQUESTCODE);
                break;
            case R.id.sell_cat_prompt:
                break;
            case R.id.sell_back:
                finish();
                break;

        }
    }

    private void submit()
    {
        String phone=sell_cat_phone.getText().toString().trim();
        if(!RegTool.isMobile(phone))
        {
            ToastTool.show(this,"手机号格式错误！");
            return;
        }
        startActivity(new Intent(this,FastSellCarActivity.class).putExtra("phone",phone));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1) {
            if (requestCode == REQUESTCODE) {
                String gpsCity="";
                int type = data.getExtras().getInt("type");
                if(type==1) {
                    CityInfoEntity entity = (CityInfoEntity) data.getExtras().getSerializable("CityInfoEntity");
                    gpsCity=entity.getCityName().equals("全国")?"":entity.getCityName();
                }else
                    gpsCity = data.getExtras().getString("CityInfoEntity");
                sell_cat_area_text.setText(gpsCity);
            }
        }
    }
}
