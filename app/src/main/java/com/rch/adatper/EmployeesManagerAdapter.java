package com.rch.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.activity.EmployeesAct;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.custom.MyAlertDialog;
import com.rch.entity.EmployBean;
import com.rch.entity.UserInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class EmployeesManagerAdapter extends RecyclerView.Adapter<EmployeesManagerAdapter.MyHolder> {
    private Context context;
    private List<EmployBean> list;
    private List<EmployBean> gllist = new ArrayList<>();
    private List<EmployBean> yglist = new ArrayList<>();

    private upDate update;

    public void setUpdate(upDate update) {
        this.update = update;
    }

    public EmployeesManagerAdapter(Context context, List<EmployBean> list) {
        this.context = context;
        this.list = list;
        gllist.clear();
        yglist.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsAdmin().equals("1")) {
                gllist.add(list.get(i));//管理员
            } else {//员工
                yglist.add(list.get(i));//员工管理
            }
        }


    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_employees, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tv_gly.setVisibility(View.GONE);
        holder.tv_yc.setVisibility(View.GONE);

        if (list.size()>0) {
//            if (list.get(position).getUserSex().equals("1")) {
//                holder.iv_sex.setImageResource(R.mipmap.img_boy);
//            } else if (list.get(position).getUserSex().equals("2")) {
//                holder.iv_sex.setImageResource(R.mipmap.img_girl);
//            } else {
//                holder.iv_sex.setImageResource(R.mipmap.img_moren);
//            }


            holder.tv_name.setText(list.get(position).getUserName());
            holder.tv_phone.setText(list.get(position).getMobile());
        }

    }



    @Override
    public int getItemCount() {
        return gllist.size() + yglist.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_gly, tv_cy, tv_yc, tv_name, tv_phone;
        ImageView iv_sex;

        public MyHolder(View itemView) {
            super(itemView);
            tv_gly = (TextView) itemView.findViewById(R.id.tv_gly);
            tv_cy = (TextView) itemView.findViewById(R.id.tv_cy);
            tv_yc = (TextView) itemView.findViewById(R.id.tv_yc);
            iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }


    public void ChageDate(List<EmployBean> list) {
        this.list = list;
        gllist.clear();
        yglist.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsAdmin().equals("1")) {
                gllist.add(list.get(i));//管理员
            } else {//员工
                yglist.add(list.get(i));//员工管理
            }
        }
        notifyDataSetChanged();
    }

    public static interface upDate {
        void update();
    }


}
