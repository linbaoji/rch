package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/6/4.
 */

public class GuideActivity extends BaseActivity{

    private final static int[] guideImages = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};
    private GuidePagerAdapter adapter;
    private ViewPager pager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉Activity上面的状态栏
        setContentView(R.layout.act_guide);
        initToolBar();
        pager= (ViewPager) findViewById(R.id.vp_guide);
        initDate();
    }

    private void initDate() {
        adapter = new GuidePagerAdapter();
        pager.setAdapter(adapter);
    }

    private class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return guideImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {

            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GuideActivity.this, R.layout.guide_vp_item, null);

            TextView iv_goty = (TextView) view.findViewById(R.id.iv_goty);
            if (position == guideImages.length - 1) {
                iv_goty.setVisibility(View.VISIBLE);
            } else {
                iv_goty.setVisibility(View.GONE);
            }

            view.setBackgroundResource(guideImages[position]);
            iv_goty.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GuideActivity.this, NewMainActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            ((ViewPager) container).addView(view);

            return view;
        }

    }
}
