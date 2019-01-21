package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rch.R;
import com.rch.activity.PagePhotoAct;
import com.rch.common.GlideUtils;
import com.rch.entity.PicListBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/29.
 */

public class BannerAdatper extends android.support.v4.view.PagerAdapter {

    Context context;
    List<PicListBean> strUrl;
    private String definState;


    public  BannerAdatper(Context context, List<PicListBean> strUrl,String definState) {
        this.strUrl=strUrl;
        this.context=context;
        this.definState=definState;
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
        View view=View.inflate(context, R.layout.item_cardatevp,null);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_vp);
        ImageView iv_xj= (ImageView) view.findViewById(R.id.iv_xj);

        if(definState.equals("4")){//在售中
            iv_xj.setVisibility(View.GONE);
        }else {
            if (definState.equals("1")) {
                iv_xj.setImageResource(R.mipmap.shenhez);
            } else if (definState.equals("2")) {
                iv_xj.setImageResource(R.mipmap.shenheshibai);
            } else if (definState.equals("3")) {
                iv_xj.setImageResource(R.mipmap.yitongguo);
            }
//        else if(definState.equals("4")){
//            iv_xj.setImageResource(R.mipmap.yitongguo);
//        }
            else if (definState.equals("5")) {
                iv_xj.setImageResource(R.mipmap.shouq);
            } else if (definState.equals("6")) {
                iv_xj.setImageResource(R.mipmap.xiajia);
            }
            iv_xj.setVisibility(View.VISIBLE);
        }

        if (position == 0) {
            position = strUrl.size() - 1;
        } else if (position == strUrl.size() + 1) {
            position = 0;
        } else {
            position--;
        }
        GlideUtils.showImg(context,strUrl.get(position).getVehicleImage(),R.mipmap.banner,iv);
//        Glide.with(context).load(strUrl.get(position).getVehicleImagePath()).placeholder(R.mipmap.banner).into(iv);
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

    public void updateCarBannerData(List<PicListBean> strUrl,String definState)
    {
        this.strUrl=strUrl;
        this.definState=definState;
        notifyDataSetChanged();
    }
}

