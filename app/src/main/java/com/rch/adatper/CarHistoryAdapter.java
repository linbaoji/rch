package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rch.R;
import com.rch.activity.MyAptAct;
import com.rch.entity.CarEntity;
import com.rch.entity.UserOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CarHistoryAdapter extends RecyclerView.Adapter<CarHistoryAdapter.MyHolder> {

    Context context;
    private List<UserOrderEntity>list;

    public CarHistoryAdapter(Context context)
    {
        this.context=context;
    }

    public CarHistoryAdapter(Context context, List<UserOrderEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_history_adapter,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        UserOrderEntity bean=list.get(position);
        holder.tv_time.setText(bean.getCreateTime());
        holder.tv_title.setText(bean.getVehicleFullName());
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.banner);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(bean.getVehicleImagePath()).apply(options).into(holder.iv_log);
        holder.tv_color.setText(bean.getVehicleColorName());//差白色字段；
        holder.tv_state.setText(bean.getOrderStateName());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_title,tv_color,tv_state;
        ImageView iv_log;
        LinearLayout ll_item;

        public MyHolder(View itemView) {
            super(itemView);
            tv_time= (TextView) itemView.findViewById(R.id.car_history_time);
            tv_title= (TextView) itemView.findViewById(R.id.car_history_title);
            tv_color= (TextView) itemView.findViewById(R.id.car_history_color);
            tv_state= (TextView) itemView.findViewById(R.id.tv_state);
            iv_log= (ImageView) itemView.findViewById(R.id.car_history_img);
            ll_item= (LinearLayout) itemView.findViewById(R.id.ll_item);

            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserOrderEntity bean=list.get(getPosition());
                    Intent intent=new Intent(context,MyAptAct.class);
                    intent.putExtra("userOrder",bean.getId());
                    context.startActivity(intent);

                }
            });
        }
    }


    public void updateShopListData(List<UserOrderEntity> list) {
        this.list=list;
        notifyDataSetChanged();
    }
}
