package com.rch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.BigDecmUtils;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.EncryptionTools;
import com.rch.common.ExamineTextWatcher;
import com.rch.common.GeneralUtils;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.GlideUtils;
import com.rch.common.GsonUtils;
import com.rch.common.PhotoFaUtils;
import com.rch.common.PickTimeUtil;
import com.rch.common.ProandCityUtil;
import com.rch.common.SoftHideKeyBoardUtil;
import com.rch.common.SpUtils;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.custom.SortModel;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.ChexinBean;
import com.rch.entity.Children;
import com.rch.entity.DatailBean;
import com.rch.entity.ImageBean;
import com.rch.entity.JsonBean;
import com.rch.entity.NewCarInfoBean;
import com.rch.entity.PicListBean;
import com.rch.entity.VehicleImageListEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 */

public class ReleaseAct extends SecondBaseActivity {
    @ViewInject(R.id.cb_check)
    private CheckBox cb_check;
    @ViewInject(R.id.rl_addphoto)
    private RelativeLayout rl_addphoto;
    @ViewInject(R.id.et_vin)
    private TextView et_vin;
    @ViewInject(R.id.tv_ppxh)
    private TextView tv_ppxh;
    @ViewInject(R.id.tv_clszd)
    private TextView tv_clszd;
    @ViewInject(R.id.tv_csjg)
    private TextView tv_csjg;
    @ViewInject(R.id.rg_clxz)
    private RadioGroup rg_clxz;
    @ViewInject(R.id.rg_sfsp)
    private RadioGroup rg_sfsp;
    @ViewInject(R.id.tv_ccspsj)
    private TextView tv_ccspsj;
    @ViewInject(R.id.tv_spdq)
    private TextView tv_spdq;
    @ViewInject(R.id.tv_ccrq)
    private TextView tv_ccrq;
    @ViewInject(R.id.et_bxlc)
    private EditText et_bxlc;
    @ViewInject(R.id.tv_csys)
    private EditText tv_csys;
    @ViewInject(R.id.tv_bsx)
    private TextView tv_bsx;
    @ViewInject(R.id.tv_rylx)
    private TextView tv_rylx;
    @ViewInject(R.id.tv_pfbz)
    private TextView tv_pfbz;
    @ViewInject(R.id.et_pl)
    private EditText et_pl;
    @ViewInject(R.id.et_hzrs)
    private EditText et_hzrs;
    @ViewInject(R.id.rg_clgh)
    private RadioGroup rg_clgh;
    @ViewInject(R.id.rg_syxz)
    private RadioGroup rg_syxz;
    @ViewInject(R.id.et_mdj_min)
    private EditText et_mdj;
    @ViewInject(R.id.et_mdj_max)
    private EditText et_mdj_max;
    @ViewInject(R.id.et_dlj)
    private EditText et_dlj;
    @ViewInject(R.id.et_pfj_min)
    private EditText et_pfj;
    @ViewInject(R.id.et_pfj_max)
    private EditText et_pfj_max;
    @ViewInject(R.id.rb_sh)
    private RadioButton rb_sh;
    @ViewInject(R.id.rb_gsc)
    private RadioButton rb_gsc;
    @ViewInject(R.id.rb_sp)
    private RadioButton rb_sp;
    @ViewInject(R.id.rb_wsp)
    private RadioButton rb_wsp;
    @ViewInject(R.id.rb_neng)
    private RadioButton rb_neng;
    @ViewInject(R.id.rb_buneng)
    private RadioButton rb_buneng;
    @ViewInject(R.id.rb_fyy)
    private RadioButton rb_fyy;
    @ViewInject(R.id.rb_yy)
    private RadioButton rb_yy;
    @ViewInject(R.id.tv_phonenum)
    private TextView tv_phonenum;
    @ViewInject(R.id.iv_fb)
    private ImageView iv_fb;
    @ViewInject(R.id.tv_pldw)
    private TextView tv_pldw;
    @ViewInject(R.id.ll_ccspsj)
    private LinearLayout ll_ccspsj;
    @ViewInject(R.id.ll_spdq)
    private LinearLayout ll_spdq;
    @ViewInject(R.id.tv_tj)
    private TextView tv_tj;
    @ViewInject(R.id.tv_tjsh)
    private TextView tv_tjsh;



    private String vincode;
    private String brandId;
    private String modelId;
    private String vtypeId;
    private String province;
    private String city;
    private String vehicleBody;
    private String ownership;
    private String isRegistration;
    private String registrationTime;
    private String registrationProv;
    private String registrationCity;
    private String productionTime;
    private String showMileage;
    private String vehicleColor;
    private String gearboxType;
    private String oilSupply;
    private String emissionStandard;
    private String standardDelivery;
    private String deliveryUnit;
    private String carrierNumber;
    private String isChangeowner;
    private String usingNature;
    private String salesPrice;
    private String salesPriceMax;
    private String definePricePer;
    private String definePriceEnt;



    int BRANDREQUESTCODE =10101;//品牌
    int VINCODE=1001;//VIN码code
    int CSYS=1002;//车身体颜色
    int BSX=1003;//变速箱
    int RYLX=1004;
    int PFBZ=1005;
    int CSJG=1006;
    int ADD=1007;
    int PLDW=1008;

    private int i1=-10,i2=-10;//记录车辆所在地选取的位置
    private int i3=-10,i4=-10;//记录上牌地选取的位置
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<JsonBean.CityBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private String diquselect;
    private String operType="1";//操作类型 1-新增 2-修改



    private String fromtype;
    private String id;
    private DatailBean infobean;//车辆详情
    private List<PicListBean>picklist=new ArrayList<>();//车辆图片

//    private DatailBean resultCarEntity;
//    private List<VehicleImageListEntity>vehicleImageList;

    private int screen_height;
    private String fmurl;//封面的url
    private String zcurl;//左的url

    private Gson gson;

    private ProandCityUtil cityutil;

    private String brandname,vehiclebodyname,vehiclecolorname
            ,gearboxtypename,oilsupplyname,emissionstandardname;






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
//                                initJsonData();
                                cityutil.initJsonData(options1Items,options2Items,mHandler);
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
                    ToastTool.show(ReleaseAct.this, "Parse Failed");
                    break;
            }
        }
    };

    boolean ischeck=true;//是否选中协议



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoftHideKeyBoardUtil.assistActivity(this);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.act_release);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        //默认不调取输入盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cityutil=new ProandCityUtil(ReleaseAct.this);//地区选择
        screen_height= SpUtils.get_View_heigth(SpUtils.getScreenWith(ReleaseAct.this)- GeneralUtils.dip2px(ReleaseAct.this,30),600,900);
        ViewGroup.LayoutParams params=rl_addphoto.getLayoutParams();
        params.height =screen_height;
        rl_addphoto.setLayoutParams(params);
        gson=new Gson();

        if (getIntent()!=null) {
            fromtype = getIntent().getStringExtra("fromtype");
            if (TextUtils.isEmpty(fromtype))
                fromtype = "1";
                id = getIntent().getStringExtra("id");
        }

        if(fromtype.equals("2")){//重新编辑界面
            getDate();//如果是重新编辑的话
        }else {//发布二手车
            if(!TextUtils.isEmpty(SpUtils.getCaceOldCar(ReleaseAct.this,SpUtils.getToken(ReleaseAct.this)))){
                String detil=SpUtils.getCaceOldCar(ReleaseAct.this,SpUtils.getToken(ReleaseAct.this));
                infobean=gson.fromJson(detil,DatailBean.class);
                picklist=infobean.getPicList();
            }else {
                infobean=new DatailBean();
                infobean.setUsingNature("1");
                infobean.setIsChangeowner("1");
                infobean.setIsRegistration("1");
                infobean.setOwnership("1");
                infobean.setDeliveryUnit("L");
            }
            setUi();
            setPhoto();

        }

        rg_clxz.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//车辆性质
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_sh://私户
                        infobean.setOwnership("1");
                        infobean.setOwnershipName("私家车");
                        break;
                    case R.id.rb_gsc://公司车
                        infobean.setOwnership("2");
                        infobean.setOwnershipName("公司车");
                        break;
                }
            }
        });



        rg_sfsp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//车辆是否上牌
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_sp://上牌
                        infobean.setIsRegistration("1");
                        ll_ccspsj.setVisibility(View.VISIBLE);
                        ll_spdq.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_wsp://未上牌
                        infobean.setIsRegistration("0");
                        ll_ccspsj.setVisibility(View.GONE);
                        ll_spdq.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rg_clgh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//车辆过户
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_neng:
                        infobean.setIsChangeowner("1");
                        break;
                    case R.id.rb_buneng:
                        infobean.setIsChangeowner("0");
                        break;
                }
            }
        });

        rg_syxz.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//使用性质
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_yy://营运
                        infobean.setUsingNature("2");
                        break;
                    case R.id.rb_fyy://非营运
                        infobean.setUsingNature("1");
                        break;
                }
            }
        });
//        et_pfj.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_MONEY, et_pfj));
//        et_mdj.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_MONEY, et_mdj));
//        et_dlj.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_MONEY, et_dlj));
        InputFilter[] filtermoney = {new EditInputFilter(99999,2)};
        et_pfj.setFilters(filtermoney);
        et_pfj_max.setFilters(filtermoney);
        et_mdj.setFilters(filtermoney);
        et_mdj_max.setFilters(filtermoney);
        et_dlj.setFilters(filtermoney);

        et_bxlc.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_LICHENG,et_bxlc));//变数箱里层匹配正数
        et_hzrs.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_LICHENG,et_hzrs));//荷载人数匹配正数
//        et_pl.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_PL,et_pl));
        InputFilter[] filters = {new EditInputFilter(99,1)};
        et_pl.setFilters(filters);

        cb_check.setChecked(true);
        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ischeck=true;
                }else {
                    ischeck=false;
                }
            }
        });

    }

    private void getDate() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.OLECARDATILE_URL, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    JSONObject detail=result.getJSONObject("detail");
                    infobean=gson.fromJson(detail.toString(),DatailBean.class);
                    picklist=infobean.getPicList();
                    setUi();
                    setPhoto();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

                ToastTool.show(ReleaseAct.this,error);
            }
        });
    }

    /**
     * 设置图片
     */
    private void setPhoto() {
        if(picklist!=null&&picklist.size()>0){
            for(int i=0;i<picklist.size();i++){
                if(picklist.get(i).getImageOri().equals("01")){//主图
                    fmurl=picklist.get(i).getVehicleImage();
                }
                if(picklist.get(i).getImageOri().equals("02")){
                    zcurl=picklist.get(i).getVehicleImage();
                }
            }
            GlideUtils.showImg(ReleaseAct.this,fmurl,R.mipmap.car_emp,DiskCacheStrategy.ALL,iv_fb);
//            Glide.with(ReleaseAct.this).load(fmurl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.car_emp).into(iv_fb);
            tv_phonenum.setVisibility(View.VISIBLE);
            tv_phonenum.setText("已经上传"+picklist.size()+"图片，点击继续上传>>");
        }else {
            iv_fb.setImageResource(R.mipmap.fbtop);
            tv_phonenum.setVisibility(View.GONE);
            fmurl="";
            zcurl="";
        }
    }

    private void setUi() {
        if (infobean==null)
            return;
        if(!TextUtils.isEmpty(infobean.getVinCode())){
            et_vin.setText(infobean.getVinCode());
        }

        StringBuilder builder=new StringBuilder();
        if(!TextUtils.isEmpty(infobean.getBrandName())){//品牌名名
            builder.append(infobean.getBrandName());
        }
        if(!TextUtils.isEmpty(infobean.getSeriesName())){//车系名名
            builder.append(infobean.getSeriesName());

        }
        if(!TextUtils.isEmpty(infobean.getModelName())){//车型名名
            builder.append(infobean.getModelName());

        }
        if(!TextUtils.isEmpty(builder.toString())){
            tv_ppxh.setText(builder.toString());
        }

        if(!TextUtils.isEmpty(infobean.getProvinceName())&&!TextUtils.isEmpty(infobean.getCityName())){
            if(infobean.getProvinceName().equals(infobean.getCityName())) {
                tv_clszd.setText(infobean.getProvinceName());
            }else {
                tv_clszd.setText(infobean.getProvinceName() + infobean.getCityName());
            }
        }



        if(!TextUtils.isEmpty(infobean.getVehicleBodyName())){
            tv_csjg.setText(infobean.getVehicleBodyName());
        }

        if(!TextUtils.isEmpty(infobean.getOwnership())){//车辆性质
            if(infobean.getOwnership().equals("1")){//私车
                rb_sh.setChecked(true);
            }

            if(infobean.getOwnership().equals("2")){//公司车
                rb_gsc.setChecked(true);
            }
        }

        if(!TextUtils.isEmpty(infobean.getIsRegistration())){//上牌
            if(infobean.getIsRegistration().equals("0")){//未上牌
                rb_wsp.setChecked(true);
                ll_ccspsj.setVisibility(View.GONE);
                ll_spdq.setVisibility(View.GONE);
            }

            if(infobean.getIsRegistration().equals("1")){//已上牌
                rb_sp.setChecked(true);
                ll_ccspsj.setVisibility(View.VISIBLE);
                ll_spdq.setVisibility(View.VISIBLE);
            }
        }
        if(!TextUtils.isEmpty(infobean.getRegistrationTime())){//上牌时间
            Date date=TimePareUtil.getDateTimeForTime("yyyy-MM-dd",infobean.getRegistrationTime());
            String time=TimePareUtil.getTimeForDate("yyyy-MM-dd",date);
//            tv_ccspsj.setText(resultCarEntity.getRegistrationTime());
            tv_ccspsj.setText(time);
        }

        if (!TextUtils.isEmpty(infobean.getRegistrationProvName()) && !TextUtils.isEmpty(infobean.getRegistrationCityName())) {
            if (infobean.getRegistrationProvName().equals(infobean.getRegistrationCityName())) {
                tv_spdq.setText(infobean.getRegistrationProvName());
            } else {
                tv_spdq.setText(infobean.getRegistrationProvName() + infobean.getRegistrationCityName());
            }
        }

        if(!TextUtils.isEmpty(infobean.getProductionTime())){//出厂日期
            Date date=TimePareUtil.getDateTimeForTime("yyyy-MM-dd",infobean.getProductionTime());
            String time=TimePareUtil.getTimeForDate("yyyy-MM-dd",date);
//            tv_ccrq.setText(resultCarEntity.getProductionTime());
            tv_ccrq.setText(time);

        }

        if(!TextUtils.isEmpty(infobean.getShowMileage())){//表显里程
            et_bxlc.setText(infobean.getShowMileage());
        }

//        if(!TextUtils.isEmpty(infobean.getVehicleColorName())){//车身颜色
//            tv_csys.setText(infobean.getVehicleColorName());
//        }
        if(!TextUtils.isEmpty(infobean.getVehicleColor())){//车身颜色
            tv_csys.setText(infobean.getVehicleColor());
        }

        if(!TextUtils.isEmpty(infobean.getGearboxTypeName())){//变速箱
            tv_bsx.setText(infobean.getGearboxTypeName());

        }

        if(!TextUtils.isEmpty(infobean.getOilSupplyName())){//燃油类型
            tv_rylx.setText(infobean.getOilSupplyName());

        }
        if(!TextUtils.isEmpty(infobean.getEmissionStandardName())){//排放标准
            tv_pfbz.setText(infobean.getEmissionStandardName());

        }

        if(!TextUtils.isEmpty(infobean.getStandardDelivery())){//输入排量
            et_pl.setText(infobean.getStandardDelivery());
        }

        if(!TextUtils.isEmpty(infobean.getDeliveryUnit())){//排放单位
            tv_pldw.setText(infobean.getDeliveryUnit());
        }
        if(!TextUtils.isEmpty(infobean.getCarrierNumber())){//荷载人数
            et_hzrs.setText(infobean.getCarrierNumber());
        }

        if(!TextUtils.isEmpty(infobean.getIsChangeowner())){//车辆过户
           if(infobean.getIsChangeowner().equals("1")){//能过户
               rb_neng.setChecked(true);
           }else {
               rb_buneng.setChecked(true);
           }
        }

        if(!TextUtils.isEmpty(infobean.getUsingNature())){//使用性质
            if(infobean.getUsingNature().equals("1")){//非运营
                rb_fyy.setChecked(true);
            }else {
                rb_yy.setChecked(true);
            }
        }

        if(!TextUtils.isEmpty(infobean.getSalesPriceMin())){//门店价
             et_mdj.setText(infobean.getSalesPriceMin());
        }

        if(!TextUtils.isEmpty(infobean.getSalesPriceMax())){//门店价
            et_mdj_max.setText(infobean.getSalesPriceMax());
        }

        if(!TextUtils.isEmpty(infobean.getTradePriceMin())){//批发价
            et_pfj.setText(infobean.getTradePriceMin());
        }

        if(!TextUtils.isEmpty(infobean.getTradePriceMax())){//批发价
            et_pfj_max.setText(infobean.getTradePriceMax());
        }



    }


    @OnClick({R.id.rl_addphoto, R.id.iv_back, R.id.ll_vin, R.id.ll_ppxh, R.id.ll_clszd, R.id.ll_csjg, R.id.ll_ccspsj,
            R.id.ll_spdq, R.id.ll_ccrq, R.id.ll_csys, R.id.ll_bsx, R.id.ll_rylx, R.id.ll_pfbz, R.id.tv_tj, R.id.tv_tjsh,R.id.tv_pldw,
            R.id.tv_agreement})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.rl_addphoto:
                Intent intent1=new Intent(ReleaseAct.this,PhotoAct.class);
                intent1.putExtra("imagelist", (Serializable) picklist);
                startActivityForResult(intent1,ADD);
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_vin://vin码
                Intent intent=new Intent(ReleaseAct.this,VinCodeAct.class);
                intent.putExtra("vincode",et_vin.getText().toString());
                intent.putExtra("type",fromtype);//3过去是不能修改的
                startActivityForResult(intent,VINCODE);
                break;

            case R.id.ll_ppxh://品牌型号
                startActivity(new Intent(ReleaseAct.this,NewBrandAct.class).putExtra("type",3));
//                startActivityForResult(new Intent(ReleaseAct.this, BrandActivity.class).putExtra("Type",1), BRANDREQUESTCODE);
                break;
            case R.id.ll_clszd://车辆所在地方
                diquselect="1";
                if(!isLoaded) {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }else {
                    showPickerView();
                }
                break;
            case R.id.ll_csjg://车身结构
                Intent itcsjg=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","1");
                startActivityForResult(itcsjg,CSJG);
                break;

            case R.id.ll_ccspsj:
                PickTimeUtil.picktimeShow(ReleaseAct.this,"2",tv_ccspsj, new PickTimeUtil.CheckListion() {
                    @Override
                    public void over() {
                     infobean.setRegistrationTime(tv_ccspsj.getText().toString());
                    }
                });
                break;

            case R.id.ll_spdq:
                diquselect="2";
                if(!isLoaded) {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }else {
                    showPickerView();
                }
                break;

            case R.id.ll_ccrq://出厂日期
                PickTimeUtil.picktimeShow(ReleaseAct.this,"2",tv_ccrq, new PickTimeUtil.CheckListion() {
                    @Override
                    public void over() {
                     infobean.setProductionTime(tv_ccrq.getText().toString());
                    }
                });
                break;

            case R.id.ll_csys://车声音颜色
//                Intent itcsys=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","2");
//                startActivityForResult(itcsys,CSYS);

                break;
            case R.id.ll_bsx://变速箱
                Intent itbsx=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","3");
                startActivityForResult(itbsx,BSX);
                break;

            case R.id.ll_rylx://燃油类型
                Intent itrylx=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","4");
                startActivityForResult(itrylx,RYLX);
                break;

            case R.id.ll_pfbz://排放标准
                Intent itpfbz=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","5");
                startActivityForResult(itpfbz,PFBZ);
                break;

            case R.id.tv_tj://提交
                if(isVisible()){
                   tiJiao();
                   tv_tj.setEnabled(false);
                }
                break;

            case R.id.tv_tjsh:
                if(isVisible()){
                    tiJiao();
                    tv_tjsh.setEnabled(false);
                }
                break;
            case R.id.tv_pldw:
                Intent pldw=new Intent(ReleaseAct.this,SelectAct.class).putExtra("from","6");
                startActivityForResult(pldw,PLDW);
                break;
            case R.id.tv_agreement://跳转协议，不显示底部同意按钮，仅供查看
                startActivity(new Intent(mContext,WebActivity.class).putExtra("type","4").putExtra("release_type","2").putExtra("isShowBottom",false));
                break;
        }

    }

    private boolean isVisible() {
        if(TextUtils.isEmpty(fmurl)||TextUtils.isEmpty(zcurl)){
            ToastTool.show(ReleaseAct.this,"请至少上传两张必传图片");
            return false;
        }


        if(TextUtils.isEmpty(infobean.getVinCode())){
            ToastTool.show(ReleaseAct.this,"请输入vin码");
            return false;

        }

        if(TextUtils.isEmpty(infobean.getBrandId())){
            ToastTool.show(ReleaseAct.this,"请选择品牌型号");
            return false;
        }

        if(TextUtils.isEmpty(infobean.getProvince())){
            ToastTool.show(ReleaseAct.this,"请选择车辆所在地");
            return false;
        }

        if(TextUtils.isEmpty(infobean.getVehicleBody())){
            ToastTool.show(ReleaseAct.this,"请选择车身结构");
            return false;
        }

        if(TextUtils.isEmpty(infobean.getProductionTime())){
            ToastTool.show(ReleaseAct.this,"请选择出厂日期");
            return false;
        }

        String bxlc=et_bxlc.getText().toString();
        if(TextUtils.isEmpty(bxlc)){
            ToastTool.show(ReleaseAct.this,"请输入表显里程");
            et_bxlc.requestFocus();
            return false;
        }else {
            infobean.setShowMileage(bxlc);
        }

//        if(TextUtils.isEmpty(infobean.getVehicleColor())){
//            ToastTool.show(ReleaseAct.this,"请选择车身颜色");
//            return false;
//        }
         String color=tv_csys.getText().toString().trim();
        if (TextUtils.isEmpty(color)) {
            ToastTool.show(ReleaseAct.this, "请输入车身颜色");
            tv_csys.requestFocus();
            return false;
        }else {
            infobean.setVehicleColor(color);
//            infobean.setVehicleColorName(color);
        }

        if(TextUtils.isEmpty(infobean.getGearboxType())){
            ToastTool.show(ReleaseAct.this,"请选择变速箱");
            return false;
        }

        if(TextUtils.isEmpty(infobean.getOilSupply())){
            ToastTool.show(ReleaseAct.this,"请选择燃料类型");
            return false;
        }

        if(TextUtils.isEmpty(infobean.getEmissionStandard())){
            ToastTool.show(ReleaseAct.this,"请选择排放标准");
            return false;
        }

        String pl=et_pl.getText().toString().trim();
        if(TextUtils.isEmpty(pl)){
            ToastTool.show(ReleaseAct.this,"请输入排量");
            return false;
        }else {
            infobean.setStandardDelivery(pl);
        }

        String num=et_hzrs.getText().toString().trim();
        if(TextUtils.isEmpty(num)){
            ToastTool.show(ReleaseAct.this,"请输入核载人数");
            et_hzrs.requestFocus();
            return false;
        }else {
            infobean.setCarrierNumber(num);
        }

        String mdjmin=et_mdj.getText().toString().trim();
        if(TextUtils.isEmpty(mdjmin)){
            ToastTool.show(ReleaseAct.this,"请输入门店价");
            et_mdj.requestFocus();
            return false;
        } else {
            infobean.setSalesPriceMin(mdjmin);
        }

        String mdjmax=et_mdj_max.getText().toString().trim();
        if(TextUtils.isEmpty(mdjmax)){
            ToastTool.show(ReleaseAct.this,"请输入门店价");
            et_mdj_max.requestFocus();
            return false;
        } else {
            infobean.setSalesPriceMax(mdjmax);
        }

        String pfjmin=et_pfj.getText().toString().trim();
        if(TextUtils.isEmpty(pfjmin)){
            ToastTool.show(ReleaseAct.this,"请输入批发价");
            et_pfj.requestFocus();
            return false;
        } else {
            infobean.setTradePriceMin(pfjmin);
        }

        String pfjmax=et_pfj_max.getText().toString().trim();
        if(TextUtils.isEmpty(pfjmax)){
            ToastTool.show(ReleaseAct.this,"请输入批发价");
            et_pfj_max.requestFocus();
            return false;
        } else {
            infobean.setTradePriceMax(pfjmax);
        }

        if(BigDecmUtils.compare(mdjmin,mdjmax)==1){
            ToastTool.show(ReleaseAct.this,"门店价格式错误,请重新输入");
            et_mdj.setText("");
            et_mdj_max.setText("");
            et_mdj.requestFocus();
            return false;
        }


        if(BigDecmUtils.compare(pfjmin,pfjmax)==1){
            ToastTool.show(ReleaseAct.this,"批发价格式错误,请重新输入");
            et_pfj.setText("");
            et_pfj_max.setText("");
            et_pfj.requestFocus();
            return false;
        }
        if (!ischeck){
            ToastTool.show(mContext,"您未同意协议");
            return false;
        }

        return true;
    }

    private void tiJiao() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();

        param.add("type", fromtype);//1新增加 2修改
        if(fromtype.equals("2")){
            param.add("id",id);
        }
        param.add("vinCode",infobean.getVinCode());
        param.add("brandId",infobean.getBrandId());
        param.add("seriesId",infobean.getSeriesId());
        param.add("modelId",infobean.getModelId());
        param.add("province",infobean.getProvince());
        param.add("city",infobean.getCity());
        param.add("vehicleBody",infobean.getVehicleBody());
        param.add("ownership",infobean.getOwnership());
        param.add("isRegistration",infobean.getIsRegistration());


        if(!TextUtils.isEmpty(infobean.getRegistrationProv())){//上牌
            param.add("registrationProv",infobean.getRegistrationProv());
        }

        if(!TextUtils.isEmpty(infobean.getRegistrationCity())){//上牌
            param.add("registrationCity",infobean.getRegistrationCity());
        }

        if(!TextUtils.isEmpty(infobean.getRegistrationTime())){//上牌时间
            param.add("registrationTime",infobean.getRegistrationTime());
        }

        param.add("productionTime",infobean.getProductionTime());
        param.add("showMileage",infobean.getShowMileage());
        param.add("vehicleColor",infobean.getVehicleColor());//车身体颜色id后来改成输入框了
        param.add("gearboxType",infobean.getGearboxType());
        param.add("oilSupply",infobean.getOilSupply());
        param.add("emissionStandard",infobean.getEmissionStandard());
        param.add("standardDelivery",infobean.getStandardDelivery());
        param.add("deliveryUnit",infobean.getDeliveryUnit());
        param.add("carrierNumber",infobean.getCarrierNumber());
        param.add("isChangeowner",infobean.getIsChangeowner());
        param.add("usingNature",infobean.getUsingNature());
        param.add("salesPriceMin",infobean.getSalesPriceMin());
        param.add("salesPriceMax",infobean.getSalesPriceMax());

        param.add("tradePriceMin",infobean.getTradePriceMin());
        param.add("tradePriceMax",infobean.getTradePriceMax());
        param.add("imgList", GsonUtils.bean2Json(picklist));
        OkHttpUtils.post(Config.FB_URL, param, new OkHttpCallBack(ReleaseAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                tv_tj.setEnabled(true);
                tv_tjsh.setEnabled(true);
                ToastTool.show(ReleaseAct.this,"提交成功");
//                PhotoFaUtils.setJsonBean(ReleaseAct.this,"" );//成功后给保存的清除
//                PhotoFaUtils.setJsonArray(ReleaseAct.this,"");
//                SpUtils.setCaceOldCar(ReleaseAct.this,SpUtils.getToken(ReleaseAct.this),"");//清空保存
                startActivity(new Intent(ReleaseAct.this,ComSucessAct.class).putExtra("type", "1").putExtra("from",fromtype));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                tv_tj.setEnabled(true);
                tv_tjsh.setEnabled(true);
                ToastTool.show(ReleaseAct.this,error);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == VINCODE) {//Vin码
            vincode = data.getExtras().getString("vincode", "");
            if (!TextUtils.isEmpty(vincode)) {//vin码子
                et_vin.setText(vincode);
                infobean.setVinCode(vincode);
            }
        }
//        else if(requestCode==BRANDREQUESTCODE){//品牌
//            BrandEntity entity = (BrandEntity) data.getExtras().getSerializable("BrandEntity");
//            brandId = entity.getId();
//
//        }
        else if (requestCode == CSYS) {//车身颜色
            Children c = (Children) data.getExtras().getSerializable("select");
            vehicleColor = c.getKey();//车身颜色key
            if (!TextUtils.isEmpty(vehicleColor)) {
                tv_csys.setText(c.getValue());
                vehiclecolorname=c.getValue();
                infobean.setVehicleColor(vehicleColor);
                infobean.setVehicleColorName(vehiclecolorname);
            }
        } else if (requestCode == BSX) {//变数箱
            Children c = (Children) data.getExtras().getSerializable("select");
            gearboxType = c.getKey();//变速箱的key
            if (!TextUtils.isEmpty(gearboxType)) {
                tv_bsx.setText(c.getValue());
                gearboxtypename=c.getValue();
                infobean.setGearboxType(gearboxType);
                infobean.setGearboxTypeName(gearboxtypename);
            }
        } else if (requestCode == RYLX) {//燃油类型
            Children c = (Children) data.getExtras().getSerializable("select");
            oilSupply = c.getKey();
            if (!TextUtils.isEmpty(oilSupply)) {
                tv_rylx.setText(c.getValue());
                tv_rylx.setTextColor(getResources().getColor(R.color.black_2));
                oilsupplyname=c.getValue();
                infobean.setOilSupply(oilSupply);
                infobean.setOilSupplyName(oilsupplyname);
            }
        } else if (requestCode == PFBZ) {//排放标准
            Children c = (Children) data.getExtras().getSerializable("select");
            emissionStandard = c.getKey();
            if (!TextUtils.isEmpty(emissionStandard)) {
                tv_pfbz.setText(c.getValue());
                emissionstandardname=c.getValue();
                infobean.setEmissionStandard(emissionStandard);
                infobean.setEmissionStandardName(emissionstandardname);
            }
        } else if (requestCode == CSJG) {//车身结构
            Children c = (Children) data.getExtras().getSerializable("select");
            vehicleBody = c.getKey();
            if (!TextUtils.isEmpty(vehicleBody)) {
                tv_csjg.setText(c.getValue());
                vehiclebodyname=c.getValue();
                infobean.setVehicleBody(vehicleBody);
                infobean.setVehicleBodyName(vehiclebodyname);
            }
        } else if(requestCode==PLDW){
            String dw=data.getExtras().getString("select","");
            if(!TextUtils.isEmpty(dw)){
                tv_pldw.setText(dw);
                infobean.setDeliveryUnit(dw);
            }

        } else if (requestCode == ADD) {//上传图片之后
          picklist= (List<PicListBean>) data.getExtras().getSerializable("list");
          setPhoto();

        }
    }


    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText() +
//                        options2Items.get(options1).get(options2) +
//                        options3Items.get(options1).get(options2).get(options3);
                if (diquselect.equals("1")) {//车辆所在地
                    province = options1Items.get(options1).getPickerViewText();//车辆所在省
                    city = options2Items.get(options1).get(options2).getName();//车辆所在市区
                    tv_clszd.setText(province + city);

                    infobean.setProvinceName(province);
                    infobean.setCityName(city);
                    infobean.setProvince(options1Items.get(options1).getAreaId());
                    infobean.setCity(options2Items.get(options1).get(options2).getAreaId());
                    i1 = options1;
                    i2 = options2;

                } else {//上牌地区
                    registrationProv = options1Items.get(options1).getPickerViewText();
                    registrationCity = options2Items.get(options1).get(options2).getName();
                    tv_spdq.setText(registrationProv + registrationCity);
                    infobean.setRegistrationProvName(registrationProv);
                    infobean.setRegistrationCityName(registrationCity);
                    infobean.setRegistrationProv(options1Items.get(options1).getAreaId());
                    infobean.setRegistrationCity(options2Items.get(options1).get(options2).getAreaId());
                    i3 = options1;
                    i4 = options2;

                }

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
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        if (diquselect.equals("1")) {
            if (i1 != -10 && i2 != -10) {
                pvOptions.setSelectOptions(i1, i2);
            }
        } else {
            if (i3 != -10 && i4 != -10) {
                pvOptions.setSelectOptions(i3, i4);
            }
        }
        pvOptions.show();
    }


    public static String REFRESHHOMEFRAGMENTMODULE="from.ToppedAct";
    private MyBrodcastreceive receiver;
    private class MyBrodcastreceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            SortModel brand= (SortModel) intent.getSerializableExtra("brand");//id
            CarSeriesEntity chexibean= (CarSeriesEntity) intent.getSerializableExtra("chexi");//id
            ChexinBean bean= (ChexinBean) intent.getSerializableExtra("chexin");//id


            infobean.setBrandId(brand.getBrandId());
            infobean.setSeriesId(chexibean.getId());
            infobean.setModelId(bean.getId());



            infobean.setBrandName(brand.getName());
            infobean.setSeriesName(chexibean.getModelName());//车系名称
            infobean.setModelName(bean.getModelName());//车型名称
            tv_ppxh.setText(brand.getName()+chexibean.getModelName()+bean.getModelName());
//            tv_ppxh.setText(bean.getModelName());

//            resultCarEntity.setVehicleFullName(bean.getModelName());

//            tv_ppxh.setTextColor(getResources().getColor(R.color.black_2));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver==null){
            receiver=new MyBrodcastreceive();
            registerReceiver(receiver,new IntentFilter(REFRESHHOMEFRAGMENTMODULE));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fromtype.equals("1")){
           infobean.setPicList(picklist);
           SaveEditer();//把输入框内容保存起来
           SpUtils.setCaceOldCar(ReleaseAct.this,SpUtils.getToken(ReleaseAct.this),GsonUtils.bean2Json(infobean));
        }

        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }

    private void SaveEditer() {
        String bxlc=et_bxlc.getText().toString();
        if(!TextUtils.isEmpty(bxlc)){
            infobean.setShowMileage(bxlc);
        }

        String color=tv_csys.getText().toString().trim();
        if (!TextUtils.isEmpty(color)) {
            infobean.setVehicleColor(color);
        }

        String pl=et_pl.getText().toString().trim();
        if(!TextUtils.isEmpty(pl)){
            infobean.setStandardDelivery(pl);
        }

        String num=et_hzrs.getText().toString().trim();
        if(!TextUtils.isEmpty(num)){
            infobean.setCarrierNumber(num);
        }

        String mdjmin=et_mdj.getText().toString().trim();
        if(!TextUtils.isEmpty(mdjmin)){
            infobean.setSalesPriceMin(mdjmin);
        }

        String mdjmax=et_mdj_max.getText().toString().trim();
        if(!TextUtils.isEmpty(mdjmax)){
            infobean.setSalesPriceMax(mdjmax);
        }

        String pfjmin=et_pfj.getText().toString().trim();
        if(!TextUtils.isEmpty(pfjmin)){
            infobean.setTradePriceMin(pfjmin);
        }

        String pfjmax=et_pfj_max.getText().toString().trim();
        if(!TextUtils.isEmpty(pfjmax)){
            infobean.setTradePriceMax(pfjmax);
        }

    }


}
