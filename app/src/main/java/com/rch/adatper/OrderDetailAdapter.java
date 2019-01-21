package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lidroid.xutils.util.LogUtils;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.activity.CustomerDetailActivity;
import com.rch.activity.ManageInfoAct;
import com.rch.activity.NewCarInfoAct;
import com.rch.activity.OperOrderAct;
import com.rch.activity.OrderInfo;
import com.rch.activity.TabNewCarInfoAct;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.RoncheUtil;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.entity.CustomerDetailEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户管理预约单适配器
 * Created by Administrator on 2018/7/19.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder> {
    private Context context;

    private List<CustomerDetailEntity.OrderListBean> orderList = new ArrayList<>();

    public OrderDetailAdapter(Context context,List<CustomerDetailEntity.OrderListBean> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    public void notify(List<CustomerDetailEntity.OrderListBean> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_order_msg,  parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if (orderList!=null && orderList.size()>0){
            holder.tv_name.setText(RoncheUtil.getSelfString(orderList.get(position).getContanctName()));//用户姓名
            holder.tv_time.setText(RoncheUtil.getSelfString(orderList.get(position).getCreateTime()));
            if ("1".equals(orderList.get(position).getIfNew())){
                holder.tv_car_status.setTextColor(context.getResources().getColor(R.color.green_bg));
                holder.tv_car_status.setBackgroundResource(R.drawable.green_white_bg);
            }else if ("0".equals(orderList.get(position).getIfNew())){
                holder.tv_car_status.setTextColor(context.getResources().getColor(R.color.money_red));
                holder.tv_car_status.setBackgroundResource(R.drawable.red_white_bg);
            }
            holder.tv_car_status.setText(RoncheUtil.getSelfString(orderList.get(position).getIfNewName()));
            holder.tv_car_deal_status.setText(RoncheUtil.getSelfString(orderList.get(position).getStateName()));
            holder.tv_car_name.setText(""+RoncheUtil.getSelfString(orderList.get(position).getModelName()));
            holder.tv_car_manage.setText(""+RoncheUtil.getSelfString(orderList.get(position).getEnterpriseName()));
            holder.tv_car_date.setText(""+RoncheUtil.getSelfString(orderList.get(position).getConventionTime()));
            holder.tv_car_share_name.setText(""+RoncheUtil.getSelfString(orderList.get(position).getOrginidName()));

            //看车状态：1-等待看车、2-已看车、3-客服确认中、4客服确认失败、5-未看车
            //待看车
            if (orderList.get(position).getState().equals("1")){
                holder.ll_car_cancel.setVisibility(View.GONE);
                holder.ll_car_refuse.setVisibility(View.GONE);
                holder.ll_car_look_date.setVisibility(View.GONE);
                holder.ll_bottom_cus.setVisibility(View.VISIBLE);
            }else if (orderList.get(position).getState().equals("2")){
                holder.ll_car_cancel.setVisibility(View.GONE);
                holder.ll_car_refuse.setVisibility(View.GONE);
                holder.ll_car_look_date.setVisibility(View.VISIBLE);
                holder.ll_bottom_cus.setVisibility(View.GONE);
            }else if (orderList.get(position).getState().equals("3")){
                holder.ll_car_cancel.setVisibility(View.VISIBLE);
                holder.ll_car_refuse.setVisibility(View.GONE);
                holder.ll_car_look_date.setVisibility(View.GONE);
                holder.ll_bottom_cus.setVisibility(View.GONE);
            }else if (orderList.get(position).getState().equals("4")){
                holder.ll_car_cancel.setVisibility(View.VISIBLE);
                holder.ll_car_refuse.setVisibility(View.VISIBLE);
                holder.ll_car_look_date.setVisibility(View.GONE);
                holder.ll_bottom_cus.setVisibility(View.VISIBLE);
            }else if (orderList.get(position).getState().equals("5")){
                holder.ll_car_cancel.setVisibility(View.VISIBLE);
                holder.ll_car_refuse.setVisibility(View.GONE);
                holder.ll_car_look_date.setVisibility(View.GONE);
                holder.ll_bottom_cus.setVisibility(View.GONE);
            }

            holder.tv_car_cancel.setText(""+RoncheUtil.getSelfString(orderList.get(position).getCacelReasonName()));
            holder.tv_car_refuse.setText(""+RoncheUtil.getSelfString(orderList.get(position).getRejectReasonName()));
            holder.tv_car_look_date.setText(""+RoncheUtil.getSelfString(orderList.get(position).getFactTime()));

        }

        holder.tv_cus_status_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(orderList.get(position).getId());
            }
        });
        holder.tv_cus_status_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OperOrderAct.class).putExtra("id",orderList.get(position).getId()));
            }
        });
        holder.ll_order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断新车还是二手车，跳转车辆详情
                if ("1".equals(orderList.get(position).getIfNew())){//跳转新车
                    context.startActivity(new Intent(context, NewCarInfoAct.class).putExtra("id",orderList.get(position).getVehicleId()));
                }else {
                    context.startActivity(new Intent(context, CarDetailActivity.class).putExtra("id",orderList.get(position).getVehicleId()));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String time="";
    private Date startDate;
    private void showDateDialog(final String id) {
        TimePickerView pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int days= (int) TimePareUtil.getDayDiff(TimePareUtil.getCurrentDate("yyyy-MM-dd"),date);
                if(date.before(TimePareUtil.getCurrentDate("yyyy-MM-dd HH:mm"))||days>30){
                    ToastTool.show(context, "请重新选择");
//                    showDateDialog();//再默认
                    time="";
                }else {
                    time = TimePareUtil.getTimeForDate("yyyy-MM-dd HH:mm", date);
                    startDate = TimePareUtil.getDateTimeForTime("yyyy-MM-dd HH:mm", time);
                    //走接口
                    cusHasLooked(id,time);
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .isCenterLabel(false)//年月日不显示在中间
                .isDialog(false)
                .setTitleColor(context.getResources().getColor(R.color.white))
                .setLineSpacingMultiplier(2.0f)
                .gravity(Gravity.CENTER)
                .build();

        pvTime.setKeyBackCancelable(false);
        if(startDate!=null) {
            pvTime.setDate(TimePareUtil.getCalendarForDate(startDate));
        }
        pvTime.show();


    }

    //客户已看车
    private void cusHasLooked(String id,String time) {
        RequestParam param = new RequestParam();
        if (!TextUtils.isEmpty(id))
            param.add("id",id);
        param.add("type","1");//看车
        param.add("factDate",time);

        OkHttpUtils.post(Config.OPER_ORDER_URL, param, new OkHttpCallBack(context,"加载中...") {
            @Override
            public void onSuccess(String data) {
                context.sendBroadcast(new Intent(CustomerDetailActivity.CUSTOMER_DETAIL));
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(context,error);
            }
        });

    }
    public class MyHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_order_item,ll_car_cancel,ll_car_refuse,ll_car_look_date,ll_bottom_cus;
        TextView tv_name,tv_time,tv_car_status,tv_car_deal_status,tv_car_name,tv_car_manage,tv_car_date,tv_car_share_name,tv_car_cancel,tv_cus_status_look,tv_cus_status_cancel,tv_car_refuse,tv_car_look_date;
        public MyHolder(View itemView) {
            super(itemView);
            ll_order_item = (LinearLayout) itemView.findViewById(R.id.ll_order_item);
            ll_car_cancel = (LinearLayout) itemView.findViewById(R.id.ll_car_cancel);
            ll_car_refuse = (LinearLayout) itemView.findViewById(R.id.ll_car_refuse);
            ll_car_look_date = (LinearLayout) itemView.findViewById(R.id.ll_car_look_date);
            ll_bottom_cus = (LinearLayout) itemView.findViewById(R.id.ll_bottom_cus);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_car_status = (TextView) itemView.findViewById(R.id.tv_car_status);
            tv_car_deal_status = (TextView) itemView.findViewById(R.id.tv_car_deal_status);
            tv_car_name = (TextView) itemView.findViewById(R.id.tv_car_name);
            tv_car_manage = (TextView) itemView.findViewById(R.id.tv_car_manage);
            tv_car_date = (TextView) itemView.findViewById(R.id.tv_car_date);
            tv_car_share_name = (TextView) itemView.findViewById(R.id.tv_car_share_name);
            tv_car_cancel = (TextView) itemView.findViewById(R.id.tv_car_cancel);
            tv_cus_status_look = (TextView) itemView.findViewById(R.id.tv_cus_status_look);
            tv_cus_status_cancel = (TextView) itemView.findViewById(R.id.tv_cus_status_cancel);
            tv_car_refuse = (TextView) itemView.findViewById(R.id.tv_car_refuse);
            tv_car_look_date = (TextView) itemView.findViewById(R.id.tv_car_look_date);
        }

    }

}
