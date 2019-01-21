package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.GsonUtils;
import com.rch.common.SpUtils;
import com.rch.custom.CityLayout;
import com.rch.entity.CityInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/13.
 */

public class CitySelectAdapter extends RecyclerView.Adapter<CitySelectAdapter.MyHolderView> {

    Context context;
    List<CityInfoEntity> cityList;
    onSelCityNameInterface onSelCityNameInterface;

    String locationCity="";
    boolean isHideHead=false;


    public void setOnSelCityNameInterface(onSelCityNameInterface onSelCityNameInterface) {
        this.onSelCityNameInterface = onSelCityNameInterface;
    }

    public CitySelectAdapter(Context context, List<CityInfoEntity> cityList)
    {
        this.context=context;
        this.cityList=cityList;

    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.city_adapter,parent,false);
        MyHolderView myHolderView=new MyHolderView(view);
        return myHolderView;
    }


    @Override
    public void onBindViewHolder(MyHolderView holder, int position) {
        final CityInfoEntity cityEntity=cityList.get(position);
        holder.getPos(position);

        holder.name.setText(cityEntity.getCityName());
        holder.line.setVisibility(View.VISIBLE);
        holder.chat.setVisibility(View.GONE);

        if(cityEntity.getIdentityCount())
            holder.line.setVisibility(View.GONE);

        if(cityEntity.getIdentity())
        {
            holder.chat.setText(cityEntity.getCityLetter());
            holder.chat.setVisibility(View.VISIBLE);
        }
        if (cityEntity.getSelected()){
            holder.iv_check.setVisibility(View.VISIBLE);
        }else {
            holder.iv_check.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }



    public class MyHolderView extends RecyclerView.ViewHolder {
        TextView chat,gps,name,fire;
        LinearLayout head,resetGps,fireLayout,item;
        CityLayout cityLayout;
        View line;
        ImageView iv_check;
        int dex;

        public MyHolderView(View itemView) {
            super(itemView);
            chat= (TextView) itemView.findViewById(R.id.city_adapter_chat);//Abcd
            name= (TextView) itemView.findViewById(R.id.city_adapter_name);//城市名称
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);//是否选中
            line=  itemView.findViewById(R.id.city_adapter_line);//最后一根线


            cityLayout= (CityLayout) itemView.findViewById(R.id.city_adapter_city_layout);
            head= (LinearLayout) itemView.findViewById(R.id.city_adapter_head);
            fireLayout= (LinearLayout) itemView.findViewById(R.id.city_adapter_fire_Layout);
            gps= (TextView) itemView.findViewById(R.id.city_adapter_gps);
            fire= (TextView) itemView.findViewById(R.id.city_adapter_fire);
            resetGps= (LinearLayout) itemView.findViewById(R.id.city_adapter_reset_gps);
            item= (LinearLayout) itemView.findViewById(R.id.city_adapter_item);


            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                      if(cityList.get(dex).getSelected()) {
//                          cityList.get(dex).setSelected(false);
//                      }else {
//                          cityList.get(dex).setSelected(true);
//                      }
//                      notifyDataSetChanged();
                     if(dex==0){//选中时候要
                         if(cityList.get(0).getSelected()){
                             cityList.get(0).setSelected(false);
                         }else {
                             for (int i = 0; i < cityList.size(); i++) {
                                 if (i == 0) {
                                     cityList.get(i).setSelected(true);
                                 } else {
                                     cityList.get(i).setSelected(false);
                                 }
                             }
                         }

                     }else if(dex==1){//选中上海
                        if(cityList.get(1).getSelected()){
                            cityList.get(1).setSelected(false);
                            for (int t=0;t<cityList.size();t++){
                                if(cityList.get(t).getCityName().equals("上海")){
                                    cityList.get(t).setSelected(false);
                                }
                            }
                        }else {
                            cityList.get(0).setSelected(false);
                            cityList.get(1).setSelected(true);
                            for (int t=0;t<cityList.size();t++){
                                if(cityList.get(t).getCityName().equals("上海")){
                                    cityList.get(t).setSelected(true);
                                }
                            }
                        }
                    }else {
                         if(cityList.get(dex).getCityName().equals("上海")){
                             if (cityList.get(dex).getSelected()) {
                                 cityList.get(dex).setSelected(false);
                                 cityList.get(1).setSelected(false);
                             } else {
                                 cityList.get(0).setSelected(false);
                                 cityList.get(dex).setSelected(true);
                                 cityList.get(1).setSelected(true);
                             }
                         }else {
                             if (cityList.get(dex).getSelected()) {
                                 cityList.get(dex).setSelected(false);
                             } else {
                                 cityList.get(0).setSelected(false);
                                 cityList.get(dex).setSelected(true);
                             }
                         }
                     }

                     notifyDataSetChanged();

                    if(onSelCityNameInterface!=null)
                        onSelCityNameInterface.onItem(cityList);
                }
            });
        }

        /**
         * 把位置记录好
         * @param position
         */
        public void getPos(int position){
            this.dex=position;
        }

    }


    public void updateCityData(List<CityInfoEntity> list)
    {
        this.cityList=list;
        isHideHead=false;
        notifyDataSetChanged();
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        if(section.equals("热"))
            return 0;
        for (int i = 0; i < getItemCount(); i++) {
            CityInfoEntity cityEntity = cityList.get(i);
            if (cityEntity.getCityLetter().equalsIgnoreCase(section)&&cityEntity.getIdentity()) {
                return i;
            }
        }
        return -1;
    }


    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
        isHideHead=false;
        notifyDataSetChanged();
    }

    public interface onSelCityNameInterface
    {
        public void onItem(List<CityInfoEntity>list);
        public void onResetGps();
        public void onGpsName(String gpsGityName);
    }
}
