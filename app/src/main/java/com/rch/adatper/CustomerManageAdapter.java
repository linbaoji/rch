package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.RoncheUtil;
import com.rch.common.ToastTool;
import com.rch.custom.MyAlertDialog;
import com.rch.entity.CustomerManagerEntity;
import com.rch.entity.EmployBean;
import com.rch.entity.UserInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理适配器
 * Created by Administrator on 2018/7/19.
 */

public class CustomerManageAdapter extends RecyclerView.Adapter<CustomerManageAdapter.MyHolder> {
    private OnItemClick onItemClick;
    private Context context;
    private List<CustomerManagerEntity.UserListBean> customerManagerList = new ArrayList<>();

    public CustomerManageAdapter(Context context,List<CustomerManagerEntity.UserListBean> customerManagerList) {
        this.context = context;
        this.customerManagerList = customerManagerList;
    }
    public void updateData(List<CustomerManagerEntity.UserListBean> customerManagerList){
        this.customerManagerList = customerManagerList;
        notifyDataSetChanged();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_employees, null);
        MyHolder holder = new MyHolder(view,onItemClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tv_gly.setVisibility(View.GONE);
        holder.tv_yc.setText("");
        holder.setPostion(position);
        if (customerManagerList!=null && customerManagerList.size()>0){
            holder.tv_name.setText(RoncheUtil.getSelfString(customerManagerList.get(position).getUserName()));
            holder.tv_phone.setText(RoncheUtil.getSelfString(customerManagerList.get(position).getMobile()));
            holder.tv_yc.setText(RoncheUtil.getSelfString(customerManagerList.get(position).getFollowResultName()));
            // 跟进状态名称 ：1-成功销售、2-预订车辆、3-到店看车、4-销售失败、5-约定下次跟进
            if (customerManagerList.get(position).getFollowResult().equals("4")){
                holder.tv_yc.setTextColor(context.getResources().getColor(R.color.failure));
            }else {
                holder.tv_yc.setTextColor(context.getResources().getColor(R.color.gray_3));
            }

        }
    }


    @Override
    public int getItemCount() {
        return customerManagerList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClick mListener;
        TextView tv_gly, tv_cy, tv_yc, tv_name, tv_phone;
        ImageView iv_sex;
        int dex;

        public MyHolder(View itemView,OnItemClick onItemClick) {
            super(itemView);
            tv_gly = (TextView) itemView.findViewById(R.id.tv_gly);
            tv_cy = (TextView) itemView.findViewById(R.id.tv_cy);
            tv_yc = (TextView) itemView.findViewById(R.id.tv_yc);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            this.mListener = onItemClick;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
//                mListener.onItemClick(v, getPosition());
                mListener.onItemClick(v, dex);
            }

        }

        public void setPostion(int i){
            this.dex=i;
        }


    }


    public interface OnItemClick{
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param onItemClick
     */
    public void setOnItemClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
