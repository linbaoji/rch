package com.rch.activity;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
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
import com.rch.common.EncryptionTools;
import com.rch.common.GetJsonDataUtil;
import com.rch.common.GsonUtils;
import com.rch.common.PhotoFaUtils;
import com.rch.common.ProandCityUtil;
import com.rch.common.RoncheUtil;
import com.rch.common.SoftHideKeyBoardUtil;
import com.rch.common.ToastTool;
import com.rch.custom.FullyGridLayoutManager;
import com.rch.custom.MySelfSheetDialog;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.ChexinBean;
import com.rch.entity.Children;
import com.rch.entity.DatailBean;
import com.rch.entity.JsonBean;
import com.rch.entity.NewCarImgEntity;
import com.rch.entity.NewCarInfoBean;
import com.rch.entity.PicListBean;
import com.rch.entity.VehicleImageListEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.rch.view.OnRecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

/**
 * 发布新车
 * Created by Administrator on 2018/10/10.
 */

public class ReleaseNewCarActivity extends SecondBaseActivity {
    //1.2.3新增字段
    @ViewInject(R.id.et_refit_info)
    private EditText et_refit_info;//改装信息
    @ViewInject(R.id.et_interior_color)
    private EditText et_interior_color;//内饰颜色
    @ViewInject(R.id.et_body_color)
    private EditText et_body_color;//车身颜色

    @ViewInject(R.id.cb_check)
    private CheckBox cb_check;
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


    private List<NewCarImgEntity> vehicleImageList = new ArrayList<>();
    private String operType="1";//操作类型 1-新增 2-修改

    private boolean isChanged = false;//是否修改过车辆图片
    private boolean isMainChanged = false;//是否修改过车辆主图图片
    private boolean isLocalImg = false;//是否本地保存的有图片数据
    private boolean isUploadImg = false;//是否上传图片

    private List<LocalMedia> selectAllList = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectCropList = new ArrayList<>();//本地本次返回裁剪的媒体图片列表
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;

    private List<LocalMedia> selectMainList = new ArrayList<>();
    @ViewInject(R.id.recycler_main)
    private RecyclerView recyclerView_main;
    private final int REQUEST_MAIN_PHONT = 666;
    private GridImageAdapter adapterMain;
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
    private String brandName;//品牌
    private String modelName;//车型
    private String seriesName;//车系
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

    boolean ischeck=true;//是否选中协议

    private String str_refit_info;//改装信息
    private String str_interior_color;//内饰颜色
    private String str_body_color;//车身颜色

    private String mainPhoto = "";//主图地址
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void setLayout() {
        setContentView(R.layout.act_release_new_car);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoftHideKeyBoardUtil.assistActivity(this);
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
                    if (!vehicleImageList.get(0).getType().equals("1"))
                        vehicleImageList.add(0,new NewCarImgEntity("1",mainPhoto));
                    submitData(id,brandId,modelId,seriesId,str_body_color,str_interior_color,str_refit_info,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
//                    if (isChanged || isLocalImg){//增加图片或者重新选择图片  有本地保存图片数据
//                        vehicleImageList.clear();
//                        for (int i = 0; i < selectList.size(); i++) {
//                            uploadPhoto(i, new File(selectList.get(i).getCompressPath()),false);
//                        }
//                    }else {
//
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
    public void removeIitem(int position,boolean isMain){
        if (isMain){
            mainPhoto="";
            selectMainList.clear();
        }else {
            if (vehicleImageList != null && (position-1) < vehicleImageList.size()) {
                vehicleImageList.remove(position - 1);
                selectList.remove(position-1);
            }
        }

        tv_img_num.setText((selectList.size()+selectMainList.size())+"");
    }

    //临时退出保存当前界面数据
    private void saveData(){
        NewCarInfoBean bean = new NewCarInfoBean();
        DatailBean datailBean = new DatailBean();
        datailBean.setBrandId(brandId);
        datailBean.setSeriesId(seriesId);
        datailBean.setModelId(modelId);
        if (!"请选择品牌车型".equals(tv_ppcx.getText().toString().trim())){
            datailBean.setBrandName(brandName);
            datailBean.setModelName(modelName);
            datailBean.setSeriesName(seriesName);
            datailBean.setVehicleFullName(tv_ppcx.getText().toString().trim());
        }

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

        if (!TextUtils.isEmpty(et_body_color.getText().toString().trim()))
            datailBean.setVehicleColor(et_body_color.getText().toString().trim());
        if (!TextUtils.isEmpty(et_interior_color.getText().toString().trim()))
            datailBean.setInsideColor(et_interior_color.getText().toString().trim());
        if (!TextUtils.isEmpty(et_refit_info.getText().toString().trim()))
            datailBean.setModified(et_refit_info.getText().toString().trim());

        if (!TextUtils.isEmpty(et_mendian_money_one.getText().toString().trim()))
            datailBean.setSalesPriceMin(et_mendian_money_one.getText().toString().trim());
        if (!TextUtils.isEmpty(et_mendian_money_two.getText().toString().trim()))
            datailBean.setSalesPriceMax(et_mendian_money_two.getText().toString().trim());
        if (!TextUtils.isEmpty(et_pifa_money_one.getText().toString().trim()))
            datailBean.setTradePriceMin(et_pifa_money_one.getText().toString().trim());
        if (!TextUtils.isEmpty(et_pifa_money_two.getText().toString().trim()))
            datailBean.setTradePriceMax(et_pifa_money_two.getText().toString().trim());

        if (selectMainList!=null && selectMainList.size()>0) {
            if (selectMainList.get(0).getCompressPath().startsWith("http"))
                datailBean.setLocalMediaMainList(selectMainList);
        }

        if (selectAllList!=null && selectAllList.size()>0){
            if (selectAllList.get(0).getCompressPath().equals("https://www.baidu.com/img/bd_logo1.png"))
                selectAllList.remove(0);
            selectList.clear();
            selectList.addAll(selectAllList);
        }
        if (selectList!=null && selectList.size()>0)
            datailBean.setLocalMediaList(selectList);

        bean.setDetail(datailBean);
        PhotoFaUtils.setCarnewdetail(mContext,GsonUtils.bean2Json(bean));
    }
    private void setData(NewCarInfoBean bean){
        tv_ppcx.setTextColor(getResources().getColor(R.color.black_2));
        tv_ss.setTextColor(getResources().getColor(R.color.black_2));
        tv_cg.setTextColor(getResources().getColor(R.color.black_2));
//        if (!TextUtils.isEmpty(bean.getDetail().getVehicleFullName())) {
//            tv_ppcx.setText(RoncheUtil.getSelfString(bean.getDetail().getVehicleFullName()));
//        }
//        else {
//            tv_ppcx.setTextColor(getResources().getColor(R.color.gray_3));
//        }
        StringBuilder builder=new StringBuilder();
        if(!TextUtils.isEmpty(bean.getDetail().getBrandName())){//品牌名名
            builder.append(bean.getDetail().getBrandName());
        }
        if(!TextUtils.isEmpty(bean.getDetail().getSeriesName())){//车系名名
            builder.append(bean.getDetail().getSeriesName());

        }
        if(!TextUtils.isEmpty(bean.getDetail().getModelName())){//车型名名
            builder.append(bean.getDetail().getModelName());

        }
        if(!TextUtils.isEmpty(builder.toString())){
            tv_ppcx.setText(builder.toString());
        }else {
            tv_ppcx.setTextColor(getResources().getColor(R.color.gray_3));
        }
        if (!TextUtils.isEmpty(bean.getDetail().getProvinceName()))
            tv_ss.setText(RoncheUtil.getSelfString(bean.getDetail().getProvinceName())+RoncheUtil.getSelfString(bean.getDetail().getCityName()));
        else
            tv_ss.setTextColor(getResources().getColor(R.color.gray_3));
        if (!TextUtils.isEmpty(bean.getDetail().getVehicleStandardName()))
            tv_cg.setText(RoncheUtil.getSelfString(bean.getDetail().getVehicleStandardName()));
        else
            tv_cg.setTextColor(getResources().getColor(R.color.gray_3));

        if (!TextUtils.isEmpty(bean.getDetail().getVehicleColor()))
            et_body_color.setText(RoncheUtil.getSelfString(bean.getDetail().getVehicleColor()));
        if (!TextUtils.isEmpty(bean.getDetail().getInsideColor()))
            et_interior_color.setText(RoncheUtil.getSelfString(bean.getDetail().getInsideColor()));
        if (!TextUtils.isEmpty(bean.getDetail().getModified()))
            et_refit_info.setText(RoncheUtil.getSelfString(bean.getDetail().getModified()));

        if (!TextUtils.isEmpty(bean.getDetail().getSalesPriceMin()))
            et_mendian_money_one.setText(RoncheUtil.getSelfString(bean.getDetail().getSalesPriceMin()));
        if (!TextUtils.isEmpty(bean.getDetail().getSalesPriceMax()))
            et_mendian_money_two.setText(RoncheUtil.getSelfString(bean.getDetail().getSalesPriceMax()));
        if (!TextUtils.isEmpty(bean.getDetail().getTradePriceMin()))
            et_pifa_money_one.setText(RoncheUtil.getSelfString(bean.getDetail().getTradePriceMin()));
        if (!TextUtils.isEmpty(bean.getDetail().getTradePriceMax()))
            et_pifa_money_two.setText(RoncheUtil.getSelfString(bean.getDetail().getTradePriceMax()));
        List<PicListBean> imgList =  bean.getDetail().getPicList();//服务器图片数据
        if (imgList!=null && imgList.size()>0) {
            brandId = bean.getDetail().getBrandId();
            modelId = bean.getDetail().getModelId();
            seriesId = bean.getDetail().getSeriesId();
            brandName = bean.getDetail().getBrandName();
            modelName = bean.getDetail().getModelName();
            seriesName = bean.getDetail().getSeriesName();
            province = bean.getDetail().getProvinceName();
            provinceId = bean.getDetail().getProvince();
            city = bean.getDetail().getCityName();
            cityId = bean.getDetail().getCity();
            str_body_color = bean.getDetail().getVehicleColor();
            str_interior_color = bean.getDetail().getInsideColor();
            str_refit_info = bean.getDetail().getModified();
            strCg_key = bean.getDetail().getVehicleStandard();
            vehicleImageList.clear();
            tv_img_num.setText(imgList.size()+"");
            for (int i = 0; i < imgList.size(); i++) {
                Log.e("===pic",imgList.get(i).getVehicleImage());
                LocalMedia media = new LocalMedia();
                media.setPath(imgList.get(i).getVehicleImage());
                media.setCompressPath(imgList.get(i).getVehicleImage());
                if (imgList.get(i).getType().equals("1")) {
                    selectMainList.add(media);
                    mainPhoto = imgList.get(i).getVehicleImage();
                }
                else {
                    selectList.add(media);
                    vehicleImageList.add(new NewCarImgEntity(imgList.get(i).getType(), imgList.get(i).getVehicleImage()));
                }
            }
            notifyList();
            adapterMain.setList(selectMainList);
            adapterMain.notifyDataSetChanged();
        }else {
            brandId = bean.getDetail().getBrandId();
            seriesId = bean.getDetail().getSeriesId();
            modelId = bean.getDetail().getModelId();
            brandName = bean.getDetail().getBrandName();
            modelName = bean.getDetail().getModelName();
            seriesName = bean.getDetail().getSeriesName();
            provinceId = bean.getDetail().getProvince();
            cityId = bean.getDetail().getCity();
            str_body_color = bean.getDetail().getVehicleColor();
            str_interior_color = bean.getDetail().getInsideColor();
            str_refit_info = bean.getDetail().getModified();
            province = bean.getDetail().getProvinceName();
            city = bean.getDetail().getCityName();
            vehicleStandardName = bean.getDetail().getVehicleStandardName();
            strCg_key = bean.getDetail().getVehicleStandard();
            selectList = bean.getDetail().getLocalMediaList();//本地图片数据
            if (selectList!=null && selectList.size()>0){
                isLocalImg = true;
                notifyList();
                for (int i=0;i<selectList.size();i++){
                    vehicleImageList.add(new NewCarImgEntity("2", selectList.get(i).getCompressPath()));
                }
            }else {
                selectList = new ArrayList<>();
            }
            //本地主图
            selectMainList = bean.getDetail().getLocalMediaMainList();
            if (selectMainList!=null && selectMainList.size()>0){
                isLocalImg = true;
                mainPhoto = selectMainList.get(0).getCompressPath();
                adapterMain.setList(selectMainList);
                adapterMain.notifyDataSetChanged();
            }else {
                selectMainList = new ArrayList<>();
            }
        }

        tv_img_num.setText((selectList.size()+selectMainList.size())+"");
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
    LocalMedia media ;

    private void initLocalMedia(){
        if (media==null) {
            media = new LocalMedia();
            media.setPath("https://www.baidu.com/img/bd_logo1.png");
            media.setCompressPath("https://www.baidu.com/img/bd_logo1.png");
        }
    }
    private void init(){
        initLocalMedia();
        cb_check.setChecked(true);
        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ischeck=true;
                }else {
                    ischeck=false;
                }
            }
        });


        themeId = R.style.picture_default_style;
        InputFilter[] filtermoney = {new EditInputFilter(99999,2)};
        et_mendian_money_one.setFilters(filtermoney);
        et_mendian_money_two.setFilters(filtermoney);
        et_pifa_money_one.setFilters(filtermoney);
        et_pifa_money_two.setFilters(filtermoney);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReleaseNewCarActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ReleaseNewCarActivity.this, onAddPicClickListener,false);
        selectAllList.clear();
        selectAllList.add(media);
        selectAllList.addAll(selectList);
        adapter.setList(selectAllList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    //获取真正的车辆图片列表
                    if (selectAllList!=null && selectAllList.size()>0) {
                        if (selectAllList.get(0).getCompressPath().equals("https://www.baidu.com/img/bd_logo1.png"))
                            selectAllList.remove(0);
                        selectList.clear();
                        selectList.addAll(selectAllList);
                    }

                    LocalMedia media = selectList.get(position-1);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(ReleaseNewCarActivity.this).themeStyle(themeId).openExternalPreview(position-1, selectList);
                            notifyList();
                            break;
                    }
                }
            }
        });
        FullyGridLayoutManager managerMain = new FullyGridLayoutManager(ReleaseNewCarActivity.this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView_main.setLayoutManager(managerMain);
        adapterMain = new GridImageAdapter(ReleaseNewCarActivity.this, onAddMainPicClickListener,true);
        adapterMain.setList(selectMainList);
        adapterMain.setSelectMax(1);
        recyclerView_main.setAdapter(adapterMain);
        adapterMain.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectMainList.size() > 0) {
                    LocalMedia media = selectMainList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(ReleaseNewCarActivity.this).themeStyle(themeId).openExternalPreview(position, selectMainList);
                            break;
                    }
                }
            }
        });

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
//                Toast.makeText(getApplicationContext(), selectAllList.get(vh.getLayoutPosition()).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //判断被拖拽的是否是第一个，如果不是则执行拖拽  小于14张图并且不是最后一个
                if (vh.getLayoutPosition()!=0 ) {
                    if (selectAllList.size()!=15 && vh.getLayoutPosition() == selectAllList.size() ){

                    }else {
                        mItemTouchHelper.startDrag(vh);

                        //获取系统震动服务
                        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                        vib.vibrate(70);
                    }

                }
            }
        });
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                Log.e("===fromPosition:",fromPosition+"==toPosition:"+toPosition);
                //起始位置结束位置均不能为0，如果图片列表小于15则起始位置结束位置均不能为最后
                if (fromPosition!=0 && toPosition!=0) {
                    if (selectAllList.size()!=15 && (fromPosition == selectAllList.size() || toPosition == selectAllList.size())) {
                        return false;
                    }else {
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(selectAllList, i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(selectAllList, i, i - 1);
                            }
                        }
                        adapter.notifyItemMoved(fromPosition, toPosition);
                        return true;
                    }
                }
//                if (fromPosition!=0 && toPosition!=0 && fromPosition < selectAllList.size() && toPosition < selectAllList.size()) {
//                    Collections.swap(selectAllList, fromPosition, toPosition);
//                    adapter.notifyItemMoved(fromPosition, toPosition);
//                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(ReleaseNewCarActivity.this)
                        .openGallery(ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum-selectAllList.size())// 最大图片选择数量
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
                        .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
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
    //主图适配器
    private GridImageAdapter.onAddPicClickListener onAddMainPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = true;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(ReleaseNewCarActivity.this)
                        .openGallery(ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(3)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE )// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectMainList)// 是否传入已选图片
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(REQUEST_MAIN_PHONT);//结果回调onActivityResult code
            }
        }

    };
    final int CG=1101;
    @OnClick({R.id.ll_ppcx,R.id.ll_clszd,R.id.ll_cg,R.id.tv_tj,R.id.iv_add_photo,R.id.tv_agreement})
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
                    if (!vehicleImageList.get(0).getType().equals("1"))
                        vehicleImageList.add(0,new NewCarImgEntity("1",mainPhoto));
                    Log.e("===",vehicleImageList.toString());

                    submitData(id,brandId,modelId,seriesId,str_body_color,str_interior_color,str_refit_info,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);

//                    if (isChanged || isLocalImg) {//增加图片或者重新选择图片
//                        vehicleImageList.clear();
//                        for (int i = 0; i < selectList.size(); i++) {
//                            uploadPhoto(i, new File(selectList.get(i).getCompressPath()),false);
//                        }
//                    }else {
//                        submitData(id,brandId,modelId,seriesId,str_body_color,str_interior_color,str_refit_info,etMendianBefore,etMendianAfter,etPifaBefore,etPifaAfter,strCg_key);
//                    }
                }
                break;
            case R.id.iv_add_photo:

                break;
            case R.id.tv_agreement://跳转协议，不显示底部同意按钮，仅供查看
                startActivity(new Intent(mContext,WebActivity.class).putExtra("type","4").putExtra("release_type","1").putExtra("isShowBottom",false));
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

        str_body_color = et_body_color.getText().toString().trim();//车身颜色
        str_interior_color = et_interior_color.getText().toString().trim();//内饰颜色
        str_refit_info = et_refit_info.getText().toString().trim();//改装信息

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
        if (TextUtils.isEmpty(str_body_color) ){
            ToastTool.show(mContext,"请输入车身颜色");
            return false;
        }
        if (TextUtils.isEmpty(str_interior_color) ){
            ToastTool.show(mContext,"请输入内饰颜色");
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
        if (selectMainList==null || selectMainList.size()==0){
            ToastTool.show(mContext,"请选择车辆主图");
            return false;
        }

        if (selectList==null || selectList.size()==0){
            ToastTool.show(mContext,"请选择车辆图片");
            return false;
        }

        if (selectList.size()<5){
//            ToastTool.show(mContext,"请至少选择5张车辆图片");
            ToastTool.show(mContext,"最多可以上传15张图片，最少上传6张(含封面)");
            return false;
        }

        if (!ischeck){
            ToastTool.show(mContext,"您未同意协议");
            return false;
        }
        return true;
    }
    //更新车辆图片列表
    private void notifyList(){
        //手动添加第一位item数据
        selectAllList.clear();
        initLocalMedia();
        selectAllList.add(0,media);
        selectAllList.addAll(selectList);
        Log.e("===","selectAllList.size()"+selectAllList.size());
        adapter.setList(selectAllList);
        adapter.notifyDataSetChanged();
    }
    private int flag =0;//标志位，传入的数量，每次加1
    private void uploadPhoto(final int i, File file, final boolean isMain) {
        if (i==0)
            upLoadingShow();
        RequestParam param=new RequestParam();
        param.addFile("f1",file);
        if (!param.hasFiles()) {//参数不为空
            OkHttpUtils.post(Config.UPLOAD, param, new OkHttpCallBack(mContext, "上传中...") {
                @Override
                public void onSuccess(String data) {
//                    isUploadImg = true;
                    try {
                        JSONObject object = new JSONObject(data.toString());
                        JSONArray array = object.getJSONArray("result");
                        JSONObject obj = (JSONObject) array.get(0);
                        String imagepath = obj.getString("imageUrl");
                        if (i == 0 && isMain) {
                            //主图服务器地址
                            mainPhoto = imagepath;
                            LocalMedia media = selectMainList.get(0);
                            media.setCutPath(mainPhoto);
                            media.setPath(mainPhoto);
                            media.setCompressPath(mainPhoto);
                            selectMainList.clear();
                            selectMainList.add(media);
//                            vehicleImageList.add(new NewCarImgEntity("1", imagepath));
                        } else {
                            selectCropList.get(i).setCutPath(imagepath);
                            selectCropList.get(i).setPath(imagepath);
                            selectCropList.get(i).setCompressPath(imagepath);
                            Log.e("===",selectCropList.size()+"==i="+i+selectCropList.get(i));
                            selectList.add(selectCropList.get(i));
                            Log.e("===","selectList.size():"+selectList.size());
                            vehicleImageList.add(new NewCarImgEntity("2", imagepath));
                        }

                        Log.e("====",flag+"==="+selectCropList.size());

                        if(i==selectMainList.size()-1 && isMain){
                            adapterMain.setList(selectMainList);
                            adapterMain.notifyDataSetChanged();
                            tv_img_num.setText((selectList.size()+selectMainList.size())+"");
                            upLoadingClose();
                        }else {
                            notifyList();
                            tv_img_num.setText((selectList.size()+selectMainList.size())+"");
                            if (flag == selectCropList.size()-1) {
                                upLoadingClose();
                                flag =0;
                            }
                        }
                        flag++;


//                        if (vehicleImageList.size() == selectList.size()+1 && selectList.size()>5) {
//                            Log.e("===vehicleImageList:", vehicleImageList.toString());
////                            submitData(id, brandId, modelId, seriesId, str_body_color, str_interior_color, str_refit_info, etMendianBefore, etMendianAfter, etPifaBefore, etPifaAfter, strCg_key);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        flag--;
//                        upLoadingClose();
//                        tv_img_num.setText((selectList.size()+selectMainList.size())+"");
//                        if (isMain){
//                            adapterMain.setList(selectMainList);
//                            adapterMain.notifyDataSetChanged();
//                        }else {
//                            notifyList();
//                        }
                    }

                }

                @Override
                public void onError(int code, String error) {
                    ToastTool.show(mContext, error);
//                    flag--;
                    upLoadingClose();
//                    tv_img_num.setText((selectList.size()+selectMainList.size())+"");
//                    if (isMain){
//                        adapterMain.setList(selectMainList);
//                        adapterMain.notifyDataSetChanged();
//                    }else {
//                        notifyList();
//                    }
                }
            });
        }else {

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

            brandName = brand.getName();
            modelName = bean.getModelName();
            seriesName = chexibean.getModelName();

            String str=brand.getName()+chexibean.getModelName()+bean.getModelName();
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
            if(operType.equals("1")) {
                saveData();
            }
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
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectCropList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    flag = 0;
                    for (int i=0;i<selectCropList.size();i++){
                        uploadPhoto(i, new File(selectCropList.get(i).getCompressPath()),false);
                    }


                    break;
                case REQUEST_MAIN_PHONT:
                    isMainChanged = true;
                    selectMainList = PictureSelector.obtainMultipleResult(data);
                    uploadPhoto(0, new File(selectMainList.get(0).getCompressPath()), true);

                    break;

            }

        }

    }

    //id 当操作类型为修改时id为车辆主键
    private void submitData(String id,String brandId,String modelId,String seriesId,String vehicleColor,String insideColor,String modified, String salesPriceMin,String salesPriceMax,String tradePriceMin,String tradePriceMax,String vehicleStandard){
        upLoadingShow();
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

        if (!TextUtils.isEmpty(vehicleColor))
            param.add("vehicleColor",vehicleColor);
        if (!TextUtils.isEmpty(insideColor))
            param.add("insideColor",insideColor);
        if (!TextUtils.isEmpty(modified))
            param.add("modified",modified);

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
//        if (isUploadImg){
//            param.add("imgList", GsonUtils.bean2Json(vehicleImageList));
//        }else if (operType.equals("2") && vehicleImageList!=null && vehicleImageList.size() >= 0) {
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
                vehicleImageList.remove(0);
            }
        });
    }
}
