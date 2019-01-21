package com.rch.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.CityAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GPSTool;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.CustomContactViewControl;
import com.rch.custom.LoadingView;
import com.rch.entity.CityInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CityLocationActivity extends BaseActivity implements AMapLocationListener{



    @ViewInject(R.id.tv_num)
    private TextView tv_num;
    @ViewInject(R.id.tv_ok)
    private TextView tv_ok;
    @ViewInject(R.id.city_recycler)
    RecyclerView recyclerView;
    @ViewInject(R.id.city_chat)
    CustomContactViewControl customContactViewControl;
    CityAdapter adatper;
    @ViewInject(R.id.city_back)
    ImageView back;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;

    List<CityInfoEntity> list=new ArrayList<>();
    final int REQUESTGPSCODE=101;

    int SELLCARREQUESTCODE=10103;//卖车
    int CITYREQUESTCODE=10102;//城市

    ArrayList<CityInfoEntity> listSelected = new ArrayList<>();


    String code="";
    boolean isResetLocation=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_location);
        initToolBar();
        ViewUtils.inject(this);
        code=getIntent().getStringExtra("requestType");
        initControls();
        setData();


    }

    private void updateLocalData(){
        String loacationCityList = SpUtils.getLoacationCityList(this);
        List<CityInfoEntity> cityInfoEntityList = GsonUtils.json2List(loacationCityList);
        listSelected.clear();
        if (cityInfoEntityList!=null && cityInfoEntityList.size()>0)
            listSelected.addAll(cityInfoEntityList);
        if (listSelected!=null && listSelected.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < listSelected.size(); j++) {
                    if (list.get(i).getCityName().equals(listSelected.get(j).getCityName()) ) {
                        list.get(i).setSelected(true);
                    }
                }
            }
        }
        Log.e("---list:",list.toString());
        adatper.updateCityData(list);
        tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));
    }
    @OnClick({R.id.city_back,R.id.tv_ok})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.city_back:
                finish();
                break;
            case R.id.tv_ok:
                Log.e("======ok:",listSelected.toString());
                setResult(RESULT_OK,new Intent().putParcelableArrayListExtra("CityInfoEntity",listSelected).putExtra("type",1));
                finish();
                break;
        }

    }
    private void initControls() {

//        recyclerView= (RecyclerView) findViewById(R.id.city_recycler);
//        customContactViewControl= (CustomContactViewControl) findViewById(R.id.city_chat);
//        load_view= (LoadingView) findViewById(R.id.load_view);
//        back= (ImageView) findViewById(R.id.city_back);


//        requestPermissions();
        Log.e("sha1",sHA1(this));

//        back.setOnClickListener(this);
        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                requestCarBrandData();
            }
        });
    }

    LinearLayoutManager linearLayoutManager=null;
    private void setData() {
        requestCarBrandData();
       // initTestData();
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adatper=new CityAdapter(this,list);
        recyclerView.setAdapter(adatper);

        customContactViewControl.setOnSingerNameIndexerClicked(new CustomContactViewControl.OnSingerNameIndexerClicked() {
            @Override
            public void singerNameItemClicked(String selectName) {
                //该字母首次出现的位置
                int position = adatper.getPositionForSection(selectName);

                if (position != -1) {
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                    //recyclerView.scrollToPosition(position);
                }
            }
        });

        adatper.setOnSelCityNameInterface(new CityAdapter.onSelCityNameInterface() {
            @Override
            public void onItem(CityInfoEntity entity,int position) {
                Log.e("entity:",entity.toString());
                Log.e("listSelected:",listSelected.toString());
//                setResult(RESULT_OK,new Intent().putExtra("CityInfoEntity",entity).putExtra("type",1));
//                finish();
                //判断选中的是否是全国，是则将其他选中的都设置为不选中
                if (entity.getCityName().equals("全国") && !entity.getSelected()){//全国第一次选中
                    listSelected.clear();
                    entity.setSelected(true);
                    listSelected.add(entity);
                    for (int i =0;i<list.size();i++){
                        list.get(i).setSelected(false);
                    }
                    adatper.updateCityData(list);
                    adatper.updateCityLayout(entity);
                    tv_num.setText(MessageFormat.format("已选择{0}个城市", listSelected.size()));
                    return;
                }else if (entity.getCityName().equals("全国") && entity.getSelected()){//第二次选中
                    for (CityInfoEntity city : listSelected) {
                        if (city.getCityName().equals(entity.getCityName())){
                            listSelected.remove(city);
                            break;
                        }
                    }
//                    listSelected.remove(entity);
                    entity.setSelected(false);
                    adatper.updateCityLayout(entity);
                    tv_num.setText(MessageFormat.format("已选择{0}个城市", listSelected.size()));
                    return;
                }

                //判断选中列表是否包含全国，包含则取消全国选中状态
                for (CityInfoEntity cityInfoEntity:listSelected){
                    if (cityInfoEntity.getCityName().equals("全国")){
                        listSelected.remove(cityInfoEntity);
                        adatper.updateFireCityList(0,false);
                        adatper.updateCityLayout(entity);
                    }
                }

                //判断选中的是否是上海
                if (entity.getCityName().equals("上海")){
                    entity.setSelected(!entity.getSelected());
                    Log.e("=====entity==:",entity.toString());
                    for (int i =0;i<list.size();i++){
                        if (list.get(i).getCityName().equals(entity.getCityName())) {
                            list.get(i).setSelected(entity.getSelected());
                            Log.e("=====list[i]:",list.get(i).toString());
                        }
                    }
                    List<CityInfoEntity> removeList = new ArrayList<>();
                    int size = 0;
                    //更新热门城市选中状态，同时更新列表选中状态
                    for (CityInfoEntity city : listSelected) {//移除
                        if (city.getCityName().equals(entity.getCityName())){
                            removeList.add(city);
                        }
                    }
                    if (removeList!=null && removeList.size()!=0) {//选中过
                        listSelected.removeAll(removeList);//移除
                    }else {//未选中过
                        //判断选中的是热门城市还是列表中的上海
                        if (entity.getTotal().equals("0")) {//热门城市
                            listSelected.add(entity);
                            for (CityInfoEntity city : listSelected) {//添加
                                if (city.getCityName().equals(entity.getCityName())) {
                                    listSelected.add(city);
                                    break;
                                }
                            }
                        }else {//列表
                            entity.setSelected(true);
                            listSelected.add(entity);
                            CityInfoEntity cityInfoEntity=new CityInfoEntity();
                            cityInfoEntity.setIdentity(false);
                            cityInfoEntity.setTotal("0");
                            cityInfoEntity.setCityLetter("热门城市");
                            cityInfoEntity.setIdentityCount(true);
                            cityInfoEntity.setCityName("上海");
                            cityInfoEntity.setCity("3");
                            cityInfoEntity.setSelected(true);
                            listSelected.add(cityInfoEntity);
                        }
                    }

                    adatper.updateCityData(list);
                    adatper.updateCityLayout(entity);
                    Log.e("====listSelected:",listSelected.toString());
                    tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));
                    return;
                }
                //判断选中列表中是否包含该实体，如果包含则移除，不包含则加入列表
                boolean isAdded = false;//是否已经添加进入该列表
                for (CityInfoEntity cityInfoEntity: listSelected) {
                    if (cityInfoEntity.getCityName().equals(entity.getCityName())){
                        if (!entity.getCityName().equals("上海") || (entity.getCityName().equals("上海") && entity.getSelected()))//热门和城市列表的上海可以共存
                            isAdded = true;
                    }
                }
                if (isAdded) {//已经添加了
                    entity.setSelected(false);
                    for (CityInfoEntity city : listSelected) {
                        if (city.getCityName().equals(entity.getCityName())){
                            listSelected.remove(city);
                            break;
                        }
                    }
//                    listSelected.remove(entity);
                } else {//没有添加的
                    entity.setSelected(true);
                    listSelected.add(entity);
                }
                tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));

                if (position>=0)
                    adatper.notifyItemChanged(position,entity);
                else
                    adatper.updateCityLayout(entity);

            }

            @Override
            public void onResetGps() {
                isResetLocation=true;
                requestPermissions();
            }

            @Override
            public void onGpsName(String gpsGityName) {
//                setResult(RESULT_OK,new Intent().putExtra("CityInfoEntity",gpsGityName).putExtra("type",2));
//                finish();
                CityInfoEntity entity = new CityInfoEntity();
                entity.setCityName(gpsGityName);

                //判断选中的是否是全国，是则将其他选中的都设置为不选中
                if (entity.getCityName().equals("全国") && !entity.getSelected()){//全国第一次选中
                    listSelected.clear();
                    entity.setSelected(true);
                    listSelected.add(entity);
                    for (int i =0;i<list.size();i++){
                        list.get(i).setSelected(false);
                    }
                    adatper.updateCityData(list);
                    adatper.updateCityLayout(entity);
                    tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));
                    return;
                }else if (entity.getCityName().equals("全国") && entity.getSelected()){//第二次选中
                    entity.setSelected(false);
                    listSelected.remove(entity);
                    adatper.updateCityLayout(entity);
                    tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));
                    return;
                }
                //判断选中列表是否包含全国，包含则取消全国选中状态
                for (CityInfoEntity cityInfoEntity:listSelected){
                    if (cityInfoEntity.getCityName().equals("全国")){
                        listSelected.remove(cityInfoEntity);
                        adatper.updateFireCityList(0,false);
                        adatper.updateCityLayout(entity);
                    }
                }
                //判断选中列表中是否包含该实体，如果包含则移除，不包含则加入列表
                boolean isAdded = false;//是否已经添加进入该列表
                for (CityInfoEntity cityInfoEntity: listSelected) {
                    if (cityInfoEntity.getCityName().equals(entity.getCityName()) || (entity.getCityName().equals("上海") && entity.getSelected())){
                        isAdded = true;
                    }
                }
                if (isAdded)
                    listSelected.remove(entity);
                else
                    listSelected.add(entity);
                tv_num.setText(MessageFormat.format("已选择{0}个城市", getCitySize()));
            }
        });

    }

    private int getCitySize(){
        boolean isHasSH = false;//是否包含上海
        for (CityInfoEntity entity :listSelected) {
            if (entity.getCityName().equals("上海"))
                isHasSH = true;
        }

        if (isHasSH)
            return listSelected.size()-1;
        else
            return listSelected.size();
    }

//    private void initTestData()
//    {
//        String[] strChat=new String[]{"热","S","H","C","Q","W","E"};
//        customContactViewControl.setIndexs(strChat);
//        CityInfoEntity cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("S");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("上海");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("S");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("苏州");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("S");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("沈阳");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("S");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("石家庄");
//        list.add(cityInfoEntity);
//
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("S");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("深圳");
//        list.add(cityInfoEntity);
//
//
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("H");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("湖南");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("H");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("湖北");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("H");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("海口");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("H");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("杭州");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("H");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("哈尔滨");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("C");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("重庆");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("C");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("成都");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("C");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长春");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("C");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长沙");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("C");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("潮汕");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("Q");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("重庆");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("Q");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("成都");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("Q");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长春");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("Q");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长沙");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("Q");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("潮汕");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("重庆");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("成都");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长春");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长沙");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("潮汕");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("重庆");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("成都");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长春");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长沙");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("W");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("潮汕");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(true);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("E");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("重庆");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("E");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("成都");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("E");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长春");
//        list.add(cityInfoEntity);
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("E");
//        cityInfoEntity.setIdentityCount(false);
//        cityInfoEntity.setCityName("长沙");
//        list.add(cityInfoEntity);
//
//
//        cityInfoEntity=new CityInfoEntity();
//        cityInfoEntity.setIdentity(false);
//        cityInfoEntity.setTotal("0");
//        cityInfoEntity.setCityLetter("E");
//        cityInfoEntity.setIdentityCount(true);
//        cityInfoEntity.setCityName("潮汕");
//        list.add(cityInfoEntity);
//
//
//    }

    private void requestCarBrandData() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        OkHttpUtils.post(Config.CITYLIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                String[] strChat= JsonTool.getChatSize(data);
                if(strChat.length>0)
                    strChat[0]="热";
                customContactViewControl.setIndexs(strChat);
                list=JsonTool.getCityListData(data);
                if(list.size()==0){
                    load_view.noContent();
                    load_view.setNoContentTxt("暂无数据");
                }else {
                    updateLocalData();
//                    adatper.updateCityData(list);
                }

            }

            @Override
            public void onError(int code, String error) {
                load_view.loadError();
                ToastTool.show(CityLocationActivity.this,error);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.city_back:
//                finish();
//                break;
//        }
//    }


    private void requestPermissions()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTGPSCODE);
            return;
        }
        //注掉自动定位
//        GPSTool.initGPS(getApplicationContext(),this);
    }



    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result= hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    //声明定位回调监听器
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                adatper.setLocationCity("定位成功");
                if(isResetLocation)
                    ToastTool.show(this,"定位成功");
                Log.e("city",aMapLocation.getCity());
                adatper.setLocationCity(aMapLocation.getCity());
            }else {
                adatper.setLocationCity("定位失败");
                ToastTool.show(this,"定位失败");
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
  /*
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Log.e("city",aMapLocation.getCity());
                    adatper.setLocationCity(aMapLocation.getCity());
                }else {
                    adatper.setLocationCity("定位失败");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case REQUESTGPSCODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED||grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    ToastTool.show(this,"获取位置权限失败，请手动开启");
                    return;
                }
                requestPermissions();
                break;
        }
    }

}
