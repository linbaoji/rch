package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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
import com.rch.common.CalculationTool;
import com.rch.common.StrSplitTool;
import com.rch.common.TimeSplitTool;
import com.rch.entity.CarEntity;
import com.rch.entity.CollectionBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.MyHolder>{
    private Context context;
    private List<CollectionBean> list;
    private String priceType;

    public CollectAdapter(Context context,List<CollectionBean>list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.car_list_adapter,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_text.setText("批发价:");
        holder.tv_name.setText(list.get(position).getVehicleFullName());

        String year= TimeSplitTool.getYM(list.get(position).getRegistrationTime());//年月

        String lime="/0公里";
        int iShowMileage=list.get(position).getShowMileage().isEmpty()?0: Integer.parseInt(list.get(position).getShowMileage());
        if(iShowMileage<10000)
            lime = "/"+String.valueOf(iShowMileage)+"公里";
        else
            lime = "/"+StrSplitTool.retainOneNumber(String.valueOf(iShowMileage))+"万公里";

        holder.xh.setText(year+lime);//描述
        holder.tv_mdj.setText(CalculationTool.getCaluculationIntUnit(list.get(position).getSalesPrice()));
        holder.tv_pfj.setText(CalculationTool.getCaluculationIntUnit(list.get(position).getRatePrice()));
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.car_emp);
        Glide.with(context).load(list.get(position).getVehicleImagePath()).apply(options).into(holder.iv_leftlogo);


        if(priceType.equals("0")) {
            holder.tvOrdinary.setTextSize(12f);
            holder.money.setTextSize(15f);
            holder.discountLayout.setVisibility(View.GONE);
        }
        else if(priceType.equals("1"))
        {
            holder.tvOrdinary.setTextSize(11f);
            holder.money.setTextSize(11f);
            holder.discountLayout.setVisibility(View.VISIBLE);
//            holder.tvStore.setText("企业价:");
            holder.tvStore.setText("批发价:");
        }else
        {
            holder.tvOrdinary.setTextSize(11f);
            holder.money.setTextSize(11f);
            holder.discountLayout.setVisibility(View.VISIBLE);
            holder.tvStore.setText("分销价:");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_leftlogo;
        TextView tv_name,xh,tv_mdj,tv_pfj,tv_text,tvStore,money,tvOrdinary;
        LinearLayout ll_shop,discountLayout;
        public MyHolder(View itemView) {
            super(itemView);
            iv_leftlogo= (ImageView) itemView.findViewById(R.id.shop_list_adapter_img);//图片
            tv_name= (TextView) itemView.findViewById(R.id.shop_list_adapter_title);//标题
            xh= (TextView) itemView.findViewById(R.id.shop_list_adapter_desc);//描述
            tv_mdj= (TextView) itemView.findViewById(R.id.shop_list_adapter_money);//门店价格
            tv_pfj= (TextView) itemView.findViewById(R.id.shop_list_adapter_area);//批发价
            tv_text= (TextView) itemView.findViewById(R.id.shop_list_adapter_store);//描述企业价格
            ll_shop= (LinearLayout) itemView.findViewById(R.id.shop_list_adapter_layout);
            discountLayout= (LinearLayout) itemView.findViewById(R.id.shop_list_adapter_discount_layout);
            tvStore= (TextView) itemView.findViewById(R.id.shop_list_adapter_store);
            money= (TextView) itemView.findViewById(R.id.shop_list_adapter_money);
            tvOrdinary= (TextView) itemView.findViewById(R.id.shop_list_adapter_ordinary);

            ll_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CollectionBean carEntity=list.get(getPosition());
                    String id=carEntity.getId();
                    String version=carEntity.getVersion();
                    context.startActivity(new Intent(context, CarDetailActivity.class).putExtra("id", id).putExtra("version", version).putExtra("orderid", ""));

                }
            });
        }
    }

    public void update(List<CollectionBean>datalist,String priceType) {
        this.list=datalist;
        this.priceType=priceType;
        notifyDataSetChanged();

    }


}
