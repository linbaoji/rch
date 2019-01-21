package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.base.MyApplication;
import com.rch.common.BigDecmUtils;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ExamineTextWatcher;
import com.rch.common.JsonTool;
import com.rch.common.PickTimeUtil;
import com.rch.common.ToastTool;
import com.rch.custom.BrandBrowseHistoryLayout;
import com.rch.custom.SortModel;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarEntity;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.CityInfoEntity;
import com.rch.entity.LableEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SeachCarServerActivity extends BaseActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvBrand, tvCarAge, tvSubmit;
    EditText etPrice, etRemark;

    FrameLayout fLine;

    BrandEntity entity;
    int BRANDREQUESTCODE = 10101;//品牌

    String brandId = "", id = "", ageLables = "", money = "", remark = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_cat_server);
        initToolBar();
        initContros();
        AppManager.getAppManager().addActivity(this);
    }

    private void initContros() {
        ivBack = (ImageView) findViewById(R.id.serach_car_back);

        tvBrand = (TextView) findViewById(R.id.serach_car_brand);
        tvCarAge = (TextView) findViewById(R.id.serach_car_age);
        tvSubmit = (TextView) findViewById(R.id.serach_car_submit);

        etPrice = (EditText) findViewById(R.id.serach_car_price);
        etPrice.addTextChangedListener(moneytextWatcher);
//        etPrice.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_ZCMONEY, etPrice));

        etRemark = (EditText) findViewById(R.id.serach_car_remark);

        etRemark.addTextChangedListener(textWatcher);
        fLine = (FrameLayout) findViewById(R.id.fl_line);

        ivBack.setOnClickListener(this);
        tvBrand.setOnClickListener(this);
        tvCarAge.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serach_car_back:
                finish();
                break;
            case R.id.serach_car_brand:
//                startActivityForResult(new Intent(this, SellCarBrandActivity.class), BRANDREQUESTCODE);
                startActivityForResult(new Intent(this, NewBrandAct.class).putExtra("type",2), BRANDREQUESTCODE);
                break;
            case R.id.serach_car_age:
                PickTimeUtil.ShowPickerView(this, tvCarAge, new PickTimeUtil.ageListener() {
                    @Override
                    public void ageItem(String ageLable) {
                        ageLables = ageLable;
                        chageColor();
                    }
                });
                break;
            case R.id.serach_car_submit:
                submit();
                break;
        }
    }

    private void submit() {
        money = etPrice.getText().toString().trim();
        if(money.endsWith(".")){
            money = etPrice.getText().toString().trim()+".0";
        }
        remark = etRemark.getText().toString().trim();
        if (id.isEmpty()) {
            ToastTool.show(this, "请选择预约车辆！");
            return;
        } else if (ageLables.isEmpty()) {
            ToastTool.show(this, "请选择车龄！");
            return;
        } else if (money.isEmpty()) {
            ToastTool.show(this, "请输入预算价格！");
            return;
        } else if (remark.isEmpty()) {
            ToastTool.show(this, "请输入需求补充说明！");
            etRemark.requestFocus();
            return;
        }else if (remark.length()<10) {
            ToastTool.show(this, "补充说明不能少于10个字！");
            etRemark.requestFocus();
            return;
        }
        httpSearchCar();
    }


    public void httpSearchCar() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken() == null ? "" : getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("brandId", brandId);
        param.add("modelId", id);
        param.add("years", ageLables);
        param.add("budgetPrice", money);
        param.add("notes", remark);
        OkHttpUtils.post(Config.SAVEQUESTVEHICLE, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                ToastTool.show(SeachCarServerActivity.this, "提交成功！");
                startActivity(new Intent(SeachCarServerActivity.this, SeachCarServerSubmitActivity.class).putExtra("type", 2));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SeachCarServerActivity.this, error);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == BRANDREQUESTCODE) {
//                CarSeriesEntity entity = (CarSeriesEntity) data.getExtras().getSerializable("CarSeriesEntity");
//                id = entity.getId();
//                brandId = entity.getBrandId();
//                tvBrand.setText(entity.getModelName());
//                tvBrand.setTextColor(getResources().getColor(R.color.black_2));
//                chageColor();

                SortModel brand= (SortModel) data.getSerializableExtra("sortmodel");
                CarSeriesEntity chexi= (CarSeriesEntity) data.getSerializableExtra("chexi");
                brandId=brand.getBrandId();
                id=chexi.getId();
                tvBrand.setText(brand.getName()+chexi.getModelName());
                tvBrand.setTextColor(getResources().getColor(R.color.black_2));
                chageColor();


            }
        }
    }

    private String beforeText;

    TextWatcher moneytextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                String afterText = s.toString();
                boolean isValid = true;

                isValid = BigDecmUtils.isMoney(s.toString());

                if (!isValid) {//不规则
                    int differ = afterText.length() - beforeText.length();
                    etPrice.setText(beforeText);
                    // 光标移动到文本末尾
                    etPrice.setSelection(afterText.length() - differ);
                }else {
                    if(!s.toString().endsWith(".")) {
                        if (BigDecmUtils.compare(s.toString(), "10000") == 1) {
                            etPrice.setText("");
                            etPrice.requestFocus();
                            ToastTool.show(MyApplication.getInstance(), "预算价格超过上限，请重新输入");
                            return;
                        }

                        if (BigDecmUtils.compare(s.toString(), "1") == -1) {
                            etPrice.setText("");
                            etPrice.requestFocus();
                            ToastTool.show(MyApplication.getInstance(), "预算价格不能低于1万元");
                            return;
                        }

                        money=s.toString();
                        chageColor();
                    }

                }



            } else {
                tvSubmit.setBackgroundColor(getResources().getColor(R.color.gray_3));
                tvSubmit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private int counter(String s, char c) {

        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                remark = s.toString();
                chageColor();
            } else {
                remark="";
                tvSubmit.setBackgroundColor(getResources().getColor(R.color.gray_3));
                tvSubmit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void chageColor() {
        if (!TextUtils.isEmpty(brandId) && !TextUtils.isEmpty(ageLables) && !TextUtils.isEmpty(money) && !TextUtils.isEmpty(remark)) {
            tvSubmit.setBackground(getResources().getDrawable(R.drawable.button));
            tvSubmit.setEnabled(true);
        } else {
            tvSubmit.setBackgroundColor(getResources().getColor(R.color.gray_3));
            tvSubmit.setEnabled(false);
        }
    }


}
