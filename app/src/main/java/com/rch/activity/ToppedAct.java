package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.custom.SortModel;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.ChexinBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by acer on 2018/8/19.
 */

public class ToppedAct extends BaseActivity{

    @ViewInject(R.id.lv_chexi)
    private ListView lv_chexin;

    private SortModel sortmodel;
    private CarSeriesEntity chexibean;

    List<ChexinBean>list;
    private ChexinBean chexinbean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_topped);
        initToolBar();
        ViewUtils.inject(this);
        sortmodel= (SortModel) getIntent().getSerializableExtra("sortmodel");
        chexibean= (CarSeriesEntity) getIntent().getSerializableExtra("chexibean");
        getChexin();

        lv_chexin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chexinbean=list.get(i);
                sendBroadcast(new Intent(ReleaseAct.REFRESHHOMEFRAGMENTMODULE).
                        putExtra("brand",sortmodel).putExtra("chexi",chexibean).putExtra("chexin",chexinbean));
                sendBroadcast(new Intent(ReleaseNewCarActivity.REFRESHHOMEFRAGMENTMODULE).
                        putExtra("brand",sortmodel).putExtra("chexi",chexibean).putExtra("chexin",chexinbean));
                AppManager.getAppManager().finishActivity(NewBrandAct.class);
                AppManager.getAppManager().finishActivity(NewCarModleAct.class);
                finish();
            }
        });
    }

    private void getChexin() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("seriesId",chexibean.getId());
        OkHttpUtils.post(Config.VTYPELIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray array=object.getJSONArray("result");
                    list=gson.fromJson(array.toString(),new TypeToken<List<ChexinBean>>(){}.getType());
                    if(list!=null&&list.size()>0){
                        lv_chexin.setAdapter(new MyAdapter());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(int code, String error) {
                //    load_view.loadError();
                ToastTool.show(ToppedAct.this,error);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= View.inflate(ToppedAct.this,R.layout.sell_brand_adapter,null);
            TextView name = (TextView) view.findViewById(R.id.sell_adapter_text);
//            View line = view.findViewById(R.id.sell_adapter_line);
            LinearLayout item = (LinearLayout) view.findViewById(R.id.sell_adapter_item);
            name.setText(list.get(i).getModelName());
            return view;
        }
    }

    @OnClick({R.id.brand_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.brand_back:
                finish();
                break;
        }
    }
}
