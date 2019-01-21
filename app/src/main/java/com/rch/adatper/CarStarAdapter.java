package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.activity.NewCarInfoAct;
import com.rch.common.CalculationTool;
import com.rch.common.GlideUtils;
import com.rch.common.StrSplitTool;
import com.rch.common.TimeSplitTool;
import com.rch.entity.CarEntity;
import com.rch.entity.CollectionBean;

import java.util.List;

/**
 * Created by Administrator on 2018/12/10.
 */

public class CarStarAdapter extends RecyclerView.Adapter<CarStarAdapter.MyHolder>{
    private Context context;
    private List<CarEntity> list;
    private int starType = 0;


    public CarStarAdapter(Context context,List<CarEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_home_star_module,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final CarEntity carEntity = list.get(position);
        GlideUtils.showImg(context,list.get(position).getVehicleImage(),holder.iv_star_car_img);
        holder.tv_star_car_name.setText(list.get(position).getVehicleFullName());
        if (!TextUtils.isEmpty(carEntity.getSalesPriceMin()) && !TextUtils.isEmpty(carEntity.getSalesPriceMax())) {
            if (carEntity.getSalesPriceMin().equals(carEntity.getSalesPriceMax())) {
//                holder.tv_star_car_price.setText(isNull(CalculationTool.getCaluculationIntUnit(carEntity.getSalesPriceMin())) + "万");
                holder.tv_star_car_price.setText(isNull(carEntity.getSalesPriceMin()) + "万");
            } else {
//                holder.tv_star_car_price.setText(isNull(CalculationTool.getCaluculationIntUnit(carEntity.getSalesPriceMin())) + "万" +"-"+ isNull(CalculationTool.getCaluculationIntUnit(carEntity.getSalesPriceMax())) + "万");
                holder.tv_star_car_price.setText(isNull(carEntity.getSalesPriceMin()) + "万" +"-"+ isNull(carEntity.getSalesPriceMax()) + "万");
            }
        } else {
//            holder.tv_star_car_price.setText(isNull(CalculationTool.getCaluculationIntUnit(carEntity.getSalesPriceMin())) + "万" +"-"+ isNull(CalculationTool.getCaluculationIntUnit(carEntity.getSalesPriceMax())) + "万");
            holder.tv_star_car_price.setText(isNull(carEntity.getSalesPriceMin()) + "万" +"-"+ isNull(carEntity.getSalesPriceMax()) + "万");
        }

        holder.ll_home_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starType==0) {//新车
                    context.startActivity(new Intent(context, NewCarInfoAct.class).putExtra("id", carEntity.getId()));
                }else {//旧车
                    context.startActivity(new Intent(context, CarDetailActivity.class).putExtra("id", carEntity.getId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list!=null && list.size()!=0)
            return list.size()>6?6:list.size();
        else
            return 0;
    }


    private String isNull(String tex) {
        if(!TextUtils.isEmpty(tex)){
            return tex;
        }else {
            return "--";
        }
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_star_car_img;
        TextView tv_star_car_name,tv_star_car_price;
        LinearLayout ll_home_star;
        public MyHolder(View itemView) {
            super(itemView);
            iv_star_car_img = (ImageView) itemView.findViewById(R.id.iv_star_car_img);
            tv_star_car_name = (TextView) itemView.findViewById(R.id.tv_star_car_name);
            tv_star_car_price = (TextView) itemView.findViewById(R.id.tv_star_car_price);
            ll_home_star = (LinearLayout) itemView.findViewById(R.id.ll_home_star);
        }
    }

    public void updateData(List<CarEntity> list,int starType){
        this.list = list;
        this.starType = starType;
        notifyDataSetChanged();
    }
}
