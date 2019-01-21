package com.rch.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.adatper.SellBrandAdapter;
import com.rch.entity.BrandEntity;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2018/8/2.
 */

public class SellCarBrandLayout extends LinearLayout implements View.OnClickListener{
    Context context;

    public SellCarBrandLayout(Context context) {
        super(context);
        this.context=context;
    }

    public SellCarBrandLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SellCarBrandLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    Map<String,Integer> map=new HashMap<>();
    View  view;
    TextView chat, name;
    ImageView icon;
    View line;
    LinearLayout item;
    List<BrandEntity> list=new ArrayList<>();
    public void init(List<BrandEntity> list)
    {
        if(list!=null&&list.size()>0) {
            map.put("*",0);
            this.list=list;
            int endHeight=0;
            DensityUtil densityUtil=new DensityUtil(context);
            int px=densityUtil.dip2px(40);
            int linePx=densityUtil.dip2px(2);
            int chatPx=densityUtil.dip2px(25);
            Log.e("px",String.valueOf(px));
            Log.e("lineg",String.valueOf(linePx));
            for (int i=0;i<list.size();i++) {
                view = LayoutInflater.from(context).inflate(R.layout.brand_adapter, null, false);

                chat = (TextView) view.findViewById(R.id.brand_adapter_chat);
                name = (TextView) view.findViewById(R.id.brand_adapter_name);
                icon = (ImageView) view.findViewById(R.id.brand_adapter_icon);
                line = view.findViewById(R.id.brand_adapter_line);
                item = (LinearLayout) view.findViewById(R.id.brand_adapter_item);
                item.setTag(i);
                item.setOnClickListener(this);


                BrandEntity brandEntity = list.get(i);

                name.setText(brandEntity.getBrandName());
                if (brandEntity.getBrandImagePath().isEmpty())
                    icon.setVisibility(View.GONE);
                else {
                    icon.setVisibility(View.VISIBLE);
                    Glide.with(context).load(brandEntity.getBrandImagePath()).into(icon);
                }

                line.setVisibility(View.VISIBLE);
                chat.setVisibility(View.GONE);

                if (brandEntity.getIdentityCount()) {
                    line.setVisibility(View.GONE);
                }

                int tmp=i+1;
                endHeight=px*tmp+linePx*tmp;

                if (brandEntity.getIdentity()) {
                    chat.setText(brandEntity.getFirstLetter());
                    chat.setVisibility(View.VISIBLE);
                    int first=endHeight+chatPx;
                    map.put(brandEntity.getFirstLetter(),first);
                }

                addView(view);
            }
        }
    }

    public int getLoaclPosition(String chat)
    {
        for (Map.Entry<String,Integer> entry :map.entrySet()) {
            if(entry.getKey().equals(chat))
                return entry.getValue();
        }
        return 0;
    }


    @Override
    public void onClick(View v) {
        if (onSelBrandNameInterface != null)
            onSelBrandNameInterface.onItem(list.get(Integer.parseInt(String.valueOf(v.getTag()))));
    }


    onSelBrandNameInterface onSelBrandNameInterface;

    public interface onSelBrandNameInterface {
        public void onItem(BrandEntity entity);

    }
    public void setOnSelBrandNameInterface(onSelBrandNameInterface onSelBrandNameInterface) {
        this.onSelBrandNameInterface = onSelBrandNameInterface;
    }
}
