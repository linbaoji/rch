package com.rch.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class BannerPagerAdapter extends PagerAdapter {

    Context context;
    List<String> strUrl;
    ImageLoader loader;
    DisplayImageOptions options;
    public BannerPagerAdapter(Context context, List<String>  strUrl, ImageLoader loader, DisplayImageOptions options) {
        this.strUrl=strUrl;
        this.context=context;
        this.loader=loader;
        this.options=options;
    }


    @Override
    public int getCount() {
        if(strUrl.size()==0||strUrl.size()==1)
            return strUrl.size();
        else
             return strUrl.size()+2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView iv=new ImageView(context);
        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Log.e("instantiateItem", String.valueOf(position));
        if(position==0)
            position=strUrl.size()-1;
        else if(position==strUrl.size()+1)
            position=0;
        else
            position--;
        loader.displayImage(strUrl.get(position), iv, options);
        /*Glide.with(context).load(strUrl.get(position)).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                int width = resource.getWidth();
                int height = resource.getHeight();
                Log.e("ssssss",String.valueOf(width)+"-------------"+String.valueOf(height));
                //获取imageView的宽
                int imageViewWidth= iv.getWidth();
                //计算缩放比例
                float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
                //计算图片等比例放大后的高
                int imageViewHeight= (int) (height * sy);
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.height = imageViewHeight;
                iv.setLayoutParams(params);
            }
        });*/
        container.addView(iv);
        final int finalPosition = position;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ToastTool.show(context,strUrl.get(finalPosition)+"<------->"+finalPosition);
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void updateBannerData(List<String> strUrl)
    {
        this.strUrl=strUrl;
        notifyDataSetChanged();
    }
}
