package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.activity.OrderResultActivity;
import com.rch.common.RoncheUtil;
import com.rch.entity.CustomerDetailEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理预约单适配器
 * Created by Administrator on 2018/7/19.
 */

public class InquiryDetailAdapter extends RecyclerView.Adapter<InquiryDetailAdapter.MyHolder> {
    private Context context;
    private List<CustomerDetailEntity.QuerySaleListBean> queryList = new ArrayList<>();

    public void notify(List<CustomerDetailEntity.QuerySaleListBean> queryList){
        this.queryList = queryList;
        notifyDataSetChanged();
    }
    public InquiryDetailAdapter(Context context,List<CustomerDetailEntity.QuerySaleListBean> queryList) {
        this.context = context;
        this.queryList = queryList;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_inquiry_msg,  parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        if (queryList!=null && queryList.size()>0){
            holder.tv_name.setText(RoncheUtil.getSelfString(queryList.get(position).getContanctName()));//用户姓名
            holder.tv_time.setText(RoncheUtil.getSelfString(queryList.get(position).getCreateTime()));
            if ("1".equals(queryList.get(position).getIfNew())){
                holder.tv_car_status.setTextColor(context.getResources().getColor(R.color.green_bg));
                holder.tv_car_status.setBackgroundResource(R.drawable.green_white_bg);
            }else if ("0".equals(queryList.get(position).getIfNew())){
                holder.tv_car_status.setTextColor(context.getResources().getColor(R.color.money_red));
                holder.tv_car_status.setBackgroundResource(R.drawable.red_white_bg);
            }
            holder.tv_car_deal_status.setText(RoncheUtil.getSelfString(queryList.get(position).getStatusName()));
            holder.tv_car_status.setText(RoncheUtil.getSelfString(queryList.get(position).getIfNewName()));
            holder.tv_car_name.setText(""+RoncheUtil.getSelfString(queryList.get(position).getModelName()));
            holder.tv_car_manage.setText(""+RoncheUtil.getSelfString(queryList.get(position).getEnterpriseName()));
            holder.tv_mendian_price.setText(""+RoncheUtil.getSelfString(queryList.get(position).getSalesPriceMin()+"-"+queryList.get(position).getSalesPriceMax()+"万"));
            holder.tv_pifa_price.setText(""+RoncheUtil.getSelfString(queryList.get(position).getTradePriceMin()+"-"+queryList.get(position).getTradePriceMax()+"万"));
            if (!TextUtils.isEmpty(queryList.get(position).getOperatorName())) {
                holder.rl_not_deal.setVisibility(View.GONE);
                holder.ll_deal_person.setVisibility(View.VISIBLE);
                holder.ll_deal_result.setVisibility(View.VISIBLE);
                holder.tv_deal_person.setText("" + RoncheUtil.getSelfString(queryList.get(position).getOperatorName()));
                holder.tv_deal_result.setText("" + RoncheUtil.getSelfString(queryList.get(position).getNotes()));
            }else {
                holder.rl_not_deal.setVisibility(View.VISIBLE);
                holder.ll_deal_person.setVisibility(View.GONE);
                holder.ll_deal_result.setVisibility(View.GONE);
            }
        }

        holder.tv_deal_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OrderResultActivity.class).putExtra("id",queryList.get(position).getId()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return queryList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_deal_person,ll_deal_result;
        RelativeLayout rl_not_deal;
        TextView tv_name,tv_time,tv_car_status,tv_car_deal_status,tv_car_name,tv_car_manage,tv_mendian_price,tv_pifa_price,tv_deal_info,tv_deal_status,tv_deal_person,tv_deal_result;
        public MyHolder(View itemView) {
            super(itemView);
            ll_deal_person = (LinearLayout) itemView.findViewById(R.id.ll_deal_person);
            ll_deal_result = (LinearLayout) itemView.findViewById(R.id.ll_deal_result);
            rl_not_deal = (RelativeLayout) itemView.findViewById(R.id.rl_not_deal);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_car_status = (TextView) itemView.findViewById(R.id.tv_car_status);
            tv_car_deal_status = (TextView) itemView.findViewById(R.id.tv_car_deal_status);
            tv_car_name = (TextView) itemView.findViewById(R.id.tv_car_name);
            tv_car_manage = (TextView) itemView.findViewById(R.id.tv_car_manage);
            tv_mendian_price = (TextView) itemView.findViewById(R.id.tv_mendian_price);
            tv_pifa_price = (TextView) itemView.findViewById(R.id.tv_pifa_price);
            tv_deal_info = (TextView) itemView.findViewById(R.id.tv_deal_info);
            tv_deal_status = (TextView) itemView.findViewById(R.id.tv_deal_status);
            tv_deal_person = (TextView) itemView.findViewById(R.id.tv_deal_person);
            tv_deal_result = (TextView) itemView.findViewById(R.id.tv_deal_result);
        }

    }

}
