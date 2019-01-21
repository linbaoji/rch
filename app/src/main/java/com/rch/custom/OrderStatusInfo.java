package com.rch.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.rch.R;
import com.rch.common.DensityUtil;
import com.rch.common.GsonUtils;
import com.rch.common.ObjectUtils;
import com.rch.common.ToastTool;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/8.
 */

public class OrderStatusInfo extends LinearLayout {
    private Context context;
    private ImageView iv_duigou;
    private View viewline,view_sbd;
    private TextView tv_info,tv_zt,tv_time;
    public OrderStatusInfo(Context context) {
        super(context);
        this.context=context;
    }

    public OrderStatusInfo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public OrderStatusInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }


    public void init(JSONArray array)
    {
        Gson gson=new Gson();
        removeAllViews();
        for (int i=0;i<array.length();i++)
        {
            try {
                View view=View.inflate(context, R.layout.item_orderstep,null);
                TextView tt= (TextView) view.findViewById(R.id.tv_tt);
                TextView tv_info= (TextView) view.findViewById(R.id.tv_info);
                view_sbd=view.findViewById(R.id.view_sbd);
                
                if(i==array.length()-1){
                    view_sbd.setVisibility(GONE);
                }
                JSONObject object= (JSONObject) array.get(i);


//                List<Keybean>list=gson.fromJson(object.toString(),new TypeToken<List<Keybean>>(){}.getType());

                StringBuilder builder=new StringBuilder();
                String title = null;
                for(int j=0;j<20;j++){
                    if(!object.isNull(j+"")){
                        if(j==1) {
                            title=object.getString(j+"");
                        }else {
                            builder.append(object.getString(j + "") + "\n\n");
                        }
                    }
                 }

                    tt.setText(title);
                    if(builder.toString().length()>0) {
                        tv_info.setText(builder.toString());
                    }
                    addView(view);
            } catch (Exception e) {
                ToastTool.show(context,"解析异常");
                e.printStackTrace();
            }



        }
    }
}
