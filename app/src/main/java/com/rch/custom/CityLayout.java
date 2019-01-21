package com.rch.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.DensityUtil;
import com.rch.entity.CityInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class  CityLayout extends LinearLayout {

    Context context;

    LinearLayout rowsLayout;
    TextView tvName;
    LayoutParams params;

    View line;
    ImageView seat;

    onSelCity onSelCity;

    public void setOnSelCity(CityLayout.onSelCity onSelCity) {
        this.onSelCity = onSelCity;
    }

    public CityLayout(Context context) {
        super(context);
        this.context=context;
    }

    public CityLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public CityLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }


    public void initData(final List<CityInfoEntity> list)
    {
        if(list!=null&&list.size()>0)
        {
            removeAllViews();

            for (int i=0;i<list.size();i++)
            {
                final CityInfoEntity entity = list.get(i);
                final View view= LayoutInflater.from(context).inflate(R.layout.item_citylayout,null);
                TextView tvName= (TextView) view.findViewById(R.id.city_adapter_name);
                final ImageView iv_image= (ImageView) view.findViewById(R.id.iv_check);
                tvName.setText(entity.getCityName());

                if (entity.getSelected()){
                    iv_image.setVisibility(View.VISIBLE);
                }else {
                    iv_image.setVisibility(View.INVISIBLE);
                }
                view.setTag(i);


                view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index = Integer.parseInt(String.valueOf(v.getTag()));
                        if(onSelCity!=null)
                            onSelCity.onItem(list.get(index),index);

//                            if (index == 0) {
//                                if (viewList.get(0).getVisibility() == View.VISIBLE) {
//                                    viewList.get(0).setVisibility(INVISIBLE);
////                                    entity.setSelected(false);
//                                    viewList.get(1).setVisibility();
//                                } else {
//                                    iv_image.setVisibility(VISIBLE);
//                                    entity.setSelected(true);
//                                }
//
//                            }
                        }
                    });



                addView(view);
            }
        }
    }


    /*多行，多列*/
   /*  int columnCount=4;
    int listIndex=0;
   public void initData(final List<CityInfoEntity> list)
    {
        if(list!=null&&list.size()>0)
        {
            removeAllViews();
            listIndex=0;
            int rows= list.size()%columnCount==0? list.size()/columnCount: list.size()/columnCount+1;
            for (int i=0;i<rows;i++)
            {
                rowsLayout=new LinearLayout(context);
                params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                rowsLayout.setLayoutParams(params);
                for (int j=0;j<columnCount;j++)
                {
                    tvName=new TextView(context);
                    params=new LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT);
                    params.weight=1;
                    tvName.setLayoutParams(params);
                    tvName.setGravity(Gravity.CENTER);
                    tvName.setTextSize(14f);
                    tvName.setTag(listIndex);
                    tvName.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index=Integer.parseInt(String.valueOf(v.getTag()));
                            if(onSelCity!=null)
                                onSelCity.onItem(list.get(index));
                        }
                    });
                    if(listIndex>list.size()-1) {
                        tvName.setVisibility(INVISIBLE);
                        tvName.setEnabled(false);
                    }
                    else
                    {
                        tvName.setText(list.get(listIndex).getCityName());
                        tvName.setVisibility(VISIBLE);
                        tvName.setEnabled(true);
                    }
                    listIndex++;
                    rowsLayout.addView(tvName);
                }
                addView(rowsLayout);
            }
        }
    }*/

    public interface onSelCity
    {
        public void onItem(CityInfoEntity entity,int index);
    }
}
