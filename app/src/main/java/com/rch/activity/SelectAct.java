package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.common.Config;
import com.rch.custom.CommonView;
import com.rch.entity.Children;
import com.rch.entity.MeiJuBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择
 * Created by Administrator on 2018/8/3.
 */

public class SelectAct extends FragmentActivity {
    @ViewInject(R.id.tv_bt)
    private TextView tv_bt;
    @ViewInject(R.id.cv_name)
    private CommonView cv_name;
    @ViewInject(R.id.lv_select)
    private ListView lv_select;


    private String from;
    private List<Children>beanlist=new ArrayList<>();
    private int RESULTCODE=10000;
    private List<MeiJuBean>list;
    List<Children> pflist;
    List<Children> csjglist;
    List<Children> csyslist ;
    List<Children> bsxlist;
    List<Children> rylelist;
    private List<Children> rulersList=new ArrayList<>();//车规
    private List<Children> levelList=new ArrayList<>();//客户级别
    private List<Children> followWayList=new ArrayList<>();//跟进方式
    private List<Children> followResultList=new ArrayList<>();//跟进结果

    private List<String>dwlist=new ArrayList<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select);
        ViewUtils.inject(this);
        setWindowMatch();
        from=getIntent().getExtras().getString("from","");
        if(!TextUtils.isEmpty(from)){
            if(from.equals("2")){
                tv_bt.setText("请选择车身颜色");
                cv_name.setDesText("车身颜色");
                beanlist.add(new Children("01","黑色"));
                beanlist.add(new Children("02","红色"));
                beanlist.add(new Children("03","蓝色"));
                beanlist.add(new Children("04","白色"));
                beanlist.add(new Children("05","绿色"));
                beanlist.add(new Children("06","黄色"));
                beanlist.add(new Children("07","银灰色"));
                beanlist.add(new Children("08","灰色"));
                beanlist.add(new Children("01","黑色"));
                beanlist.add(new Children("01","黑色"));
                beanlist.add(new Children("01","黑色"));

            }else if(from.equals("3")){
                tv_bt.setText("请选择变速箱");
                cv_name.setDesText("变数箱");
            }else if(from.equals("4")){
                tv_bt.setText("请选择燃油类型");
                cv_name.setDesText("燃油类型");
            }else if(from.equals("5")){
                tv_bt.setText("请选择排放标准");
                cv_name.setDesText("排放标准");
            }else if(from.equals("1")){
                tv_bt.setText("请选择车身结构");
                cv_name.setDesText("车身结构");
            }else if(from.equals("6")){
                tv_bt.setText("请选择排量单位");
                cv_name.setDesText("排量单位");
            }else if (from.equals("7")){
                tv_bt.setText("请选择车规");
                cv_name.setDesText("车规");
//                rulersList.add(new Children("1","中规美规"));
//                rulersList.add(new Children("2","中东版"));
//                rulersList.add(new Children("3","加版"));
//                rulersList.add(new Children("4","欧版"));
//                rulersList.add(new Children("5","德版"));
//                rulersList.add(new Children("6","墨西哥版"));
            }else if (from.equals("8")){
                tv_bt.setText("请选择客户级别");
                cv_name.setDesText("客户级别");
//                levelList.add(new Children("1","H级"));
//                levelList.add(new Children("2","A级"));
//                levelList.add(new Children("3","B级"));
//                levelList.add(new Children("4","C级"));
//                levelList.add(new Children("5","N级"));
            }else if (from.equals("9")){
                tv_bt.setText("请选择跟进方式");
                cv_name.setDesText("跟进方式");
//                followWayList.add(new Children("1","电话"));
//                followWayList.add(new Children("2","短信"));
//                followWayList.add(new Children("3","微信"));
//                followWayList.add(new Children("4","展厅"));
//                followWayList.add(new Children("5","其他"));
            } else if (from.equals("10")){
                tv_bt.setText("请选择跟进结果");
                cv_name.setDesText("跟进结果");
//                followResultList.add(new Children("1","成功销售"));
//                followResultList.add(new Children("2","预订车辆"));
//                followResultList.add(new Children("3","到店看车"));
//                followResultList.add(new Children("4","销售失败"));
//                followResultList.add(new Children("5","约定下次跟进"));
            }
        }


            lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (from.equals("7")||from.equals("8")||from.equals("9")||from.equals("10")) {
                        Intent intent = new Intent();
                        Log.e("=====", beanlist.size() + "==" + beanlist.toString());
                        intent.putExtra("select", beanlist.get(i));
                        setResult(RESULTCODE, intent);
                        finish();
                    }

                }
            });


        if(!from.equals("6")) {
            lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent();
                    intent.putExtra("select",beanlist.get(i));
                    setResult(RESULTCODE,intent);
                    finish();

                }
            });
            getMeiDate();//获取数据
        }else if (from.equals("6")){
            dwlist.clear();
            dwlist.add("L");
            dwlist.add("T");
            lv_select.setAdapter(new MyDwAdapter());

            lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent();
                    intent.putExtra("select",dwlist.get(i));
                    setResult(RESULTCODE,intent);
                    finish();
                }
            });
        }
    }

    private void getMeiDate() {
        RequestParam param = new RequestParam();
        OkHttpUtils.post(Config.DOWNENUM_URL, param, new OkHttpCallBack(SelectAct.this, "加载中...") {
            @Override
            public void onSuccess(String data) {
                beanlist.clear();
                try {
                    Gson gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    JSONArray array=result.getJSONArray("list");
                    list=gson.fromJson(array.toString(),new TypeToken<List<MeiJuBean>>(){}.getType());

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getVlaue().equals("排放标准")) {
                            pflist=list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("车体形式")) {
                           csjglist=list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("颜色")) {
                            csyslist=list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("变速箱类型")) {
                            bsxlist=list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("燃油类型")) {
                            rylelist=list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("客户级别")){
                            levelList = list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("跟进方式")){
                            followWayList = list.get(i).getChildren();
                        } else  if (list.get(i).getVlaue().equals("跟进结果")){
                            followResultList = list.get(i).getChildren();
                        } else if (list.get(i).getVlaue().equals("车规")){
                            rulersList = list.get(i).getChildren();
                        }
                    }

                    Log.e("tgy",pflist.toString());
                    if(from.equals("2")){
                        for (int i=0;i<csyslist.size();i++){
                        beanlist.add(csyslist.get(i));
                        }
                    }else if(from.equals("3")){
                        for (int i=0;i<bsxlist.size();i++){
                            beanlist.add(bsxlist.get(i));
                        }

                    }else if(from.equals("4")){
                        for (int i=0;i<rylelist.size();i++){
                            beanlist.add(rylelist.get(i));
                        }

                    }else if(from.equals("5")){
                        for (int i=0;i<pflist.size();i++){
                            beanlist.add(pflist.get(i));
                        }

                    }else if(from.equals("1")){
                        for (int i=0;i<csjglist.size();i++){
                            beanlist.add(csjglist.get(i));
                        }

                    }else if (from.equals("7")){
                        beanlist.addAll(rulersList);
                    }else if (from.equals("8")){
                        beanlist.addAll(levelList);
                    }else if (from.equals("9")){
                        beanlist.addAll(followWayList);
                    }else if (from.equals("10")){
                        beanlist.addAll(followResultList);
                    }


                    lv_select.setAdapter(new MyAdapter());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

            }
        });

    }

    private void setWindowMatch() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;//全屏幕
        dialogWindow.setAttributes(lp);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
    }


    @OnClick({R.id.iv_back, R.id.ll_zw})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
//                Intent intent=new Intent();
//                intent.putExtra("select","");
//                setResult(RESULTCODE,intent);
                finish();
                break;

            case R.id.ll_zw:

                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
                return beanlist.size();

        }

        @Override
        public Object getItem(int i) {
            return beanlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=View.inflate(SelectAct.this, R.layout.item_select,null);
            TextView tv_info= (TextView) view.findViewById(R.id.tv_info);
            tv_info.setText(beanlist.get(i).getValue());
            return view;
        }
    }

    private class MyDwAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dwlist.size();
        }

        @Override
        public Object getItem(int i) {
            return dwlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=View.inflate(SelectAct.this, R.layout.item_select,null);
            TextView tv_info= (TextView) view.findViewById(R.id.tv_info);
            tv_info.setText(dwlist.get(i));
            return view;
        }
    }
}
