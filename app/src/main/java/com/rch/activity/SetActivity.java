package com.rch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.CacheUtils;
import com.rch.common.Config;
import com.rch.common.GeneralUtils;
import com.rch.common.GsonUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.RoncheUtil;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.PromptDialog;
import com.rch.entity.VersionBean;
import com.rch.fragment.BusinessCircleFragment;
import com.rch.fragment.HomeFragment;
import com.rch.fragment.MyFragment;
import com.rch.gildeCatch.GlideCatchUtil;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SetActivity extends BaseActivity implements View.OnClickListener {
    TextView cacheSize, tvEsc,tvVersion;
    LinearLayout lAbout, lPwd, lClear,lVersion,ll_update,set_score_layout;
    ImageView ivBack;

    private GeneralUtils generalUtils;

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initToolBar();
        initControls();

    }

    private void initControls() {


        lAbout = (LinearLayout) findViewById(R.id.set_about_layout);
        lPwd = (LinearLayout) findViewById(R.id.set_pwd_layout);
        lClear = (LinearLayout) findViewById(R.id.set_clear_layout);
        lVersion = (LinearLayout) findViewById(R.id.set_version_layout);
        set_score_layout = (LinearLayout) findViewById(R.id.set_score_layout);
        set_score_layout.setOnClickListener(this);

        cacheSize = (TextView) findViewById(R.id.set_cache);
        tvEsc = (TextView) findViewById(R.id.set_esc);
        tvVersion = (TextView) findViewById(R.id.set_version);

        ll_update= (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);

        ivBack= (ImageView) findViewById(R.id.set_back);

//        cacheSize.setText(GlideCatchUtil.getInstance().getCacheSize());
        try {
            cacheSize.setText(CacheUtils.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        if(isLogin()){
            tvEsc.setVisibility(View.VISIBLE);
        }else {
            tvEsc.setVisibility(View.GONE);
        }

        lAbout.setOnClickListener(this);
        lPwd.setOnClickListener(this);
        lClear.setOnClickListener(this);
        tvEsc.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_about_layout:
                //startActivity(new Intent(this, AboutActivity.class));
                Intent intent=new Intent(SetActivity.this,WebActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.set_pwd_layout:
                startActivity(new Intent(this, UpdatePwdActivity.class));
                break;
            case R.id.set_clear_layout:
                clearCache();
                break;
            case R.id.set_esc:
                outLogin();
                break;
            case R.id.set_back:
                finish();
                break;

            case R.id.ll_update:
                registPersim();//动态注册权限并且获取版本更新
//                generalUtils=new GeneralUtils();
//                generalUtils.toVersion(SetActivity.this,"2");
                break;
            case R.id.set_score_layout:

                break;
        }
    }

    final int REQUESTWRITEPERIMISSINCODE=102;//读写权限
    private void registPersim() {
        RequestParam param = new RequestParam();
        param.add("versionType","1");//android
        OkHttpUtils.post(Config.APPVERSION, param, new OkHttpCallBack(SetActivity.this,"") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject=new JSONObject(data);
                    JSONObject result=jsonObject.getJSONObject("result");
                    VersionBean bean= GsonUtils.gsonToBean(result.toString(),VersionBean.class);
                    if(Integer.parseInt(bean.getVersionCode()+"")>Integer.parseInt(RoncheUtil.getVersionCode(SetActivity.this))){

                        mPermissionList.clear();
                        for (int j = 0; j < permissions.length; j++) {
                            if (ContextCompat.checkSelfPermission(SetActivity.this, permissions[j]) != PackageManager.PERMISSION_GRANTED) {
                                mPermissionList.add(permissions[j]);
                            }
                        }

                        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissions, REQUESTWRITEPERIMISSINCODE);
                            }
                        }else {
                            generalUtils = GeneralUtils.getInstance();
                            generalUtils.toVersion(SetActivity.this,"2");
                        }

                    }else {
                        ToastTool.show(SetActivity.this,"当前已经是最新版本");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onError(int code, String error) {

            }
        });


    }

    PromptDialog dialog;
    private void clearCache()
    {
        dialog =new PromptDialog(this);
        dialog.setText("您确定要清除本地缓存吗？");
        dialog.setLeftButtonText("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        dialog.setRightButtonText("确定", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                GlideCatchUtil.getInstance().clearCacheMemory();
                GlideCatchUtil.getInstance().cleanCatchDisk();
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                CacheUtils.clearAllCache(getApplicationContext());
                try {
                    cacheSize.setText(CacheUtils.getTotalCacheSize(getApplicationContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void outLogin()
    {
        dialog =new PromptDialog(this);
//        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.setText("确定要退出当前账户？");
        dialog.setLeftButtonText("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        dialog.setRightButtonText("确定", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                clearAll();
                saveUser("","");
                //sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
                dialog.dismiss();
                SpUtils.setIsLogin(SetActivity.this,false);
                SpUtils.setToken(SetActivity.this,"");
                startActivity(new Intent(SetActivity.this,LoginActivity.class));
                sendBroadcast(new Intent(BusinessCircleFragment.TEXT_RECEIVER));
                finish();
            }
        });

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTWRITEPERIMISSINCODE:
//                if(!permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
//                {
//                    ToastTool.show(NewMainActivity.this,"请手动开启文件写入权限！");
//                    return;
//                }
//                else if(!permissions[1].equals(android.Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
//                {
//                    ToastTool.show(NewMainActivity.this,"请手动开启文件读取权限！");
//                    return;
//                }

                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){//拒绝
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SetActivity.this, permissions[i]);
                        if (showRequestPermission) {//是否被勾选默认没
                            ToastTool.show(SetActivity.this,"版本更新需要您允许文件读取权限，请允许访问文件读取权限！");
                            return;
                        }else {//勾选上了
                            ToastTool.show(SetActivity.this,"版本更新需要您允许文件读取权限,请前往手机设置页手动开启文件读取权限！");
                            return;
                        }
                    }
                    registPersim();//动态注册权限并且获取版本更新
                }
                break;

        }
    }

}
