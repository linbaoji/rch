package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rch.R;
import com.rch.custom.CommonView;
import com.rch.entity.CustomerManagerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理适配器
 * Created by Administrator on 2018/7/19.
 */

public class CarDealerAdapter extends RecyclerView.Adapter<CarDealerAdapter.MyHolder> {
    private OnItemClick onItemClick;
    private Context context;
    private List<CustomerManagerEntity.EnterListBean> list=new ArrayList<>();

    public CarDealerAdapter(Context context, List<CustomerManagerEntity.EnterListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void updateData(List<CustomerManagerEntity.EnterListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_car_dealer, null);
        MyHolder holder = new MyHolder(view,onItemClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.cv_car_dealer.setDesText(list.get(position).getEnterpriseName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClick mListener;
        CommonView cv_car_dealer;
        public MyHolder(View itemView,OnItemClick onItemClick) {
            super(itemView);
            cv_car_dealer = (CommonView) itemView.findViewById(R.id.cv_car_dealer);
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
                mListener.onItemClick(v, getPosition());
            }

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
