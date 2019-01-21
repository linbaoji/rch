package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;


/**
 * Created by Administrator on 2018/8/27.
 */

public class LookDialog implements View.OnClickListener{
    private Context context;
    private Dialog dialog;
    private Display display;
    private ImageView iv_sl;
    private LinearLayout ll_iv;

    public LookDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public LookDialog build(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_look, null);
        iv_sl= (ImageView) view.findViewById(R.id.iv_sl);
        ll_iv = (LinearLayout) view.findViewById(R.id.ll_iv);

        iv_sl.setOnClickListener(this);
        ll_iv.setOnClickListener(this);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager manager=dialogWindow.getWindowManager();
        Display d=manager.getDefaultDisplay();
        lp.width=d.getWidth();
        lp.height=d.getHeight();
//        lp.x = 0;
//        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.ll_iv:
         case R.id.iv_sl:
             if(dialog.isShowing()){
                 dialog.dismiss();
             }else {
                 dialog.show();
             }
             break;
     }
    }



    public LookDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public LookDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void dissmiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }

    public LookDialog setResure(int id) {
        iv_sl.setImageResource(id);
        return this;
    }

}
