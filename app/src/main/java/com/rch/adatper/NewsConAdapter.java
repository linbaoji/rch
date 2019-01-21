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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.R;
import com.rch.activity.NewInofActivity;
import com.rch.common.GlideUtils;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.entity.HomeDateBean;
import com.rch.entity.RchNewsBean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class NewsConAdapter extends RecyclerView.Adapter<NewsConAdapter.MyHolder>{
    private Context context;
    private List<RchNewsBean> list;
    public NewsConAdapter(Context context, List<RchNewsBean> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyData(List<RchNewsBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public NewsConAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newscon, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(NewsConAdapter.MyHolder holder, int position) {
         holder.setPostion(position);
         GlideUtils.showImg(context,list.get(position).getImage(),R.mipmap.car_emp, DiskCacheStrategy.ALL,holder.iv_newsban);
         holder.tv_title.setText(list.get(position).getTitle());
         holder.tv_cpr.setText(list.get(position).getSource());
         Date date= TimePareUtil.getDateTimeForTime("yyyy-MM-dd",list.get(position).getDeploydate());
         holder.tv_time.setText(TimePareUtil.getTimeForDate("yyyy-MM-dd",date));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_cpr,tv_time;
        ImageView iv_newsban;
        LinearLayout ll_item;
        int dex;
        public MyHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_cpr= (TextView) itemView.findViewById(R.id.tv_cpr);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            iv_newsban= (ImageView) itemView.findViewById(R.id.iv_newban);
            ll_item= (LinearLayout) itemView.findViewById(R.id.ll_item);

            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //是否外链：0-否，1-是
                    if (list.get(dex).getDisplaytype().equals("1")) {
                        context.startActivity(new Intent(context, NewInofActivity.class).putExtra(NewInofActivity.NOTE_URL, list.get(dex).getTitleouturl()).putExtra("from", "2"));
                    }else {
                        context.startActivity(new Intent(context, NewInofActivity.class).putExtra(NewInofActivity.ID, list.get(dex).getId()).putExtra("from", "2"));
                    }
                    }
            });
        }

        public void setPostion(int dex){
            this.dex=dex;
        }
    }
}
