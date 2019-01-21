package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rch.R;
import com.rch.activity.PagePhotoAct;
import com.rch.entity.PicListBean;
import com.rch.entity.VehicleImageListEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CarBannerAdatper extends android.support.v4.view.PagerAdapter {

    Context context;
    List<PicListBean> strUrl;
    private String shelvesStatus;


    public CarBannerAdatper(Context context, List<PicListBean> strUrl,String shelvesStatus) {
        this.strUrl=strUrl;
        this.context=context;
        this.shelvesStatus=shelvesStatus;

    }


    @Override
    public int getCount() {

            if (strUrl.size() == 0 || strUrl.size() == 1) {
                return strUrl.size();
            }
            else {
                return strUrl.size() + 2;
            }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context,R.layout.item_cardatevp,null);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_vp);
        ImageView iv_xj= (ImageView) view.findViewById(R.id.iv_xj);

        if(shelvesStatus.equals("2")||shelvesStatus.equals("3")){
            if(shelvesStatus.equals("2")){
                iv_xj.setImageResource(R.mipmap.xiajia);
            }else {
                iv_xj.setImageResource(R.mipmap.shouq);
            }
            iv_xj.setVisibility(View.VISIBLE);
        }else {
            iv_xj.setVisibility(View.GONE);
        }

        if (position == 0) {
            position = strUrl.size() - 1;
        }
        else if (position == strUrl.size() + 1) {
            position = 0;
        }
        else {
            position--;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.banner);
        Glide.with(context).load(strUrl.get(position).getVehicleImage()).apply(options).into(iv);
        container.addView(view);
        final int finalPosition = position;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastTool.show(context,strUrl.get(finalPosition)+"<------->"+finalPosition);
                context.startActivity(new Intent(context,PagePhotoAct.class).
                        putExtra("VehicleImageListEntity", (Serializable) strUrl).putExtra("index",finalPosition));
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void updateCarBannerData(List<PicListBean> strUrl,String shelvesStatus)
    {
        this.strUrl=strUrl;
        this.shelvesStatus=shelvesStatus;
        notifyDataSetChanged();
    }
}
