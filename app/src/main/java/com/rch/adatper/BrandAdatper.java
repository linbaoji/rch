package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.entity.BrandEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class BrandAdatper extends RecyclerView.Adapter<BrandAdatper.MyHolder> {

    Context context;
    List<BrandEntity> list;

    onSelBrandNameInterface onSelBrandNameInterface;

    int type=1;

    public void setOnSelBrandNameInterface(BrandAdatper.onSelBrandNameInterface onSelBrandNameInterface) {
        this.onSelBrandNameInterface = onSelBrandNameInterface;
    }

    public BrandAdatper(Context context, List<BrandEntity> list,int type)
    {
        this.context=context;
        this.list=list;
        this.type=type;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.brand_adapter,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        BrandEntity brandEntity=list.get(position);

        holder.name.setText(brandEntity.getBrandName());
        if(brandEntity.getBrandImagePath().isEmpty())
            holder.icon.setVisibility(View.GONE);
        else {
            holder.icon.setVisibility(View.VISIBLE);
            Glide.with(context).load(brandEntity.getBrandImagePath()).into(holder.icon);
        }

        holder.line.setVisibility(View.VISIBLE);
        holder.chat.setVisibility(View.GONE);

        if(brandEntity.getIdentityCount())
            holder.line.setVisibility(View.GONE);

        if(brandEntity.getIdentity())
        {
            holder.chat.setText(brandEntity.getFirstLetter());
            holder.chat.setVisibility(View.VISIBLE);
        }

        /*品牌类型----字母检索显示*，不显示item*/
        /*if(type==2)
        {
            if(brandEntity.getFirstLetter().equals("*")) {
                holder.chat.setVisibility(View.GONE);
                holder.item.setVisibility(View.GONE);
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView chat,name;
        ImageView icon;
        View line;
        LinearLayout item;
        public MyHolder(View itemView) {
            super(itemView);
            chat= (TextView) itemView.findViewById(R.id.brand_adapter_chat);
            name= (TextView) itemView.findViewById(R.id.brand_adapter_name);
            icon= (ImageView) itemView.findViewById(R.id.brand_adapter_icon);
            line=  itemView.findViewById(R.id.brand_adapter_line);
            item= (LinearLayout) itemView.findViewById(R.id.brand_adapter_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrandEntity entity = list.get(getPosition());
                    if (onSelBrandNameInterface != null)
                        onSelBrandNameInterface.onItem(entity);
                }
            });

        }
    }

    public void updateBrandData(List<BrandEntity> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = ((BrandEntity)list.get(i)).getFirstLetter();
            if (sortStr.equalsIgnoreCase(section)){
                return i;
            }
        }
        return -1;
    }

    public interface onSelBrandNameInterface
    {
        public void onItem(BrandEntity entity);

    }
}
