package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.HistoryCarBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */

public class NewCarModleAct extends SecondBaseActivity{
    @ViewInject(R.id.lv_cx)
    private ListView lv_cx;

    private SortModel sortmodel;
    private int type;
    List<CarSeriesEntity> carSeriesList=new ArrayList<>();//车系list
    CxAdpter cxadapter;//车系
    private CarSeriesEntity chexibean;
    private List<HistoryCarBean>historlist;
    private Gson gson;

    @Override
    public void setLayout() {
        setContentView(R.layout.act_newcarmodle);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        sortmodel= (SortModel) getIntent().getExtras().getSerializable("sortmodel");
        type=getIntent().getExtras().getInt("type",2);//默认给2到车系就完成了
        gson=new Gson();
        requestCarSeriesData(sortmodel);//继续选择车系

        if(type==2){
            String jsonarray=SpUtils.gethistory(NewCarModleAct.this);
            historlist=gson.fromJson(jsonarray,new TypeToken<List<HistoryCarBean>>(){}.getType());
        }

        //车系
        lv_cx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chexibean=carSeriesList.get(i);//车系列
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
                    SpUtils.sethistoty(NewCarModleAct.this, GsonUtils.bean2Json(historlist));
                    AppManager.getAppManager().finishActivity(NewBrandAct.class);
                    finish();
                }else {//继续往下调接口
                    startActivity(new Intent(NewCarModleAct.this, ToppedAct.class).
                            putExtra("sortmodel", sortmodel).
                            putExtra("chexibean", chexibean)
                    );
                }
            }
        });

    }

    /*获取车系*/
    private void requestCarSeriesData(SortModel model)
    {
        RequestParam param = new RequestParam();
        param.add("brandId",model.getBrandId());
        OkHttpUtils.post(Config.CARSERIES, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                carSeriesList= JsonTool.getCarSeriesListData(data);
                if(carSeriesList.size()==0){
                    lv_cx.setVisibility(View.GONE);
                    ToastTool.show(NewCarModleAct.this,"暂无数据");
                }else {
                    lv_cx.setVisibility(View.VISIBLE);
                    cxadapter=new CxAdpter();
                    lv_cx.setAdapter(cxadapter);

                }
            }

            @Override
            public void onError(int code, String error) {
                //    load_view.loadError();
                ToastTool.show(NewCarModleAct.this,error);
            }
        });
    }

    public class CxAdpter extends BaseAdapter {

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
            view= View.inflate(NewCarModleAct.this,R.layout.sell_brand_adapter,null);
            TextView name = (TextView) view.findViewById(R.id.sell_adapter_text);
            LinearLayout item = (LinearLayout) view.findViewById(R.id.sell_adapter_item);
            name.setText(carSeriesList.get(i).getModelName());
            return view;
        }
    }

    @OnClick({R.id.brand_back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.brand_back:
                finish();
                break;
        }
    }
}
