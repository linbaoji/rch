package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.activity.ManageInfoAct;
import com.rch.activity.OrderInfo;
import com.rch.common.CalculationTool;
import com.rch.entity.OderListEntity;

import java.util.List;

/**
 * 订单管理列表
 * Created by Administrator on 2018/7/31.
 */

public class BookorderAdapter extends RecyclerView.Adapter<BookorderAdapter.MyHolder>{
    private Context context;
    List<OderListEntity> list;
    String priceType="";
    public BookorderAdapter(Context context, List<OderListEntity> list,String priceType) {
        this.context = context;
        this.list=list;
        this.priceType=priceType;
    }

    @Override
    public BookorderAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(context, R.layout.item_vehicle,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookorderAdapter.MyHolder holder, int position) {
        OderListEntity oderListEntity=list.get(position);
        holder.tv_item_layout.setTag(position);
        holder.tv_item_name.setText(oderListEntity.getOrderUserName());
        holder.tv_item_phone.setText(oderListEntity.getOrderUserMobile());
        holder.tv_item_status.setText(oderListEntity.getOrderStateName());
        holder.tv_item_title.setText(oderListEntity.getVehicleFullName());
        Glide.with(context).load(oderListEntity.getVehicleImage()).into(holder.tv_item_img);

        if(oderListEntity.getOrderState().equals("6")||oderListEntity.getOrderState().equals("5")){//已取消和确定失效
            holder.tv_item_status.setTextColor(Color.parseColor("#999999"));
        }else {
            holder.tv_item_status.setTextColor(Color.parseColor("#ff6b3f"));
        }

        String sMoney= CalculationTool.getCaluculationIntUnit(oderListEntity.getSalesPrice());//.isEmpty()?"":carEntity.getSalesPrice()+"万";
        holder.tv_item_lable_one_money.setText(sMoney);

        String sDiscountMoney=CalculationTool.getCaluculationIntUnit(oderListEntity.getRatePrice());//carEntity.getRatePrice()+"万";
        holder.tv_item_lable_two_money.setText(sDiscountMoney);
        if(priceType.equals("0"))
        {
            holder.tv_item_lable_two_layout.setVisibility(View.GONE);
            holder.tv_item_lable_one.setTextSize(12f);
            holder.tv_item_lable_one_money.setTextSize(15f);
        }
        else if(priceType.equals("1"))
        {
            holder.tv_item_lable_two_layout.setVisibility(View.VISIBLE);
//                    tvStore.setText("企业价:");
            holder.tv_item_lable_two.setText("批发价:");
            holder.tv_item_lable_one.setTextSize(11f);
            if (sMoney.length()>7){
                holder.tv_item_lable_one_money.setTextSize(6);
            }else {
                holder.tv_item_lable_one_money.setTextSize(11);
            }

            if (sDiscountMoney.length()>7){
                holder.tv_item_lable_two_money.setTextSize(6);
            }else {
                holder.tv_item_lable_two_money.setTextSize(11);
            }
        }else
        {
            holder.tv_item_lable_two_layout.setVisibility(View.VISIBLE);
            holder.tv_item_lable_two.setText("分销价:");
            holder.tv_item_lable_one.setTextSize(11f);
            if (sMoney.length()>7){
                holder.tv_item_lable_one_money.setTextSize(6);
            }else {
                holder.tv_item_lable_one_money.setTextSize(11);
            }

            if (sDiscountMoney.length()>7){
                holder.tv_item_lable_two_money.setTextSize(6);
            }else {
                holder.tv_item_lable_two_money.setTextSize(11);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name,tv_item_phone,tv_item_status,tv_item_title,tv_item_lable_one,tv_item_lable_one_money,tv_item_lable_two,tv_item_lable_two_money;
        ImageView tv_item_img;//主图
        LinearLayout tv_item_layout,tv_item_lable_one_layout,tv_item_lable_two_layout;
        public MyHolder(View itemView) {
            super(itemView);

            tv_item_name= (TextView) itemView.findViewById(R.id.tv_item_name);
            tv_item_phone= (TextView) itemView.findViewById(R.id.tv_item_phone);
            tv_item_status= (TextView) itemView.findViewById(R.id.tv_item_status);
            tv_item_title= (TextView) itemView.findViewById(R.id.tv_item_title);
            tv_item_lable_one= (TextView) itemView.findViewById(R.id.tv_item_lable_one);
            tv_item_lable_one_money= (TextView) itemView.findViewById(R.id.tv_item_lable_one_money);
            tv_item_lable_two= (TextView) itemView.findViewById(R.id.tv_item_lable_two);
            tv_item_lable_two_money= (TextView) itemView.findViewById(R.id.tv_item_lable_two_money);
            tv_item_img= (ImageView) itemView.findViewById(R.id.tv_item_img);
            tv_item_layout= (LinearLayout) itemView.findViewById(R.id.tv_item_layout);
            tv_item_lable_one_layout= (LinearLayout) itemView.findViewById(R.id.tv_item_lable_one_layout);
            tv_item_lable_two_layout= (LinearLayout) itemView.findViewById(R.id.tv_item_lable_two_layout);

            tv_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,OrderInfo.class).putExtra("orderId",
                            list.get(Integer.parseInt(String.valueOf(view.getTag()))).getId()).putExtra("type","1"));
                }
            });
        }
    }
    public void updata( List<OderListEntity> list,String priceType)
    {
        this.priceType=priceType;
        this.list=list;
        notifyDataSetChanged();
    }
}
