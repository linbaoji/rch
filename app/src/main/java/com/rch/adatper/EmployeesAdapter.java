package com.rch.adatper;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyHolder> {
    private Context context;
    private List<EmployBean> list;
    private UserInfoEntity entity;
    private int isAdmin;
    private List<EmployBean> gllist = new ArrayList<>();
    private List<EmployBean> yglist = new ArrayList<>();

    private upDate update;

    public void setUpdate(upDate update) {
        this.update = update;
    }

    public EmployeesAdapter(Context context, List<EmployBean> list, UserInfoEntity entity, int isAdmin) {
        this.context = context;
        this.entity = entity;
        this.isAdmin = isAdmin;
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
        if (gllist.size() >0&&yglist.size()>0) {//说明有管理员
            if(position==0){
                holder.tv_gly.setVisibility(View.VISIBLE);
                holder.tv_gly.setText("管理员");
                holder.tv_yc.setVisibility(View.GONE);
            }

            if(position>0&&position<=gllist.size()-1){
                holder.tv_gly.setVisibility(View.GONE);
                holder.tv_gly.setText("管理员");
                holder.tv_yc.setVisibility(View.GONE);
            }

            if(position>gllist.size()-1){
                if(position==gllist.size()){
                    holder.tv_gly.setVisibility(View.VISIBLE);
                    holder.tv_gly.setText("成员");
                    holder.tv_yc.setVisibility(View.VISIBLE);
                }else{
                    holder.tv_gly.setVisibility(View.GONE);
                    holder.tv_gly.setText("成员");
                    holder.tv_yc.setVisibility(View.VISIBLE);
                }
            }

        } else {//没有管理员
            if(gllist.size()>0){//只有管理员
                if(position==0){
                    holder.tv_gly.setVisibility(View.VISIBLE);
                    holder.tv_gly.setText("管理员");
                    holder.tv_yc.setVisibility(View.GONE);
                }else {
                    holder.tv_gly.setVisibility(View.GONE);
                    holder.tv_gly.setText("管理员");
                    holder.tv_yc.setVisibility(View.GONE);
                }

                ((EmployeesAct)context).setMemberGone();
            }

            if(yglist.size()>0){
                if(position==0){
                    holder.tv_gly.setVisibility(View.VISIBLE);
                    holder.tv_gly.setText("成员");
                    holder.tv_yc.setVisibility(View.VISIBLE);
                }else {
                    holder.tv_gly.setVisibility(View.GONE);
                    holder.tv_gly.setText("成员");
                    holder.tv_yc.setVisibility(View.VISIBLE);
                }
            }

        }


        holder.tv_gly.setVisibility(View.GONE);

        if (isAdmin == 1) {
            holder.tv_yc.setText("移除");
        } else {
            holder.tv_yc.setText("退出企业");
        }

        if (list.get(position).getUserSex().equals("1")) {
            holder.iv_sex.setImageResource(R.mipmap.img_boy);
        } else if (list.get(position).getUserSex().equals("2")) {
            holder.iv_sex.setImageResource(R.mipmap.img_girl);
        } else {
            holder.iv_sex.setImageResource(R.mipmap.img_moren);
        }

        holder.tv_name.setText(list.get(position).getUserName());
        holder.tv_phone.setText(list.get(position).getMobile());
        holder.tv_yc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlet(list.get(position).getId(), position);
            }
        });


    }

    private void showAlet(final String id, final int dex) {
        MyAlertDialog dialog = new MyAlertDialog(context).builder();
//        dialog.setTitle("");
        final String type;
        if (isAdmin == 1) {
            dialog.setMsg("确认移除该员工？");
            type = "2";
        } else {
            dialog.setMsg("确认退出该企业？");
            type = "3";
        }

        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delet(id, dex, type);
            }
        });

        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.show();

//        dialog.builder().setTitle("").setMsg("确定移除该员工").setPositiveButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             delet(id,dex);
//            }
//        }).setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }).show();
    }

    private void delet(String id, final int dex, String type) {
        RequestParam param = new RequestParam();
        EncryptionTools.initEncrypMD5(entity.getToken() == null ? "" : entity.getToken());
        param.add("token", entity.getToken() == null ? "" : entity.getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("operType", type);
        param.add("id", id);
        OkHttpUtils.post(Config.OPERUSER, param, new OkHttpCallBack(context, "加载中...") {
            @Override
            public void onSuccess(String data) {
//             list.remove(dex);
//             notifyDataSetChanged();
                update.update();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(context, error);
            }
        });
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


    public void ChageDate(List<EmployBean> list, int isAdmin) {
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).getIsEntAdmin().equals("1")){
//                list.remove(i);
//            }
//        }
        this.list = list;
        this.isAdmin = isAdmin;
        gllist.clear();
        yglist.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsAdmin().equals("1")) {
                gllist.add(list.get(i));//管理员
            } else {//员工
                yglist.add(list.get(i));//员工管理
            }
        }
//        Log.e("g",gllist.size()+"");
//        Log.e("y",yglist.size()+"");
        notifyDataSetChanged();
    }

    public static interface upDate {
        void update();
    }


}
