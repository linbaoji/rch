package com.rch.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.common.GlideUtils;
import com.rch.common.SpUtils;
import com.rch.entity.VehicleImageListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class CarImageDisplay extends LinearLayout {
    Context context;
    ImageView imageView;
    LayoutParams params;
    public CarImageDisplay(Context context) {
        super(context);
        this.context=context;
    }

    public CarImageDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public CarImageDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void initData(List<VehicleImageListEntity> list)
    {
        if(list!=null&&list.size()>0) {
            int screen_height= SpUtils.get_View_heigth(SpUtils.getScreenWith(context),600,900);//根据图片的像素和手机屏幕宽度设置控件高度
            for (int i = 0; i < list.size(); i++)
            {
                imageView=new ImageView(context);
                //params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
                params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin=15;
                params.height=screen_height;
                imageView.setLayoutParams(params);
            /*    imageView.setScaleType(ImageView.ScaleType.FIT_XY);*/
                GlideUtils.showImg(context,list.get(i).getVehicleImagePath(),R.mipmap.banner,true,imageView);
//                Glide.with(context).load(list.get(i).getVehicleImagePath()).skipMemoryCache(true).placeholder(R.mipmap.banner).into(imageView);
                addView(imageView);
            }
        }
    }
}
