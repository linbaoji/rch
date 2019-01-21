package com.rch.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CodeUtils;
import com.rch.common.Config;
import com.rch.common.DialogTool;
import com.rch.common.EncryptionTools;
import com.rch.common.FileTool;
import com.rch.common.GlideUtils;
import com.rch.common.GsonUtils;
import com.rch.common.ImageCompressTool;
import com.rch.common.JsonTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.IDCardPhotoEntity;
import com.rch.entity.UserInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/7/20.
 */

public class PerfectInfoActivity extends BaseActivity implements View.OnClickListener {

    ImageView perfect_back,img;
    TextView perfect_phone,perfect_ok,perfect_send_code;
    EditText perfect_price,perfect_code;

    LinearLayout img_layout;
    String phone="";

    CodeUtils codeUtils;

    String perfectPrice="",codeText="";
    View popView;

    String cameraPath="",sPhootPath="";
    List<IDCardPhotoEntity> idCardList=new ArrayList<>();//服务器返回的身份证照片
    String sServicePhotoPath ="";

    String str="";
    String id="",brandId="",carName="",sProvince="",sCity="",ctiyText="",sTime="",mileage="";

    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0x1;//动态请求写入权限
    final int PHOTOCODE=0x2;//动态请求写入权限

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);
        initToolBar();
        phone=getIntent().getStringExtra("phone");
        str=getIntent().getStringExtra("str");
        initControsl();
    }

    private void initControsl() {
        perfect_back= (ImageView) findViewById(R.id.perfect_back);
        img= (ImageView) findViewById(R.id.img);

        perfect_phone= (TextView) findViewById(R.id.perfect_phone);
        perfect_ok= (TextView) findViewById(R.id.perfect_ok);
        perfect_send_code= (TextView) findViewById(R.id.perfect_send_code);

        perfect_price= (EditText) findViewById(R.id.perfect_price);
        perfect_code= (EditText) findViewById(R.id.perfect_code);

        img_layout= (LinearLayout) findViewById(R.id.img_layout);

        initFastSellData();

        perfect_phone.setText(phone);

        perfect_back.setOnClickListener(this);
        perfect_ok.setOnClickListener(this);
        perfect_send_code.setOnClickListener(this);
        img_layout.setOnClickListener(this);
        img.setOnClickListener(this);
    }

    private void initFastSellData()
    {
        if(str.isEmpty())
            return;
        String[] fastFields=str.split(",");
        id=fastFields[0];
        brandId=fastFields[1];
        carName=fastFields[2];
        sProvince=fastFields[3];
        sCity=fastFields[4];
        ctiyText=fastFields[5];
        sTime=fastFields[6];
        mileage=fastFields[7];
    }


    /*验证图形验证码*/
    private void verifyImageCode()
    {
        String sCodeText=etCodeEt.getText().toString().trim();
        //获取图形验证码
        String codeStr = codeUtils.getCode();
        if(sCodeText.equalsIgnoreCase(codeStr)) {
            if (dialog != null)
                dialog.cancel();
            requestCodeData();
        }else
            ToastTool.show(this,"验证码不正确！");
    }


    String strPhone;
    /*短信验证码是否超过3次*/
    private void isExceedSendCodeAmount()
    {
        strPhone=perfect_phone.getText().toString().trim();
        if(strPhone.isEmpty())
        {
            ToastTool.show(this,"手机号码不能为空！");
            return;
        }
        else if(!RegTool.isMobile(strPhone))
        {
            ToastTool.show(this,"手机号码不正确！");
            return;
        }
        RequestParam param = new RequestParam();
        param.add("mobile",strPhone);
        param.add("type","1");
        OkHttpUtils.post(Config.SENDNUMBER, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                int count = JsonTool.getResultStr(data,"smsNumber").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"smsNumber"));
                if(count>=3)
                    showCodeImage();
                else
                    requestCodeData();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(PerfectInfoActivity.this,error);
            }
        });
    }


    Dialog dialog;
    TextView tvCodeOk;
    ImageView ivCodeImage;
    EditText etCodeEt;
    /*显示验证码图片*/
    private void showCodeImage()
    {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();

        View view= LayoutInflater.from(this).inflate(R.layout.image_code_layout,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog=new Dialog(this);
        dialog.addContentView(view,params);
        Window window=dialog.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        tvCodeOk= (TextView) view.findViewById(R.id.code_ok);
        etCodeEt= (EditText) view.findViewById(R.id.code_et);
        ivCodeImage= (ImageView) view.findViewById(R.id.code_image);


        ivCodeImage.setImageBitmap(bitmap);

        tvCodeOk.setOnClickListener(this);
        ivCodeImage.setOnClickListener(this);
        dialog.show();
    }


    /*请求验证码*/
    private void requestCodeData()
    {
        RequestParam param = new RequestParam();
        param.add("mobile",strPhone);
        OkHttpUtils.post(Config.CODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(PerfectInfoActivity.this,error);
            }
        });
    }

    int interval=1000;
    int countdown=60;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(countdown>0) {
                countdown--;
                perfect_send_code.setEnabled(false);
                //code.setText("重新获取验证码("+countdown+")");
                perfect_send_code.setText(String.valueOf(countdown)+"S"+"重新获取");
            }
            else {
                perfect_send_code.setText("重新获取");
                perfect_send_code.setEnabled(true);
                countdown = 60;
                stopTime();
            }
        }
    };


    Timer timer=null;
    TimerTask timerTask=null;

    /**
     * 开启定时器
     */
    private void startTime()
    {
        if(timer==null)
        {
            timer=new Timer();
            if(timerTask==null)
            {
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                };
            }
            timer.schedule(timerTask,interval,interval);
        }
    }

    /**
     * 停止定时器
     */
    private void stopTime()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer.purge();
            timer=null;
        }
        if(timerTask!=null)
        {
            timerTask.cancel();
            timerTask=null;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTime();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.perfect_ok:
                verif();
                break;
            case R.id.perfect_send_code:
                if(!ButtonUtils.isFastDoubleClick(v.getId()))
                    isExceedSendCodeAmount();
                break;
            case R.id.perfect_back:
                finish();
                break;
            case R.id.img_layout:
            case R.id.img:
                initPaperwordPhotoPopLayout();
                break;
            case R.id.code_ok:
                verifyImageCode();
                break;
            case R.id.code_image:
                ivCodeImage.setImageBitmap(codeUtils.createBitmap());
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
        }
    }


    PopupWindow popupWindow;
    TextView tvPopCamera,tvPopPhoto,tvPopEsc;
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
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTOCODE);
    }

    /*调用相机*/
    private void camera()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasWriteContactsPermission =  this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadContactsPermission =  this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasCameraContactsPermission =  this.checkSelfPermission(Manifest.permission.CAMERA);
            if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED||hasReadContactsPermission!=PackageManager.PERMISSION_GRANTED||hasCameraContactsPermission!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUESTWRITEPERIMISSINCODE);
                return;
            }
        }
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            cameraPath = FileTool.path + System.currentTimeMillis() + ".jpg";
            File outFile = FileTool.createFile(FileTool.path, System.currentTimeMillis() + ".jpg");
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Uri uri;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.rch.fileprovider", outFile);
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
        }

    }

    private void verif()
    {
        if(!sPhootPath.isEmpty())
            uploadPhoto();
        else
            submit();
    }

    private void submit() {
        if(!submitVerify())
            return;
        else if(sServicePhotoPath.isEmpty())
        {
            ToastTool.show(this,"请上传图片！");
            return;
        }
        RequestParam param = new RequestParam();
        param.add("source","3");
        param.add("mobile", phone);
        param.add("brandId",brandId);
        param.add("modelId", id);
        param.add("registrationProv", sProvince);
        param.add("registrationCity",sCity);
        param.add("registrationTime",sTime);
        param.add("showMileage",mileage);
        param.add("hopingPrice",perfectPrice);
        param.add("imageUrl",sServicePhotoPath);
        param.add("validCode",codeText);
        param.add("type","1");
        OkHttpUtils.post(Config.SELLCAR, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                clearSellData();
                startActivity(new Intent(PerfectInfoActivity.this,SeachCarServerSubmitActivity.class).putExtra("type",1));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(PerfectInfoActivity.this,error);
            }
        });
    }


    /*提交注册信息--验证*/
    public boolean submitVerify()
    {
        perfectPrice=perfect_price.getText().toString().trim();
        codeText=perfect_code.getText().toString().trim();
        if(perfectPrice.isEmpty())
        {
            ToastTool.show(this,"期望售价不能为空！");
            return false;
        }
        else if(Double.parseDouble(perfectPrice)<1)
        {
            ToastTool.show(this,"期望售价不能低于10000！");
            return false;
        }
        else if(!RegTool.isMileage(perfectPrice))
        {
            ToastTool.show(this,"期望售价有误！");
            return false;
        }
        else if(!RegTool.isMobile(phone))
        {
            ToastTool.show(this,"手机号码不正确！");
            return false;
        }
        else if(codeText.isEmpty())
        {
            ToastTool.show(this,"验证码不能为空！");
            return false;
        }
        return true;
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
        param.addFile("f1",new File(sPhootPath));
        param.add("source","3");
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(this,"上传中...") {
            @Override
            public void onSuccess(String data) {
                idCardList= JsonTool.getIDCardPhoto(data);
                if(idCardList.size()==1)
                    sServicePhotoPath =idCardList.get(0).getImageUrl();
                upLoadingClose();
                submit();
            }
            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(PerfectInfoActivity.this);
                    startActivity(new Intent(PerfectInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(PerfectInfoActivity.this,error);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if(requestCode==CAMERACODE)
            {
                Log.e("path=" , cameraPath);
                cameraPath= ImageCompressTool.compressImageToFile(cameraPath);
                paperworkPhotoPaht(cameraPath);
            }
            else if(requestCode==PHOTOCODE)
            {
                Uri selectedImage = data.getData();
                Log.e("uri",String.valueOf(selectedImage));
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                Log.e("imagePath",imagePath);
                imagePath=ImageCompressTool.compressImageToFile(imagePath);
                //imagePath = copyRenamePhoto(imagePath);
                paperworkPhotoPaht(imagePath);
                //paperworkPhotoPaht(imagePath);
                /*Bitmap bm = BitmapFactory.decodeFile(imaePath);*/
            }
        }
    }

    /*拷贝文件-并修改照片名称*/
    private String copyRenamePhoto(String path)
    {
        Random random=new Random();
        String timesTamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+  random.nextInt(10000);
        String photoName=timesTamp+".jpg";
        FileTool.copyFile(this,path,photoName);
        return  FileTool.path+photoName;
    }

    private void paperworkPhotoPaht(String photoUrl) {
        sPhootPath=photoUrl;
        img.setVisibility(View.VISIBLE);
        img_layout.setVisibility(View.GONE);
        GlideUtils.showImg(this,"file://" + photoUrl,R.mipmap.banner,img);
//        Glide.with(this).load("file://" + photoUrl).placeholder(R.mipmap.banner).into(img);
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
                    ToastTool.show(this,"请手动开启文件访问权限！");
                    return;
                }
                else if(!permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
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

}

