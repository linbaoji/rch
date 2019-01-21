package com.rch.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.rch.R;
import com.rch.common.DensityUtil;
import com.rch.common.GeneralUtils;
import com.rch.entity.LableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class HomeLable extends LinearLayout {
    Context context;
    LinearLayout layout;
    TextView lable,close;
    ImageView resetIcon;
    LayoutParams params;
    int measuredWidthTicketNum=0;
    int colorListIndex=0;
    DensityUtil densityUtil;

    List<View> viewList=new ArrayList<>();
    List<LableEntity> list;

    OnTtemListener onTtemListener;

    public void setOnTtemListener(OnTtemListener onTtemListener) {
        this.onTtemListener = onTtemListener;
    }

    public HomeLable(Context context) {
        super(context);
        this.context=context;
    }

    public HomeLable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public HomeLable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }
    int left;
//    @TargetApi(Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initData(List<LableEntity> list,int screenWidth)
    {
        this.list=list;
        viewList.clear();
        removeAllViews();
        measuredWidthTicketNum=0;
        densityUtil=new DensityUtil(context);
        colorListIndex=0;
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                LableEntity lableEntity = list.get(i);

                left = Math.round(densityUtil.getScale() * 7);
                layout = new LinearLayout(context);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.leftMargin = left;
                layout.setLayoutParams(params);
                layout.setBackground(context.getResources().getDrawable(R.drawable.home_lable));
                layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDel(v);
                    }
                });


                lable = new TextView(context);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lable.setLayoutParams(params);
                lable.setPadding(left, left, 0, left);
                lable.setTextSize(12f);
                lable.setTextColor(getResources().getColor(R.color.gray_3));
                lable.setText(lableEntity.getLable());

                close = new TextView(context);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                close.setLayoutParams(params);
                close.setPadding(left, 0, left, 0);
                close.setText("x");
                close.setGravity(Gravity.CENTER);

                layout.addView(lable);
                layout.addView(close);
                viewList.add(layout);
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                layout.measure(spec, spec);
                int tempWidth = layout.getMeasuredWidth();
                if (measuredWidthTicketNum < tempWidth)
                    measuredWidthTicketNum = tempWidth;
            }

            addResetView();

            int columnCount = screenWidth / (measuredWidthTicketNum);
            int rows = viewList.size() % columnCount == 0 ? viewList.size() / columnCount : viewList.size() / columnCount + 1;
            for (int i = 0; i < rows; i++) {
                //行
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(HORIZONTAL);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin=10;
//                params.topMargin = GeneralUtils.dip2px(context,10);
//                params.bottomMargin = GeneralUtils.dip2px(context,10);
//                params.leftMargin = GeneralUtils.dip2px(context,8);

                linearLayout.setLayoutParams(params);
                for (int j = 0; j < columnCount; j++) {
                    //列
                    LinearLayout linearLayout1 = (LinearLayout) viewList.get(colorListIndex);
                    linearLayout1.setTag(i + "," + j + "," + colorListIndex);
                    colorListIndex++;
                    linearLayout.addView(linearLayout1);
                    if (colorListIndex == viewList.size())
                        break;
                }
                addView(linearLayout);
            }
        }

    }

  //  @RequiresApi(api = Build.VERSION_CODES.M)
    private void addResetView()
    {

        layout = new LinearLayout(context);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = left;
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(HORIZONTAL);

        resetIcon = new ImageView(context);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        resetIcon.setLayoutParams(params);
        resetIcon.setImageResource(R.mipmap.rotating_icon);

        lable = new TextView(context);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin=left;
        params.topMargin=left;
        params.bottomMargin=left;
        lable.setLayoutParams(params);
        lable.setTextSize(12f);
        lable.setTextColor(Color.parseColor("#413f3b"));
        lable.setText("重置");
//        lable.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //removeAllViews();
//                if(onTtemListener!=null)
//                    onTtemListener.onReset();
//            }
//        });
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeAllViews();
                if(onTtemListener!=null)
                    onTtemListener.onReset();
            }
        });

        layout.addView(resetIcon);
        layout.addView(lable);
        viewList.add(layout);
    }

    private void onDel(View v)
    {
        String str=String.valueOf(v.getTag());
        int row=Integer.parseInt(str.split(",")[0]);
        int columns=Integer.parseInt(str.split(",")[1]);
        int index=Integer.parseInt(str.split(",")[2]);
        LinearLayout rowLayout= (LinearLayout) getChildAt(row);
        rowLayout.removeViewAt(columns);
        list.remove(index);
        if(onTtemListener!=null)
            onTtemListener.onDel(list);
    }


    public interface OnTtemListener{
        public void onDel(List<LableEntity> list);
        public void onReset();
    }

    /*private void setData()
    {
        list=new ArrayList<>();
        list.add("奥迪");
        list.add("0-30万");
    }*/


}
