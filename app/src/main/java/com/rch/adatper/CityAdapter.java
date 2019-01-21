package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.GsonUtils;
import com.rch.common.SpUtils;
import com.rch.custom.CityLayout;
import com.rch.entity.BrandEntity;
import com.rch.entity.CityEntity;
import com.rch.entity.CityInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyHolderView> {

    Context context;
    List<CityInfoEntity> cityList;
    onSelCityNameInterface onSelCityNameInterface;
    String locationCity="";
    boolean isHideHead=false;


    public void setOnSelCityNameInterface(CityAdapter.onSelCityNameInterface onSelCityNameInterface) {
        this.onSelCityNameInterface = onSelCityNameInterface;
    }

    public CityAdapter(Context context, List<CityInfoEntity> cityList)
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

        Log.e("position",String.valueOf(position));
        holder.cityLayout.removeAllViews();
        //if(!isHideHead)
        if(position==0)
        {
            holder.head.setVisibility(View.VISIBLE);
            holder.gps.setText(locationCity);
            isHideHead=true;
            initFireCity(holder.fireLayout,holder.cityLayout);
        }
        else {
            holder.head.setVisibility(View.GONE);
            holder.fireLayout.setVisibility(View.GONE);
        }
        holder.head.setVisibility(View.GONE);

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
      /*  holder.cityLayout.initData(cityEntity.getList());
        holder.cityLayout.setOnSelCity(new CityLayout.onSelCity() {
            @Override
            public void onItem(CityInfoEntity entity) {
                if(onSelCityNameInterface!=null)
                    onSelCityNameInterface.onItem(entity);
            }
        });*/

      if(!locationCity.equals("定位失败")) {
          holder.gps.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (onSelCityNameInterface != null)
                      onSelCityNameInterface.onGpsName(locationCity);
              }
          });
      }

        holder.resetGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelCityNameInterface!=null)
                    onSelCityNameInterface.onResetGps();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
        isHideHead=false;
        notifyDataSetChanged();
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
            chat= (TextView) itemView.findViewById(R.id.city_adapter_chat);
            cityLayout= (CityLayout) itemView.findViewById(R.id.city_adapter_city_layout);
            head= (LinearLayout) itemView.findViewById(R.id.city_adapter_head);
            fireLayout= (LinearLayout) itemView.findViewById(R.id.city_adapter_fire_Layout);
            gps= (TextView) itemView.findViewById(R.id.city_adapter_gps);
            fire= (TextView) itemView.findViewById(R.id.city_adapter_fire);
            resetGps= (LinearLayout) itemView.findViewById(R.id.city_adapter_reset_gps);
            line=  itemView.findViewById(R.id.city_adapter_line);
            item= (LinearLayout) itemView.findViewById(R.id.city_adapter_item);
            name= (TextView) itemView.findViewById(R.id.city_adapter_name);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                      if(cityList.get(dex).getSelected()) {
//                          cityList.get(dex).setSelected(false);
//                      }else {
//                          cityList.get(dex).setSelected(true);
//                      }
//                      notifyDataSetChanged();
                    if(onSelCityNameInterface!=null)
                        onSelCityNameInterface.onItem(cityList.get(dex),getPosition());
                }
            });
        }

        /**
         * 把位置记录好
         * @param position
         */
        public void getPos(int position){
            dex=position;

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




    List<CityInfoEntity> fireCityList=new ArrayList<>();
    private CityLayout city_layout;
    private void initFireCity(LinearLayout fireLayout,CityLayout cityLayout)
    {

        CityInfoEntity cityInfoEntityLocal=null;
        String loacationCityList = SpUtils.getLoacationCityList(context);
        List<CityInfoEntity> cityInfoEntityList = GsonUtils.json2List(loacationCityList);
        if (cityInfoEntityList!=null && cityInfoEntityList.size() > 0){
            for (int i=0;i<cityInfoEntityList.size();i++){
                if (cityInfoEntityList.get(i).getTotal().equals("0")){//热门城市 只有可能是其中一个
                    cityInfoEntityLocal = cityInfoEntityList.get(i);
                }
            }
        }

        city_layout = cityLayout;
//        fireCityList=null;
//        fireCityList=new ArrayList<>();
//        fireCityList.clear();
        CityInfoEntity cityInfoEntity=null;
        cityInfoEntity=new CityInfoEntity();
        cityInfoEntity.setIdentity(true);
        cityInfoEntity.setTotal("0");
        cityInfoEntity.setCityLetter("热门城市");
        cityInfoEntity.setIdentityCount(false);
        cityInfoEntity.setCityName("全国");
        cityInfoEntity.setCity("");
        if (cityInfoEntityLocal!=null && cityInfoEntityLocal.getCityName().equals("全国"))
            cityInfoEntity.setSelected(true);
        boolean isAdded = false;//是否已经添加进入该列表
        for (CityInfoEntity entity: fireCityList) {
            if (cityInfoEntity.getCityName().equals(entity.getCityName())){
                isAdded = true;
            }
        }
        if (!isAdded) {
            fireCityList.add(cityInfoEntity);
            isAdded = false;
        }


        cityInfoEntity=new CityInfoEntity();
        cityInfoEntity.setIdentity(false);
        cityInfoEntity.setTotal("0");
        cityInfoEntity.setCityLetter("热门城市");
        cityInfoEntity.setIdentityCount(true);
        cityInfoEntity.setCityName("上海");
        cityInfoEntity.setCity("3");
        if (cityInfoEntityLocal!=null && cityInfoEntityLocal.getCityName().equals("上海"))
            cityInfoEntity.setSelected(true);
        for (CityInfoEntity entity: fireCityList) {
            if (cityInfoEntity.getCityName().equals(entity.getCityName())){
                isAdded = true;
            }
        }
        if (!isAdded) {
            fireCityList.add(cityInfoEntity);
        }

        fireLayout.setVisibility(View.VISIBLE);
        cityLayout.initData(fireCityList);

        cityLayout.setOnSelCity(new CityLayout.onSelCity() {
            @Override
            public void onItem(CityInfoEntity entity,int index) {
                if(onSelCityNameInterface!=null)
                    onSelCityNameInterface.onItem(entity,index-2);

            }
        });

    }

    public void updateFireCityList(int position,boolean isSelected){
        fireCityList.get(position).setSelected(isSelected);
    }
    public void updateCityLayout(CityInfoEntity entity){
//        Log.e("===entity:",entity.toString());
        //判断当前城市是否已经被选中
        if (entity.getSelected() && entity.getCityName().equals("全国")){
            fireCityList.get(0).setSelected(true);
            fireCityList.get(1).setSelected(false);
        }else if (entity.getSelected() && entity.getCityName().equals("上海")){
            fireCityList.get(0).setSelected(false);
            fireCityList.get(1).setSelected(true);
        }else {
            for (int i=0;i<fireCityList.size();i++) {
                if (fireCityList.get(i).getCityName().equals(entity.getCityName())){
                    fireCityList.get(i).setSelected(entity.getSelected());
                }
            }
        }
//        Log.e("===fireCityList:",fireCityList.toString());
        city_layout.initData(fireCityList);
    }

    public interface onSelCityNameInterface
    {
        public void onItem(CityInfoEntity entity,int position);
        public void onResetGps();
        public void onGpsName(String gpsGityName);
    }
}
