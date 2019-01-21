package com.rch.adatper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.activity.LoginActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.PromptDialog;
import com.rch.entity.BussBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/12/17.
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.MyHolder> {
    private Context context;
    private List<BussBean> list;

    public CircleAdapter(Context context, List<BussBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CircleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_circle, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(CircleAdapter.MyHolder holder, int position) {
        holder.getPostion(position);
        holder.tv_phone.setText(isNull(list.get(position).getMobile()));
        holder.tv_time.setText(isNull(list.get(position).getAuditDateStr()));
        holder.tv_content.setText(isNull(list.get(position).getContent()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyDateChange(List<BussBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_person;
        TextView tv_phone, tv_time, tv_content;
        LinearLayout ll_call;
        int dex;

        public MyHolder(View itemView) {
            super(itemView);
            iv_person = (ImageView) itemView.findViewById(R.id.iv_person);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ll_call = (LinearLayout) itemView.findViewById(R.id.ll_call);

            ll_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ToastTool.show(context,"点击了"+dex+"位置");
                    if (SpUtils.getIsLogin(context)) {//登录
                        if (!ButtonUtils.isFastDoubleClick(view.getId()))
                            getCircleMobile(list.get(dex).getId());
                    } else {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                }
            });
        }

        public void getPostion(int dex) {
            this.dex = dex;
        }


    }

    /**
     * 查看联系方式
     */
    private void getCircleMobile(String id) {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.GETCIRCLEMOBILE_URL, param, new OkHttpCallBack(context, "") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    String phone = object.getString("result");
                    if (!TextUtils.isEmpty(phone)) {
                        showPhone(context, phone);
                    } else {
                        ToastTool.show(context, "暂无联系方式");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(context, error);
            }
        });

    }

    private PromptDialog dialog;

    private void showPhone(final Context context, final String phone) {

        if (dialog == null)
            dialog = new PromptDialog(context, R.style.LoadingDialogStyle);

//        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.setText(phone);
        dialog.setLeftButtonText("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        dialog.setRightButtonText("拨打", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    private String isNull(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            return mobile;
        } else {
            return "--";
        }
    }

}
