package com.rch.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.rch.R;
import com.rch.activity.LoginActivity;
import com.rch.activity.MaterialPerfectActivity;
import com.rch.base.BaseFragment;
import com.rch.base.MyApplication;
import com.rch.common.Config;
import com.rch.common.DialogTool;
import com.rch.common.EncryptionTools;
import com.rch.common.FileTool;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.ImageCompressTool;
import com.rch.common.ImageLoaderTool;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.CertifiedEntity;
import com.rch.entity.IDCardPhotoEntity;
import com.rch.entity.JsonBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/4/12.
 */

public class PersonalFragment extends BaseFragment implements View.OnClickListener{

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;

    EditText etName,etCode,etAddress;//etEmail,etPwd,etConfirmPwd,
    TextView tvArea,tvOk,tvPopCamera,tvPopPhoto,tvPopEsc,tvSex,tvPopMan,tvPopWoman,tvPopSexEsc;
    ImageView ivPositivePhoto,ivCounterPhoto;

    boolean isVisible;
    View view,popView,popSexView;
    PopupWindow popupWindow;
    List<String> IDCardPhotoList=new ArrayList<>();//提交身份证照片集合
    List<IDCardPhotoEntity> idCardList=new ArrayList<>();//服务器返回的身份证照片
    //sSex:1-男，2-女
    String id="",sName="",sCode="",sSex="",sProvince ="", sCity ="",ctiyText="",sAddress="",sSubmitPositivePhotoPath="",sSubmitCounterPhotoPath="";//,sEmail="",sPwd="",sComfirmPwd=""

    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0x1;//动态请求写入权限
    final int PHOTOCODE=0x2;//动态请求写入权限

    String cameraPath="",sPositivePhotoPath="",sCounterPhotoPath="";

    boolean bPositiveAndCounterPhoto;//true-身份证正面，false-身份证反面

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
            view=inflater.inflate(R.layout.personal_fragment,container,false);
        init();
        return view;
    }


    private void init() {
        if(isVisible) {
            if(view==null)
                return;
            initControl();
        }
    }

    private void initControl() {
       /* etEmail= (EditText) view.findViewById(R.id.personal_email);
        etPwd= (EditText) view.findViewById(R.id.personal_pwd);
        etConfirmPwd= (EditText) view.findViewById(R.id.personal_confirm_pwd);*/
        etName= (EditText) view.findViewById(R.id.personal_name);
        etCode= (EditText) view.findViewById(R.id.personal_code);
        etAddress= (EditText) view.findViewById(R.id.personal_address);

        tvSex= (TextView) view.findViewById(R.id.personal_sex);
        tvArea= (TextView) view.findViewById(R.id.personal_area);
        tvOk= (TextView) view.findViewById(R.id.personal_ok);


        ivPositivePhoto= (ImageView) view.findViewById(R.id.personal_positive_photo);
        ivCounterPhoto= (ImageView) view.findViewById(R.id.personal_counter_photo);

        ImageLoaderTool imageLoaderTool=new ImageLoaderTool(getActivity(),R.mipmap.banner);
        imageLoader=imageLoaderTool.getImageLoader();
        options=imageLoaderTool.getOptions();

//        if(getPersonalDialog()){//不要谈框
//            showPersonalPromptDialog();
//            setPersonalDialog(false);
//        }

        if(entity!=null&&entity.getAuthenticateType().equals("2"))
            initUserCertifiedInfo();

        tvSex.setOnClickListener(this);
        ivPositivePhoto.setOnClickListener(this);
        ivCounterPhoto.setOnClickListener(this);
        tvArea.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @SuppressLint("CheckResult")
    private void initUserCertifiedInfo() {
        sName=entity.getUserName();
        sSex=entity.getUserSex();
        sCode=entity.getLicenseNo();
        sProvince=entity.getProvince();
        sCity=entity.getCity();
        sAddress=entity.getAddress();
        sSubmitPositivePhotoPath=entity.getLicenseUrl();
        sSubmitCounterPhotoPath=entity.getLicenseBackUrl();

        etName.setText(sName);
        etCode.setText(sCode);
        String location=sProvince+sCity;
        tvArea.setText(location.isEmpty()?"请选择省市":location);

        if(tvArea.getText().equals("请选择省市")) {
            tvArea.setTextColor(getResources().getColor(R.color.gray_1));
        }else {
            tvArea.setTextColor(getResources().getColor(R.color.black_1));
        }

        etAddress.setText(sAddress);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.id_card_positive);
        Glide.with(getActivity()).load(entity.getLicenseUrlPath()).apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (ivPositivePhoto == null) {
                            return false;
                        }
                        if (ivPositivePhoto.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            ivPositivePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = ivPositivePhoto.getLayoutParams();
                        int vw = ivPositivePhoto.getWidth() - ivPositivePhoto.getPaddingLeft() - ivPositivePhoto.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + ivPositivePhoto.getPaddingTop() + ivPositivePhoto.getPaddingBottom();
                        ivPositivePhoto.setLayoutParams(params);
                        return false;
                    }
                }).into(ivPositivePhoto);
//        Glide.with(getActivity()).load(entity.getLicenseUrlPath()).placeholder(R.mipmap.id_card_positive).listener(new RequestListener<String, GlideDrawable>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                if (ivPositivePhoto == null) {
//                    return false;
//                }
//                if (ivPositivePhoto.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                    ivPositivePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//                ViewGroup.LayoutParams params = ivPositivePhoto.getLayoutParams();
//                int vw = ivPositivePhoto.getWidth() - ivPositivePhoto.getPaddingLeft() - ivPositivePhoto.getPaddingRight();
//                float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                params.height = vh + ivPositivePhoto.getPaddingTop() + ivPositivePhoto.getPaddingBottom();
//                ivPositivePhoto.setLayoutParams(params);
//                return false;
//            }
//        }).into(ivPositivePhoto);
        // Glide.with(getActivity()).load(entity.getLicenseBackUrlPath()).fitCenter().placeholder(R.mipmap.id_card_counter).into(ivCounterPhoto);
        // 图片等比加载
        RequestOptions optionsDisk = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getActivity()).asBitmap().apply(optionsDisk).load(entity.getLicenseBackUrlPath()).into(new BitmapImageViewTarget(ivCounterPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                int width = resource.getWidth();
                int height = resource.getHeight();
                //获取imageView的宽
                int imageViewWidth= ivCounterPhoto.getWidth();
                //计算缩放比例
                float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
                //计算图片等比例放大后的高
                int imageViewHeight= (int) (height * sy);
                ViewGroup.LayoutParams params = ivCounterPhoto.getLayoutParams();
                params.height = imageViewHeight;
                ivCounterPhoto.setLayoutParams(params);
            }
        });

        if(sSex.equals("1"))
            tvSex.setText("男");
        else if(sSex.equals("2"))
            tvSex.setText("女");
        tvSex.setTextColor(getResources().getColor(R.color.black_1));
    }


    @Override
    protected void onVisible() {
        isVisible =true;
        init();
    }

    @Override
    protected void onInvisible() {
        isVisible =false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.personal_area:
                if(!isLoaded)
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                else
                    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
                break;
            case R.id.personal_ok:
                verif();
                break;
            case R.id.personal_positive_photo:
                bPositiveAndCounterPhoto=true;
                initPaperwordPhotoPopLayout();
                break;
            case R.id.personal_counter_photo:
                bPositiveAndCounterPhoto=false;
                initPaperwordPhotoPopLayout();
                break;
            case R.id.personal_sex:
                initSexPopLayout();
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
                tvSex.setText("男");
                tvSex.setTextColor(getResources().getColor(R.color.black_1));
                sSex="1";
                popupWindow.dismiss();
                break;
            case R.id.pop_woman:
                tvSex.setText("女");
                tvSex.setTextColor(getResources().getColor(R.color.black_1));
                sSex="2";
                popupWindow.dismiss();
                break;
            case R.id.pop_sex_esc:
                popupWindow.dismiss();
                break;
        }
    }


    Dialog dialog;
    /*个人认证提示框*/
    private void showPersonalPromptDialog()
    {
        View dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.personal_perompt_dialog,null,false);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog=new Dialog(getActivity());
        dialog.setContentView(dialogView,params);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView btn = (TextView) dialogView.findViewById(R.id.personal_perompt_btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(dialog!=null)
                    dialog.cancel();
            }
        });
        dialog.show();
    }

    private void showPhoto()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission =  getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTPHOTOPERIMISSINCODE);
                return;
            }
        }
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTOCODE);
    }

    /*调用相机*/
    private void camera()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission =  getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadContactsPermission =  getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasCameraContactsPermission =  getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED||hasReadContactsPermission!=PackageManager.PERMISSION_GRANTED||hasCameraContactsPermission!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUESTWRITEPERIMISSINCODE);
                return;
            }
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraPath = FileTool.path + System.currentTimeMillis() + ".jpg";
        File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
        // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri uri;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.rch.fileprovider", outFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else {   // 把文件地址转换成Uri格式
            uri = Uri.fromFile(outFile);
        }
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, CAMERACODE);
        Log.e("getAbsolutePath",outFile.getAbsolutePath());



        // 指定开启系统相机的Action
    /*    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            cameraPath = FileTool.path + System.currentTimeMillis() + ".jpg";
            File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Uri uri;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.rch.fileprovider", outFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            else {   // 把文件地址转换成Uri格式
                uri = Uri.fromFile(outFile);
            }
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１
            //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, CAMERACODE);
            Log.e("getAbsolutePath",outFile.getAbsolutePath());
        }*/

    }

    /*初始底部相机选择框*/
    private void initPaperwordPhotoPopLayout()
    {
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.camera_layout, null);
        tvPopCamera= (TextView) popView.findViewById(R.id.pop_camera);
        tvPopPhoto= (TextView) popView.findViewById(R.id.pop_photo);
        tvPopEsc= (TextView) popView.findViewById(R.id.pop_esc);
        tvPopCamera.setOnClickListener(this);
        tvPopPhoto.setOnClickListener(this);
        tvPopEsc.setOnClickListener(this);
        popupWindow= DialogTool.showPopWindow(getActivity(),popView);
    }


    /*初始底部男女选择框*/
    private void initSexPopLayout()
    {
        hideKeys();;
        popSexView = LayoutInflater.from(getActivity()).inflate(R.layout.sex_layout, null);
        tvPopMan= (TextView) popSexView.findViewById(R.id.pop_man);
        tvPopWoman= (TextView) popSexView.findViewById(R.id.pop_woman);
        tvPopSexEsc= (TextView) popSexView.findViewById(R.id.pop_sex_esc);
        tvPopMan.setOnClickListener(this);
        tvPopWoman.setOnClickListener(this);
        tvPopSexEsc.setOnClickListener(this);
        popupWindow= DialogTool.showPopWindow(getActivity(),popSexView);
    }


    private void verif()
    {
        IDCardPhotoList.clear();
        if(!sPositivePhotoPath.isEmpty())
            IDCardPhotoList.add(sPositivePhotoPath);
        if(!sCounterPhotoPath.isEmpty())
            IDCardPhotoList.add(sCounterPhotoPath);
        if(IDCardPhotoList.size()>0)
            uploadPhoto();
        else
            submit();
    }

    /*照片上传*/
    private void uploadPhoto(){
        if(!submitVerify())
            return;
        upLoadingShow("照片上传中...");
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param=new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        for (int i=0;i<IDCardPhotoList.size();i++)
            param.addFile("f1",new File(IDCardPhotoList.get(i)));
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(getActivity(),"上传中...") {
            @Override
            public void onSuccess(String data) {
                idCardList= JsonTool.getIDCardPhoto(data);
                if(idCardList.size()==1)
                {
                    if(!sPositivePhotoPath.isEmpty())
                        sSubmitPositivePhotoPath=idCardList.get(0).getImageUrl();
                    else
                        sSubmitCounterPhotoPath=idCardList.get(0).getImageUrl();
                }else if(idCardList.size()==2)
                {
                    sSubmitPositivePhotoPath=idCardList.get(0).getImageUrl();
                    sSubmitCounterPhotoPath=idCardList.get(1).getImageUrl();
                }
                upLoadingClose();
                submit();
            }
            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(getActivity());
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                ToastTool.show(getActivity(),error);

            }
        });
    }


    /*提交注册信息*/
    public boolean submitVerify()
    {
      /*  sEmail=etEmail.getText().toString().trim();
        sPwd=etPwd.getText().toString().trim();
        sComfirmPwd=etConfirmPwd.getText().toString().trim();*/
        sName=etName.getText().toString().trim();
        sCode=etCode.getText().toString().trim();
        sAddress=etAddress.getText().toString().trim();
     /*   if(sEmail.isEmpty())
        {
            ToastTool.show(getActivity(),"邮箱不能为空！");
            return false;
        }
        else if(!RegTool.isEmail(sEmail))
        {
            ToastTool.show(getActivity(),"邮箱格式不正确！");
            return false;
        }
        else if(sPwd.isEmpty())
        {
            ToastTool.show(getActivity(),"密码不能为空！");
            return false;
        }
        else if(sPwd.length()<6)
        {
            ToastTool.show(getActivity(),"密码长度不能少于6位！");
            return false;
        }
        else if(sComfirmPwd.isEmpty())
        {
            ToastTool.show(getActivity(),"确认密码不能为空！");
            return false;
        }
        else if(sComfirmPwd.length()<6)
        {
            ToastTool.show(getActivity(),"确定密码长度不能少于6位！");
            return false;
        }else if(!sPwd.equals(sComfirmPwd))
        {
            ToastTool.show(getActivity(),"2次密码不匹配，请重新输入！");
            return false;
        }*/
        if(sName.isEmpty())
        {
            ToastTool.show(getActivity(),"请输入姓名！");
            return false;
        }
        else if(sCode.isEmpty())
        {
            ToastTool.show(getActivity(),"请输入身份证！");
            return false;
        }
        else if(!RegTool.isValidateIdCard(sCode))
        {
            ToastTool.show(getActivity(),"身份证不能格式不正确！");
            return false;
        }
        else if(sCity.isEmpty())
        {
            ToastTool.show(getActivity(),"请选择所在地！");
            return false;
        }
        else if(sAddress.isEmpty())
        {
            ToastTool.show(getActivity(),"请输入地址！");
            return false;
        }
        return true;
    }

    /*提交注册信息*/
    private void submit()
    {
        if(!submitVerify())
            return;
        else if(sSubmitPositivePhotoPath.isEmpty())
        {
            ToastTool.show(getActivity(),"请上传正面照片！");
            return;
        }
        else if(sSubmitCounterPhotoPath.isEmpty())
        {
            ToastTool.show(getActivity(),"请上传反面照片！");
            return;
        }
        upLoadingShow();
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param=new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("authenticateType","2");
   /*     param.add("authenticateType","2");
        param.add("enterpriseName","");
        param.add("enterpriseUrl","");
        param.add("enterpriseProvince","");
        param.add("enterpriseCity","");
        param.add("enterpriseAddress","");*/
        param.add("userName",sName);
        param.add("userSex",sSex);
        param.add("licenseNo",sCode);
        param.add("province",sProvince);
        param.add("city",sCity);
        param.add("address",sAddress);
        param.add("licenseUrl",sSubmitPositivePhotoPath);//身份证正面照片
        param.add("licenseBackUrl",sSubmitCounterPhotoPath);//身份证反面照片
        OkHttpUtils.post(Config.SUBMITREGISTER, param, new OkHttpCallBack(getActivity(),"加载中...") {
            @Override
            public void onSuccess(String data) {
                getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
                startActivity(new Intent(getActivity(), MaterialPerfectActivity.class));
                upLoadingClose();
                getActivity().finish();
            }

            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(getActivity());
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                ToastTool.show(MyApplication.getInstance(),error);
            }
        });
    }






    /*城市选择器*/
    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                sProvince =options1Items.get(options1).getPickerViewText();
                sCity =  options2Items.get(options1).get(options2);
                // area= options3Items.get(options1).get(options2).get(options3);
               /* locationText = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);*/
                ctiyText= sProvince + sCity;
                tvArea.setText(ctiyText);
                tvArea.setTextColor(getResources().getColor(R.color.black_1));
                // ToastTool.show(AddressAddOrEdit.this,locationText);
            }
        })

               /*  .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
               .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(12)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();*/

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setTitleColor(Color.BLUE)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(getResources().getColor(R.color.gray))//标题背景颜色 Night mode
//                .setContentTextSize(20)
//                .build();

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.gray_13))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.gray_3))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.gray))//标题背景颜色 Night mode
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded=true;
                    hideKeys();
                    ShowPickerView();
                    break;

                case MSG_LOAD_FAILED:
                    ToastTool.show(getActivity(),"解析数据出错");
                    break;

            }
        }
    };


    private void hideKeys()
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvArea.getWindowToken(), 0); //强制隐藏键盘
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(getActivity(),"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if(requestCode==CAMERACODE)
            {
                Log.e("path=" , cameraPath);
                Log.e("ss","123");
                cameraPath= ImageCompressTool.compressImageToFile(cameraPath);
                Log.e("ss","1234");
                Log.e("压缩完path=" , cameraPath);
                paperworkPhotoPaht(cameraPath);
            }
            else if(requestCode==PHOTOCODE)
            {
                Uri selectedImage = data.getData();
                Log.e("uri",String.valueOf(selectedImage));
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                Log.e("imagePath",imagePath);
                imagePath=ImageCompressTool.compressImageToFile(imagePath);
                paperworkPhotoPaht(imagePath);
                /*Bitmap bm = BitmapFactory.decodeFile(imaePath);*/
            }
        }
    }

    private void paperworkPhotoPaht(String photoUrl) {
        if(bPositiveAndCounterPhoto) {
            sPositivePhotoPath=photoUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.id_card_positive);
            Glide.with(getActivity()).load("file://" + photoUrl).apply(options).into(ivPositivePhoto);
            //imageLoader.displayImage("file://" + photoUrl, ivPositivePhoto, options);
        }
        else {
            sCounterPhotoPath=photoUrl;
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.id_card_counter);
            Glide.with(getActivity()).load("file://" + photoUrl).apply(options).into(ivCounterPhoto);
            //  Glide.with(getActivity()).load("file://" + photoUrl).override(630,380).placeholder(R.mipmap.id_card_counter).into(ivCounterPhoto);
         /*   Glide.with(getActivity()).load(sCounterPhotoPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(ivCounterPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    int bwidht=resource.getWidth();
                    int bheight=resource.getHeight();
                    float sx= (float) ((bwidht*0.1)/(bheight*0.1));
                    Matrix matrix = new Matrix();
                    matrix.postScale(sx,sx); //长和宽放大缩小的比例
                    Bitmap newb = Bitmap.createBitmap(resource,0,0,bwidht, bheight,matrix,true );
                    //获取imageView的宽
                    int imageViewWidth= ivCounterPhoto.getWidth();
                    //计算缩放比例
                    float sy= (float) (imageViewWidth* 0.1)/(float) (width * 0.1);
                    //计算图片等比例放大后的高
                    int imageViewHeight= (int) (height * sy);
                    ViewGroup.LayoutParams params = ivCounterPhoto.getLayoutParams();
                    params.height = imageViewHeight;
                    ivCounterPhoto.setLayoutParams(params);
                }
            });*/
            //imageLoader.displayImage("file://" + photoUrl, ivCounterPhoto, options);
        }
        popupWindow.dismiss();
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
                    ToastTool.show(getActivity(),"请手动开启文件访问权限！");
                    return;
                }
                else if(!permissions[1].equals(Manifest.permission.CAMERA)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许文件读取权限！");
                    ToastTool.show(getActivity(),"请手动开启文件读取权限！");

                    return;
                }
                else if(!permissions[2].equals(Manifest.permission.CAMERA)||grantResults[2]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许相机权限！");
                    ToastTool.show(getActivity(),"请手动开启相机权限！");

                    return;
                }
                camera();
                break;
            case REQUESTPHOTOPERIMISSINCODE:
                if(!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
//                    ToastTool.show(getActivity(),"请允许文件权限！");
                    ToastTool.show(getActivity(),"请手动开启文件访问权限！");

                    return;
                }
                showPhoto();
                break;
        }
    }

    CertifiedEntity entity;
    public void transferData(CertifiedEntity entity)
    {
        this.entity=entity;
        init();
    }


}
