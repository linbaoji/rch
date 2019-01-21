package com.rch.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.common.GlideUtils;
import com.rch.entity.VehicleImageListEntity;

import java.util.List;

/**
 * Created by acer on 2018/8/18.
 */

public class ManageBannerAdapter extends android.support.v4.view.PagerAdapter {
    Context context;
    List<VehicleImageListEntity> strUrl;
    private String vehicleType;
    private boolean one;

    public ManageBannerAdapter(Context context, List<VehicleImageListEntity> strUrl) {
        this.strUrl=strUrl;
        this.context=context;
        vehicleType="1";
    }


    @Override
    public int getCount() {
        if(vehicleType.equals("0")||vehicleType.equals("1")) {
            if (strUrl.size() == 0 || strUrl.size() == 1) {
                return strUrl.size();
            }
            else {
                return strUrl.size() + 2;
            }
        }else {//下架的话就只能是一张,和售罄
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.item_cardatevp,null);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_vp);
        ImageView iv_xj= (ImageView) view.findViewById(R.id.iv_xj);

        if(vehicleType.equals("2")||vehicleType.equals("3")){
            if(vehicleType.equals("2")){
                iv_xj.setImageResource(R.mipmap.xiajia);
            }else {
                iv_xj.setImageResource(R.mipmap.ysq);
            }
            iv_xj.setVisibility(View.VISIBLE);
        }else {
            iv_xj.setVisibility(View.GONE);
        }

        if (position == 0)
            position = strUrl.size() - 1;
        else if (position == strUrl.size() + 1)
            position = 0;
        else
            position--;

        if(one){//只有一张时候选第一张
            GlideUtils.showImg(context,strUrl.get(0).getVehicleImagePath(),R.mipmap.banner,iv);
//            Glide.with(context).load(strUrl.get(0).getVehicleImagePath()).placeholder(R.mipmap.banner).into(iv);
        }else {
            GlideUtils.showImg(context,strUrl.get(position).getVehicleImagePath(),R.mipmap.banner,iv);
//            Glide.with(context).load(strUrl.get(position).getVehicleImagePath()).placeholder(R.mipmap.banner).into(iv);
        }
        container.addView(view);
        final int finalPosition = position;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastTool.show(context,strUrl.get(finalPosition)+"<------->"+finalPosition);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void updateCarBannerData(List<VehicleImageListEntity> strUrl,String vehicleType)
    {
        this.strUrl=strUrl;
        this.vehicleType=vehicleType;
        if(vehicleType.equals("0")||vehicleType.equals("1")) {
            one=false;
        }else {//下架的话就只能是一张,和售罄
            one=true;
        }
        notifyDataSetChanged();
    }
}
