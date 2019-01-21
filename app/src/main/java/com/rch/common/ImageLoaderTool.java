package com.rch.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ImageLoaderTool {

   ImageLoader imageLoader;
    DisplayImageOptions options;

    public ImageLoaderTool(Context context,int resId) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
               // .memoryCacheExtraOptions(480, 800) // 保存每个缓存图片的最大长和宽
                .threadPoolSize(3) // 线程池的大小 这个其实默认就是3
                .threadPriority(Thread. NORM_PRIORITY - 2)//设置线程优先级
                .denyCacheImageMultipleSizesInMemory() // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .memoryCache( new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)// 设置缓存的最大字节
                .tasksProcessingOrder(QueueProcessingType. LIFO)//设置图片下载和显示的工作队列排序
                .defaultDisplayImageOptions(DisplayImageOptions. createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context,
                                5 * 1000, 30 * 1000))// connectTimeout 超时时间
                .writeDebugLogs().build();// 开始构建
        //ImageLoader. getInstance().init(config);// 全局初始化此配置
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(config);



        //创建默认的ImageLoader配置参数
    /*    ImageLoaderConfiguration config = ImageLoaderConfiguration
                .createDefault(context);
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(config);*/
        /*DiskCache diskCache=imageLoader.getDiskCache();
        diskCache.getDirectory()*/

        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId)
                .showImageOnLoading(resId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }
}
