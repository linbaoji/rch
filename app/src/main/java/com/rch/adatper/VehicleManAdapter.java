package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.rch.R;

import com.rch.activity.OrderInfo;
import com.rch.common.CalculationTool;
import com.rch.entity.UserOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class VehicleManAdapter extends RecyclerView.Adapter<VehicleManAdapter.MyHolder>{
    private Context context;
    private List<UserOrderEntity>list;
    private String priceType;

    public VehicleManAdapter(Context context,List<UserOrderEntity>list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public VehicleManAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(context, R.layout.item_vehicle,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VehicleManAdapter.MyHolder holder, int position) {
        UserOrderEntity bean=list.get(position);
        holder.tv_item_layout.setTag(position);
        holder.tv_item_name.setText(bean.getOrderUserName());
        holder.tv_item_phone.setText(bean.getOrderUserMobile());
        holder.tv_item_status.setText(bean.getOrderStateName());
        holder.tv_item_title.setText(bean.getVehicleFullName());
        Glide.with(context).load(bean.getVehicleImage()).into(holder.tv_item_img);

        if(bean.getOrderState().equals("6")||bean.getOrderState().equals("5")){//已取消和确定失效
            holder.tv_item_status.setTextColor(Color.parseColor("#999999"));
        }else {
            holder.tv_item_status.setTextColor(Color.parseColor("#ff6b3f"));
        }

        String sMoney= CalculationTool.getCaluculationIntUnit(bean.getSalesPrice());//.isEmpty()?"":carEntity.getSalesPrice()+"万";
        holder.tv_item_lable_one_money.setText(sMoney);

        String sDiscountMoney=CalculationTool.getCaluculationIntUnit(bean.getRatePrice());//carEntity.getRatePrice()+"万";
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
            holder.tv_item_lable_one_money.setTextSize(11f);
        }else
        {
            holder.tv_item_lable_two_layout.setVisibility(View.VISIBLE);
            holder.tv_item_lable_two.setText("分销价:");
            holder.tv_item_lable_one.setTextSize(11f);
            holder.tv_item_lable_one_money.setTextSize(11f);
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
                    context.startActivity(new Intent(context,OrderInfo.class).
                            putExtra("orderId",list.get(getPosition()).getId())
                            .putExtra("type","0"));
                }
            });
        }
    }

    public void notifyDate(List<UserOrderEntity>list,String priceType) {
        this.list=list;
        this.priceType=priceType;
        notifyDataSetChanged();

    }
}
