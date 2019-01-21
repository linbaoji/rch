package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.R;

import com.rch.base.BaseActivity;
import com.rch.common.GlideUtils;
import com.rch.custom.HackyViewPager;
import com.rch.entity.PicListBean;
import com.rch.entity.VehicleImageListEntity;


import java.util.List;




/**
 * Created by Administrator on 2018/10/11.
 */

public class PagePhotoAct extends BaseActivity{
    @ViewInject(R.id.view_pager)
    private HackyViewPager view_pager;
    @ViewInject(R.id.indicator)
    private TextView indicator;

    List<PicListBean> strUrl;
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pagephoto);
        initToolBar();
        ViewUtils.inject(this);
        strUrl= (List<PicListBean>) getIntent().getExtras().getSerializable("VehicleImageListEntity");
        index=getIntent().getExtras().getInt("index",0);
        if(strUrl.size()>0) {
            view_pager.setAdapter(new SamplePagerAdapter());
            CharSequence text = getString(R.string.viewpager_indicator,
                     index+1, view_pager.getAdapter().getCount());
            indicator.setText(text);
            view_pager.setCurrentItem(index);
        }

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        position + 1, view_pager.getAdapter().getCount());
                indicator.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return strUrl.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setImageResource(sDrawables[position]);
            // Now just add PhotoView to ViewPager and return it
            GlideUtils.showImg(PagePhotoAct.this,strUrl.get(position).getVehicleImage(),R.mipmap.banner,photoView);
//            Glide.with(PagePhotoAct.this).load(strUrl.get(position).getVehicleImagePath()).placeholder(R.mipmap.banner).into(photoView);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }






}
