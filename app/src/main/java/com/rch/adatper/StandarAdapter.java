package com.rch.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.rch.R;
import com.rch.entity.StandarBean;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */

public class StandarAdapter extends BaseAdapter{
    private Context context;
    private List<StandarBean>list;
    private int dex=-1;
    private String text;

    public StandarAdapter(Context context, List<StandarBean> list,String text) {
        this.context = context;
        this.list = list;
        this.text=text;
        for(int i=0;i<list.size();i++){
            if(text.equals("规格")){
                dex=0;
            }else {
                if(text.equals(list.get(i).getVehicleStandardName())){
                    dex=i;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=View.inflate(context, R.layout.item_stander,null);
        LinearLayout ll_bag= (LinearLayout) view.findViewById(R.id.ll_bag);
        TextView tv_stander= (TextView) view.findViewById(R.id.tv_stander);
        ImageView iv_check= (ImageView) view.findViewById(R.id.iv_check);
        tv_stander.setText(list.get(i).getVehicleStandardName());
        if(dex==i){
            ll_bag.setBackgroundColor(context.getResources().getColor(R.color.gray_2));
            iv_check.setVisibility(View.VISIBLE);
            tv_stander.setTextColor(context.getResources().getColor(R.color.orange_2));
        }else {
            ll_bag.setBackgroundColor(context.getResources().getColor(R.color.white));
            iv_check.setVisibility(View.GONE);
            tv_stander.setTextColor(context.getResources().getColor(R.color.gray_3));
        }
        return view;
    }

    public void setCurrentItem(int currentItem){
        this.dex=currentItem;
        notifyDataSetChanged();
    }
}
