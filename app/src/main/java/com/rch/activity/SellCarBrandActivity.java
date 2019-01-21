package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rch.R;
import com.rch.adatper.SellBrandAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.SellCarBrandLayout;
import com.rch.common.ToastTool;
import com.rch.custom.BrandBrowseHistoryLayout;
import com.rch.custom.CustomContactViewControl;
import com.rch.custom.LoadingView;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarSeriesEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */

public class SellCarBrandActivity extends BaseActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    SellBrandAdapter adapter;
    CustomContactViewControl customContactViewControl;
    ImageView back;
    List<BrandEntity> list=new ArrayList<>();
    List<CarSeriesEntity> carSeriesList=new ArrayList<>();

    List<CarSeriesEntity> historyList=new ArrayList<>();
    private LoadingView load_view;
    SellCarBrandLayout sell_brand_layout_data;

    LinearLayout sell_brand_history_layout;
    TextView sell_brand_del;
    BrandBrowseHistoryLayout sell_brand_history;
    ScrollView sell_brand_scroll_view;
    boolean isOpen=false;
    String id="",brandName="";
    BrandEntity brandEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_car_brand);
        initToolBar();
        initControls();
        setData();
    }


    private void initControls() {
        customContactViewControl= (CustomContactViewControl) findViewById(R.id.sell_brand_chat);
        back= (ImageView) findViewById(R.id.sell_brand_back);
        sell_brand_layout_data= (SellCarBrandLayout) findViewById(R.id.sell_brand_layout_data);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        sell_brand_history_layout= (LinearLayout) findViewById(R.id.sell_brand_history_layout);
        sell_brand_del= (TextView) findViewById(R.id.sell_brand_del);
        sell_brand_history= (BrandBrowseHistoryLayout) findViewById(R.id.sell_brand_history);
        sell_brand_scroll_view= (ScrollView) findViewById(R.id.sell_brand_scroll_view);

        analysisHistory();
        back.setOnClickListener(this);
        sell_brand_del.setOnClickListener(this);
        load_view= (LoadingView) findViewById(R.id.load_view);
        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                requestBrandData();
            }
        });
    }
    private void setData() {
        //setTestData();
        requestBrandData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SellBrandAdapter(this,carSeriesList);
        recyclerView.setAdapter(adapter);
        customContactViewControl.setOnSingerNameIndexerClicked(new CustomContactViewControl.OnSingerNameIndexerClicked() {
            @Override
            public void singerNameItemClicked(String selectName) {
                Log.e("selectName",selectName);
                //该字母首次出现的位置
                int position=sell_brand_layout_data.getLoaclPosition(selectName);
                sell_brand_scroll_view.scrollTo(0,position);
            }
        });

        sell_brand_layout_data.setOnSelBrandNameInterface(new SellCarBrandLayout.onSelBrandNameInterface() {
            @Override
            public void onItem(BrandEntity entity) {
                brandName=entity.getBrandName();
                id=entity.getId();
                requestCarSeriesData(id);
            }
        });

        adapter.setSelSellCarName(new SellBrandAdapter.selSellCarName() {
            @Override
            public void onItem(CarSeriesEntity entity) {

               // ToastTool.show(SellCarBrandActivity.this,entity.getBrandName());

                ToastTool.show(SellCarBrandActivity.this,entity.getBrandName());

//                ToastTool.show(SellCarBrandActivity.this,entity.getBrandName());

                isEistsHistory(entity);
                sell_brand_history_layout.setVisibility(View.VISIBLE);
                setResult(RESULT_OK, new Intent().putExtra("CarSeriesEntity", entity).putExtra("id",id).putExtra("brandName",brandName));
                finish();
            }
        });

        /*历史*/
        sell_brand_history.setOnHistoryItem(new BrandBrowseHistoryLayout.onHistoryItem() {
            @Override
            public void onItem(CarSeriesEntity entity) {
                setResult(RESULT_OK, new Intent().putExtra("CarSeriesEntity", entity).putExtra("id",entity.getId()).putExtra("brandName",entity.getBrandName()));
                finish();
            }
        });
    }

    /*解析历史*/
    private void analysisHistory()
    {
        historyList.clear();
        if(!getBrowseHistory().isEmpty()) {
            if (getBrowseHistory().indexOf("@") > -1) {
                String[] history = getBrowseHistory().split("@");
                for (int i=0;i<history.length;i++)
                {
                    String[] fields=history[i].toString().split(",");
                    analysisFields(fields);
                }
            }else{
                String[] fields=getBrowseHistory().split(",");
                analysisFields(fields);
            }
        }
        if(historyList.size()>0)
        {
            sell_brand_history_layout.setVisibility(View.VISIBLE);
            sell_brand_history.init(historyList);
        }
            /* if(!getBrowseHistory().isEmpty()) {
            if (getBrowseHistory().indexOf("@") > -1) {
                String[] history = getBrowseHistory().split("@");
                for (int i=0;i<history.length;i++)
                {
                    String[] fields=history[i].toString().split(",");
                    CarSeriesEntity entity=new CarSeriesEntity();
                    entity.setId(fields[0]);
                    entity.setBrandId(fields[1]);
                    entity.setModelName(fields[2]);
                    entity.setModelImage(fields[3]);
                    entity.setRemark(fields[4]);
                    entity.setIdentity(Boolean.parseBoolean(fields[5]));
                    entity.setIdentityCount(Boolean.parseBoolean(fields[6]));
                    entity.setBrandName(fields[7]);
                    historyList.add(entity);
                }
            }else{
                String[] fields=getBrowseHistory().split(",");
                CarSeriesEntity entity=new CarSeriesEntity();
                entity.setId(fields[0]);
                entity.setBrandId(fields[1]);
                entity.setModelName(fields[2]);
                entity.setModelImage(fields[3]);
                entity.setRemark(fields[4]);
                entity.setIdentity(Boolean.parseBoolean(fields[5]));
                entity.setIdentityCount(Boolean.parseBoolean(fields[6]));
                entity.setBrandName(fields[7]);
                historyList.add(entity);
            }
        }*/
    }

    private void analysisFields(String[] fields)
    {
        CarSeriesEntity entity=new CarSeriesEntity();
        entity.setId(fields[0]);
        entity.setBrandId(fields[1]);
        entity.setModelName(fields[2]);
        entity.setModelImage(fields[3]);
        entity.setRemark(fields[4]);
        entity.setIdentity(Boolean.parseBoolean(fields[5]));
        entity.setIdentityCount(Boolean.parseBoolean(fields[6]));
        entity.setBrandName(fields[7]);
        historyList.add(entity);
    }

    /*检查历史记录*/
    private void isEistsHistory(CarSeriesEntity entity)
    {
        String str="";
        if(getBrowseHistory().isEmpty())
            setBrowseHistory(entity.toString());
        else {
            if(getBrowseHistory().indexOf("@")>-1) {
                String[] history = getBrowseHistory().split("@");
                if (history.length >= 4) {
                    str = entity.toString() + "@" + history[0] + "@" + history[1] + "@" + history[2];
                    setBrowseHistory(str);
                } else {
                    for (int i=0;i<history.length;i++)
                        str+="@"+history[i];
                    str=entity.toString()+str;
                    setBrowseHistory(str);
                }
            }else {
                str=entity.toString()+"@"+getBrowseHistory();
                setBrowseHistory(str);
            }
        }
    }

    private void setTestData()
    {
        String[] indexs = {"*","A", "B", "C", "D", "E", "F"};
        customContactViewControl.setIndexs(indexs);
        BrandEntity entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("*");
        entity.setIdentity(true);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("A");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("B");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("C");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("D");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("E");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(true);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(false);
        list.add(entity);

        entity = new BrandEntity();
        entity.setId("");
        entity.setBrandName("不限品牌");
        entity.setFirstLetter("F");
        entity.setIdentity(false);
        entity.setIdentityCount(true);
        list.add(entity);


    }

    /*获取品牌*/
    private void requestBrandData()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        OkHttpUtils.post(Config.BRANDSLIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                String[] strChat= JsonTool.getChatSize(data);
                customContactViewControl.setIndexs(strChat);
                list=JsonTool.getBrandListData(data);
                if(list.size()==0){
                    load_view.noContent();
                    load_view.setNoContentTxt("暂无数据");
                }else {
                    list.remove(0);
                    sell_brand_layout_data.init(list);
                }
            }

            @Override
            public void onError(int code, String error) {
                load_view.loadError();
                ToastTool.show(SellCarBrandActivity.this,error);
            }
        });
    }


    /*获取车系*/
    private void requestCarSeriesData(String brandId)
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("brandId",brandId);
        OkHttpUtils.post(Config.CARSERIES, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                String[] strChat= JsonTool.getChatSize(data);
                customContactViewControl.setIndexs(strChat);
                carSeriesList=JsonTool.getCarSeriesListData(data);
                if(carSeriesList.size()==0){
                    //      load_view.noContent();
                    //     load_view.setNoContentTxt("暂无数据");
                }else {
                    adapter.updateBrandData(carSeriesList);
                    // isOpen=!isOpen;
                    if(!isOpen) {
                        isOpen=!isOpen;
                        showFilterPanel(isOpen, sell_brand_layout_data, recyclerView);
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                //    load_view.loadError();
                ToastTool.show(SellCarBrandActivity.this,error);
            }
        });
    }


    /*打开关闭动画*/
    public void showFilterPanel(final boolean isAnim,final View clickView,final View animView)
    {
        clickView.setEnabled(false);
        Animation animation;
        if(isAnim) {
            animation = AnimationUtils.loadAnimation(this, R.anim.filter_in);
            animView.setClickable(true);
        }
        else {
            animation = AnimationUtils.loadAnimation(this, R.anim.filter_out);
            animView.setClickable(false);

        }
        animView.clearAnimation();
        animView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clickView.setEnabled(true);
                if(isAnim) {
                    animView.setVisibility(View.VISIBLE);
                }
                else {
                    animView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.brand_back:
                finish();
                break;

            case R.id.sell_brand_del:
                sell_brand_history_layout.setVisibility(View.GONE);
                sell_brand_history.removeAllViews();
                clearBrowseHistory();
                break;
        }
    }
}
