package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyHolder>{
    private Context context;
    private List<FeedbackEntity> list = new ArrayList<>();

    public FeedAdapter(Context context,List<FeedbackEntity> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyData(List<FeedbackEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public FeedAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(FeedAdapter.MyHolder holder, int position) {
        holder.tv_time.setText("提交日期："+list.get(position).getCreateTime());
        //0未处理1已处理
        if (list.get(position).getStatus().equals("0")){
            holder.tv_state.setText("未处理");
            holder.tv_state.setBackgroundResource(R.drawable.gray_bg_radio);
        }else {
            holder.tv_state.setText("已处理");
            holder.tv_state.setBackground(context.getResources().getDrawable(R.drawable.orng_bg_radio));
        }

        holder.tv_fk.setText("反馈问题：【"+list.get(position).getFbtypeName()+"】"+list.get(position).getSuggestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_state,tv_fk;

        public MyHolder(View itemView) {
            super(itemView);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            tv_state= (TextView) itemView.findViewById(R.id.tv_state);
            tv_fk= (TextView) itemView.findViewById(R.id.tv_fk);
            tv_state.setTextColor(context.getResources().getColor(R.color.white));

        }
    }
}
