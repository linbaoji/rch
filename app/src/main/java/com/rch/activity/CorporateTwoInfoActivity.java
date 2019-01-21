package com.rch.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.base.MyApplication;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.DialogTool;
import com.rch.common.EncryptionTools;
import com.rch.common.FileTool;
import com.rch.common.GsonUtils;
import com.rch.common.ImageCompressTool;
import com.rch.common.JsonTool;
import com.rch.common.MyBitmapUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.CertifiedEntity;
import com.rch.entity.IDCardPhotoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class CorporateTwoInfoActivity extends BaseActivity implements View.OnClickListener{
    ImageView corporate_two_info_back,corporate_two_info_id_zm,corporate_two_info_id_fm;
    TextView corporate_two_info_sex,corporate_two_info_ok;
    EditText corporate_two_info_name,corporate_two_info_id;

    View popView,popSexView;
    String str="",type;

    private String photoSavePath;
    private String photoSaveName;
    private CertifiedEntity entity;
    Uri uri;
    File file;

    File zmfile;
    File fmfile;

    //sSex:1-男，2-女
    String legalName="",licenseNo="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_two_info);
        initToolBar();
        initControls();
        photoSavePath = Environment.getExternalStorageDirectory().getPath() + "/Rcchregist/cache/";
        File tempFile = new File(photoSavePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        if(!TextUtils.isEmpty(SpUtils.getCartifi(CorporateTwoInfoActivity.this,SpUtils.getToken(CorporateTwoInfoActivity.this)+"renzhen"))){
            entity=GsonUtils.gsonToBean(SpUtils.getCartifi(CorporateTwoInfoActivity.this,SpUtils.getToken(CorporateTwoInfoActivity.this)+"renzhen"),CertifiedEntity.class);
            setUi();
        }
    }

    private void setUi() {
        if(!TextUtils.isEmpty(entity.getLegalName())){
            corporate_two_info_name.setText(entity.getLegalName());
        }

        if(!TextUtils.isEmpty(entity.getLicenseNo())){
            corporate_two_info_id.setText(entity.getLicenseNo());
        }

        if(!TextUtils.isEmpty(entity.getLicenseUrl())){
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.banner);
            Glide.with(CorporateTwoInfoActivity.this).load(entity.getLicenseUrl()).apply(options).into(corporate_two_info_id_zm);
        }

        if(!TextUtils.isEmpty(entity.getLicenseBackUrl())){
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.banner);
            Glide.with(CorporateTwoInfoActivity.this).load(entity.getLicenseBackUrl()).apply(options).into(corporate_two_info_id_fm);
        }

    }

    private void initControls() {

        corporate_two_info_back= (ImageView) findViewById(R.id.corporate_two_info_back);
        corporate_two_info_id_zm= (ImageView) findViewById(R.id.corporate_two_info_id_zm);
        corporate_two_info_id_fm= (ImageView) findViewById(R.id.corporate_two_info_id_fm);

        corporate_two_info_sex= (TextView) findViewById(R.id.corporate_two_info_sex);
        corporate_two_info_ok= (TextView) findViewById(R.id.corporate_two_info_ok);

        corporate_two_info_name= (EditText) findViewById(R.id.corporate_two_info_name);
        corporate_two_info_id= (EditText) findViewById(R.id.corporate_two_info_id);

        corporate_two_info_back.setOnClickListener(this);
        corporate_two_info_id_zm.setOnClickListener(this);
        corporate_two_info_id_fm.setOnClickListener(this);
        corporate_two_info_sex.setOnClickListener(this);
        corporate_two_info_ok.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.corporate_two_info_back:
                finish();
                break;
            case R.id.corporate_two_info_id_zm:
                bPositiveAndCounterPhoto=true;
                initPaperwordPhotoPopLayout();
                break;
            case R.id.corporate_two_info_id_fm:
                bPositiveAndCounterPhoto=false;
                initPaperwordPhotoPopLayout();
                break;
            case R.id.corporate_two_info_sex:
                initSexPopLayout();
                break;
            case R.id.corporate_two_info_ok:
                if(!ButtonUtils.isFastDoubleClick(R.id.corporate_two_info_ok)){
                if(submitVerify()) {
                    legalName = corporate_two_info_name.getText().toString().trim();
                    licenseNo = corporate_two_info_id.getText().toString().trim();
                    entity.setLegalName(legalName);
                    entity.setLicenseNo(licenseNo);
                    SpUtils.setCartifi(CorporateTwoInfoActivity.this, SpUtils.getToken(CorporateTwoInfoActivity.this) + "renzhen", GsonUtils.bean2Json(entity));
                    submit();
                }
                }
                break;
            case R.id.pop_camera:
                camera();
                break;
            case R.id.pop_photo:
                showPhoto();
                break;
            case R.id.pop_esc:
                popupWindow.dismiss();
                break;
            case R.id.pop_man:
                corporate_two_info_sex.setText("男");
                corporate_two_info_sex.setTextColor(getResources().getColor(R.color.black_1));
//                sSex="1";
                popupWindow.dismiss();
                break;
            case R.id.pop_woman:
                corporate_two_info_sex.setText("女");
                corporate_two_info_sex.setTextColor(getResources().getColor(R.color.black_1));
//                sSex="2";
                popupWindow.dismiss();
                break;
            case R.id.pop_sex_esc:
                popupWindow.dismiss();
                break;
        }
    }

    private void showPhoto()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission =  this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTPHOTOPERIMISSINCODE);
                return;
            }
        }
        //调用相册
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openAlbumIntent, PHOTOCODE);
    }

    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0x1;//动态请求写入权限
    final int PHOTOCODE=0x2;//动态请求写入权限

    /*调用相机*/
    private void camera()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadContactsPermission =  this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasCameraContactsPermission = this.checkSelfPermission(Manifest.permission.CAMERA);
            if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED||hasReadContactsPermission!=PackageManager.PERMISSION_GRANTED||hasCameraContactsPermission!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUESTWRITEPERIMISSINCODE);
                return;
            }
        }
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
            uri = FileProvider.getUriForFile(CorporateTwoInfoActivity.this, CorporateTwoInfoActivity.this.getApplicationContext().getPackageName() + ".provider", new File(photoSavePath, photoSaveName));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(openCameraIntent, CAMERACODE);
        } else {
            ToastTool.show(CorporateTwoInfoActivity.this,"存储卡不存在！");
        }

    }


    TextView tvPopCamera,tvPopPhoto,tvPopEsc,tvPopMan,tvPopWoman,tvPopSexEsc;
    PopupWindow popupWindow;
    /*初始底部相机选择框*/
    private void initPaperwordPhotoPopLayout()
    {
        popView = LayoutInflater.from(this).inflate(R.layout.camera_layout, null);
        tvPopCamera= (TextView) popView.findViewById(R.id.pop_camera);
        tvPopPhoto= (TextView) popView.findViewById(R.id.pop_photo);
        tvPopEsc= (TextView) popView.findViewById(R.id.pop_esc);
        tvPopCamera.setOnClickListener(this);
        tvPopPhoto.setOnClickListener(this);
        tvPopEsc.setOnClickListener(this);
        popupWindow= DialogTool.showPopWindow(this,popView);
    }


    /*初始底部男女选择框*/
    private void initSexPopLayout()
    {
        hideKeys();
        popSexView = LayoutInflater.from(this).inflate(R.layout.sex_layout, null);
        tvPopMan= (TextView) popSexView.findViewById(R.id.pop_man);
        tvPopWoman= (TextView) popSexView.findViewById(R.id.pop_woman);
        tvPopSexEsc= (TextView) popSexView.findViewById(R.id.pop_sex_esc);
        tvPopMan.setOnClickListener(this);
        tvPopWoman.setOnClickListener(this);
        tvPopSexEsc.setOnClickListener(this);
        popupWindow= DialogTool.showPopWindow(this,popSexView);
    }

    private void hideKeys()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(corporate_two_info_sex.getWindowToken(), 0); //强制隐藏键盘
    }



    List<File> IDCardPhotoList=new ArrayList<>();//提交身份证照片集合
    List<IDCardPhotoEntity> idCardList=new ArrayList<>();//服务器返回的身份证照片


    /*照片上传*/
    private void uploadPhoto(){
        upLoadingShow("照片上传中...");
        RequestParam param=new RequestParam();
        param.addFile("f1",file);
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(this,"上传中...") {
            @Override
            public void onSuccess(String data) {
                upLoadingClose();
                idCardList= JsonTool.getIDCardPhoto(data);
                if(idCardList.size()==1) {
                    if(bPositiveAndCounterPhoto) {
                        entity.setLicenseUrl(idCardList.get(0).getImageUrl());
                    }else {
                        entity.setLicenseBackUrl(idCardList.get(0).getImageUrl());
                    }
                }

            }
            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(CorporateTwoInfoActivity.this);
                    startActivity(new Intent(CorporateTwoInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(CorporateTwoInfoActivity.this,error);

            }
        });
    }


    /*提交注册信息*/
    public boolean submitVerify()
    {
        legalName=corporate_two_info_name.getText().toString().trim();
        licenseNo=corporate_two_info_id.getText().toString().trim();

        if(legalName.isEmpty())
        {
            ToastTool.show(this,"请输入姓名！");
            return false;
        }
        if(legalName.length()<2)
        {
            ToastTool.show(this,"请输入至少两位数姓名！");
            return false;
        }

        else if(licenseNo.isEmpty())
        {
            ToastTool.show(this,"请输入身份证！");
            return false;
        }
        else if(!RegTool.isValidateIdCard(licenseNo))
        {
            ToastTool.show(this,"身份证格式不正确！");
            return false;
        }

        if(TextUtils.isEmpty(entity.getLicenseUrl())){
            ToastTool.show(this,"请上传身份证件正面照！");
            return false;
        }
        if(TextUtils.isEmpty(entity.getLicenseBackUrl())){
            ToastTool.show(this,"请上传身份证件反面照！");
            return false;
        }

        return true;
    }

    /*提交注册信息*/
    private void submit()
    {
        upLoadingShow();
        RequestParam param=new RequestParam();
        param.add("authenticateType","1"+"");
        param.add("enterpriseName",entity.getEnterpriseName()+"");
        param.add("enterpriseNo",entity.getEnterpriseNo()+"");
        param.add("enterpriseProvince",entity.getEnterpriseProvince()+"");
        param.add("enterpriseCity",entity.getEnterpriseCity()+"");
        param.add("enterpriseAddress",entity.getEnterpriseAddress()+"");
        param.add("contacts",entity.getContacts()+"");
        param.add("contactsPhone",entity.getContactsPhone()+"");
        param.add("enterpriseUrl",entity.getEnterpriseUrl()+"");
        param.add("legalName",entity.getLegalName()+"");
        param.add("licenseNo",entity.getLicenseNo()+"");
        param.add("licenseUrl",entity.getLicenseUrl()+"");//身份证正面照片
        param.add("licenseBackUrl",entity.getLicenseBackUrl()+"");//身份证反面照片



        OkHttpUtils.post(Config.SUBMITREGISTER, param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {
//                sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
//                startActivity(new Intent(CorporateTwoInfoActivity.this, SeachCarServerSubmitActivity.class).putExtra("type", 3));
                startActivity(new Intent(CorporateTwoInfoActivity.this, VerifySucessActivity.class).putExtra("from","1"));
                AppManager.getAppManager().finishActivity(AuthenticateActivity.class);
                AppManager.getAppManager().finishActivity(CorporateInfoActivity.class);
                upLoadingClose();
                clearCorporateData();
                SpUtils.setCartifi(CorporateTwoInfoActivity.this,SpUtils.getToken(CorporateTwoInfoActivity.this)+"renzhen","");
                finish();
                //更新我的界面认证状态
            }

            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(CorporateTwoInfoActivity.this);
                    startActivity(new Intent(CorporateTwoInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(MyApplication.getInstance(),error);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==CAMERACODE)
            {
                 chage(uri);
            }
            else if(requestCode==PHOTOCODE)
            {
                if(resultCode!=RESULT_OK||data==null){
                    return;
                }
                uri = data.getData();//路径
                String type = data.getType();
                if (type == null) {
                    String realPath = uri.getEncodedPath();//获取图片真实路径
                    boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
                    if (sdCardExist) {
                        String replaceStr = Environment.getExternalStorageDirectory().getPath();
                        if (realPath.contains("external_storage_root")) {
                            realPath = realPath.replaceFirst("/external_storage_root", replaceStr);
                            Log.e("realpath:", realPath);
                            uri = MyBitmapUtils.pathToUri(this, realPath);
                        }

                    }
                }
                chage(uri);

            }

    }



    boolean bPositiveAndCounterPhoto;//true-身份证正面，false-身份证反面



    private void chage(Uri imageUri) {
        Bitmap bit = null;
        if(imageUri!=null) {
            try {
                bit = MyBitmapUtils.getBitmapFormUri(CorporateTwoInfoActivity.this, imageUri);//比例压缩质量压缩
                if (bPositiveAndCounterPhoto) {
                    file = MyBitmapUtils.saveBitmap(bit, "sfzzm.png");//把mitmap转成file文件保存在本地
                    corporate_two_info_id_zm.setImageBitmap(bit);
                } else {
                    file = MyBitmapUtils.saveBitmap(bit, "sfzsm.png");//把mitmap转成file文件保存在本地
                    corporate_two_info_id_fm.setImageBitmap(bit);
                }
                popupWindow.dismiss();
                uploadPhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTWRITEPERIMISSINCODE:
                if(!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许文件写入权限！");
                    ToastTool.show(this,"请手动开启文件访问权限！");
                    return;
                }
                else if(!permissions[1].equals(Manifest.permission.CAMERA)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许文件读取权限！");
                    ToastTool.show(this,"请手动开启文件读取权限！");

                    return;
                }
                else if(!permissions[2].equals(Manifest.permission.CAMERA)||grantResults[2]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许相机权限！");
                    ToastTool.show(this,"请手动开启相机权限！");

                    return;
                }
                camera();
                break;
            case REQUESTPHOTOPERIMISSINCODE:
                if(!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许文件权限！");
                    ToastTool.show(this,"请手动开启文件访问权限！");

                    return;
                }
                showPhoto();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        saveCace();
        super.onDestroy();
    }

    private void saveCace() {
        legalName=corporate_two_info_name.getText().toString().trim();
        licenseNo=corporate_two_info_id.getText().toString().trim();
        entity.setLegalName(legalName);
        entity.setLicenseNo(licenseNo);
        SpUtils.setCartifi(CorporateTwoInfoActivity.this,SpUtils.getToken(CorporateTwoInfoActivity.this)+"renzhen",GsonUtils.bean2Json(entity));
        sendBroadcast(new Intent(CorporateInfoActivity.UPDATECACE));
    }

}
