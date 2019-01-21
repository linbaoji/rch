package com.rch.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.AuitListBean;
import com.rch.entity.OrderProcessBean;
import com.rch.entity.OwnerBean;

import java.util.List;

/**
 * Created by acer on 2018/8/9.
 */

public class StepOwnView extends LinearLayout {
    private Context context;
    private ImageView iv_duigou;
    private View viewline,view_sbd;
    private TextView tv_info,tv_zt,tv_time;
    public StepOwnView(Context context) {
        super(context);
        this.context=context;
    }

    public StepOwnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public StepOwnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }


    public void initDate(List<AuitListBean> list){
        if(list!=null&&list.size()>0){
            removeAllViews();
            for(int i=0;i<list.size();i++){
                View view=View.inflate(context, R.layout.item_ownstep,null);
                iv_duigou= (ImageView) view.findViewById(R.id.iv_dg);
                viewline=view.findViewById(R.id.view_line);
                view_sbd=view.findViewById(R.id.view_sbd);
                tv_info= (TextView) view.findViewById(R.id.tv_info);
                tv_zt= (TextView) view.findViewById(R.id.tv_zt);
                tv_time= (TextView) view.findViewById(R.id.tv_time);
                if(i==0){
                    view_sbd.setVisibility(INVISIBLE);
                }else {
                    view_sbd.setVisibility(VISIBLE);
                }

                if(list.get(i).getIsColor().equals("1")){//亮亮的
                    iv_duigou.setImageResource(R.mipmap.yydg);
                    viewline.setBackgroundColor(Color.parseColor("#54d78e"));
                    view_sbd.setBackgroundColor(Color.parseColor("#54d78e"));
                    tv_zt.setTextColor(Color.parseColor("#413f3b"));
                    tv_time.setTextColor(Color.parseColor("#999999"));
                    tv_info.setTextColor(Color.parseColor("#666666"));

                }else {
                    iv_duigou.setImageResource(R.mipmap.yynodg);
                    viewline.setBackgroundColor(Color.parseColor("#6654d78e"));
                    view_sbd.setBackgroundColor(Color.parseColor("#6654d78e"));

                    tv_zt.setTextColor(Color.parseColor("#cfcfcf"));
                    tv_time.setTextColor(Color.parseColor("#cfcfcf"));
                    tv_info.setTextColor(Color.parseColor("#cfcfcf"));
                }
                tv_info.setText(list.get(i).getContent());
                tv_zt.setText(isNull(list.get(i).getTitle()));
                tv_time.setText(isNull(list.get(i).getDay())+" "+list.get(i).getDate());
                if(i==list.size()-1){
                    viewline.setVisibility(INVISIBLE);
                }
                addView(view);
            }


        }
    }

    private String isNull(String tex) {
        if(TextUtils.isEmpty(tex)){
            return "";
        }else {
            return tex;
        }


    }
}
