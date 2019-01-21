package com.rch.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.custom.MyAlertDialog;


/**
 * Created by Administrator on 2018/8/27.
 */

public class ShareDialog{
    private Context context;
    private Dialog dialog;
    private Display display;

    TextView tv_name;
    TextView tv_lv;
    TextView tv_time,tv_dec;
    private LinearLayout ll_share,ll_sharepyq;
    private TextView cancle;

    public ShareDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ShareDialog build(){
        View view = LayoutInflater.from(context).inflate(R.layout.mysharebottom_dialog, null);
         tv_name= (TextView) view.findViewById(R.id.tv_name);
         ll_share= (LinearLayout) view.findViewById(R.id.ll_share);
         ll_sharepyq= (LinearLayout) view.findViewById(R.id.ll_sharepyq);
         TextView cancle= (TextView) view.findViewById(R.id.tv_cancle);
         cancle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog.dismiss();
             }
         });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager manager=dialogWindow.getWindowManager();
        Display d=manager.getDefaultDisplay();
        lp.width=d.getWidth();
//        lp.x = 0;
//        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

//    @Override
//    public void onClick(View v) {
//     switch (v.getId()){
//         case R.id.tv_cancle:
//             dialog.dismiss();
//             break;
//
//         case R.id.ll_share:
//             goshare();
//             break;
//
//         case R.id.ll_sharepyq:
//             goshare();
//             break;
//     }
//
//    }
    public ShareDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ShareDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void dissmiss() {
        dialog.dismiss();
    }

    public ShareDialog setName(String name) {
       tv_name.setText(name);
        return this;
    }

    public ShareDialog setlv(String lv) {
        tv_lv.setText(lv);
        return this;
    }

    public ShareDialog settime(String time) {
        tv_time.setText(time);
        return this;
    }

    public ShareDialog setdec(String dec) {
        tv_dec.setText(dec);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public ShareDialog setWeixin(final View.OnClickListener listener) {

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(v);
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });
        return this;
    }

    public ShareDialog setWeixinPyq(final View.OnClickListener listener) {

        ll_sharepyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(v);
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });
        return this;
    }


}
