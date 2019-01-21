package com.rch.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.GsonUtils;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;
import com.rch.custom.CommonView;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.JsonBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/23.
 */

public class VehicleActivity extends BaseActivity{
    @ViewInject(R.id.cv_city)
    private CommonView cv_city;
    @ViewInject(R.id.act_brand)
    private CommonView act_brand;
    @ViewInject(R.id.act_address)
    private EditText act_address;
    @ViewInject(R.id.act_proper)
    private EditText act_proper;
    @ViewInject(R.id.act_phone)
    private EditText act_phone;
    @ViewInject(R.id.act_ok)
    private TextView act_ok;
    @ViewInject(R.id.act_checks)
    private CheckBox act_checks;






    int BRANDREQUESTCODE =10101;//品牌

    private int i1=-10,i2=-10,i3=-10;

    String brandId="",id="",province="",city="",district="",cityText="",addressDetail="",proper="",phone="";

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;
    private boolean ischeck=false;



    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
//                        Toast.makeText(VehicleActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(VehicleActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    showPickerView();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    ToastTool.show(VehicleActivity.this, "Parse Failed");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vehicle);
        initToolBar();
        ViewUtils.inject(this);
        if(isLogin()){
            act_phone.setText(getUserInfo().getMobile());
            act_phone.setSelection(act_phone.getText().length());
            phone=getUserInfo().getMobile();
        }

        chageColor();


        act_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if(charSequence.length()>0){
                 addressDetail=charSequence.toString();
                 chageColor();
             }else {
                 addressDetail="";
                 chageColor();
             }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        act_proper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if(charSequence.length()>0){
                 proper=charSequence.toString();
                 chageColor();
             }else {
                 proper="";
                 chageColor();
             }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        act_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if(charSequence.length()==11){
                 phone=charSequence.toString();
                 chageColor();
             }else {
                 phone="";
                 chageColor();
             }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        act_checks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ischeck=true;
                    chageColor();
                }else {
                    ischeck=false;
                    chageColor();
                }
            }
        });
    }

    @OnClick({R.id.cv_city,R.id.act_brand,R.id.act_ok,R.id.car_detail_back,R.id.tv_chekinfo})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.cv_city:
                if(!isLoaded) {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }else {
                    showPickerView();
                }
//

                break;
            case R.id.act_brand:
//                startActivityForResult(new Intent(this,SellCarBrandActivity.class),BRANDREQUESTCODE);
                startActivityForResult(new Intent(this, NewBrandAct.class).putExtra("type",2), BRANDREQUESTCODE);
                break;
            case R.id.act_ok:
                submit();
                break;

            case R.id.car_detail_back:
                finish();
                break;

            case R.id.tv_chekinfo:

                Intent intent=new Intent(VehicleActivity.this,WebActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
                break;
        }
    }

    private void submit() {
        addressDetail= act_address.getText().toString().trim();
        proper=act_proper.getText().toString().trim();
        phone=act_phone.getText().toString().trim();

        if(id.isEmpty())
        {
            ToastTool.show(this,"请选择品牌！");
            return;
        }
        else if(cityText.isEmpty())
        {
            ToastTool.show(this,"请选择车辆归属地！");
            return;
        }else if(addressDetail.isEmpty())
        {
            ToastTool.show(this,"请输入详细地址！");
            act_address.requestFocus();
            return;
        }
        else if(addressDetail.length()<6)
        {
            ToastTool.show(this,"详细地址不能少于6个字");
            act_address.requestFocus();
            act_address.setSelection(addressDetail.length());
            return;
        }
        else if(proper.isEmpty())
        {
            ToastTool.show(this,"请输入委托人姓名！");
            act_proper.requestFocus();
            return;
        }
        else if(proper.length()<2)
        {
            ToastTool.show(this,"委托人不能少于2个字");
            act_proper.requestFocus();
            act_proper.setSelection(proper.length());
            return;
        }
        else if(phone.isEmpty())
        {
            ToastTool.show(this,"请输入联系电话！");
            return;
        }
        else if(!RegTool.isMobile(phone))
        {
            ToastTool.show(this,"联系电话有误！");
            return;
        }
       /* if(!act_checks.isChecked())
        {
            ToastTool.show(this,"请勾选阅读协议！");
            return;
        }*/
        httpSearchCar();
    }

    public void httpSearchCar()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("addSource", "3");
        param.add("addIdentity", "1");
        param.add("modelId", id);
        param.add("brandId", brandId);
        param.add("addrProvince", province);
        param.add("addrCity", city);
        param.add("addrDistrict", district);
        param.add("addrDetail", addressDetail);
        param.add("clientName", proper);
        param.add("mobile", phone);
        OkHttpUtils.post(Config.CARJC, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                ToastTool.show(VehicleActivity.this,"提交成功！");
                startActivity(new Intent(VehicleActivity.this,SeachCarServerSubmitActivity.class).putExtra("type",2));
                finish();
            }
            @Override
            public void onError(int code, String error) {
                ToastTool.show(VehicleActivity.this,error);
            }
        });
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                cityText= options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                cv_city.setcentText(cityText);
                cv_city.setCentTextColor(getResources().getColor(R.color.black_2));

                province= options1Items.get(options1).getPickerViewText();
                city=  options2Items.get(options1).get(options2);
                district=  options3Items.get(options1).get(options2).get(options3);
                chageColor();
                i1=options1;
                i2=options2;
                i3=options3;

//                Toast.makeText(JsonDataActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .build();
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.gray_13))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.gray_3))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.gray))//标题背景颜色 Night mode
                .setContentTextSize(20)
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        if(i1!=-10&&i2!=-10&&i3!=-10) {
            pvOptions.setSelectOptions(i1,i2,i3);
        }
        pvOptions.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1) {
            if (requestCode == BRANDREQUESTCODE) {
//                CarSeriesEntity entity = (CarSeriesEntity) data.getExtras().getSerializable("CarSeriesEntity");
//                id=entity.getId();
//                brandId=entity.getBrandId();
//                act_brand.setcentText(entity.getBrandName()+entity.getModelName());
//                act_brand.setCentTextColor(getResources().getColor(R.color.black_2));

                SortModel brand= (SortModel) data.getSerializableExtra("sortmodel");
                CarSeriesEntity chexi= (CarSeriesEntity) data.getSerializableExtra("chexi");
                brandId=brand.getBrandId();
                id=chexi.getId();
                act_brand.setcentText(brand.getName()+chexi.getModelName());
                act_brand.setCentTextColor(getResources().getColor(R.color.black_2));
                chageColor();
            }
        }
    }

    private void chageColor() {
        if (!TextUtils.isEmpty(brandId) && !TextUtils.isEmpty(province) && !TextUtils.isEmpty(addressDetail) && !TextUtils.isEmpty(proper)&& !TextUtils.isEmpty(phone)&&ischeck) {
            act_ok.setBackground(getResources().getDrawable(R.drawable.button));
            act_ok.setEnabled(true);
        } else {
            act_ok.setBackgroundColor(getResources().getColor(R.color.gray_3));
            act_ok.setEnabled(false);
        }
    }
}
