package com.rch.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.LableEntity;
import com.rch.entity.SearchEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class SerchLableView extends LinearLayout {
    Context context;
    LayoutParams params;
    List<View> viewList=new ArrayList<>();
    int measuredWidthTicketNum=0;
    int colorListIndex=0;

    OnTtemListener onTtemListener;

    public void setOnTtemListener(OnTtemListener onTtemListener) {
        this.onTtemListener = onTtemListener;
    }

    public SerchLableView(Context context) {
        super(context);
        this.context=context;
    }

    public SerchLableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SerchLableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void initData(List<SearchEntity>list, int screenWidth){
        removeAllViews();
        viewList.clear();
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                 final SearchEntity entity=list.get(i);
                 View view=View.inflate(context,R.layout.item_serchlable,null);
                 TextView tv_lab= (TextView) view.findViewById(R.id.tv_lable);
                 final LinearLayout ll_tab= (LinearLayout) view.findViewById(R.id.ll_tab);
                 ll_tab.setTag(i+"");
                 if(!TextUtils.isEmpty(entity.getSeriesName())) {
                     tv_lab.setText(entity.getSeriesName());
                 }else {
                     tv_lab.setText(entity.getBrandName());
                 }
                 viewList.add(view);
                 int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                 view.measure(spec, spec);
                 int tempWidth = view.getMeasuredWidth();
                 if (measuredWidthTicketNum < tempWidth)
                     measuredWidthTicketNum = tempWidth;

                 ll_tab.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         onTtemListener.onClick(entity);
                     }
                 });
            }
            int columnCount = screenWidth / (measuredWidthTicketNum);
            int rows = viewList.size() % columnCount == 0 ? viewList.size() / columnCount : viewList.size() / columnCount + 1;
            for (int i = 0; i < rows; i++) {
                //行
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(HORIZONTAL);
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin=20;
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

    public interface OnTtemListener{
        public void onClick(SearchEntity entity);
    }
}
