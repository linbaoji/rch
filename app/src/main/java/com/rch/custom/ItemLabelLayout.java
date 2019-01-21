package com.rch.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.CityInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/12/10.
 */

public class ItemLabelLayout extends LinearLayout {
    private Context context;

    public ItemLabelLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ItemLabelLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ItemLabelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public void initData(String recom, String star, String select) {
        removeAllViews();
        final View view = LayoutInflater.from(context).inflate(R.layout.item_labellayout, null);
        TextView tv_iscom= (TextView) view.findViewById(R.id.tv_iscom);
        TextView tv_isstart= (TextView) view.findViewById(R.id.tv_isstart);
        TextView tv_isselect= (TextView) view.findViewById(R.id.tv_isselect);
        tv_iscom.setVisibility(GONE);
        tv_isstart.setVisibility(GONE);
        tv_isselect.setVisibility(GONE);
        if (!recom.equals("1") && !star.equals("1") && !select.equals("1")) {
            removeAllViews();
        }else {
            if(recom.equals("1")) {
                tv_iscom.setVisibility(VISIBLE);
            }
            if(star.equals("1")) {
                tv_isstart.setVisibility(VISIBLE);
            }
            if(select.equals("1")) {
                tv_isselect.setVisibility(VISIBLE);
            }
            addView(view);
        }
    }
}





