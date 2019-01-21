package com.rch.common;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rch.R;

/**
 * Created by Administrator on 2018/10/25.
 */

public class GlideUtils {
    //只有默认图片
    public static void  showImg(Context context, String url, int res, ImageView ivShowPhoto){
        RequestOptions options = new RequestOptions();
        options.placeholder(res);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(ivShowPhoto);
    }

    //缩放图像让它填充到 ImageView 界限内并且裁剪额外的部分
    public static void  showImg(Context context, String url, ImageView ivShowPhoto){
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.banner);
        options.centerCrop();
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(ivShowPhoto);
    }

    //是否跳过缓存
    public static void  showImg(Context context, String url, int res,boolean cache, ImageView ivShowPhoto){
        RequestOptions options = new RequestOptions();
        options.placeholder(res);
        options.skipMemoryCache(cache);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(ivShowPhoto);
    }
    //缓存模式
    public static void  showImg(Context context, String url, int res, DiskCacheStrategy diskCacheStrategy, ImageView ivShowPhoto){
        RequestOptions options = new RequestOptions();
        options.placeholder(res);
        options.diskCacheStrategy(diskCacheStrategy);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(ivShowPhoto);
    }
}
