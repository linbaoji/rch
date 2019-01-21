package com.rch.adatper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.StrSplitTool;
import com.rch.common.TimeSplitTool;
import com.rch.custom.StepOwnView;
import com.rch.entity.OwnerBean;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SoldcarAdapter extends RecyclerView.Adapter<SoldcarAdapter.MyHolder>{
    private Context context;
    private List<OwnerBean> list;

    // 列表展开标识
    int opened = -1;
    private int dex;


    public SoldcarAdapter(Context context,List<OwnerBean> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_soldcar,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getVehicleFullName());
        holder.tv_time.setText(isnull(list.get(position).getRegistrationTime())+"/"+isnull(list.get(position).getShowMileage())+"公里");

        String year= TimeSplitTool.getYM(list.get(position).getRegistrationTime());
        String lime="/0公里";
        int iShowMileage=list.get(position).getShowMileage().isEmpty()?0: Integer.parseInt(list.get(position).getShowMileage());
        if(iShowMileage<10000)
            lime = "/"+String.valueOf(iShowMileage)+"公里";
        else
            lime = "/"+ StrSplitTool.retainOneNumber(String.valueOf(iShowMileage))+"万公里";
        holder.tv_time.setText(year+lime);

        if(list.get(position).getAuitList()!=null&&list.get(position).getLogList().size()>0){
            holder.stp_own.initDate(list.get(position).getLogList());
        }

        holder.bind(position);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout ll_zt,ll_sq;
        StepOwnView stp_own;
        TextView tv_name,tv_time,tv_sq;
        ImageView iv_sq;
        public MyHolder(View itemView) {
            super(itemView);
//            ll_zt= (LinearLayout) itemView.findViewById(R.id.ll_zt);//整个一个布局
            ll_sq= (LinearLayout) itemView.findViewById(R.id.ll_sq);
            stp_own= (StepOwnView) itemView.findViewById(R.id.stp_own);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            tv_sq=(TextView) itemView.findViewById(R.id.tv_sq);
            iv_sq=(ImageView) itemView.findViewById(R.id.iv_sq);

            ll_sq.setOnClickListener(this);

        }

        public void bind(int pos){
            if(pos==opened){
                stp_own.setVisibility(View.VISIBLE);
                tv_sq.setText("收起动态");
                iv_sq.setImageResource(R.mipmap.wmdcsq);
            }else {
                stp_own.setVisibility(View.GONE);
                tv_sq.setText("打开动态");
                iv_sq.setImageResource(R.mipmap.wmdcdk);
            }

        }

        @Override
        public void onClick(View view) {
         if(opened==getLayoutPosition()){//相等
             opened=-1;
             notifyItemChanged(getLayoutPosition());
         }else {//不相等
             int oldOpened = opened;
             opened = getLayoutPosition();//复值
             notifyItemChanged(oldOpened);
             notifyItemChanged(opened);


         }
        }
    }

    public void change(List<OwnerBean> datelist) {
        this.list=datelist;
        notifyDataSetChanged();
    }

    private String isnull(String tex){
        if(TextUtils.isEmpty(tex)){
            return "--";
        }else {
            return tex;
        }

    }
}
