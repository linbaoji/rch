package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.SellBrandAdapter;
import com.rch.adatper.SortAdapter;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GlideUtils;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.CharacterParser;
import com.rch.custom.PinyinComparator;
import com.rch.custom.SideBar;
import com.rch.custom.SortModel;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.HistoryCarBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by acer on 2018/8/19.
 */

public class NewBrandAct extends BaseActivity{
    @ViewInject(R.id.sidrbar)
    private SideBar sidrbar;
    @ViewInject(R.id.lv_brand)
    private ListView lv_brand;
    @ViewInject(R.id.lv_cx)
    private ListView lv_cx;
    @ViewInject(R.id.ll_histry)
    private LinearLayout ll_histry;
    @ViewInject(R.id.tv_scls)
    private TextView tv_scls;
    @ViewInject(R.id.gv_history)
    private GridView gv_history;//历史记录


    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;

    List<BrandEntity>list=new ArrayList<>();
    private SortAdapter adapter;

    private SortModel sortmodel;
    private CarSeriesEntity chexibean;
    CxAdpter cxadapter;//车系

    private List<HistoryCarBean>historlist;
    private Gson gson;

    List<CarSeriesEntity> carSeriesList=new ArrayList<>();//车系list

    private int type;//1只选择品牌，2选择品牌车系，3选择品牌车系车型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_newbrand);
        initToolBar();
        ViewUtils.inject(this);
        AppManager.getAppManager().addActivity(this);
        type=getIntent().getExtras().getInt("type",1);


        gson=new Gson();
        if(type==2){//历史记录只有品牌车系时候有
           String jsonarray=SpUtils.gethistory(NewBrandAct.this);
           if(!TextUtils.isEmpty(jsonarray)){
               ll_histry.setVisibility(View.VISIBLE);
               historlist=gson.fromJson(jsonarray,new TypeToken<List<HistoryCarBean>>(){}.getType());
               gv_history.setAdapter(new MgridAdapter());

               gv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       SortModel model=new SortModel();
                       model.setBrandId(historlist.get(i).getBrandId());
                       model.setName(historlist.get(i).getName());
                       model.setBrandImagePath(historlist.get(i).getBrandImagePath());

                       CarSeriesEntity bean=new CarSeriesEntity();
                       bean.setId(historlist.get(i).getId());
                       bean.setModelName(historlist.get(i).getModelName());

                       setResult(RESULT_OK,new Intent().
                               putExtra("sortmodel",model).
                               putExtra("chexi",bean));

                       AppManager.getAppManager().finishActivity(NewBrandAct.this);
                   }

               });
           }else {
               historlist=new ArrayList<>();
               ll_histry.setVisibility(View.GONE);
           }

        }else {
            ll_histry.setVisibility(View.GONE);
        }


        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    lv_brand.setSelection(position);
                }

            }
        });


        //品牌
        lv_brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sortmodel=((SortModel)adapter.getItem(position));
                if(type==1){//只选择品牌
                    setResult(RESULT_OK,new Intent().putExtra("sortmodel",sortmodel));
                    AppManager.getAppManager().finishActivity(NewBrandAct.this);
                }else {
//                    requestCarSeriesData(sortmodel);//继续选择车系
                    startActivity(new Intent(NewBrandAct.this,NewCarModleAct.class).putExtra("sortmodel",sortmodel).putExtra("type",type));
                }

            }
        });

        //车系
        lv_cx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chexibean=carSeriesList.get(i);
                if(type==2){//知道车系就完不选择了
                    setResult(RESULT_OK,new Intent().putExtra("chexi",chexibean).putExtra("sortmodel",sortmodel));
                    //加到历史记录里面去
                    HistoryCarBean carhistbean=new HistoryCarBean();
                    carhistbean.setBrandId(sortmodel.getBrandId());
                    carhistbean.setName(sortmodel.getName());
                    carhistbean.setBrandImagePath(sortmodel.getBrandImagePath());
                    carhistbean.setId(chexibean.getId());
                    carhistbean.setModelName(chexibean.getModelName());

                    historlist.add(0,carhistbean);//每次都加到最前面
                    SpUtils.sethistoty(NewBrandAct.this, GsonUtils.bean2Json(historlist));

                    AppManager.getAppManager().finishActivity(NewBrandAct.class);
                }else {//继续往下调接口
                            startActivity(new Intent(NewBrandAct.this, ToppedAct.class).
                            putExtra("sortmodel", sortmodel).
                            putExtra("chexibean", chexibean)
                    );
                }
            }
        });

        requestBrandData();

    }

    //获取品牌
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
                list=JsonTool.getBrandListData(data);
                 if(list!=null&&list.size()>0){
                      if(type!=1) {//表示只选品牌
                          for (int j = 0; j < list.size(); j++) {
                              if (list.get(j).getBrandName().equals("不限品牌")) {
                                  list.remove(j);
                              }
                          }
                      }


                    SourceDateList = filledData(list);
                    Collections.sort(SourceDateList, pinyinComparator);
                    adapter = new SortAdapter(NewBrandAct.this, SourceDateList);
                    lv_brand.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int code, String error) {

                ToastTool.show(NewBrandAct.this,error);
            }
        });
    }

    private List<SortModel> filledData(List<BrandEntity>list){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<list.size(); i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(list.get(i).getBrandName());
            sortModel.setBrandId(list.get(i).getId());
            sortModel.setBrandImagePath(list.get(i).getBrandLogo());
            String pinyin = characterParser.getSelling(list.get(i).getBrandName());
            String sortString = pinyin.substring(0, 1).toUpperCase();


            if(sortString.matches("[A-Z]")){
                if(list.get(i).getBrandName().equals("不限品牌")){
                    sortModel.setSortLetters("*");
                }else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    /*获取车系*/
    private void requestCarSeriesData(SortModel model)
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("brandId",model.getBrandId());
        OkHttpUtils.post(Config.CARSERIES, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                carSeriesList=JsonTool.getCarSeriesListData(data);
                if(carSeriesList.size()==0){
                    lv_cx.setVisibility(View.GONE);
                    ToastTool.show(NewBrandAct.this,"暂无数据");
                }else {
                    lv_cx.setVisibility(View.VISIBLE);
                    cxadapter=new CxAdpter();
                    lv_cx.setAdapter(cxadapter);

                }
            }

            @Override
            public void onError(int code, String error) {
                //    load_view.loadError();
                ToastTool.show(NewBrandAct.this,error);
            }
        });
    }

    @OnClick({R.id.brand_back,R.id.tv_scls})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.brand_back:
                finish();
                break;

            case R.id.tv_scls:
                SpUtils.sethistoty(NewBrandAct.this,"");
                historlist.clear();
                ll_histry.setVisibility(View.GONE);
                break;
        }
    }


    public class CxAdpter extends BaseAdapter{

        @Override
        public int getCount() {
            return carSeriesList.size() ;
        }

        @Override
        public Object getItem(int i) {
            return carSeriesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
             view= View.inflate(NewBrandAct.this,R.layout.sell_brand_adapter,null);
             TextView name = (TextView) view.findViewById(R.id.sell_adapter_text);
             LinearLayout item = (LinearLayout) view.findViewById(R.id.sell_adapter_item);
             name.setText(carSeriesList.get(i).getModelName());
             return view;
        }
    }

    private class MgridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(historlist.size()>5){
                return 5;
            }else {
                return historlist.size();
            }
        }

        @Override
        public Object getItem(int i) {
            return historlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=View.inflate(NewBrandAct.this,R.layout.item_historygrid,null);
            ImageView iv_pp= (ImageView) view.findViewById(R.id.iv_pp);
            TextView tv_pp= (TextView) view.findViewById(R.id.tv_pp);
            tv_pp.setText(historlist.get(i).getName());
            GlideUtils.showImg(NewBrandAct.this,historlist.get(i).getBrandImagePath(),R.mipmap.car_emp,iv_pp);
//            Glide.with(NewBrandAct.this).load(historlist.get(i).getBrandImagePath()).placeholder(R.mipmap.car_emp).into(iv_pp);
            return view;
        }
    }
}
