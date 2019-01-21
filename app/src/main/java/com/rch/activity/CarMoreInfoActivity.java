package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.StrSplitTool;
import com.rch.common.TimePareUtil;
import com.rch.common.TimeSplitTool;
import com.rch.entity.CarDetailEntity;
import com.rch.entity.DatailBean;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/7.
 */

public class CarMoreInfoActivity extends SecondBaseActivity {
    @ViewInject(R.id.tv_ppxh)
    private TextView tv_ppxh;
    @ViewInject(R.id.tv_clszd)
    private TextView tv_clszd;
    @ViewInject(R.id.tv_csjg)
    private TextView tv_csjg;
    @ViewInject(R.id.tv_clxz)
    private TextView tv_clxz;
    @ViewInject(R.id.tv_sfsp)
    private TextView tv_sfsp;
    @ViewInject(R.id.tv_ccspsj)
    private TextView tv_ccspsj;
    @ViewInject(R.id.tv_spdq)
    private TextView tv_spdq;
    @ViewInject(R.id.tv_ccrq)
    private TextView tv_ccrq;
    @ViewInject(R.id.tv_bxlc)
    private TextView tv_bxlc;
    @ViewInject(R.id.tv_csys)
    private TextView tv_csys;
    @ViewInject(R.id.tv_bsx)
    private TextView tv_bsx;
    @ViewInject(R.id.tv_rylx)
    private TextView tv_rylx;
    @ViewInject(R.id.tv_pfbz)
    private TextView tv_pfbz;
    @ViewInject(R.id.tv_pl)
    private TextView tv_pl;
    @ViewInject(R.id.tv_hzrs)
    private TextView tv_hzrs;
    @ViewInject(R.id.tv_clgh)
    private TextView tv_clgh;
    @ViewInject(R.id.tv_syxz)
    private TextView tv_syxz;

    private DatailBean infobean;
    private int type;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_car_more_info);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        infobean = (DatailBean) getIntent().getExtras().getSerializable("infobean");
        type=getIntent().getExtras().getInt("type",1);//1是选车 2是工作台列表
        initData();
    }


    private void initData() {
        if (infobean != null) {
//             tv_ppxh.setText(isNull(infobean.getBrandName()+isNull(infobean.getSeriesName())));
            tv_ppxh.setText(isNull(infobean.getBrandName()));

            if (TextUtils.isEmpty(infobean.getProvinceName()) && TextUtils.isEmpty(infobean.getCityName())) {
                tv_clszd.setText(isNull(infobean.getProvinceName()));
            } else {
                if (!TextUtils.isEmpty(infobean.getProvinceName()) &&!TextUtils.isEmpty(infobean.getCityName())) {
                    if (infobean.getProvinceName().equals(infobean.getCityName())) {
                        tv_clszd.setText(infobean.getProvinceName());
                    } else {
                        tv_clszd.setText(infobean.getProvinceName() + infobean.getCityName());
                    }
                }else {
                    tv_clszd.setText(isNull(infobean.getProvinceName())+isNull(infobean.getCityName()));
                }

            }

            tv_csjg.setText(isNull(infobean.getVehicleBodyName()));

            if(type==1) {
                tv_clxz.setText(isNull(infobean.getOwnerTypeName()));//选车二手车的车辆性质
            }else {
                tv_clxz.setText(isNull(infobean.getOwnerShipName()));//工作台二手车车辆性质
            }
            tv_sfsp.setText(isNull(infobean.getIsRegistrationName()));
            if (!TextUtils.isEmpty(infobean.getRegistrationTime())) {//上牌时间
                Date date = TimePareUtil.getDateTimeForTime("yyyy-MM", infobean.getRegistrationTime());
                String time = TimePareUtil.getTimeForDate("yyyy-MM", date);
//            tv_ccspsj.setText(resultCarEntity.getRegistrationTime());
                tv_ccspsj.setText(time);
            } else {
                tv_ccspsj.setText("--");
            }

            if (TextUtils.isEmpty(infobean.getRegistrationProvName()) && TextUtils.isEmpty(infobean.getRegistrationCityName())) {
                tv_spdq.setText(isNull(infobean.getRegistrationProvName()));
            } else {
                if (!TextUtils.isEmpty(infobean.getRegistrationProvName()) && !TextUtils.isEmpty(infobean.getRegistrationCityName())) {
                    if (infobean.getRegistrationProvName().equals(infobean.getRegistrationCityName())) {
                        tv_spdq.setText(infobean.getRegistrationCityName());
                    } else {
                        tv_spdq.setText(infobean.getRegistrationProvName() + infobean.getRegistrationCityName());
                    }
                }else {
                    tv_spdq.setText(isNull(infobean.getRegistrationProvName()) + isNull(infobean.getRegistrationCityName()));
                }

            }


            if (!TextUtils.isEmpty(infobean.getProductionTime())) {//出厂日期
                Date date = TimePareUtil.getDateTimeForTime("yyyy-MM", infobean.getProductionTime());
                String time = TimePareUtil.getTimeForDate("yyyy-MM", date);
                tv_ccrq.setText(time);
            }else {
                tv_ccrq.setText("--");
            }

            String lime = "0公里";
            int iShowMileage = infobean.getShowMileage().isEmpty() ? 0 : Integer.parseInt(infobean.getShowMileage());//由CarEntity改成
            if (iShowMileage < 10000) {
                lime = String.valueOf(iShowMileage) + "公里";
            } else {
                lime = StrSplitTool.retainOneNumber(String.valueOf(iShowMileage)) + "万公里";
            }
            tv_bxlc.setText(isNull(lime));
            tv_csys.setText(isNull(infobean.getVehicleColorName()));
            tv_bsx.setText(isNull(infobean.getGearboxTypeName()));
            tv_rylx.setText(isNull(infobean.getOilSupplyName()));
            tv_pfbz.setText(isNull(infobean.getEmissionStandardName()));
            tv_pl.setText(isNull(infobean.getStandardDelivery()) + isNull(infobean.getDeliveryUnit()));
            tv_hzrs.setText(isNull(infobean.getCarrierNumber()));
            tv_clgh.setText(isNull(infobean.getIsChangeownerName()));
            tv_syxz.setText(isNull(infobean.getUsingNatureName()));
        }
    }


    private String isNull(String a) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        } else {
            return "--";
        }
    }

    @OnClick({R.id.car_more_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_more_back:
                finish();
                break;
        }
    }
}
