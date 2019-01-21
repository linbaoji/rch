package com.rch.activity;

import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/31.
 */

public class AuthenticateActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout authen_one_layout,authen_two_layout;
    ImageView auth_back;
    TextView tv_finish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        initToolBar();
        initControl();
        AppManager.getAppManager().addActivity(this);
    }

    private void initControl() {
        authen_one_layout= (LinearLayout) findViewById(R.id.authen_one_layout);
        authen_two_layout= (LinearLayout) findViewById(R.id.authen_two_layout);
        auth_back= (ImageView) findViewById(R.id.auth_back);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        tv_finish.setOnClickListener(this);
        authen_one_layout.setOnClickListener(this);
        authen_two_layout.setOnClickListener(this);
        auth_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reg_esc:
                if (authenDialog!=null && authenDialog.isShowing()){
                    authenDialog.dismiss();
                    authenDialog = null;
                }
                break;
            case R.id.tv_zbrz:
                if (authenDialog!=null && authenDialog.isShowing()){
                    authenDialog.dismiss();
                    authenDialog = null;
                }
                //去首页
                startActivity(new Intent(this, NewMainActivity.class).putExtra("from_authen",true));
                finish();
                break;
            case R.id.tv_finish:
                showDialog();
                break;
            case R.id.auth_back:
                finish();
                break;
            case R.id.authen_one_layout://认证车商
                String type=String.valueOf(v.getTag());
                startActivity(new Intent(this,CorporateInfoActivity.class));
                break;
            case R.id.authen_two_layout://认证分销商
                startActivity(new Intent(this,DistributorActivity.class));
                break;
        }
    }

    Dialog authenDialog;
    ImageView reg_esc;
    TextView tv_zbrz;
    private void showDialog()
    {
        authenDialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        View view= LayoutInflater.from(this).inflate(R.layout.authentication_dialog_layout,null);
        authenDialog.setContentView(view);

        authenDialog.setCanceledOnTouchOutside(false);
        authenDialog.setCancelable(false);
        Window dialogWindow = authenDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager manager=dialogWindow.getWindowManager();
        Display d=manager.getDefaultDisplay();
        lp.width=d.getWidth();
//        lp.x = 0;
//        lp.y = 0;
        dialogWindow.setAttributes(lp);
        tv_zbrz= (TextView) view.findViewById(R.id.tv_zbrz);
        reg_esc= (ImageView) view.findViewById(R.id.reg_esc);
        tv_zbrz.setOnClickListener(this);
        reg_esc.setOnClickListener(this);
        authenDialog.setCancelable(false);
        authenDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    return false;
                }
                return false;
            }
        });
        authenDialog.show();
    }
}
