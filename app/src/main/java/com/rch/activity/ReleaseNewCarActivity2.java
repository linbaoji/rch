package com.rch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rch.R;
import com.rch.adatper.GridImageAdapter;
import com.rch.base.SecondBaseActivity;
import com.rch.common.BigDecmUtils;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.GsonUtils;
import com.rch.common.PhotoFaUtils;
import com.rch.common.ProandCityUtil;
import com.rch.common.RoncheUtil;
import com.rch.common.ToastTool;
import com.rch.custom.FullyGridLayoutManager;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.ChexinBean;
import com.rch.entity.Children;
import com.rch.entity.DatailBean;
import com.rch.entity.JsonBean;
import com.rch.entity.NewCarImgEntity;
import com.rch.entity.NewCarInfoBean;
import com.rch.entity.PicListBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * 发布新车(裁剪过的不重新裁剪，上传过的不重新上传)
 * Created by Administrator on 2018/10/10.
 */

public class ReleaseNewCarActivity2 extends SecondBaseActivity {
    @ViewInject(R.id.tv_img_num)
    private TextView tv_img_num;
    @ViewInject(R.id.tv_ppcx)
    TextView tv_ppcx;
    @ViewInject(R.id.tv_ss)
    TextView tv_ss;
    @ViewInject(R.id.tv_cg)
    TextView tv_cg;
    @ViewInject(R.id.et_mendian_money_one)
    EditText et_mendian_money_one;
    @ViewInject(R.id.et_mendian_money_two)
    EditText et_mendian_money_two;
    @ViewInject(R.id.et_pifa_money_one)
    EditText et_pifa_money_one;
    @ViewInject(R.id.et_pifa_money_two)
    EditText et_pifa_money_two;

    private String strPpcx;//品牌车型
    private String strProvince;//省市
    private String strCg;//车规
    private String strCg_key;
    private String etMendianBefore;//门店价前
    private String etMendianAfter;//门店价后
    private String etPifaBefore;//批发价前
    private String etPifaAfter;//批发价后

    private boolean isSubmit = false;//是否提交成功

    private String province;
    private String city;


    private List<NewCarImgEntity> vehicleImageGetList = new ArrayList<>();//从服务器获取到的图片列表
    private List<NewCarImgEntity> vehicleImageList = new ArrayList<>();//提交审核要上传的图片列表
    private String operType="1";//操作类型 1-新增 2-修改

    private boolean isChanged = false;//是否修改过图片
    private boolean isLocalImg = false;//是否本地保存的有图片数据
//    private boolean isUploadImg = false;//是否上传图片

    private boolean realPosition = false;//真实的上传位置

    private List<LocalMedia> selectList = new ArrayList<>();//本地保存的媒体图片列表
    private List<LocalMedia> selectCropList = new ArrayList<>();//本地本次返回裁剪的媒体图片列表
    private List<LocalMedia> selectUpList = new ArrayList<>();//本地本次需要上传的媒体图片列表
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 15;
    private int themeId;

    private int i1=-10,i2=-10;//记录车辆所在地选取的位置
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<JsonBean.CityBean>> options2Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;

    private String id;//车辆主键id
    private String brandId;//品牌
    private String modelId;//车型
    private String seriesId;//车系
    private String provinceId;//省代码
    private String cityId;//市代码
    private String vehicleStandardName;//车规名
    private ProandCityUtil cityutil;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
//                        Toast.makeText(VehicleActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                cityutil.initJsonData(options1Items,options2Items,mHandler);
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(VehicleActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    showPickerView();
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    ToastTool.show(mContext, "Parse Failed");
                    break;
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.act_release_new_car);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("发布新车");
        cityutil=new ProandCityUtil(mContext);//地区选择
        if (getIntent()!=null){
            id = getIntent().getStringExtra("id");//编辑界面进入获取车辆id
            operType = getIntent().getStringExtra("operType");//操作类型
            if (TextUtils.isEmpty(operType))
                operType="1";
        }

        setTopRight("提交审核", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ButtonUtils.isFastDoubleClick(view.getId()))
                if (isAvailable()){
//                    if (isChanged || isLocalImg){//增加图片或者重新选择图片  有本地保存图片数据
                        vehicleImageList.clear();
                        for (int i = 0; i < selectUpList.size(); i++) {
                            if (!TextUtils.isEmpty(selectUpList.get(i).getCutPath()+"")) {
                                uploadPhoto(i, new File(selectUpList.get(i).getCompressPath()));
                            }
                        }
//                    }else {
//                        Log.e("===EvehicleImageList:",vehicleImageList.toString());
//                        submitData(id,brandId,modelId,seriesId,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
//                    }

                }
            }
        });
        init();

        if (operType.equals("2")){//编辑，获取车辆数据
            getData();
        }else {
            String carnewdetail = PhotoFaUtils.getCarnewdetail(mContext);//获取本地保存编辑数据
            if (!TextUtils.isEmpty(carnewdetail)) {
                NewCarInfoBean bean = GsonUtils.gsonToBean(carnewdetail, NewCarInfoBean.class);
                if (bean!=null){
                    setData(bean);
                }
            }

        }
    }

    //移除vehicleImageList第position个
    public void removeIitem(int position){
        if (vehicleImageGetList!=null && position<vehicleImageGetList.size()) {
            vehicleImageGetList.remove(position);
            if (vehicleImageGetList!=null && vehicleImageGetList.size()>0){//设置第一张图片为主图
                vehicleImageGetList.get(0).setType("1");
            }
        }else if (vehicleImageList!=null && position<vehicleImageList.size()) {
            vehicleImageList.remove(position);
            if (vehicleImageList!=null && vehicleImageList.size()>0){//设置第一张图片为主图
                vehicleImageList.get(0).setType("1");
            }
        }

        tv_img_num.setText("1. 最多可以上传15张图片，您已上传"+selectUpList.size()+"张；");
    }

    //临时退出保存当前界面数据
    private void saveData(){
        NewCarInfoBean bean = new NewCarInfoBean();
        DatailBean datailBean = new DatailBean();
        datailBean.setBrandId(brandId);
        datailBean.setSeriesId(seriesId);
        datailBean.setModelId(modelId);
        if (!"请选择品牌车型".equals(tv_ppcx.getText().toString().trim()))
            datailBean.setVehicleFullName(tv_ppcx.getText().toString().trim());
        if (!"请选择省市".equals(tv_ss.getText().toString().trim())) {
            //设置省市代码
            datailBean.setProvinceName(province);
            datailBean.setCityName(city);
            datailBean.setProvince(provinceId);
            datailBean.setCity(cityId);
        }
        if (!"请选择车规".equals(tv_cg.getText().toString().trim())){
            datailBean.setVehicleStandard(strCg_key);
            datailBean.setVehicleStandardName(vehicleStandardName);
        }
        if (!TextUtils.isEmpty(et_mendian_money_one.getText().toString().trim()))
            datailBean.setSalesPriceMin(et_mendian_money_one.getText().toString().trim());
        if (!TextUtils.isEmpty(et_mendian_money_two.getText().toString().trim()))
            datailBean.setSalesPriceMax(et_mendian_money_two.getText().toString().trim());
        if (!TextUtils.isEmpty(et_pifa_money_one.getText().toString().trim()))
            datailBean.setTradePriceMin(et_pifa_money_one.getText().toString().trim());
        if (!TextUtils.isEmpty(et_pifa_money_two.getText().toString().trim()))
            datailBean.setTradePriceMax(et_pifa_money_two.getText().toString().trim());
        if (selectUpList!=null && selectUpList.size()>0)
            datailBean.setLocalMediaList(selectUpList);

        bean.setDetail(datailBean);
        PhotoFaUtils.setCarnewdetail(mContext,GsonUtils.bean2Json(bean));
    }
    private void setData(NewCarInfoBean bean){
        tv_ppcx.setTextColor(getResources().getColor(R.color.black_2));
        tv_ss.setTextColor(getResources().getColor(R.color.black_2));
        tv_cg.setTextColor(getResources().getColor(R.color.black_2));
        tv_ppcx.setText(RoncheUtil.getSelfString(bean.getDetail().getVehicleFullName()));
        tv_ss.setText(RoncheUtil.getSelfString(bean.getDetail().getProvinceName())+RoncheUtil.getSelfString(bean.getDetail().getCityName()));
        tv_cg.setText(RoncheUtil.getSelfString(bean.getDetail().getVehicleStandardName()));
        et_mendian_money_one.setText(RoncheUtil.getSelfString(bean.getDetail().getSalesPriceMin()));
        et_mendian_money_two.setText(RoncheUtil.getSelfString(bean.getDetail().getSalesPriceMax()));
        et_pifa_money_one.setText(RoncheUtil.getSelfString(bean.getDetail().getTradePriceMin()));
        et_pifa_money_two.setText(RoncheUtil.getSelfString(bean.getDetail().getTradePriceMax()));
        List<PicListBean> imgList =  bean.getDetail().getPicList();//服务器图片数据
        if (imgList!=null && imgList.size()>0) {
            brandId = bean.getDetail().getBrandId();
            modelId = bean.getDetail().getModelId();
            seriesId = bean.getDetail().getSeriesId();
            province = bean.getDetail().getProvinceName();
            provinceId = bean.getDetail().getProvince();
            city = bean.getDetail().getCityName();
            cityId = bean.getDetail().getCity();
            strCg_key = bean.getDetail().getVehicleStandard();
            vehicleImageGetList.clear();
            tv_img_num.setText("1. 最多可以上传15张图片，您已上传"+imgList.size()+"张；");
            for (int i = 0; i < imgList.size(); i++) {
                Log.e("===pic",imgList.get(i).getVehicleImage());
                LocalMedia media = new LocalMedia();
                media.setPath(imgList.get(i).getVehicleImage());
                media.setCompressPath(imgList.get(i).getVehicleImage());
                selectList.add(media);
                vehicleImageGetList.add(new NewCarImgEntity(imgList.get(i).getType(),imgList.get(i).getVehicleImage()));
            }
            selectUpList.addAll(selectList);
            adapter.setList(selectUpList);
            adapter.notifyDataSetChanged();
        }else {
            brandId = bean.getDetail().getBrandId();
            seriesId = bean.getDetail().getSeriesId();
            modelId = bean.getDetail().getModelId();
            provinceId = bean.getDetail().getProvince();
            cityId = bean.getDetail().getCity();
            province = bean.getDetail().getProvinceName();
            city = bean.getDetail().getCityName();
            vehicleStandardName = bean.getDetail().getVehicleStandardName();
            strCg_key = bean.getDetail().getVehicleStandard();
            selectList = bean.getDetail().getLocalMediaList();//本地图片数据
            if (selectList!=null && selectList.size()>0){
                selectUpList.addAll(selectList);
                isLocalImg = true;
                adapter.setList(selectUpList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    //获取新车详情
    private void getData() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.NEWCARDEATILE_URL, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    NewCarInfoBean newCarInfoBean = GsonUtils.gsonToBean(result.toString(), NewCarInfoBean.class);
                    setData(newCarInfoBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
            }
        });
    }
    private void init(){
        themeId = R.style.picture_default_style;
        InputFilter[] filtermoney = {new EditInputFilter(99999,2)};
        et_mendian_money_one.setFilters(filtermoney);
        et_mendian_money_two.setFilters(filtermoney);
        et_pifa_money_one.setFilters(filtermoney);
        et_pifa_money_two.setFilters(filtermoney);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReleaseNewCarActivity2.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ReleaseNewCarActivity2.this, onAddPicClickListener,false);
        adapter.setList(selectUpList);
        adapter.setSelectMax(maxSelectNum-selectUpList.size());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectUpList.size() > 0) {
                    LocalMedia media = selectUpList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(ReleaseNewCarActivity2.this).themeStyle(themeId).openExternalPreview(position, selectUpList);
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(ReleaseNewCarActivity2.this)
                        .openGallery(ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum-selectUpList.size())// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(3)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE )// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
//                        .selectionMedia(selectList)// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }

    };

    final int CG=1101;
    @OnClick({R.id.ll_ppcx,R.id.ll_clszd,R.id.ll_cg,R.id.tv_tj,R.id.iv_add_photo})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_ppcx://品牌车型
                startActivity(new Intent(mContext,NewBrandAct.class).putExtra("type",3));
                break;
            case R.id.ll_clszd://车辆所在地
                if(!isLoaded) {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }else {
                    showPickerView();
                }
                break;
            case R.id.ll_cg://车规
                Intent intent=new Intent(mContext,SelectAct.class).putExtra("from","7");
                startActivityForResult(intent,CG);
                break;
            case R.id.tv_tj://提交审核
                if(!ButtonUtils.isFastDoubleClick(view.getId()))
                if (isAvailable()) {
//                    if (isChanged || isLocalImg) {//增加图片或者重新选择图片
                        vehicleImageList.clear();
                        for (int i = 0; i < selectUpList.size(); i++) {
                            uploadPhoto(i, new File(selectUpList.get(i).getCompressPath()));
                        }
//                    }else {
//                        submitData(id,brandId,modelId,seriesId,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
//                    }
                }
                break;
            case R.id.iv_add_photo:

                break;
        }

    }

    private boolean isAvailable(){
        strPpcx = tv_ppcx.getText().toString().trim();
        strProvince = tv_ss.getText().toString().trim();
        strCg = tv_cg.getText().toString().trim();
        etMendianBefore = et_mendian_money_one.getText().toString().trim();
        etMendianAfter = et_mendian_money_two.getText().toString().trim();
        etPifaBefore = et_pifa_money_one.getText().toString().trim();
        etPifaAfter = et_pifa_money_two.getText().toString().trim();

        if (TextUtils.isEmpty(strPpcx) || strPpcx.equals("请选择品牌车型")){
            ToastTool.show(mContext,"请选择品牌车型");
            return false;
        }
        if (TextUtils.isEmpty(strProvince) || strProvince.equals("请选择省市")){
            ToastTool.show(mContext,"请选择省市");
            return false;
        }
        if (TextUtils.isEmpty(strCg) || strCg.equals("请选择车规")){
            ToastTool.show(mContext,"请选择车规");
            return false;
        }
        if (TextUtils.isEmpty(etMendianBefore)){
            ToastTool.show(mContext,"请输入金额");
            et_mendian_money_one.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etMendianAfter)){
            ToastTool.show(mContext,"请输入金额");
            et_mendian_money_two.requestFocus();
            return false;
        }
        if(BigDecmUtils.compare(etMendianBefore,etMendianAfter)==1){
            ToastTool.show(mContext,"门店价格式错误，请重新输入");
            et_mendian_money_two.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPifaBefore)){
            ToastTool.show(mContext,"请输入金额");
            et_pifa_money_one.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPifaAfter)){
            ToastTool.show(mContext,"请输入金额");
            et_pifa_money_two.requestFocus();
            return false;
        }
        if (BigDecmUtils.compare(etPifaBefore,etPifaAfter)==1){
            ToastTool.show(mContext,"批发价格式错误，请重新输入");
            et_pifa_money_two.requestFocus();
            return false;
        }
        if (selectUpList==null || selectUpList.size()==0){
            ToastTool.show(mContext,"请选择车辆图片");
            return false;
        }
        return true;
    }

    private void uploadPhoto(final int i,File file) {
        if (i==0)
            upLoadingShow();
        RequestParam param=new RequestParam();
        param.addFile("f1",file);
        if (!param.hasFiles()) {
            OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(mContext, "上传中...") {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject object = new JSONObject(data.toString());
                        JSONArray array = object.getJSONArray("result");
                        JSONObject obj = (JSONObject) array.get(0);
                        String imagepath = obj.getString("imageUrl");
                        if (i == 0 && vehicleImageGetList.size() != 0) {
                            vehicleImageList.add(new NewCarImgEntity("2", imagepath));
                        } else if (i == 0 && vehicleImageGetList.size() == 0) {
                            vehicleImageList.add(new NewCarImgEntity("1", imagepath));
                        } else {
                            vehicleImageList.add(new NewCarImgEntity("2", imagepath));
                        }

                        if (vehicleImageList.size() == selectUpList.size()) {
                            submitData(id,brandId,modelId,seriesId,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(int code, String error) {
                    ToastTool.show(mContext, error);
                    upLoadingClose();
                }
            });
        }else if (i==0 && vehicleImageGetList.size()!=0){
            vehicleImageList.addAll(vehicleImageGetList);
            if (vehicleImageList.size() == selectUpList.size()  && selectUpList.size()==1)
                submitData(id,brandId,modelId,seriesId,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
        }else if (vehicleImageList.size() == selectUpList.size()) {
            submitData(id,brandId,modelId,seriesId,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
        }
    }
    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1).getPickerViewText();//车辆所在省
                city = options2Items.get(options1).get(options2).getName();//车辆所在市区
                provinceId = options1Items.get(options1).getAreaId();
                cityId = options2Items.get(options1).get(options2).getAreaId();
                tv_ss.setText(province + city);
                tv_ss.setTextColor(getResources().getColor(R.color.black_2));
                i1=options1;
                i2=options2;

            }
        })

//                .setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
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

//        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器

        if (i1 != -10 && i2 != -10) {
            pvOptions.setSelectOptions(i1, i2);
        }
        pvOptions.show();
    }


    public static String REFRESHHOMEFRAGMENTMODULE="new_from.ToppedAct";
    private MyBrodcastreceive receiver;
    private class MyBrodcastreceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            SortModel brand= (SortModel) intent.getSerializableExtra("brand");//id
            CarSeriesEntity chexibean= (CarSeriesEntity) intent.getSerializableExtra("chexi");//id
            ChexinBean bean= (ChexinBean) intent.getSerializableExtra("chexin");//id

            brandId = brand.getBrandId();
            modelId = bean.getId();
            seriesId = chexibean.getId();

            String str=bean.getModelName();
            tv_ppcx.setText(str);
            tv_ppcx.setTextColor(getResources().getColor(R.color.black_2));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver==null){
            receiver=new MyBrodcastreceive();
            registerReceiver(receiver,new IntentFilter(REFRESHHOMEFRAGMENTMODULE));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSubmit) {
            PhotoFaUtils.setCarnewdetail(mContext, "");
        }else {
            if (operType.equals("1"))
                saveData();
        }
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CG){
            if (data == null)
                return;
            if (data!=null && data.getExtras()!=null) {
                Children cg = (Children) data.getExtras().getSerializable("select");
                tv_cg.setText(cg.getValue());
                tv_cg.setTextColor(getResources().getColor(R.color.black_2));
                vehicleStandardName = cg.getValue();
                strCg_key = cg.getKey();
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    isChanged = true;
                    // 图片选择结果回调
                    selectCropList = PictureSelector.obtainMultipleResult(data);
                    selectUpList.addAll(selectCropList);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectUpList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    tv_img_num.setText("1. 最多可以上传15张图片，您已上传"+selectUpList.size()+"张；");
                    adapter.setList(selectUpList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }

    //id 当操作类型为修改时id为车辆主键
    private void submitData(String id,String brandId,String modelId,String seriesId,String salesPriceMin,String salesPriceMax,String tradePriceMin,String tradePriceMax,String vehicleStandard){
        RequestParam param = new RequestParam();
        param.add("type", operType);
        if(!operType.equals("1")){
            if (!TextUtils.isEmpty(id))
                param.add("id", id);
        }
        if (!TextUtils.isEmpty(brandId))
        param.add("brandId",brandId);
        if (!TextUtils.isEmpty(modelId))
        param.add("modelId",modelId);
        if (!TextUtils.isEmpty(seriesId))
        param.add("seriesId",seriesId);
        if (!TextUtils.isEmpty(province))
        param.add("province",provinceId);
        if (!TextUtils.isEmpty(city))
        param.add("city",cityId);
        if (!TextUtils.isEmpty(salesPriceMin))
        param.add("salesPriceMin",salesPriceMin);
        if (!TextUtils.isEmpty(salesPriceMax))
        param.add("salesPriceMax",salesPriceMax);
        if (!TextUtils.isEmpty(tradePriceMin))
        param.add("tradePriceMin",tradePriceMin);
        if (!TextUtils.isEmpty(tradePriceMax))
        param.add("tradePriceMax",tradePriceMax);
        if (!TextUtils.isEmpty(vehicleStandard))
        param.add("vehicleStandard",vehicleStandard);

        //初次新增  （1）直接上传 （2）本地保存有图片
        //编辑  已经上传有图片  （1）删除完  （2）删除主图  （3）删除除主图之外的


//        if (isUploadImg){//直接上传图片
//            param.add("imgList", GsonUtils.bean2Json(vehicleImageList));
//        }else if (operType.equals("2") && vehicleImageList!=null && vehicleImageList.size() >= 0) {//编辑已经有图片未重新增加图片
//            vehicleImageList.get(0).setType("1");
//            param.add("imgList", GsonUtils.bean2Json(vehicleImageList));
//        }else
        if (vehicleImageList!=null && vehicleImageList.size()>0){
            param.add("imgList", GsonUtils.bean2Json(vehicleImageList));
        }
        OkHttpUtils.post(Config.ADD_OR_EDIT_NEW_CAR, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                isSubmit = true;
                upLoadingClose();
                ToastTool.show(mContext,"提交成功");
//                startActivity(new Intent(mContext, SeachCarServerSubmitActivity.class).putExtra("type", 5));
//                finish();

                startActivity(new Intent(mContext,ComSucessAct.class).putExtra("type", "5").putExtra("from",operType));
                finish();

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
                upLoadingClose();
            }
        });
    }
}
