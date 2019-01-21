package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarSeriesEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */

public class SellBrandAdapter extends RecyclerView.Adapter<SellBrandAdapter.MyHolder> {

    Context context;
    List<CarSeriesEntity> list;



    public SellBrandAdapter(Context context, List<CarSeriesEntity> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sell_brand_adapter,parent,false);
        SellBrandAdapter.MyHolder myHolder=new SellBrandAdapter.MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CarSeriesEntity brandEntity = list.get(position);

        holder.name.setText(brandEntity.getModelName());

        holder.line.setVisibility(View.VISIBLE);

        if (brandEntity.getIdentityCount())
            holder.line.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name;
        View line;
        LinearLayout item;

        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.sell_adapter_text);
            line = itemView.findViewById(R.id.sell_adapter_line);
            item = (LinearLayout) itemView.findViewById(R.id.sell_adapter_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarSeriesEntity entity = list.get(getPosition());
                    if (selSellCarName != null)
                        selSellCarName.onItem(entity);
                }
            });

        }
    }

    public void updateBrandData(List<CarSeriesEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface selSellCarName{
        public void onItem(CarSeriesEntity entity);
    }

    selSellCarName selSellCarName;

    public void setSelSellCarName(SellBrandAdapter.selSellCarName selSellCarName) {
        this.selSellCarName = selSellCarName;
    }
}