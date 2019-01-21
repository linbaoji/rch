package com.rch.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.common.GlideUtils;
import com.rch.entity.CarSeriesEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */

public class BrandBrowseHistoryLayout extends LinearLayout {

    Context context;
    LayoutParams params;

    public BrandBrowseHistoryLayout(Context context) {
        super(context);
        this.context=context;
    }

    public BrandBrowseHistoryLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public BrandBrowseHistoryLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void init(final List<CarSeriesEntity> carSeriesList)
    {
        if(carSeriesList!=null&&carSeriesList.size()>0) {
            for (int i = 0; i< carSeriesList.size(); i++) {
                final CarSeriesEntity carSeriesEntity=carSeriesList.get(i);
                LinearLayout layout = new LinearLayout(context);
                params = new LayoutParams(0, 300);
                params.weight = 1;
                layout.setLayoutParams(params);
                layout.setGravity(Gravity.CENTER);
                layout.setOrientation(VERTICAL);
                layout.setTag(i);
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CarSeriesEntity entity=carSeriesList.get(Integer.parseInt(String.valueOf(v.getTag())));
                        if(onHistoryItem!=null)
                            onHistoryItem.onItem(entity);
                    }
                });


                ImageView icon = new ImageView(context);
                params = new LayoutParams(80, 80);
                icon.setLayoutParams(params);
                GlideUtils.showImg(context,carSeriesEntity.getModelImage(),R.mipmap.car_emp,icon);
//                Glide.with(context).load(carSeriesEntity.getModelImage()).placeholder(R.mipmap.car_emp).into(icon);

                TextView text=new TextView(context);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.topMargin=20;
                text.setLayoutParams(params);
                text.setText(carSeriesEntity.getModelName());
                text.setEllipsize(TextUtils.TruncateAt.END);

                layout.addView(icon);
                layout.addView(text);
                addView(layout);
            }
            int zw=4-carSeriesList.size();
            if(zw>0){
                for (int i = 0; i< zw; i++) {
                    LinearLayout layout = new LinearLayout(context);
                    params = new LayoutParams(0, 300);
                    params.weight = 1;
                    layout.setLayoutParams(params);
                    layout.setGravity(Gravity.CENTER);
                    layout.setOrientation(VERTICAL);
                    addView(layout);
                }
            }
        }
    }

    onHistoryItem onHistoryItem;

    public void setOnHistoryItem(BrandBrowseHistoryLayout.onHistoryItem onHistoryItem) {
        this.onHistoryItem = onHistoryItem;
    }

    public interface onHistoryItem
    {
        public void onItem( CarSeriesEntity carSeriesEntity);
    }
}

