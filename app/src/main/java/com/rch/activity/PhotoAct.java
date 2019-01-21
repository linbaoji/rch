package com.rch.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.AddphotoUtil;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.MyBitmapUtils;
import com.rch.common.PhotoFaUtils;
import com.rch.common.ToastTool;
import com.rch.custom.AddPhotoView;
import com.rch.custom.MySelfSheetDialog;
import com.rch.entity.PicListBean;
import com.rch.entity.VehicleImageListEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 */

public class PhotoAct extends BaseActivity implements AddPhotoView.openCanme,AddPhotoView.deleatUrl{

    @ViewInject(R.id.ad_view1)
    private AddPhotoView ad_view1;
    @ViewInject(R.id.ad_view2)
    private AddPhotoView ad_view2;

    @ViewInject(R.id.ad_view3)
    private AddPhotoView ad_view3;
    @ViewInject(R.id.ad_view4)
    private AddPhotoView ad_view4;

    @ViewInject(R.id.ad_view5)
    private AddPhotoView ad_view5;
    @ViewInject(R.id.ad_view6)
    private AddPhotoView ad_view6;

    @ViewInject(R.id.ad_view7)
    private AddPhotoView ad_view7;
    @ViewInject(R.id.ad_view8)
    private AddPhotoView ad_view8;

    @ViewInject(R.id.ad_view9)
    private AddPhotoView ad_view9;
    @ViewInject(R.id.ad_view10)
    private AddPhotoView ad_view10;

    @ViewInject(R.id.ad_view11)
    private AddPhotoView ad_view11;
    @ViewInject(R.id.ad_view12)
    private AddPhotoView ad_view12;

    @ViewInject(R.id.ad_view13)
    private AddPhotoView ad_view13;
    @ViewInject(R.id.ad_view14)
    private AddPhotoView ad_view14;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;



    final int REQUESTWRITEPERIMISSINCODE=102;//相机动态请求写入权限
    final int REQUESTPHOTOPERIMISSINCODE=103;//相册动态请求写入权限
    final int CAMERACODE=0;//相机
    final int PHOTOCODE=1;//相册


    private String photoSavePath;
    private String photoSaveName;
    Uri imageUri = null;

    private String token;

    ImageView iv_fengm;
    ImageView iv_deleat;
    LinearLayout ll_pr;
    String fromtype;
//    String jrtype;//1新增 2是修改

    private List<PicListBean> vehicleImageList;

    String fmurl="";
    String zcurl="";
    String zhcurl="";
    String jscurl="";
    String ybpurl="";
    String fdjurl="";
    String cturl="";
    String cwurl="";
    String ycurl="";
    String yhcurl="";
    String yqcurl="";
    String rturl="";
    String hpurl="";
    String hbxurl="";



    private List<PicListBean>list=new ArrayList<>();
    private int num;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photo_cmo);
        initToolBar();
        ViewUtils.inject(this);

        vehicleImageList= (List<PicListBean>) getIntent().getSerializableExtra("imagelist");

        list.clear();
        if(vehicleImageList!=null&&vehicleImageList.size()>0){
            for (int i=0;i<vehicleImageList.size();i++){
                if(vehicleImageList.get(i).getImageOri().equals("01")){
                   fmurl=vehicleImageList.get(i).getVehicleImage();
                   list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("02")){
                    zcurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("03")){
                    zhcurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("04")){
                    jscurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("05")){
                    ybpurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("06")){
                    fdjurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("07")){
                    cturl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("08")){
                    cwurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("09")){
                    ycurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("10")){
                    yhcurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("11")){
                    yqcurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("12")){
                    rturl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("13")){
                    hpurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
                if(vehicleImageList.get(i).getImageOri().equals("14")){
                    hbxurl=vehicleImageList.get(i).getVehicleImage();
                    list.add(vehicleImageList.get(i));
                }
            }

        }
        num=list.size();
        tv_num.setText("1. 最多可以上传14张图片，您已上传"+num+"张;");
        photoSavePath = Environment.getExternalStorageDirectory().getPath() + "/ClipFengmian/cache/";
        File tempFile = new File(photoSavePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        token=getUserInfo().getToken()==null?"":getUserInfo().getToken();

        ad_view1.initDate("01",fmurl);
        ad_view1.setOpencanme(this);
        ad_view1.setDeleat(this);
        ad_view2.initDate("02",zcurl);
        ad_view2.setOpencanme(this);
        ad_view2.setDeleat(this);
        ad_view3.initDate("03",zhcurl);
        ad_view3.setOpencanme(this);
        ad_view3.setDeleat(this);
        ad_view4.initDate("04",jscurl);
        ad_view4.setOpencanme(this);
        ad_view4.setDeleat(this);
        ad_view5.initDate("05",ybpurl);
        ad_view5.setOpencanme(this);
        ad_view5.setDeleat(this);
        ad_view6.initDate("06",fdjurl);
        ad_view6.setOpencanme(this);
        ad_view6.setDeleat(this);
        ad_view7.initDate("07",cturl);
        ad_view7.setOpencanme(this);
        ad_view7.setDeleat(this);
        ad_view8.initDate("08",cwurl);
        ad_view8.setOpencanme(this);
        ad_view8.setDeleat(this);
        ad_view9.initDate("09",ycurl);
        ad_view9.setOpencanme(this);
        ad_view9.setDeleat(this);
        ad_view10.initDate("10",yhcurl);
        ad_view10.setOpencanme(this);
        ad_view10.setDeleat(this);
        ad_view11.initDate("11",yqcurl);
        ad_view11.setOpencanme(this);
        ad_view11.setDeleat(this);
        ad_view12.initDate("12",rturl);
        ad_view12.setOpencanme(this);
        ad_view12.setDeleat(this);
        ad_view13.initDate("13",hpurl);
        ad_view13.setOpencanme(this);
        ad_view13.setDeleat(this);
        ad_view14.initDate("14",hbxurl);
        ad_view14.setOpencanme(this);
        ad_view14.setDeleat(this);
    }



    private void addPhoto() {
        MySelfSheetDialog dialog = new MySelfSheetDialog(PhotoAct.this);
        dialog.builder().addSheetItem("拍照", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    int hasCameraContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
                    if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED||hasReadContactsPermission!=PackageManager.PERMISSION_GRANTED||hasCameraContactsPermission!=PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUESTWRITEPERIMISSINCODE);
                        return;
                    }else {
                     camera();
                    }
                }else {
                    camera();
                }

            }
        }).addSheetItem("从相册选取", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(hasWriteContactsPermission!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUESTPHOTOPERIMISSINCODE);
                        return;
                    }else {
                        showPhoto();
                    }
                }else {
                    showPhoto();
                }

            }
        }).show();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTWRITEPERIMISSINCODE:
                if(!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(PhotoAct.this,"请手动开启文件写入权限！");
                    return;
                }
                else if(!permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(PhotoAct.this,"请手动开启文件读取权限！");

                    return;
                }
                else if(!permissions[2].equals(Manifest.permission.CAMERA)||grantResults[2]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(PhotoAct.this,"请手动开启相机权限！");

                    return;
                }
                camera();
                break;
            case REQUESTPHOTOPERIMISSINCODE:
                if(!permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(PhotoAct.this,"请手动开启文件访问权限！");

                    return;
                }
                showPhoto();
                break;
        }
    }

    /**
     * 调取相机
     */
    private void camera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
            imageUri = FileProvider.getUriForFile(PhotoAct.this, PhotoAct.this.getApplicationContext().getPackageName() + ".provider", new File(photoSavePath, photoSaveName));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, CAMERACODE);
        } else {
            ToastTool.show(PhotoAct.this,"存储卡不存在！");

        }
    }

    /**
     * 调取相册
     */
    private void showPhoto() {
//        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);//打开所有类容查找图片在oppo5.1里面会出现某些图片路径错误换成下面
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);//只打开相册
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openAlbumIntent, PHOTOCODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PHOTOCODE://相册
                if (resultCode != RESULT_OK||data == null) {
                 return;
            }
                try {
                    imageUri = data.getData();//路径
                    String type = data.getType();
                    if (type == null) {
                        String realPath = imageUri.getEncodedPath();//获取图片真实路径
                        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
                        if (sdCardExist) {
                            String replaceStr = Environment.getExternalStorageDirectory().getPath();
                            if (realPath.contains("external_storage_root")) {
                                realPath = realPath.replaceFirst("/external_storage_root", replaceStr);
                                imageUri = MyBitmapUtils.pathToUri(this, realPath);
                            }
                        }
                    }
                    if (imageUri != null) {
                        startUCrop(imageUri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CAMERACODE://相机
                try {
                    // 裁剪
                    startUCrop(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case  UCrop.REQUEST_CROP:
                if(data==null){
                    return;
                }
                // 裁剪照片
                final Uri croppedUri = UCrop.getOutput(data);
                Bitmap bit = null;
                if(croppedUri!=null) {
                    try {

                        bit = MyBitmapUtils.getBitmapFormUri(PhotoAct.this, croppedUri);
                        File file = MyBitmapUtils.saveBitmap(bit, fromtype + "fbphoto.png");//把mitmap转成file文件保存在本地

                        iv_fengm.setImageBitmap(bit);
                        uploadPhoto(file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;//截图完成

            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadPhoto(final File file) {
        EncryptionTools.initEncrypMD5(token);
        RequestParam param=new RequestParam();
        param.add("token", token);
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.addFile("f1",file);
        ll_pr.setVisibility(View.VISIBLE);
        OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(PhotoAct.this,"上传中...") {
            @Override
            public void onSuccess(String data) {
                iv_fengm.setEnabled(false);
                iv_deleat.setVisibility(View.VISIBLE);
                ll_pr.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray array=object.getJSONArray("result");
                    JSONObject obj= (JSONObject) array.get(0);
                    String imagepath=obj.getString("imageUrlPath");
                    checkList(fromtype,imagepath);

                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(int code,String error) {
                iv_fengm.setEnabled(true);
                ll_pr.setVisibility(View.GONE);
                if (fromtype.equals("01")) {
                    iv_fengm.setImageResource(R.mipmap.fbfm);

                }
                if (fromtype.equals("02")) {
                    iv_fengm.setImageResource(R.mipmap.fbzc);

                }
                if (fromtype.equals("03")) {
                    iv_fengm.setImageResource(R.mipmap.fbzhc);

                }
                if (fromtype.equals("04")) {
                    iv_fengm.setImageResource(R.mipmap.fbjsczy);

                }
                if (fromtype.equals("05")) {
                    iv_fengm.setImageResource(R.mipmap.fbybp);

                }
                if (fromtype.equals("06")) {
                    iv_fengm.setImageResource(R.mipmap.fdjc);

                }
                if (fromtype.equals("07")) {
                    iv_fengm.setImageResource(R.mipmap.fbct);

                }
                if (fromtype.equals("08")) {
                    iv_fengm.setImageResource(R.mipmap.fbcw);

                }
                if (fromtype.equals("09")) {
                    iv_fengm.setImageResource(R.mipmap.fbyc);
                }
                if (fromtype.equals("10")) {
                    iv_fengm.setImageResource(R.mipmap.fbyhc);

                }
                if (fromtype.equals("11")) {
                    iv_fengm.setImageResource(R.mipmap.fbyqc);
                }
                if (fromtype.equals("12")) {
                    iv_fengm.setImageResource(R.mipmap.fbryrt);

                }
                if (fromtype.equals("13")) {
                    iv_fengm.setImageResource(R.mipmap.fbhpzy);

                }
                if (fromtype.equals("14")) {
                    iv_fengm.setImageResource(R.mipmap.fbhbx);
                }
                ToastTool.show(PhotoAct.this,error);
            }
        });
    }

    private void checkList(String fromtype,String imagepath) {
        PicListBean bena=new PicListBean();
        bena.setImageOri(fromtype);
        bena.setSort(getSort(fromtype));
        bena.setVehicleImage(imagepath);

        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getImageOri().equals(fromtype)) {
                    list.remove(i);
                }
            }
        }
        list.add(bena);
        num=list.size();
        tv_num.setText("1. 最多可以上传14张图片，您已上传"+num+"张;");
    }

    private String getSort(String fromtype) {
        if(fromtype.startsWith("0")){
            return fromtype.subSequence(1,2).toString();
        }else {
            return fromtype;
        }
    }


    private Uri destinationUri;
    private void startUCrop(Uri imageUri){
        //裁剪后保存到文件中
        destinationUri = Uri.fromFile(new File(getCacheDir(), "myfm.jpg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.orange_2));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.orange_2));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        options.setHideBottomControls(true);//影厂到底部东西
        uCrop.withOptions(options);
        uCrop.withAspectRatio(900,600);
        uCrop.withMaxResultSize(900,600);
        uCrop.start(this);
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                Intent intent=new Intent();
                intent.putExtra("list", (Serializable) list);
                setResult(100,intent);
                finish();
                break;
        }
    }


    @Override
    public void open(String type,ImageView iv_fm,ImageView iv_deleat,LinearLayout ll_pr) {
        fromtype=type;
        this.iv_fengm=iv_fm;
        this.iv_deleat=iv_deleat;
        this.ll_pr=ll_pr;
        addPhoto();
    }

    @Override
    public void deleat(String type) {
        for(int i=0;i<list.size();i++){
            if(list.get(i).getImageOri().equals(type)){
                list.remove(i);
            }
        }

//        List<String>numlist=new ArrayList<>();
//        numlist.clear();
//        for(int j=0;j<list.size();j++){
//            if(TextUtils.isEmpty(list.get(j).getVehicleImage())){
//                numlist.add(list.get(j).getVehicleImage());
//            }
//        }
//        num=numlist.size();
        tv_num.setText("1. 最多可以上传14张图片，您已上传"+list.size()+"张;");
    }

    @Override
    public void onBackPressed() {
//        if(jrtype.equals("1")){//新增保存
//            PhotoFaUtils.setJsonArray(PhotoAct.this, GsonUtils.bean2Json(list));
//        }
        Intent intent=new Intent();
        intent.putExtra("list", (Serializable) list);
        setResult(100,intent);

        super.onBackPressed();

    }
}
