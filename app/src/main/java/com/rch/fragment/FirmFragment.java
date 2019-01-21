package com.rch.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
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
import com.google.gson.Gson;
import com.rch.R;
import com.rch.activity.LoginActivity;
import com.rch.activity.MaterialPerfectActivity;
import com.rch.activity.MyInfoActivity;
import com.rch.base.BaseFragment;
import com.rch.common.Config;
import com.rch.common.DialogTool;
import com.rch.common.EncryptionTools;
import com.rch.common.FileTool;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.GlideUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/4/12.
 */

public class FirmFragment extends BaseFragment implements View.OnClickListener{

    EditText etName,etAddress;//etEmail,etPwd,etConfirmPwd,etCode,
    TextView tvArea,tvOk,tvPopCamera,tvPopPhoto,tvPopEsc;;
    LinearLayout upPhoto;
    ImageView ivShowPhoto;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    String id="",sName="",sProvince ="", sCity ="",ctiyText="",sAddress="",sPhootPath="";//sEmail="",sPwd="",sComfirmPwd="",sCode="",
    String sServicePhotoPath ="";

    List<IDCardPhotoEntity> idCardList=new ArrayList<>();//服务器返回的身份证照片

    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0x1;//动态请求写入权限
    final int PHOTOCODE=0x2;//动态请求写入权限

    String cameraPath="";

    boolean isVisible;
    View view,popView;
    PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
            view=inflater.inflate(R.layout.firm_fragment,container,false);
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
        /*etEmail= (EditText) view.findViewById(R.id.firm_email);
        etPwd= (EditText) view.findViewById(R.id.firm_pwd);
        etConfirmPwd= (EditText) view.findViewById(R.id.firm_confirm_pwd);*/
        etName= (EditText) view.findViewById(R.id.firm_name);
        // etCode= (EditText) view.findViewById(R.id.firm_code);
        etAddress= (EditText) view.findViewById(R.id.firm_address);

        ivShowPhoto= (ImageView) view.findViewById(R.id.firm_photo);

        tvArea= (TextView) view.findViewById(R.id.firm_area);
        tvOk= (TextView) view.findViewById(R.id.firm_ok);
        upPhoto= (LinearLayout) view.findViewById(R.id.firm_up_photo);

        ImageLoaderTool imageLoaderTool=new ImageLoaderTool(getActivity(),R.mipmap.banner);
        imageLoader=imageLoaderTool.getImageLoader();
        options=imageLoaderTool.getOptions();

        if(entity!=null&&entity.getAuthenticateType().equals("1"))
            initUserCertifiedInfo();

        ivShowPhoto.setOnClickListener(this);
        tvArea.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        upPhoto.setOnClickListener(this);

    }

    private void initUserCertifiedInfo()
    {
        id=getUserInfo().getId();
        sName=entity.getEnterpriseName();
        sProvince=entity.getEnterpriseProvince();
        sCity=entity.getEnterpriseCity();
        sAddress=entity.getEnterpriseAddress();
        sServicePhotoPath=entity.getEnterpriseUrl();

        etName.setText(sName);
        tvArea.setText(sProvince+sCity);
        String location=sProvince+sCity;
        tvArea.setText(location.isEmpty()?"请选择省市":location);

        if(tvArea.getText().equals("请选择省市")) {
            tvArea.setTextColor(getResources().getColor(R.color.gray_1));
        }else {
            tvArea.setTextColor(getResources().getColor(R.color.black_1));
        }

        etAddress.setText(sAddress);
        if(entity.getEnterpriseUrlPath().isEmpty())
        {
            ivShowPhoto.setVisibility(View.GONE);
            upPhoto.setVisibility(View.VISIBLE);
        }
        else
        {
            ivShowPhoto.setVisibility(View.VISIBLE);
            upPhoto.setVisibility(View.GONE);
        }
        GlideUtils.showImg(getActivity(),entity.getEnterpriseUrlPath(),R.mipmap.banner,ivShowPhoto);

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
            case R.id.firm_area:
                if(!isLoaded)
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                else
                    mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
                break;
            case R.id.firm_ok:
                //submitRegisterInfo();
                verif();
                break;
            case R.id.firm_photo:
            case R.id.firm_up_photo:
                initPaperwordPhotoPopLayout();
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
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
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
        }

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


    private void verif()
    {
        if(!sPhootPath.isEmpty())
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
        param.addFile("f1",new File(sPhootPath));
        param.add("source","3");
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(getActivity(),"上传中...") {
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
                    SpUtils.clearSp(getActivity());
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                ToastTool.show(getActivity(),error);
            }
        });
    }


    /*提交注册信息--验证*/
    public boolean submitVerify()
    {
        /*sEmail=etEmail.getText().toString().trim();
        sPwd=etPwd.getText().toString().trim();
        sComfirmPwd=etConfirmPwd.getText().toString().trim();*/
        sName=etName.getText().toString().trim();
        //sCode=etCode.getText().toString().trim();
        sAddress=etAddress.getText().toString().trim();
    /*    if(sEmail.isEmpty())
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
            ToastTool.show(getActivity(),"请输入企业名称！");
            return false;
        }
       /* else if(sCode.isEmpty())
        {
            ToastTool.show(getActivity(),"统一社会信用代码不能为空！");
            return false;
        }*/
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
      /*  else if(sPhootPath.isEmpty())
        {
            ToastTool.show(getActivity(),"请上传营业执照照片！");
            return;
        }*/
        else if(sServicePhotoPath.isEmpty())
        {
            ToastTool.show(getActivity(),"请上传营业执照照片！");
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
        param.add("authenticateType","1");
        param.add("enterpriseName",sName);
        param.add("enterpriseUrl",sServicePhotoPath);
        param.add("enterpriseProvince",sProvince);
        param.add("enterpriseCity",sCity);
        param.add("enterpriseAddress",sAddress);
      /*  RequestParam param=new RequestParam();
        param.add("id",id);
        param.add("email",sEmail);
        param.add("password",sPwd);
        param.add("confirmPassword",sComfirmPwd);
        param.add("inviteCode",sInviteCode);
        param.add("mobile",sPhone);
        param.add("name",sName);
        param.add("userSex","");
        param.add("province",sProvince);
        param.add("city",sCity);
        param.add("address",sAddress);
        param.add("licenseNo",sCode);//统一社会信用代码
        param.add("licenseUrl",sServicePhotoPath);//营业执照
        param.add("licenseBackUrl","");
        param.add("agentType","1");
        param.add("registType","3");*/
        OkHttpUtils.post(Config.SUBMITREGISTER, param, new OkHttpCallBack(getActivity(),"加载中...") {
            @Override
            public void onSuccess(String data) {
                getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
                startActivity(new Intent(getActivity(), MaterialPerfectActivity.class));
                getActivity().finish();
                upLoadingClose();
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
                cameraPath= ImageCompressTool.compressImageToFile(cameraPath);
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
        FileTool.copyFile(getActivity(),path,photoName);
        return  FileTool.path+photoName;
    }

    private void paperworkPhotoPaht(String photoUrl) {
        sPhootPath=photoUrl;
        ivShowPhoto.setVisibility(View.VISIBLE);
        upPhoto.setVisibility(View.GONE);
        GlideUtils.showImg(getActivity(),"file://" + photoUrl,R.mipmap.banner,ivShowPhoto);
//        Glide.with(getActivity()).load("file://" + photoUrl).placeholder(R.mipmap.banner).into(ivShowPhoto);
        //imageLoader.displayImage("file://" + photoUrl, ivShowPhoto, options);
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
                else if(!permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
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
