package com.rch.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.PickTimeUtil;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.JsonBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/31.
 */

public class FastSellCarActivity extends BaseActivity implements View.OnClickListener{

    TextView fast_sell_car_btn,perfect_brand,perfect_area,perfect_time;
    EditText perfect_mileage;
    ImageView perfect_back;

    String id="",brandId="",carName="",brandName="", sProvince ="", sCity ="",ctiyText="",sTime="",mileage="";

    String phone="";

    private boolean isLoaded = false;
    int REQUESTCODE=1011;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_sell_car);
        initToolBar();
        phone=getIntent().getStringExtra("phone");
        initContorl();
    }

    private void initContorl()
    {
        fast_sell_car_btn= (TextView) findViewById(R.id.fast_sell_car_btn);
        perfect_brand= (TextView) findViewById(R.id.perfect_brand);
        perfect_area= (TextView) findViewById(R.id.perfect_area);
        perfect_time= (TextView) findViewById(R.id.perfect_time);
        perfect_mileage= (EditText) findViewById(R.id.perfect_mileage);
        perfect_back= (ImageView) findViewById(R.id.perfect_back);
        initData();
        fast_sell_car_btn.setOnClickListener(this);
        perfect_brand.setOnClickListener(this);
        perfect_time.setOnClickListener(this);
        perfect_area.setOnClickListener(this);
        perfect_back.setOnClickListener(this);
    }

    public void initData()
    {
        if(getSellData().isEmpty())
            return;
        String[] str=getSellData().split(",");
        id=str[0];
        brandId=str[1];
        carName=str[2];
        sProvince=str[3];
        sCity=str[4];
        ctiyText=str[5];
        sTime=str[6];
        mileage=str[7];
        perfect_brand.setText(carName);
        perfect_area.setText(ctiyText);
        perfect_time.setText(sTime);
        perfect_mileage.setText(mileage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fast_sell_car_btn:
                submit();
                //startActivity(new Intent(this,PerfectInfoActivity.class).putExtra("phone",phone));
                break;
            case R.id.perfect_brand:
//                startActivityForResult(new Intent(this,SellCarBrandActivity.class),REQUESTCODE);
                startActivityForResult(new Intent(this, NewBrandAct.class).putExtra("type",2), REQUESTCODE);
                break;
            case R.id.perfect_back:
                finish();
                break;
            case R.id.perfect_time:
                PickTimeUtil.pickShow(this, perfect_time, new PickTimeUtil.timeListener() {
                    @Override
                    public void time(String date) {
                        sTime=date;
                    }
                });
                break;
            case R.id.perfect_area:
                if(!isLoaded)
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                else
                    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
                break;
        }
    }

    private void submit()
    {
         mileage=perfect_mileage.getText().toString().trim();
        if(brandId.isEmpty())
        {
            ToastTool.show(this,"品牌车型不能为空！");
            return;
        }
        else if(ctiyText.isEmpty())
        {
            ToastTool.show(this,"上牌地区不能为空！");
            return;
        }
        else  if(sTime.isEmpty())
        {
            ToastTool.show(this,"首次上牌时间不能为空！");
            return;
        }
        else  if(mileage.isEmpty())
        {
            ToastTool.show(this,"表显里程不能为空！");
            return;
        }
        else if(mileage.indexOf(".")>-1) {
            if (!RegTool.isMileage(mileage)) {
                ToastTool.show(this, "表显里程输入有误！");
                return;
            }
        }
        String str=id+","+brandId+","+carName+","+sProvince+","+sCity+","+ctiyText+","+sTime+","+mileage;
        setSellData(str);
        startActivity(new Intent(this,PerfectInfoActivity.class).putExtra("phone",phone).putExtra("str",str));
        finish();
    }

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded=true;
                    hideKeys();
                    ShowPickerView();
                    break;

                case MSG_LOAD_FAILED:
                    ToastTool.show(FastSellCarActivity.this,"解析数据出错");
                    break;

            }
        }
    };


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    /*城市选择器*/
    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                sProvince =options1Items.get(options1).getPickerViewText();
                sCity =  options2Items.get(options1).get(options2);
                // area= options3Items.get(options1).get(options2).get(options3);
               /* locationText = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);*/
                ctiyText= sProvince + sCity;
                perfect_area.setText(ctiyText);
                perfect_area.setTextColor(getResources().getColor(R.color.black_1));
                // ToastTool.show(AddressAddOrEdit.this,locationText);
            }
        })

               /*  .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
               .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(12)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();*/

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setTitleColor(Color.BLUE)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(getResources().getColor(R.color.gray))//标题背景颜色 Night mode
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

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    private void hideKeys()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(perfect_area.getWindowToken(), 0); //强制隐藏键盘
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
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
            //options3Items.add(Province_AreaList);
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






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==REQUESTCODE)
            {
//                CarSeriesEntity carSeriesEntity= (CarSeriesEntity)data.getSerializableExtra("CarSeriesEntity");
//                carName=carSeriesEntity.getModelName();
//                brandName=carSeriesEntity.getBrandName();
//                carName=brandName+carName;
//                perfect_brand.setText(carName);
//                brandId=carSeriesEntity.getBrandId();
//                id= data.getStringExtra("id");

                SortModel brand= (SortModel) data.getSerializableExtra("sortmodel");
                CarSeriesEntity chexi= (CarSeriesEntity) data.getSerializableExtra("chexi");
                brandId=brand.getBrandId();
                id=chexi.getId();
                perfect_brand.setText(brand.getName()+chexi.getModelName());
                perfect_brand.setTextColor(getResources().getColor(R.color.black_2));
            }
        }
    }
}
