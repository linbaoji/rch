package com.rch.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.BannerAdatper;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.SpUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.StrSplitTool;
import com.rch.common.TimePareUtil;
import com.rch.common.TimeSplitTool;
import com.rch.common.ToastTool;
import com.rch.custom.ItemLabelLayout;
import com.rch.custom.MyAlertDialog;
import com.rch.entity.NewCarInfoBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by Administrator on 2018/7/25.
 */

public class ManageInfoAct extends SecondBaseActivity implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.car_detail_advisory)
    private RelativeLayout car_detail_advisory;//客服  二手车管理不显示
    @ViewInject(R.id.car_detail_title)
    private TextView tv_carname;
    @ViewInject(R.id.tv_carprice)
    private TextView tv_carprice;

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.tv_pfprice)
    private TextView tv_pfprice;

    @ViewInject(R.id.car_detail_location)
    private TextView tv_location;
    @ViewInject(R.id.car_detail_land)
    private TextView tv_spd;

    @ViewInject(R.id.car_detail_factory_time)
    private TextView tv_cctime;//出厂日期
    @ViewInject(R.id.car_detail_land_time)
    private TextView tv_sptime;//上牌日期
    @ViewInject(R.id.car_detail_mileage)
    private TextView tv_bxlc;//表显里程
    @ViewInject(R.id.car_detail_emission)
    private TextView tv_pfbz;//排放标准
    @ViewInject(R.id.car_detail_transmission)
    private TextView tv_bsx;//变速箱
    @ViewInject(R.id.car_detail_amount)
    private TextView tv_pl;//排量

    @ViewInject(R.id.car_detail_vp)
    private ViewPager vp;
    @ViewInject(R.id.car_detail_index)
    private TextView tvIndex;
    @ViewInject(R.id.car_num)
    private TextView car_num;

    @ViewInject(R.id.ll_bottom)
    private LinearLayout ll_bottom;
    @ViewInject(R.id.iv_delet)
    private ImageView iv_delet;
    @ViewInject(R.id.tv_scorxj)
    private TextView tv_scorxj;
    @ViewInject(R.id.tv_rebuild)
    private TextView tv_rebuild;
    @ViewInject(R.id.ll_deleat)
    private LinearLayout ll_deleat;

    @ViewInject(R.id.rl_vp)
    private RelativeLayout rl_vp;
    @ViewInject(R.id.tv_failresion)
    private TextView tv_failresion;//失败原因

    @ViewInject(R.id.tv_dec)
    private TextView tv_dec;
    @ViewInject(R.id.ll_tab)
    private ItemLabelLayout ll_tab;

    private int screen_height;

    private NewCarInfoBean infobean;
    BannerAdatper adatper;
    private Gson gson;
    private String id;//车辆id
    private boolean  fromOrder;//是否是客户详情预约单列表过来的

    @Override
    public void setLayout() {
        setContentView(R.layout.act_manageinfo);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        gson=new Gson();
        screen_height = SpUtils.get_View_heigth(SpUtils.getScreenWith(mContext), 600, 900);
        ViewGroup.LayoutParams params = rl_vp.getLayoutParams();
        params.height = screen_height;
        rl_vp.setLayoutParams(params);
        if (getIntent()!=null) {
            id = getIntent().getStringExtra("id");
            fromOrder = getIntent().getBooleanExtra("order", false);
        }

        if (!fromOrder)
            car_detail_advisory.setVisibility(View.GONE);
//        getData();
    }



    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    private void getData(){
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.OLECARDATILE_URL, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    infobean=gson.fromJson(result.toString(),NewCarInfoBean.class);
                    setUi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

                ToastTool.show(ManageInfoAct.this,error);
            }
        });
    }

    private void setUi() {
        if(infobean!=null){
         if(infobean.getDetail().getPicList()!=null&&infobean.getDetail().getPicList().size()>0){//设置图片
             adatper=new BannerAdatper(this,infobean.getDetail().getPicList(),infobean.getDefinState());
             vp.setAdapter(adatper);
             vp.setCurrentItem(1);
             tvIndex.setText("1/"+infobean.getDetail().getPicList().size());
             vp.setOnPageChangeListener(this);

         }
          tv_failresion.setVisibility(View.GONE);
         if (!fromOrder) {
             if (infobean.getDefinState().equals("1")) {//带上架待审核
                 ll_bottom.setVisibility(View.GONE);
             } else if (infobean.getDefinState().equals("2")) {//待上架审核失败
                 iv_delet.setVisibility(View.VISIBLE);
                 tv_scorxj.setText("删除该车辆");
                 tv_rebuild.setText("重新编辑");
                 tv_rebuild.setBackgroundResource(R.drawable.rebuild);
                 ll_deleat.setVisibility(View.VISIBLE);
                 ll_bottom.setVisibility(View.VISIBLE);

                 tv_failresion.setText("审核驳回原因:"+isNull(infobean.getDetail().getRejectReason()));//失败原因
                 tv_failresion.setVisibility(View.VISIBLE);

             } else if (infobean.getDefinState().equals("3")) {//待上架-审核通过
                 ll_deleat.setVisibility(View.GONE);
                 tv_rebuild.setText("编辑");
                 tv_rebuild.setBackgroundResource(R.drawable.rebuildqub);
                 ll_bottom.setVisibility(View.VISIBLE);
             } else if (infobean.getDefinState().equals("4")) {//已上架-审核通过
                 iv_delet.setVisibility(View.GONE);
                 tv_scorxj.setText("下架该车辆");
                 tv_rebuild.setText("编辑");
                 tv_rebuild.setBackgroundResource(R.drawable.rebuild);
                 ll_deleat.setVisibility(View.VISIBLE);
                 ll_bottom.setVisibility(View.VISIBLE);
             } else if (infobean.getDefinState().equals("5")) {//已经售罄
                 ll_bottom.setVisibility(View.GONE);
             } else if (infobean.getDefinState().equals("6")) {//已经下架
                 ll_deleat.setVisibility(View.GONE);
                 tv_rebuild.setText("编辑");
                 tv_rebuild.setBackgroundResource(R.drawable.rebuildqub);
                 ll_bottom.setVisibility(View.VISIBLE);
             }

         }else {
             ll_bottom.setVisibility(View.GONE);
         }

            car_num.setText(isNull("车源号"+infobean.getDetail().getSourcecode()));

//            if(!TextUtils.isEmpty(infobean.getDetail().getIsSelected())&&infobean.getDetail().getIsSelected().equals("1")){
//                SpannableStringBuilder spannableString = new SpannableStringBuilder();
//                spannableString.append("占位");
//                spannableString.append(" ");
//                spannableString.append(isNull(infobean.getDetail().getModelName()));
//                ImageSpan imageSpan = new ImageSpan(mContext, R.mipmap.jxcar);
//                //        也可以这样
//                //        Drawable drawable = context.getResources().getDrawable(R.mipmap.jxcar);
//                //        drawable.setBounds(0, 0, GeneralUtils.dip2px(context,28), GeneralUtils.dip2px(context,15));
//                //        ImageSpan imageSpan1 = new ImageSpan(drawable);
//                //        将index为6、7的字符用图片替代
//                spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tv_carname.setText(spannableString);
//            }else {
//                tv_carname.setText(isNull(infobean.getDetail().getModelName()));
//            }

            tv_carname.setText(isNull(infobean.getDetail().getModelName()));

            if(StartorSelectUtils.isBoolean(infobean.getDetail().getIsRecommend())||StartorSelectUtils.isBoolean(infobean.getDetail().getIfStar())||StartorSelectUtils.isBoolean(infobean.getDetail().getIsSelected())) {
                ll_tab.setVisibility(View.VISIBLE);
                ll_tab.initData(StartorSelectUtils.nullOrone(infobean.getDetail().getIsRecommend()), StartorSelectUtils.nullOrone(infobean.getDetail().getIfStar()), StartorSelectUtils.nullOrone(infobean.getDetail().getIsSelected()));
            }
            //"门店价:"+
            if(!TextUtils.isEmpty(infobean.getDetail().getSalesPriceMin()) &&!TextUtils.isEmpty(infobean.getDetail().getSalesPriceMax())){
                if(infobean.getDetail().getSalesPriceMin().equals(infobean.getDetail().getSalesPriceMax())){
                    tv_carprice.setText(isNull(infobean.getDetail().getSalesPriceMin())+"万");

                }else {
                    tv_carprice.setText(isNull(infobean.getDetail().getSalesPriceMin())+"万"+"-"+isNull(infobean.getDetail().getSalesPriceMax())+"万");
                }
            }else {
                tv_carprice.setText("门店价:"+isNull(infobean.getDetail().getSalesPriceMin()) +"万"+ "-"+isNull(infobean.getDetail().getSalesPriceMax()) + "万");
            }

            if (!TextUtils.isEmpty(infobean.getDetail().getTradePriceMin()) && !TextUtils.isEmpty(infobean.getDetail().getTradePriceMax())) {
                if (infobean.getDetail().getTradePriceMin().equals(infobean.getDetail().getTradePriceMax())) {
                    tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin()) + "万");
                } else {
                    tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin()) + "万" +"-"+ isNull(infobean.getDetail().getTradePriceMax()) + "万");
                }
            } else {
                tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin()) + "万" +"-"+ isNull(infobean.getDetail().getTradePriceMax()) + "万");
            }



            if(!TextUtils.isEmpty(infobean.getDetail().getProvinceName())&&!TextUtils.isEmpty(infobean.getDetail().getCityName())){
                if(infobean.getDetail().getProvinceName().equals(infobean.getDetail().getCityName())) {
                    tv_location.setText(infobean.getDetail().getProvinceName());
                }else {
                    tv_location.setText(infobean.getDetail().getProvinceName() + infobean.getDetail().getCityName());
                }
            }else {
                tv_location.setText(isNull("--"));
            }

            if(TextUtils.isEmpty(infobean.getDetail().getProvinceName())&&TextUtils.isEmpty(infobean.getDetail().getCityName())){
                tv_location.setText(isNull("--"));
            }else {
                if(!TextUtils.isEmpty(infobean.getDetail().getProvinceName())&&!TextUtils.isEmpty(infobean.getDetail().getCityName())){
                    if(infobean.getDetail().getProvinceName().equals(infobean.getDetail().getCityName())) {
                        tv_location.setText(infobean.getDetail().getProvinceName());
                    }else {
                        tv_location.setText(infobean.getDetail().getProvinceName() + infobean.getDetail().getCityName());
                    }
                }else {
                    tv_location.setText(isNull(infobean.getDetail().getProvinceName()) + isNull(infobean.getDetail().getCityName()));

                }

            }



            if(TextUtils.isEmpty(infobean.getDetail().getRegistrationProvName())&&TextUtils.isEmpty(infobean.getDetail().getRegistrationCityName())){
                tv_spd.setText(isNull("--"));
            }else {
                if(!TextUtils.isEmpty(infobean.getDetail().getRegistrationProvName())&&!TextUtils.isEmpty(infobean.getDetail().getRegistrationCityName())){
                    if(infobean.getDetail().getRegistrationProvName().equals(infobean.getDetail().getRegistrationCityName())) {
                        tv_spd.setText(infobean.getDetail().getRegistrationProvName());
                    }else {
                        tv_spd.setText(infobean.getDetail().getRegistrationProvName() + infobean.getDetail().getRegistrationCityName());
                    }
                }else {
                    tv_spd.setText(isNull(infobean.getDetail().getRegistrationProvName()) + isNull(infobean.getDetail().getRegistrationCityName()));

                }

            }


            if(!TextUtils.isEmpty(infobean.getDetail().getProductionTime())){//出厂日期
                Date date= TimePareUtil.getDateTimeForTime("yyyy-MM",infobean.getDetail().getProductionTime());
                String time=TimePareUtil.getTimeForDate("yyyy-MM",date);
//            tv_ccrq.setText(resultCarEntity.getProductionTime());
                tv_cctime.setText(time);
            }else {
                tv_cctime.setText("--");
            }


            if(!TextUtils.isEmpty(infobean.getDetail().getRegistrationTime())){//上牌时间
                Date date= TimePareUtil.getDateTimeForTime("yyyy-MM",infobean.getDetail().getRegistrationTime());
                String time=TimePareUtil.getTimeForDate("yyyy-MM",date);
//            tv_ccspsj.setText(resultCarEntity.getRegistrationTime());
                tv_sptime.setText(time);
            }else {
                tv_sptime.setText("--");
            }


            String lime="0公里";
            int iShowMileage=infobean.getDetail().getShowMileage().isEmpty()?0: Integer.parseInt(infobean.getDetail().getShowMileage());//由CarEntity改成
            if(iShowMileage<10000) {
                lime = String.valueOf(iShowMileage) + "公里";
            }
            else {
                lime = StrSplitTool.retainOneNumber(String.valueOf(iShowMileage)) + "万公里";
            }
            tv_bxlc.setText(isNull(lime));
            tv_dec.setText(tv_sptime.getText()+"/"+tv_bxlc.getText());//二手车这列是后面加的
            tv_pfbz.setText(isNull(infobean.getDetail().getEmissionStandardName()));
            tv_bsx.setText(isNull(infobean.getDetail().getGearboxTypeName()));
            tv_pl.setText(isNull(infobean.getDetail().getStandardDelivery())+isNull(infobean.getDetail().getDeliveryUnit()));

        }
    }


    @OnClick({R.id.ll_deleat, R.id.tv_rebuild, R.id.car_detail_more,R.id.car_detail_back,R.id.car_detail_advisory})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_deleat:
                if (infobean.getDefinState().equals("3") || infobean.getDefinState().equals("4")) {
                    startActivity(new Intent(mContext, DismountActivity.class).putExtra("id", id).putExtra("from","1"));
                }else {
                    showDeleatDialog();
                }

                break;

            case R.id.tv_rebuild:
                Intent intent=new Intent(ManageInfoAct.this,ReleaseAct.class);
                intent.putExtra("fromtype","2");//重新编辑进入
                intent.putExtra("id",id);
                startActivity(intent);
                break;

            case R.id.car_detail_back:
                finish();
                break;

            case R.id.car_detail_more:
                if(infobean!=null&&infobean.getDetail()!=null) {
                    startActivity(new Intent(this, CarMoreInfoActivity.class).putExtra("infobean", infobean.getDetail()).putExtra("type",2));
                }
                break;
            case R.id.car_detail_advisory:
//                if(!SpUtils.getIsLogin(mContext))
//                    startActivity(new Intent(mContext, LoginActivity.class));
//                else {
                    startActivity(new Intent(mContext, CustomerAct.class));
//                }
                break;


        }

    }

    private void showDeleatDialog() {
        MyAlertDialog dialog = new MyAlertDialog(mContext);
        dialog.builder().setTitle("提示").setMsg("删除后无法恢复,确认删除？").setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleat();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void deleat() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        param.add("type", "2");//1-下架 2-删除
        OkHttpUtils.post(Config.DELETE_OLD_CAR, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                ToastTool.show(mContext,"删除成功");
//                startActivity(new Intent(mContext, SeachCarServerSubmitActivity.class).putExtra("type", 5));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
            }
        });
    }

//    private void deleat() {
//        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
//        RequestParam param = new RequestParam();
//        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
//        param.add("timestamp", EncryptionTools.TIMESTAMP);
//        param.add("nonce", EncryptionTools.NONCE);
//        param.add("signature", EncryptionTools.SIGNATURE);
//        param.add("id",resultCarEntity.getId());
//        param.add("type","4");//删除类型
//        param.add("vehicleBaseId", resultCarEntity.getVehicleBaseId());
//        param.add("vehicleSaleId", resultCarEntity.getVehicleSaleId());
//        OkHttpUtils.post(Config.FB_URL, param, new OkHttpCallBack(ManageInfoAct.this,"加载中...") {
//            @Override
//            public void onSuccess(String data) {
//                ToastTool.show(ManageInfoAct.this,"已删除");
//                finish();//提示已删除，然后跳转到列表页
//            }
//
//            @Override
//            public void onError(int code, String error) {
//             ToastTool.show(ManageInfoAct.this,error);
//            }
//        });
//    }



    //返回“--"
    private String isNull(String registrationArea) {
        if(!TextUtils.isEmpty(registrationArea)){
            return registrationArea;
        }else {
            return "--";
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("position",String.valueOf(position));
        if(position==0||position==vp.getAdapter().getCount() - 2)//右滑动
            tvIndex.setText(String.valueOf(infobean.getDetail().getPicList().size())+"/"+infobean.getDetail().getPicList().size());
        else if(position==1||position==vp.getAdapter().getCount()-1)//左滑动
            tvIndex.setText("1/"+infobean.getDetail().getPicList().size());
        else
            tvIndex.setText(String.valueOf(position%infobean.getDetail().getPicList().size())+"/"+infobean.getDetail().getPicList().size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state){
            case ViewPager.SCROLL_STATE_IDLE:
                if(vp.getCurrentItem()==0) {
                    vp.setCurrentItem(vp.getAdapter().getCount() - 2,false);
                }
                else if(vp.getCurrentItem()==vp.getAdapter().getCount()-1) {
                    vp.setCurrentItem(1,false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }


}
