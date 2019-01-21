package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.activity.ManageInfoAct;
import com.rch.activity.NewCarInfoAct;
import com.rch.activity.TabNewCarInfoAct;
import com.rch.common.CalculationTool;
import com.rch.common.GlideUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.StrSplitTool;
import com.rch.common.TimeSplitTool;
import com.rch.custom.ItemLabelLayout;
import com.rch.entity.CarEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.MyHolder> {
    Context context;
    List<CarEntity> shopList;
    private int from;//0是新车 1是二手车(点击item要跳转的地方不一样的)

    public CarListAdapter(Context context, List<CarEntity> shopList,int from)
    {
        this.context=context;
        this.shopList=shopList;
        this.from=from;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.car_list_adapter,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CarEntity entity = shopList.get(position);
        GlideUtils.showImg(context,entity.getVehicleImage(),R.mipmap.car_emp,DiskCacheStrategy.ALL,holder.imageView);
//        Glide.with(context).load(entity.getVehicleImagePath()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.car_emp).into(holder.imageView);


//        if(entity.getIsSelected().equals("1")) {
//            SpannableStringBuilder spannableString = new SpannableStringBuilder();
//            spannableString.append("占位");
//            spannableString.append(" ");
//            spannableString.append(isNull(entity.getModelName()));
//            ImageSpan imageSpan = new ImageSpan(context, R.mipmap.jxcar);
////              也可以这样
////              Drawable drawable = context.getResources().getDrawable(R.mipmap.jxcar);
////              drawable.setBounds(0, 0, GeneralUtils.dip2px(context,28), GeneralUtils.dip2px(context,15));
////              ImageSpan imageSpan1 = new ImageSpan(drawable);
////              将index为6、7的字符用图片替代
//            spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            holder.title.setText(spannableString);
//        }else {
//            holder.title.setText(isNull(entity.getModelName()));
//        }
//
//        if(entity.getIsRecommend().equals("1")){
//            holder.iv_lefttop.setVisibility(View.VISIBLE);
//        }else {
//            holder.iv_lefttop.setVisibility(View.GONE);
//        }

        holder.title.setText(isNull(entity.getModelName()));

        if(StartorSelectUtils.isBoolean(entity.getIsRecommend())||StartorSelectUtils.isBoolean(entity.getIfStar())||StartorSelectUtils.isBoolean(entity.getIsSelected())) {
            holder.ll_tab.setVisibility(View.VISIBLE);
            holder.ll_tab.initData(StartorSelectUtils.nullOrone(entity.getIsRecommend()), StartorSelectUtils.nullOrone(entity.getIfStar()), StartorSelectUtils.nullOrone(entity.getIsSelected()));
        }else {
            holder.ll_tab.setVisibility(View.GONE);
        }

        if(entity.getShelvesStatus().equals("2")||entity.getShelvesStatus().equals("3")){
            if(entity.getShelvesStatus().equals("2")){
                holder.iv_xj.setImageResource(R.mipmap.xiajia);
            }else {
                holder.iv_xj.setImageResource(R.mipmap.shouq);
            }
            holder.iv_xj.setVisibility(View.VISIBLE);
        }else if(entity.getShelvesStatus().equals("0")){
            if(entity.getAuditState().equals("1")){
                holder.iv_xj.setImageResource(R.mipmap.shenhez);
            }else if(entity.getAuditState().equals("2")){
                holder.iv_xj.setImageResource(R.mipmap.yitongguo);
            }else if(entity.getAuditState().equals("3")){
                holder.iv_xj.setImageResource(R.mipmap.shenheshibai);
            }
            holder.iv_xj.setVisibility(View.VISIBLE);
        }else {
            holder.iv_xj.setVisibility(View.GONE);
        }



        if (from == 0) {//新车
            holder.tv_dec.setText(isNull(entity.getVehicleStandardName()) + "/"+
                    isNull(entity.getStandardDelivery() +
                            isNull(entity.getDeliveryUnit()) + "/"
                            + isNull(entity.getDriverType())));
        } else {//二手车
            String lime = "/0公里";
            int iShowMileage = entity.getShowMileage().isEmpty() ? 0 :  Integer.parseInt(entity.getShowMileage());
            if (iShowMileage < 10000) {
                lime = "/" + String.valueOf(iShowMileage) + "公里";
            } else {
                lime = "/" + StrSplitTool.retainOneNumber(String.valueOf(iShowMileage)) + "万公里";
            }
            holder.tv_dec.setText(isNull(entity.getRegistrationTime()) + lime);
        }

        holder.tv_city.setText(isNull(entity.getCityName()));//城市名称

        //门店价格
        if (!TextUtils.isEmpty(entity.getSalesPriceMin()) && !TextUtils.isEmpty(entity.getSalesPriceMax())) {
            if (entity.getSalesPriceMin().equals(entity.getSalesPriceMax())) {
                holder.desc.setText(isNull(entity.getSalesPriceMin()) + "万");
            } else {
                holder.desc.setText(isNull(entity.getSalesPriceMin()) +"万"+"-"+ isNull(entity.getSalesPriceMax()) + "万");
            }
        } else {
            holder.desc.setText(isNull(entity.getSalesPriceMin()) +"万"+"-"+ isNull(entity.getSalesPriceMax()) + "万");
        }

        if (!TextUtils.isEmpty(entity.getTradePriceMin()) && !TextUtils.isEmpty(entity.getTradePriceMax())) {
            if (entity.getTradePriceMin().equals(entity.getTradePriceMax())) {
                holder.tv_price.setText(isNull(entity.getTradePriceMin()) + "万");
            } else {
                holder.tv_price.setText(isNull(entity.getTradePriceMin()) +"万"+"-"+ isNull(entity.getTradePriceMax()) + "万");
            }
        } else {
            holder.tv_price.setText(isNull(entity.getTradePriceMin())+"万" +"-"+ isNull(entity.getTradePriceMax()) + "万");
        }



    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout layout, discountLayout;
        ImageView imageView, iv_lefttop,iv_xj;
        TextView title, desc, money, area, tvStore, tvOrdinary;
        RelativeLayout rl_car_manage;
        TextView tv_brand, tv_time, tv_status,tv_price,tv_dec,tv_city;
        View view, view_car_manage;
        ItemLabelLayout ll_tab;

        public MyHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.shop_list_adapter_layout);
            discountLayout = (LinearLayout) itemView.findViewById(R.id.shop_list_adapter_discount_layout);
            imageView = (ImageView) itemView.findViewById(R.id.shop_list_adapter_img);
            title = (TextView) itemView.findViewById(R.id.shop_list_adapter_title);
            desc = (TextView) itemView.findViewById(R.id.shop_list_adapter_desc);//车辆管理需隐藏
            money = (TextView) itemView.findViewById(R.id.shop_list_adapter_money);
            area = (TextView) itemView.findViewById(R.id.shop_list_adapter_area);
            tvStore = (TextView) itemView.findViewById(R.id.shop_list_adapter_store);
            tvOrdinary = (TextView) itemView.findViewById(R.id.shop_list_adapter_ordinary);
            //以下车辆管理需显示，其他需隐藏
//            rl_car_manage = (RelativeLayout) itemView.findViewById(R.id.rl_car_manage);
//            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
//            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            view = itemView.findViewById(R.id.view);
            view_car_manage = itemView.findViewById(R.id.view_car_manage);
            view_car_manage.setVisibility(View.VISIBLE);
            iv_lefttop = (ImageView) itemView.findViewById(R.id.iv_lefttop);
            iv_xj= (ImageView) itemView.findViewById(R.id.iv_xj);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
            tv_dec = (TextView) itemView.findViewById(R.id.tv_dec);//车辆管理需隐藏
            tv_city= (TextView) itemView.findViewById(R.id.tv_city);
            ll_tab= (ItemLabelLayout) itemView.findViewById(R.id.ll_tab);
            ll_tab.setVisibility(View.GONE);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(from==0){//新车
                        context.startActivity(new Intent(context, TabNewCarInfoAct.class).putExtra("id",shopList.get(getPosition()).getId()));

                    }else {//旧车
//                        context.startActivity(new Intent(context, ManageInfoAct.class).putExtra("id", id));
                        context.startActivity(new Intent(context, ManageInfoAct.class).putExtra("id",shopList.get(getPosition()).getId()));
                    }
                }
            });
        }
    }

    public void updateShopListData(List<CarEntity> shopList) {
        this.shopList=shopList;
        notifyDataSetChanged();
    }

    private String isNull(String tex) {
        if(!TextUtils.isEmpty(tex)){
            return tex;
        }else {
            return "--";
        }
    }

}
