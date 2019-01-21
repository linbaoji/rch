package com.rch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;


import android.view.ViewTreeObserver;

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
import com.rch.common.GsonUtils;
import com.rch.common.SpUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.ToastTool;
import com.rch.custom.ItemLabelLayout;
import com.rch.custom.MyAlertDialog;
import com.rch.entity.NewCarInfoBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 从新车管理进来的新车详情
 * Created by Administrator on 2018/10/29.
 */

public class TabNewCarInfoAct extends SecondBaseActivity implements ViewPager.OnPageChangeListener {
    //1.2.3新增字段
    @ViewInject(R.id.tv_brand)
    private TextView tv_brand;//品牌
    @ViewInject(R.id.tv_series)
    private TextView tv_series;//车系
    @ViewInject(R.id.tv_body_color)
    private TextView tv_body_color;//车身颜色
    @ViewInject(R.id.tv_interior_color)
    private TextView tv_interior_color;//内饰颜色
    @ViewInject(R.id.tv_config)
    private TextView tv_config;//配置说明
    @ViewInject(R.id.ll_configuration)
    private LinearLayout ll_configuration;//配置说明

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.ll_kefu)
    private LinearLayout ll_kefu;//客服  从新车管理过来的不显示
    @ViewInject(R.id.car_detail_back)
    private ImageView car_detail_back;
    @ViewInject(R.id.tv_carName)
    private TextView tv_carName;//title车辆名称
    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;
    @ViewInject(R.id.iv_msg)
    private ImageView ivMsg;
    @ViewInject(R.id.tv_kefu)
    private TextView tv_kefu;
    @ViewInject(R.id.sc_view)
    private NestedScrollView sc_view;
    @ViewInject(R.id.car_detail_vp)
    private ViewPager vp;
    @ViewInject(R.id.car_num)
    private TextView car_num;
    @ViewInject(R.id.car_detail_index)
    private TextView tvIndex;
    @ViewInject(R.id.car_detail_title)
    private TextView tv_carname;
    @ViewInject(R.id.tv_dec)
    private TextView tv_dec;
    @ViewInject(R.id.tv_price)
    private TextView tv_price;
    @ViewInject(R.id.tv_pfprice)
    private TextView tv_pfprice;

    @ViewInject(R.id.tv_cg)
    private TextView tv_cg;
    @ViewInject(R.id.tv_nk)
    private TextView tv_nk;
    @ViewInject(R.id.tv_pl)
    private TextView tv_pl;
    @ViewInject(R.id.tv_pfbz)
    private TextView tv_pfbz;
    @ViewInject(R.id.tv_bsxlx)
    private TextView tv_bsxlx;
    @ViewInject(R.id.tv_qdfs)
    private TextView tv_qdfs;
    @ViewInject(R.id.tv_zws)
    private TextView tv_zws;
    @ViewInject(R.id.tv_zzslx)
    private TextView tv_zzslx;
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
    @ViewInject(R.id.ll_tab)
    private ItemLabelLayout ll_tab;
    @ViewInject(R.id.tv_city)
    private TextView tv_city;

    private int screen_height;

    private NewCarInfoBean infobean;
    BannerAdatper adatper;
    private Gson gson;

    private String id;//车辆id
    private boolean  fromOrder = false;//是否是客户详情预约单列表过来的

    @Override
    public void setLayout() {
        setContentView(R.layout.act_tabnewcarinfo);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        gson = new Gson();
        screen_height = SpUtils.get_View_heigth(SpUtils.getScreenWith(mContext), 600, 900);
        ViewGroup.LayoutParams params = rl_vp.getLayoutParams();
        params.height = screen_height;
        rl_vp.setLayoutParams(params);
        if (getIntent()!=null) {
            id = getIntent().getStringExtra("id");
            fromOrder = getIntent().getBooleanExtra("order", false);
        }
        if (!fromOrder)
            ll_kefu.setVisibility(View.GONE);
        initListeners();
        scrollChange();
//        getDate();
    }
    private int imageHeight;

    private void initListeners() {
        // 获取顶部图片高度
        ViewTreeObserver vto = vp.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = vp.getHeight()-150;
                Log.e("===",imageHeight+"]]]");
            }
        });
    }
    private void scrollChange(){
        sc_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    toolbar.setBackgroundResource(R.mipmap.detail_shadow);//AGB由相关工具获得，或者美工提供
                    car_detail_back.setImageResource(R.mipmap.back_white);
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    tv_carName.setVisibility(View.GONE);
                } else if (scrollY > 0 && scrollY <= imageHeight) {
                    float scale = (float) scrollY / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    toolbar.setBackgroundResource(R.mipmap.detail_shadow);
                    car_detail_back.setImageResource(R.mipmap.back_white);
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    tv_carName.setVisibility(View.GONE);
                } else {
                    toolbar.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    car_detail_back.setImageResource(R.mipmap.back);
                    ivMsg.setImageResource(R.mipmap.kefuhei);
                    tv_kefu.setTextColor(getResources().getColor(R.color.kf_info));
                    tv_carName.setVisibility(View.VISIBLE);
                    tv_carName.setText("车辆详情");
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDate();
    }

    private void getDate() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.NEWCARDEATILE_URL, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    infobean = GsonUtils.gsonToBean(result.toString(), NewCarInfoBean.class);
                    setUi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
            }
        });
    }

    private void setUi() {
        if (infobean != null) {
            if (infobean.getDetail().getPicList() != null && infobean.getDetail().getPicList().size() > 0) {//设置图片
                adatper = new BannerAdatper(this, infobean.getDetail().getPicList(), infobean.getDefinState());
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

            car_num.setText("车源号:" + isNull(infobean.getDetail().getSourcecode()));

//            if (!TextUtils.isEmpty(infobean.getDetail().getIsSelected()) && infobean.getDetail().getIsSelected().equals("1")) {
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
//            } else {
//                tv_carname.setText(isNull(infobean.getDetail().getModelName()));
//            }
            tv_carname.setText(isNull(infobean.getDetail().getModelName()));
            if(StartorSelectUtils.isBoolean(infobean.getDetail().getIsRecommend())||StartorSelectUtils.isBoolean(infobean.getDetail().getIfStar())||StartorSelectUtils.isBoolean(infobean.getDetail().getIsSelected())) {
                ll_tab.setVisibility(View.VISIBLE);
                ll_tab.initData(StartorSelectUtils.nullOrone(infobean.getDetail().getIsRecommend()), StartorSelectUtils.nullOrone(infobean.getDetail().getIfStar()), StartorSelectUtils.nullOrone(infobean.getDetail().getIsSelected()));
            }

            tv_dec.setText(isNull(infobean.getDetail().getVehicleStandardName() + "/") +
                    isNull(infobean.getDetail().getStandardDelivery() +
                            isNull(infobean.getDetail().getDeliveryUnit()) + "/"
                            + isNull(infobean.getDetail().getDriverType())));

            //门店价
            if (!TextUtils.isEmpty(infobean.getDetail().getSalesPriceMin()) && !TextUtils.isEmpty(infobean.getDetail().getSalesPriceMax())) {
                if (infobean.getDetail().getSalesPriceMin().equals(infobean.getDetail().getSalesPriceMax())) {
                    tv_price.setText(isNull(infobean.getDetail().getSalesPriceMin()) + "万");

                } else {
                    tv_price.setText(isNull(infobean.getDetail().getSalesPriceMin())+ "万"+"-"+isNull(infobean.getDetail().getSalesPriceMax()) + "万");
                }
            } else {
                tv_price.setText(isNull(infobean.getDetail().getSalesPriceMin()) + "万" + "-"+ isNull(infobean.getDetail().getSalesPriceMax()) + "万");
            }

            //批发价
            if (!TextUtils.isEmpty(infobean.getDetail().getTradePriceMin()) && !TextUtils.isEmpty(infobean.getDetail().getTradePriceMax())) {
                if (infobean.getDetail().getTradePriceMin().equals(infobean.getDetail().getTradePriceMax())) {
                   tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin())+"万");
                } else {
                    tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin())+"万"+"-"+ isNull(infobean.getDetail().getTradePriceMax()) + "万");
                }
            } else {
                tv_pfprice.setText(isNull(infobean.getDetail().getTradePriceMin())+"万"+"-"+ isNull(infobean.getDetail().getTradePriceMax()) + "万");
            }


            if(TextUtils.isEmpty(infobean.getDetail().getProvinceName())&&TextUtils.isEmpty(infobean.getDetail().getCityName())){
                tv_city.setText(isNull(infobean.getDetail().getProvinceName()));
            }else {
                if(!TextUtils.isEmpty(infobean.getDetail().getProvinceName())&&!TextUtils.isEmpty(infobean.getDetail().getCityName())){
                    if(infobean.getDetail().getProvinceName().equals(infobean.getDetail().getCityName())) {
                        tv_city.setText(infobean.getDetail().getProvinceName());
                    }else {
                        tv_city.setText(infobean.getDetail().getProvinceName() +" "+infobean.getDetail().getCityName());
                    }
                }else {
                    tv_city.setText(isNull(infobean.getDetail().getProvinceName()) +" "+isNull(infobean.getDetail().getCityName()));
                }

            }
            

            tv_cg.setText(isNull(infobean.getDetail().getVehicleStandardName()));
            tv_nk.setText(isNull(infobean.getDetail().getModelYear()));
            tv_pl.setText(isNull(infobean.getDetail().getStandardDelivery() + isNull(infobean.getDetail().getDeliveryUnit())));
            tv_pfbz.setText(isNull(infobean.getDetail().getEmissionStandardName()));
            tv_bsxlx.setText(isNull(infobean.getDetail().getGearboxTypeName()));
            tv_qdfs.setText(isNull(infobean.getDetail().getDriverType()));
            tv_zws.setText(isNull(infobean.getDetail().getCarrierNumber()));
            tv_zzslx.setText(isNull(infobean.getDetail().getMakerType()));

            if (!TextUtils.isEmpty(infobean.getDetail().getModified())) {
                ll_configuration.setVisibility(View.VISIBLE);
                tv_config.setText(isNull(infobean.getDetail().getModified()));
            }else {
                ll_configuration.setVisibility(View.GONE);
            }
            tv_brand.setText(isNull(infobean.getDetail().getBrandName()));
            tv_series.setText(isNull(infobean.getDetail().getSeriesName()));
//            if (!TextUtils.isEmpty(infobean.getDetail().getVehicleColor())) {
//                tv_body_color.setBackgroundResource(R.drawable.money_frame);
//                if (infobean.getDetail().getVehicleColor().length()>18)
//                    tv_body_color.setText(infobean.getDetail().getVehicleColor().substring(0,17)+"...");
//                else
//                    tv_body_color.setText(isNull(infobean.getDetail().getVehicleColor()));
//            }else {
//                tv_body_color.setText(isNull(infobean.getDetail().getVehicleColor()));
//            }
            tv_body_color.setText(isNull(infobean.getDetail().getVehicleColor()));
//            if (!TextUtils.isEmpty(infobean.getDetail().getInsideColor())){
//                tv_interior_color.setBackgroundResource(R.drawable.money_frame);
//                if (infobean.getDetail().getInsideColor().length()>18)
//                    tv_interior_color.setText(infobean.getDetail().getInsideColor().substring(0,17)+"...");
//                else
//                    tv_interior_color.setText(isNull(infobean.getDetail().getInsideColor()));
//            }else {
//                tv_interior_color.setText(isNull(infobean.getDetail().getInsideColor()));
//            }
            tv_interior_color.setText(isNull(infobean.getDetail().getInsideColor()));
        }

    }

    private String isNull(String text) {
        if (TextUtils.isEmpty(text)) {
            return "--";
        } else {
            return text;
        }
    }

    @OnClick({R.id.ll_deleat,R.id.tv_rebuild,R.id.car_detail_more,R.id.car_detail_back,R.id.ll_kefu})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_deleat:
                if (infobean.getDefinState().equals("3") || infobean.getDefinState().equals("4")) {
                    startActivity(new Intent(TabNewCarInfoAct.this, DismountActivity.class).putExtra("id", id).putExtra("from","0"));
                }else {
                    showDeleatDialog();
                }
                break;

            case R.id.tv_rebuild://重新发布
                startActivity(new Intent(TabNewCarInfoAct.this, ReleaseNewCarActivity.class).putExtra("id",id).putExtra("operType","2"));
                break;

            case R.id.car_detail_more:
                startActivity(new Intent(mContext,MorNewCarActivity.class).putExtra("id",infobean.getDetail().getId()));
                break;
            case R.id.car_detail_back:
                finish();
                break;
            case R.id.ll_kefu:
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
        OkHttpUtils.post(Config.DELETE_NEW_CAR, param, new OkHttpCallBack(mContext,"加载中...") {
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("position", String.valueOf(position));
        if (position == 0 || position == vp.getAdapter().getCount() - 2)//右滑动
            tvIndex.setText(String.valueOf(infobean.getDetail().getPicList().size()) + "/" + infobean.getDetail().getPicList().size());
        else if (position == 1 || position == vp.getAdapter().getCount() - 1)//左滑动
            tvIndex.setText("1/" + infobean.getDetail().getPicList().size());
        else
            tvIndex.setText(String.valueOf(position % infobean.getDetail().getPicList().size()) + "/" + infobean.getDetail().getPicList().size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (vp.getCurrentItem() == 0) {
                    vp.setCurrentItem(vp.getAdapter().getCount() - 2, false);
                } else if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                    vp.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }

    }


}
