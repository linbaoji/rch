package com.rch.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.activity.NewCarInfoAct;
import com.rch.common.CalculationTool;
import com.rch.common.DensityUtil;
import com.rch.common.GlideUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.StrSplitTool;
import com.rch.common.TimeSplitTool;
import com.rch.entity.CarEntity;

import java.util.List;


/**
 * Created by Administrator on 2018/4/16.
 */

public class HomeCarLayout extends LinearLayout {

    Context context;

    LinearLayout layout,leftLayout,areaLayout,itemll;
    ImageView imageView,iv_lefttop,iv_xj;
    TextView title,desc,moneyTitle,money,discountMoneyTitle, discountMoney,area,tvStore,tvOrdinary,tv_price;
    LayoutParams params;
    TextView tv_city;
    ItemLabelLayout ll_tab;


    private LinearLayout ll_qyj;


    View line;

    public HomeCarLayout(Context context) {
        super(context);
        this.context=context;
    }

    public HomeCarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public HomeCarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    CarEntity carEntity;
    //public void initData(final List<CarEntity> list, String agentType, String userResultState)
    public void initData(final List<CarEntity> list, final int from)
    {
        Log.e("-----",list.toString());
        removeAllViews();
        if(list!=null&&list.size()>0) {
            for (int i=0;i<list.size();i++) {
                 carEntity=list.get(i);
                 View view= LayoutInflater.from(context).inflate(R.layout.item_carliebiao,null);
                 imageView= (ImageView) view.findViewById(R.id.shop_list_adapter_img);//tuxiang
                 title= (TextView) view.findViewById(R.id.shop_list_adapter_title);//标题
                 desc= (TextView) view.findViewById(R.id.shop_list_adapter_desc);//描述
                 tvOrdinary= (TextView) view.findViewById(R.id.shop_list_adapter_ordinary);//门店价
                 money= (TextView) view.findViewById(R.id.shop_list_adapter_money);//门店价格
                 tvStore= (TextView) view.findViewById(R.id.shop_list_adapter_store);//企业价
                 area= (TextView) view.findViewById(R.id.shop_list_adapter_area);//企业价格
                 ll_qyj= (LinearLayout) view.findViewById(R.id.shop_list_adapter_discount_layout);
                 itemll= (LinearLayout) view.findViewById(R.id.shop_list_adapter_layout);
                 tv_price= (TextView) view.findViewById(R.id.tv_price);
                 iv_lefttop= (ImageView) view.findViewById(R.id.iv_lefttop);
                 iv_xj= (ImageView) view.findViewById(R.id.iv_xj);
                 tv_city=(TextView)view.findViewById(R.id.tv_city);
                 ll_tab= (ItemLabelLayout) view.findViewById(R.id.ll_tab);
                 ll_tab.setVisibility(GONE);
                 itemll.setTag(i);
                 itemll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            int index=Integer.parseInt(String.valueOf(v.getTag()));
                            if(from==0) {//新车
                                context.startActivity(new Intent(context, NewCarInfoAct.class).putExtra("id", list.get(index).getId()));
                            }else {//旧车
                                context.startActivity(new Intent(context, CarDetailActivity.class).putExtra("id", list.get(index).getId()));
                            }
                        }
                });

                GlideUtils.showImg(context,carEntity.getVehicleImage(),R.mipmap.car_emp,imageView);
//                Glide.with(context).load(carEntity.getVehicleImagePath()).placeholder(R.mipmap.car_emp).into(imageView);
//                title.setText(carEntity.getBrandName()+" "+carEntity.getModelName()+" "+carEntity.getVehicleName());
//                if(carEntity.getIsSelected().equals("1")) {
//                    SpannableStringBuilder spannableString = new SpannableStringBuilder();
//                    spannableString.append("占位");
//                    spannableString.append(" ");
//                    spannableString.append(isNull(carEntity.getModelName()));
//                    ImageSpan imageSpan = new ImageSpan(context, R.mipmap.jxcar);
////              也可以这样
////              Drawable drawable = context.getResources().getDrawable(R.mipmap.jxcar);
////              drawable.setBounds(0, 0, GeneralUtils.dip2px(context,28), GeneralUtils.dip2px(context,15));
////              ImageSpan imageSpan1 = new ImageSpan(drawable);
////              将index为6、7的字符用图片替代
//                    spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                    title.setText(spannableString);
//                }else {
//                    title.setText(isNull(carEntity.getModelName()));
//                }
//
//                if(carEntity.getIsRecommend().equals("1")){
//                    iv_lefttop.setVisibility(VISIBLE);
//                }else {
//                    iv_lefttop.setVisibility(GONE);
//                }

                title.setText(isNull(carEntity.getModelName()));
                if(StartorSelectUtils.isBoolean(carEntity.getIsRecommend())||StartorSelectUtils.isBoolean(carEntity.getIfStar())||StartorSelectUtils.isBoolean(carEntity.getIsSelected())) {
                    ll_tab.setVisibility(VISIBLE);
                    ll_tab.initData(StartorSelectUtils.nullOrone(carEntity.getIsRecommend()), StartorSelectUtils.nullOrone(carEntity.getIfStar()), StartorSelectUtils.nullOrone(carEntity.getIsSelected()));
                }else {
                    ll_tab.setVisibility(GONE);
                }

                if(carEntity.getShelvesStatus().equals("2")||carEntity.getShelvesStatus().equals("3")){
                    if(carEntity.getShelvesStatus().equals("2")){
                        iv_xj.setImageResource(R.mipmap.xiajia);
                    }else {
                        iv_xj.setImageResource(R.mipmap.shouq);
                    }
                    iv_xj.setVisibility(VISIBLE);
                }else {
                    iv_xj.setVisibility(GONE);
                }

                if (from == 0) {//新车
                    desc.setText(isNull(carEntity.getVehicleStandardName() + "/") +
                            isNull(carEntity.getStandardDelivery() +
                                    isNull(carEntity.getDeliveryUnit()) + "/"
                                    + isNull(carEntity.getDriverType())));
                } else {//二手车
//                    String year = TimeSplitTool.getYM(carEntity.getRegistrationTime());
                    String lime = "/0公里";
                    int iShowMileage = carEntity.getShowMileage().isEmpty() ? 0 :  Integer.parseInt(carEntity.getShowMileage());
                    if (iShowMileage < 10000) {
                        lime = "/" + String.valueOf(iShowMileage) + "公里";
                    } else {
                        lime = "/" + StrSplitTool.retainOneNumber(String.valueOf(iShowMileage)) + "万公里";
                    }
                    desc.setText(isNull(carEntity.getRegistrationTime()) + lime);
                }


                if (!TextUtils.isEmpty(carEntity.getSalesPriceMin()) && !TextUtils.isEmpty(carEntity.getSalesPriceMax())) {
                    if (carEntity.getSalesPriceMin().equals(carEntity.getSalesPriceMax())) {
//                      holder.tv_price.setText(isNull(CalculationTool.getCaluculationIntUnit(entity.getSalesPriceMin())) + "万");
                        tv_price.setText(isNull(carEntity.getSalesPriceMin()) + "万");
                    } else {
//                      holder.tv_price.setText(isNull(CalculationTool.getCaluculationIntUnit(entity.getSalesPriceMin())) + "万" +"-"+ isNull(CalculationTool.getCaluculationIntUnit(entity.getSalesPriceMax())) + "万");
                        tv_price.setText(isNull(carEntity.getSalesPriceMin()) + "万" +"-"+ isNull(carEntity.getSalesPriceMax()) + "万");

                    }
                } else {
//                  holder.tv_price.setText(isNull(CalculationTool.getCaluculationIntUnit(entity.getSalesPriceMin())) + "万" +"-"+ isNull(CalculationTool.getCaluculationIntUnit(entity.getSalesPriceMax())) + "万");
                    tv_price.setText(isNull(carEntity.getSalesPriceMin()) + "万" +"-"+ isNull(carEntity.getSalesPriceMax()) + "万");

                }

                tv_city.setText(isNull(carEntity.getCityName()));

                addView(view);
            }
        }
    }



    private String isNull(String tex) {
        if(!TextUtils.isEmpty(tex)){
            return tex;
        }else {
            return "--";
        }
    }


}

