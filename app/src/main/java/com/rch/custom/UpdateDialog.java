package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.common.SpUtils;

/**
 * Created by Administrator on 2018/7/19.
 */

public class UpdateDialog{
    private Context context;
    private Dialog dialog;
    private Display display;
    String path,ismandatory,dec,name;
    ImageView iv_deleat,iv_update;
    TextView tv_name,tv_dec1;
    CheckBox ckbox;
    String type;
    String versionCode;

    public UpdateDialog(Context context, String ismandatory, String dec, String name,String type,String versionCode) {
        this.context = context;
        this.ismandatory = ismandatory;
        this.dec = dec;
        this.name = name;
        this.type=type;
        this.versionCode=versionCode;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public UpdateDialog builder(){
        View view = LayoutInflater.from(context).inflate(R.layout.updatedialog, null);

        iv_deleat= (ImageView)view.findViewById(R.id.iv_delet);//不更新
        iv_update= (ImageView) view.findViewById(R.id.iv_upadate);//跟新
        tv_dec1= (TextView)view.findViewById(R.id.tv_dec1);//描述
        tv_name= (TextView)view.findViewById(R.id.tv_name);//标题
        ckbox= (CheckBox) view.findViewById(R.id.ck_box);
        iv_deleat.setVisibility(View.INVISIBLE);
        if(ismandatory.equals("1")){ //强制跟新
           iv_deleat.setVisibility(View.INVISIBLE);
           iv_deleat.setEnabled(false);
            ckbox.setVisibility(View.GONE);
        }else {
            iv_deleat.setVisibility(View.VISIBLE);
            iv_deleat.setEnabled(true);
            iv_deleat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            if(type.equals("1")) {
                ckbox.setVisibility(View.VISIBLE);
            }else {
                ckbox.setVisibility(View.GONE);
            }

//            if(SpUtils.getIsShowUpdate(context,versionCode)){
//                ckbox.setChecked(false);
//            }else {
//                ckbox.setChecked(true);
//            }

            ckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){//选中的话表示不用每次提醒
                        SpUtils.setIsShowUpdate(context,versionCode,false);
                    }else {
                        SpUtils.setIsShowUpdate(context,versionCode,true);
                    }
                }
            });
        }

        if(!dec.contains("n")) {
            tv_dec1.setText(dec);
        }else {
            tv_dec1.setText(dec.replace("\\n","\n"));
        }
        tv_name.setText(name);

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

//        lp.x = 0;
//        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }


    public UpdateDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public UpdateDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void DissMiss(){
        dialog.dismiss();
    }






    /**
     * 设置不更新
     * @param listener
     */
    public void setDeleatClick(final UpdateDialog.OnClickListener listener)
    {
            iv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onClick();
                        dialog.dismiss();
                }
            });
        }


    public interface OnClickListener{
        public void onClick();
    }

    }






