package com.rch.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.DialogTool;
import com.rch.common.ExamineTextWatcher;
import com.rch.common.GsonUtils;
import com.rch.common.MyBitmapUtils;
import com.rch.common.ProandCityUtil;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.common.ValidateUtil;
import com.rch.custom.LookDialog;
import com.rch.entity.CertifiedEntity;
import com.rch.entity.IDCardPhotoEntity;
import com.rch.entity.JsonBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class CorporateInfoActivity extends BaseActivity implements View.OnClickListener{

    ImageView corporate_info_back,corporate_info_photo;
    private EditText corporate_info_name,corporate_info_address,corporate_info_code,corporate_user_name,corporate_user_phone;
    TextView corporate_info_area,corporate_info_ok,tv_dec,tv_look;
    ImageView corporate_info_up_photo;
    private Gson gson;

    //初始化用户认证信息
    CertifiedEntity entity;

    private String enterpriseName,enterpriseNo,enterpriseProvince,enterpriseCity,enterpriseAddress,contacts,contactsPhone;



    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0x1;//相机
    final int PHOTOCODE=0x2;//相册
    private boolean isLoaded = false;

    String cameraPath="",type="";

    View popView;
    private ProandCityUtil cityutil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coporate_info);
        initToolBar();
        initControls();
        gson=new Gson();
        cityutil=new ProandCityUtil(this);//地区选择
        photoSavePath = Environment.getExternalStorageDirectory().getPath() + "/Registzj/cache/";
        File tempFile = new File(photoSavePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }


        if(!TextUtils.isEmpty(SpUtils.getCartifi(CorporateInfoActivity.this,SpUtils.getToken(CorporateInfoActivity.this)+"renzhen"))){
            entity=GsonUtils.gsonToBean(SpUtils.getCartifi(CorporateInfoActivity.this,SpUtils.getToken(CorporateInfoActivity.this)+"renzhen"),CertifiedEntity.class);
            setUi();
        }else {
            httpUserFillInInfo();
        }

    }

    private void initControls() {
        corporate_info_back= (ImageView) findViewById(R.id.corporate_info_back);
        corporate_info_photo= (ImageView) findViewById(R.id.corporate_info_photo);

        corporate_info_name= (EditText) findViewById(R.id.corporate_info_name);//企业名字
        corporate_info_address= (EditText) findViewById(R.id.corporate_info_address);//企业地址
        corporate_info_code= (EditText) findViewById(R.id.corporate_info_code);//企业编码
        corporate_user_name= (EditText) findViewById(R.id.corporate_user_name);//联系人
        corporate_user_phone= (EditText) findViewById(R.id.corporate_user_phone);//联系人姓名

        corporate_info_area= (TextView) findViewById(R.id.corporate_info_area);//所在省份
        corporate_info_ok= (TextView) findViewById(R.id.corporate_info_ok);

        corporate_info_up_photo= (ImageView) findViewById(R.id.corporate_info_up_photo);
        tv_dec= (TextView) findViewById(R.id.tv_dec);


        tv_look= (TextView) findViewById(R.id.tv_look);
        tv_look.setOnClickListener(this);
        corporate_info_back.setOnClickListener(this);
        corporate_info_up_photo.setOnClickListener(this);
        corporate_info_area.setOnClickListener(this);
        corporate_info_ok.setOnClickListener(this);
        corporate_info_photo.setOnClickListener(this);

        corporate_info_code.addTextChangedListener(new ExamineTextWatcher(ExamineTextWatcher.TYPE_UNIFIEDCODE,corporate_info_code));//变数箱里层匹配正数

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
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,null);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openAlbumIntent, PHOTOCODE);
    }


    private String photoSavePath;
    private String photoSaveName;
    Uri uri;
    File file;



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.corporate_info_back:
                finish();
                break;

            case R.id.corporate_info_area:
                if(!isLoaded)
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                else
                    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
                break;
            case R.id.corporate_info_ok://验证参数值
                if(!ButtonUtils.isFastDoubleClick(R.id.corporate_info_ok)) {
                    if (submitVerify()) {
                        saveCace();
                        startActivity(new Intent(CorporateInfoActivity.this, CorporateTwoInfoActivity.class));
                    }
                }
                break;
            case R.id.corporate_info_up_photo:
            case R.id.corporate_info_photo:
                initPaperwordPhotoPopLayout();
                break;

            case R.id.pop_camera:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                camera();
                break;
            case R.id.pop_photo:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                showPhoto();
                break;
            case R.id.pop_esc:
                popupWindow.dismiss();
                break;

            case R.id.tv_look:
                showDialog();
                break;
        }
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


        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
            uri = FileProvider.getUriForFile(CorporateInfoActivity.this, CorporateInfoActivity.this.getApplicationContext().getPackageName() + ".provider", new File(photoSavePath, photoSaveName));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(openCameraIntent, CAMERACODE);
        } else {
            ToastTool.show(CorporateInfoActivity.this,"存储卡不存在！");

        }

    }

    private void showDialog() {
        LookDialog dialog=new LookDialog(CorporateInfoActivity.this).build();
        dialog.setResure(R.mipmap.yyzz);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }





    List<IDCardPhotoEntity> idCardList=new ArrayList<>();//服务器返回的身份证照片
    String sServicePhotoPath ="";
    /*照片上传*/
    private void uploadPhoto(){
        upLoadingShow("照片上传中...");
        RequestParam param=new RequestParam();
        param.addFile("f1",file);
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(this,"上传中...") {
            @Override
            public void onSuccess(String data) {
                upLoadingClose();
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray array=object.getJSONArray("result");
                    idCardList=gson.fromJson(array.toString(),new TypeToken<List<IDCardPhotoEntity>>(){}.getType());
                    if(idCardList.size()==1)
                        sServicePhotoPath =idCardList.get(0).getImageUrlPath();
                        entity.setEnterpriseUrl(sServicePhotoPath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onError(int code,String error) {
                upLoadingClose();
                entity.setEnterpriseUrl("");
                if(code==1||code==2||code==3){
                    clearUser();
                    SpUtils.clearSp(CorporateInfoActivity.this);
                    startActivity(new Intent(CorporateInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(CorporateInfoActivity.this,error);
            }
        });
    }


    /*提交注册信息--验证*/
    public boolean submitVerify()
    {
        enterpriseName=corporate_info_name.getText().toString().trim();//公司名字
        enterpriseNo=corporate_info_code.getText().toString().trim();
        enterpriseAddress=corporate_info_address.getText().toString().trim();//详细地址
        contacts = corporate_user_name.getText().toString().trim();
        contactsPhone = corporate_user_phone.getText().toString().trim();

        if(enterpriseName.isEmpty()) {
            ToastTool.show(this,"请输入企业名称！");
            return false;
        }
        if(enterpriseName.length()<2) {
            ToastTool.show(this,"请至少输入两个字符的企业名称！");
            return false;
        }
        else if(enterpriseNo.isEmpty()) {
            ToastTool.show(this,"统一社会信用代码不能为空！");
            return false;
        }
        else if(enterpriseNo.length()<18) {
            ToastTool.show(this,"统一社会信用代码格式不正确！");
            return false;
        }else if (!ValidateUtil.isUnifiedCode(enterpriseNo)){
            ToastTool.show(this,"统一社会信用代码格式不正确！");
            return false;
        }

        else if (enterpriseAddress.isEmpty()) {
            ToastTool.show(this, "请输入详细地址！");
            return false;
        }

        else if (enterpriseAddress.length()<2) {
            ToastTool.show(this, "请至少输入两个字符的详细地址！");
            return false;
        }

        else if (contacts.isEmpty()) {
            ToastTool.show(this, "请输入运营人员姓名！");
            return false;
        }
        else if (contacts.length()<2) {
            ToastTool.show(this, "请至少输入两个字符的运营人员姓名！");
            return false;
        }

        else if (contactsPhone.isEmpty()) {
            ToastTool.show(this, "请输入运营人员手机号！");
            return false;
        }

        else if (!RegTool.isMobile(contactsPhone)) {
            ToastTool.show(this, "手机号码格式不正确！");
            return false;
        }

        if(TextUtils.isEmpty(entity.getEnterpriseUrl())){
            ToastTool.show(this,"请上传营业执照！");
            return false;
        }
        if(TextUtils.isEmpty(entity.getEnterpriseProvinceName())){
            ToastTool.show(this,"请选择车辆所在地区！");
            return false;
        }

        entity.setEnterpriseName(enterpriseName);
        entity.setEnterpriseNo(enterpriseNo);
        entity.setEnterpriseAddress(enterpriseAddress+"");
        entity.setContacts(contacts);
        entity.setContactsPhone(contactsPhone);

        return true;
    }




    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
//                                initJsonData();
                                cityutil.initJsonData(options1Items,options2Items,mHandler);
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
                    ToastTool.show(CorporateInfoActivity.this,"解析数据出错");
                    break;

            }
        }
    };


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<JsonBean.CityBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    /*城市选择器*/
    private void ShowPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String enterpriseProvinceName =options1Items.get(options1).getPickerViewText();
                String enterpriseCityName = options2Items.get(options1).get(options2).getName();//车辆所在市区

                String enterpriseProvince = options1Items.get(options1).getAreaId();
                String enterpriseCity = options2Items.get(options1).get(options2).getAreaId();

                entity.setEnterpriseProvinceName(enterpriseProvinceName);
                entity.setEnterpriseCityName(enterpriseCityName);
                entity.setEnterpriseProvince(enterpriseProvince);
                entity.setEnterpriseCity(enterpriseCity);

                corporate_info_area.setText(enterpriseProvinceName+enterpriseCityName);

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


    private void hideKeys()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(corporate_info_area.getWindowToken(), 0); //强制隐藏键盘
    }








    private void httpUserFillInInfo()
    {
        RequestParam param=new RequestParam();
        OkHttpUtils.post(Config.REFRESHCERTIFIEDINFO, param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                JSONObject object= null;
                try {
                    object = new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    entity= GsonUtils.gsonToBean(result.toString(),CertifiedEntity.class);
                    if(entity!=null) {
                        setUi();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code,String error) {
                if(code==1||code==2||code==3){
                    clearAll();
                    SpUtils.clearSp(CorporateInfoActivity.this);
                    startActivity(new Intent(CorporateInfoActivity.this,LoginActivity.class));
                }
                ToastTool.show(CorporateInfoActivity.this,error);
            }
        });
    }

    private void setUi() {
        if(!TextUtils.isEmpty(entity.getEnterpriseName())){
            corporate_info_name.setText(entity.getEnterpriseName());
        }

        if(!TextUtils.isEmpty(entity.getEnterpriseNo())){
            corporate_info_code.setText(entity.getEnterpriseNo());
        }

        StringBuilder builder=new StringBuilder();
        if(!TextUtils.isEmpty(entity.getEnterpriseProvinceName())){
            builder.append(entity.getEnterpriseProvinceName());
        }
        if(!TextUtils.isEmpty(entity.getEnterpriseCityName())){
            builder.append(entity.getEnterpriseCityName());
        }

        if(!TextUtils.isEmpty(builder.toString())){
            corporate_info_area.setText(builder.toString());
        }

        if(!TextUtils.isEmpty(entity.getEnterpriseAddress())){
            corporate_info_address.setText(entity.getEnterpriseAddress());
        }


        if(!TextUtils.isEmpty(entity.getContacts())){
            corporate_user_name.setText(entity.getContacts());//联系人

        }
        if(!TextUtils.isEmpty(entity.getContactsPhone())){
            corporate_user_phone.setText(entity.getContactsPhone());//联系人手机号
        }

        if(!TextUtils.isEmpty(entity.getEnterpriseUrl())){
            corporate_info_up_photo.setVisibility(View.GONE);
            corporate_info_photo.setVisibility(View.VISIBLE);
            sServicePhotoPath = entity.getEnterpriseUrl();
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.banner);
            Glide.with(CorporateInfoActivity.this).load(entity.getEnterpriseUrl()).apply(options).into(corporate_info_photo);
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

    private void chage(Uri imageUri) {
        Bitmap bit = null;
        if(imageUri!=null) {
            try {
                bit = MyBitmapUtils.getBitmapFormUri(CorporateInfoActivity.this, imageUri);//比例压缩质量压缩
                file = MyBitmapUtils.saveBitmap(bit, TimePareUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss")+"yzzz.png");//把mitmap转成file文件保存在本地
                corporate_info_photo.setVisibility(View.VISIBLE);
                corporate_info_up_photo.setVisibility(View.GONE);
                //imageLoader.displayImage("file://" + photoUrl, ivShowPhoto, options);
                corporate_info_photo.setImageBitmap(bit);
                popupWindow.dismiss();
                uploadPhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveCace() {
        enterpriseName=corporate_info_name.getText().toString().trim();
        enterpriseNo=corporate_info_code.getText().toString().trim();
        enterpriseAddress=corporate_info_address.getText().toString().trim();
        contacts = corporate_user_name.getText().toString().trim();
        contactsPhone = corporate_user_phone.getText().toString().trim();

        entity.setEnterpriseName(enterpriseName);
        entity.setEnterpriseNo(enterpriseNo);
        entity.setEnterpriseAddress(enterpriseAddress);
        entity.setContacts(contacts);
        entity.setContactsPhone(contactsPhone);
        SpUtils.setCartifi(CorporateInfoActivity.this,SpUtils.getToken(CorporateInfoActivity.this)+"renzhen",GsonUtils.bean2Json(entity));
    }


    private MyReceiver receiver;
    public static final String UPDATECACE ="UPDATECACA";
    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(UPDATECACE)){
                if(!TextUtils.isEmpty(SpUtils.getCartifi(CorporateInfoActivity.this,SpUtils.getToken(CorporateInfoActivity.this)+"renzhen"))){
                    entity=GsonUtils.gsonToBean(SpUtils.getCartifi(CorporateInfoActivity.this,SpUtils.getToken(CorporateInfoActivity.this)+"renzhen"),CertifiedEntity.class);
                    setUi();
                }else {
                    httpUserFillInInfo();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver==null){
            receiver=new MyReceiver();
            registerReceiver(receiver,new IntentFilter(UPDATECACE));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCace();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }


}
