package com.rch.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.OrderProcessBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */

public class StepView extends LinearLayout{
    private Context context;
    private ImageView iv_duigou;
    private View v_sbd;
    private TextView tv_info;
    public StepView(Context context) {
        super(context);
        this.context=context;
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }


    public void initDate(List<OrderProcessBean>list){
        if(list!=null&&list.size()>0){
            removeAllViews();
            for(int i=0;i<list.size();i++){
                View view=View.inflate(context, R.layout.item_step,null);
                iv_duigou= (ImageView) view.findViewById(R.id.iv_duigou);
                v_sbd=view.findViewById(R.id.view_sbd);
                tv_info= (TextView) view.findViewById(R.id.tv_info);
                if(list.get(i).getIfCompleted().equals("1")){//完成
                 iv_duigou.setImageResource(R.mipmap.yydg);
                 v_sbd.setBackgroundColor(Color.parseColor("#54d78e"));
                }else {
                    iv_duigou.setImageResource(R.mipmap.yynodg);
                    v_sbd.setBackgroundColor(Color.parseColor("#6654d78e"));
                }
                tv_info.setText(list.get(i).getNodeName());
                if(i==list.size()-1){
                    v_sbd.setVisibility(GONE);
                }
                addView(view);
            }


        }
    }
}
