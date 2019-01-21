package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.RoncheUtil;
import com.rch.entity.CustomerDetailEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理适配器
 * Created by Administrator on 2018/7/19.
 */

public class CustomerDetailAdapter extends RecyclerView.Adapter<CustomerDetailAdapter.MyHolder> {
    private Context context;
    private List<CustomerDetailEntity.FollowListBean> followList = new ArrayList<>();

    public CustomerDetailAdapter(Context context,List<CustomerDetailEntity.FollowListBean> followList) {
        this.context = context;
        this.followList = followList;
    }


    public void notify(List<CustomerDetailEntity.FollowListBean> followList) {
        this.followList = followList;
        notifyDataSetChanged();
    }

    @Override
    public CustomerDetailAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(  R.layout.item_followup_record, parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomerDetailAdapter.MyHolder holder, final int position) {
        if (followList!=null && followList.size()>0){
            holder.tv_info.setText(RoncheUtil.getSelfString(followList.get(position).getFollowDate())+" | "+RoncheUtil.getSelfString(followList.get(position).getFollowWayName())+" | "+RoncheUtil.getSelfString(followList.get(position).getUserName()));
            holder.tv_cus_level.setText("客戶级别："+RoncheUtil.getSelfString(followList.get(position).getCustomerLevelName()));
            holder.tv_cus_result.setText("跟进结果："+RoncheUtil.getSelfString(followList.get(position).getFollowResultName()));
            holder.tv_cus_content.setText("跟进内容："+RoncheUtil.getSelfString(followList.get(position).getNotes()));
            holder.tv_next_time.setText("下次回访时间："+RoncheUtil.getSelfString(followList.get(position).getNextDate()));
        }
    }


    @Override
    public int getItemCount() {
        return followList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_info,tv_cus_level,tv_cus_result,tv_cus_content,tv_next_time;
        public MyHolder(View itemView) {
            super(itemView);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            tv_cus_level = (TextView) itemView.findViewById(R.id.tv_cus_level);
            tv_cus_result = (TextView) itemView.findViewById(R.id.tv_cus_result);
            tv_cus_content = (TextView) itemView.findViewById(R.id.tv_cus_content);
            tv_next_time = (TextView) itemView.findViewById(R.id.tv_next_time);
        }

    }

}
