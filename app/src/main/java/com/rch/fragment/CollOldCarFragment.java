package com.rch.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.activity.NewBrandAct;
import com.rch.adatper.CarlibAdapter;
import com.rch.base.BaseFrag;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.GeneralUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.HomeLable;
import com.rch.custom.LoadingView;
import com.rch.custom.SortModel;
import com.rch.entity.CarEntity;
import com.rch.entity.LableEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/10/17.
 */

public class CollOldCarFragment extends BaseFrag{
    @ViewInject(R.id.ll_qb)
    private LinearLayout ll_qb;
    @ViewInject(R.id.tv_qb)
    private TextView tv_qb;
    @ViewInject(R.id.ll_zsz)
    private LinearLayout ll_zsz;
    @ViewInject(R.id.tv_zsz)
    private TextView tv_zsz;
    @ViewInject(R.id.ll_ysq)
    private LinearLayout ll_ysq;
    @ViewInject(R.id.tv_ysq)
    private TextView tv_ysq;
    @ViewInject(R.id.ll_yxj)
    private LinearLayout ll_yxj;
    @ViewInject(R.id.tv_yxj)
    private TextView tv_yxj;
    @ViewInject(R.id.view_zsz)
    private View view_zsz;
    @ViewInject(R.id.view_ysq)
    private View view_ysq;
    @ViewInject(R.id.view_yxj)
    private View view_yxj;
    @ViewInject(R.id.home_title_one_layout)
    private LinearLayout oneLayout;
    @ViewInject(R.id.home_title_two_layout)
    private LinearLayout twoLayout;
    @ViewInject(R.id.home_title_three_layout)
    private LinearLayout threeLayout;
    @ViewInject(R.id.home_title_four_layout)
    private LinearLayout fourLayout;
    @ViewInject(R.id.home_title_one_text)
    private TextView tvOne;
    @ViewInject(R.id.home_title_two_text)
    private TextView tvTwo;
    @ViewInject(R.id.home_title_three_text)
    private TextView tvThree;
    @ViewInject(R.id.home_title_four_text)
    private TextView tvFour;
    @ViewInject(R.id.home_title_one_img)
    private ImageView ivOne;
    @ViewInject(R.id.home_title_two_img)
    private ImageView ivTwo;
    @ViewInject(R.id.home_title_three_img)
    private ImageView ivThree;
    @ViewInject(R.id.home_title_four_img)
    private ImageView ivFour;
    @ViewInject(R.id.home_sort_layout)
    private LinearLayout sortLayout;
    @ViewInject(R.id.home_money_layout)
    private LinearLayout moneyLayout;
    @ViewInject(R.id.home_age_layout)
    private LinearLayout ageLayout;

    @ViewInject(R.id.sort_one)
    private TextView sortOne;
    @ViewInject(R.id.sort_two)
    private TextView sortTwo;
    @ViewInject(R.id.sort_three)
    private TextView sortThree;

    @ViewInject(R.id.money_zero)
    private TextView moneyZero;
    @ViewInject(R.id.money_one)
    private TextView moneyOne;
    @ViewInject(R.id.money_two)
    private TextView moneyTwo;
    @ViewInject(R.id.money_three)
    private TextView moneyThree;
    @ViewInject(R.id.money_four)
    private TextView moneyFour;
    @ViewInject(R.id.money_five)
    private TextView moneyFive;

    @ViewInject(R.id.age_zero)
    private TextView ageZero;
    @ViewInject(R.id.age_one)
    private TextView ageOne;
    @ViewInject(R.id.age_two)
    private TextView ageTwo;
    @ViewInject(R.id.age_three)
    private TextView ageThree;
    @ViewInject(R.id.age_four)
    private TextView ageFour;
    @ViewInject(R.id.age_five)
    private TextView ageFive;
    @ViewInject(R.id.age_six)
    private TextView ageSix;
    @ViewInject(R.id.age_seven)
    private TextView ageSeven;
    @ViewInject(R.id.et_custom_age_one)
    private EditText customAgeOne;
    @ViewInject(R.id.et_custom_age_two)
    private EditText customAgeTwo;
    @ViewInject(R.id.et_custom_money_one)
    private EditText customMoneyOne;
    @ViewInject(R.id.et_custom_money_two)
    private EditText customMoneyTwo;
    @ViewInject(R.id.home_lable_container)
    private HomeLable lable;
    @ViewInject(R.id.home_recyclerView)
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    @ViewInject(R.id.tv_totle)
    private TextView tv_totle;

    private String shelvesStatus;
    private RecyclerView recyclerView;
    int page=1;
    int pageSize=10;
    String priceType="0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
    String minAge="",maxAge="",sotrMode="",minPrice="", maxPrice="",brandId="";
    List<LableEntity> list=new ArrayList<>();//标签list

    CarlibAdapter adapter;
    boolean sotrFilter=false,moneyFilter=false,ageFilter=false;

    List<View> selMoneyColorList=new ArrayList<>();
    List<View> selOrderColorList =new ArrayList<>();
    List<View> selAgeColorList=new ArrayList<>();


    private String totalSize;
    List<CarEntity> tempList;
    List<CarEntity> shopList=new ArrayList<>();
    private Gson gson;
    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_collection,null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        gson=new Gson();
        fourLayout.setVisibility(View.VISIBLE);//设置可见到
        selOrderColorList.add(sortOne);
        selMoneyColorList.add(moneyZero);
        selAgeColorList.add(ageZero);
        //以上三个控制颜色

        recyclerView=pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                onRefresh();
            }
        });

        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page=1;
                requestShopListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                requestShopListData();
            }
        });

        lable.setOnTtemListener(new HomeLable.OnTtemListener() {
            @Override
            public void onDel(List<LableEntity> delList) {
                list=delList;
                List<String>lablelist=new ArrayList<>();
                lablelist.clear();
//                lable.initData(list,width);
                lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                if(list.size()>0)
                {
                    for(int i=0;i<list.size();i++){
                        lablelist.add(list.get(i).getType());
                    }

                    if(!lablelist.contains("4")){//没有年
                        maxAge="";
                        minAge="";

                        if(selAgeColorList.size()>0) {
                            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                        }
                        selAgeColorList.clear();
                        ageZero.setTextColor(getResources().getColor(R.color.orange_2));
                        ageZero.setBackgroundResource(R.drawable.select_price);
                        selAgeColorList.add(ageZero);
                    }
                    if(!lablelist.contains("3")){//没有金额
                        minPrice="";
                        maxPrice="";

                        if(selMoneyColorList.size()>0) {
                            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                        }
                        selMoneyColorList.clear();
                        moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
                        moneyZero.setBackgroundResource(R.drawable.select_price);
                        selMoneyColorList.add(moneyZero);
                    }
                    if(!lablelist.contains("1")){//没有金额
                        brandId="";
                    }
                } else {
                    minPrice="";
                    maxPrice="";
                    brandId="";
                    minAge="";
                    maxAge="";
                }

                onRefresh();
            }

            @Override
            public void onReset() {
                list.clear();
//                lable.initData(list,width);
                lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                minPrice="";
                maxPrice="";
                brandId="";
                minAge="";
                maxAge="";
                onRefresh();

                //当重置后把价格筛选框里面不限选中
                //当重置后把价格筛选框里面不限选中
                if(selMoneyColorList.size()>0) {
                    ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                    ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                }
                selMoneyColorList.clear();
                moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
                moneyZero.setBackgroundResource(R.drawable.select_price);
                selMoneyColorList.add(moneyZero);

                if(selAgeColorList.size()>0) {
                    ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                    ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                }
                selAgeColorList.clear();
                ageZero.setTextColor(getResources().getColor(R.color.orange_2));
                ageZero.setBackgroundResource(R.drawable.select_price);
                selAgeColorList.add(ageZero);
            }

        });
        selectCol(0);//默认在售中
        onRefresh();
        InputFilter[] filters = {new EditInputFilter(99999,2)};
        //自定义价格
        if (customMoneyOne!=null) {
            customMoneyOne.setFilters(filters);
        }
        if (customMoneyTwo!=null) {
            customMoneyTwo.setFilters(filters);
        }
    }

    private void onRefresh()
    {
        page=1;
        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);//这里是打开筛选条件时候不让点击item,关闭后可以点击
        requestShopListData();
        load_view.loading();
    }

    private void requestShopListData() {
        RequestParam param = new RequestParam();
        if(!TextUtils.isEmpty(shelvesStatus)){
            param.add("shelvesStatus",shelvesStatus);
        }
        if(!TextUtils.isEmpty(sotrMode)){//排序
            param.add("orderType",sotrMode);
        }
        if(!TextUtils.isEmpty(minPrice)){
            param.add("salesMinPrice",minPrice);
        }
        if(!TextUtils.isEmpty(maxPrice)){
            param.add("salesMaxPrice",maxPrice);
        }
        if(!TextUtils.isEmpty(brandId)){
            param.add("brandId",brandId);
        }
        if(!TextUtils.isEmpty(minAge)){
            param.add("vehicleMinYear",minAge);
        }
        if(!TextUtils.isEmpty(maxAge)){
            param.add("vehicleMaxYear",maxAge);
        }
        param.add("ifNew","0");
        param.add("currentPage",page+"");
        param.add("pageSize",pageSize+"");
        OkHttpUtils.post(Config.COLLECTVEHICLE_URL, param, new OkHttpCallBack(getActivity(),"加载中...") {
            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                pullToRefreshRecyclerView.onRefreshComplete();
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    if(!result.isNull("totalSize")){
                        totalSize=result.getString("totalSize");
                    }
                    JSONArray array=result.getJSONArray("list");
                    tempList=gson.fromJson(array.toString(),new TypeToken<List<CarEntity>>(){}.getType());
                    if ( tempList!= null && tempList.size() > 0)
                    {
                        if(page==1){
                            shopList.clear();
                        }
                        shopList.addAll(tempList);
                        if(adapter==null){
                            adapter=new CarlibAdapter(getActivity(),shopList,1);
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.updateShopListData(shopList);
                        }
//                    adapter.updateShopListData(shopList,priceType,"");
                        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                    }else {
                        if(page==1){
                            load_view.noContent();
                            load_view.setNoContentIco(R.mipmap.no_car_data);
                            load_view.setNoContentTxt("您还没有任何车辆噢~");
                            pullToRefreshRecyclerView.setVisibility(View.GONE);
                        }else {
                            pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    if(page==1){//只需要下拉时候才显示
                        if(tv_totle.getVisibility()==View.GONE) {
                            tv_totle.setVisibility(View.VISIBLE);
                        }
                        i=3;
                        tv_totle.setText("共为您找到" + totalSize + "辆车");
                        timer = new Timer();
                        myTask = new MyTimerTask();
                        timer.schedule(myTask, 0, 1000);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                pullToRefreshRecyclerView.onRefreshComplete();
                load_view.loadError();
            }
        });
    }

    private int i = 3;
    private Timer timer;
    private MyTimerTask myTask;
    /**
     * 倒计时
     *
     * @author wangbin
     *
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(i--);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                tv_btyzm.setEnabled(true);
//                tv_btyzm.setText("重新发送");
                tv_totle.setVisibility(View.GONE);
                timer.cancel();
                myTask.cancel();
            }
        }

    };

    @OnClick({R.id.ll_qb,R.id.ll_zsz,R.id.ll_ysq,R.id.ll_yxj,R.id.home_title_one_layout,R.id.home_title_two_layout,R.id.home_title_three_layout,R.id.home_title_four_layout
            ,R.id.sort_one,R.id.sort_two,R.id.sort_three,R.id.money_zero,R.id.money_one,R.id.money_two,R.id.money_three,R.id.money_four
            ,R.id.money_five,R.id.age_zero,R.id.age_one,R.id.age_two,R.id.age_three,R.id.age_four,R.id.age_five,R.id.age_six,R.id.age_seven
            ,R.id.custom_age_filter,R.id.custom_money_filter})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.ll_qb:
                selectCol(4);
                break;
            case R.id.ll_zsz:
                selectCol(0);
                break;
            case R.id.ll_ysq:
                selectCol(1);
                break;

            case R.id.ll_yxj:
                selectCol(2);
                break;
            case R.id.home_title_one_layout:
                sortPanel(0);
                break;
            case R.id.home_title_two_layout:
                sortPanel(1);
                break;
            case R.id.home_title_three_layout:
                sortPanel(2);
                break;
            case R.id.home_title_four_layout:
                sortPanel(3);
                break;
            case R.id.sort_one:
            case R.id.sort_two:
            case R.id.sort_three:
                setSortMode(v);
                break;
            case R.id.money_zero:
            case R.id.money_one:
            case R.id.money_two:
            case R.id.money_three:
            case R.id.money_four:
            case R.id.money_five:
                setMoneyData(v);
                break;

            case R.id.age_zero:
            case R.id.age_one:
            case R.id.age_two:
            case R.id.age_three:
            case R.id.age_four:
            case R.id.age_five:
            case R.id.age_six:
            case R.id.age_seven:
                setAgeData(v);
                break;

            case R.id.custom_money_filter:
                customFilter();
                break;


            case R.id.custom_age_filter:
                customAgeFilter();
                break;
        }
    }

    private void selectCol(int i) {
        //        view_zsz.setBackgroundColor(getResources().getColor(R.color.white));
//        view_ysq.setBackgroundColor(getResources().getColor(R.color.white));
//        view_yxj.setBackgroundColor(getResources().getColor(R.color.white));
        ll_qb.setBackground(getResources().getDrawable(R.drawable.check));
        tv_qb.setTextColor(getResources().getColor(R.color.btn_org1));
        ll_zsz.setBackground(getResources().getDrawable(R.drawable.check));
        tv_zsz.setTextColor(getResources().getColor(R.color.btn_org1));
        ll_ysq.setBackground(getResources().getDrawable(R.drawable.check));
        tv_ysq.setTextColor(getResources().getColor(R.color.btn_org1));
        ll_yxj.setBackground(getResources().getDrawable(R.drawable.check));
        tv_yxj.setTextColor(getResources().getColor(R.color.btn_org1));

        if(i==0){
//            view_zsz.setBackgroundColor(getResources().getColor(R.color.orange_2));
            ll_zsz.setBackground(getResources().getDrawable(R.drawable.coll_orent_radio));
            tv_zsz.setTextColor(getResources().getColor(R.color.white));
            shelvesStatus="1";
        }else if(i==1){
//            view_ysq.setBackgroundColor(getResources().getColor(R.color.orange_2));
            shelvesStatus="3";
            ll_ysq.setBackground(getResources().getDrawable(R.drawable.coll_orent_radio));
            tv_ysq.setTextColor(getResources().getColor(R.color.white));
        }else if(i==2){
            shelvesStatus="2";
//            view_yxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
            ll_yxj.setBackground(getResources().getDrawable(R.drawable.coll_orent_radio));
            tv_yxj.setTextColor(getResources().getColor(R.color.white));
        }else if(i==4){
            shelvesStatus="";
//            view_yxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
            ll_qb.setBackground(getResources().getDrawable(R.drawable.coll_orent_radio));
            tv_qb.setTextColor(getResources().getColor(R.color.white));
        }
        onRefresh();
    }

    private void customFilter() {
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }

        String min=customMoneyOne.getText().toString().trim();
        String max=customMoneyTwo.getText().toString().trim();
        if(min.isEmpty()||max.isEmpty()) {
            ToastTool.show(getActivity(), "自定义价格不能为空！");
        }
        else if(min.equals("0")&&max.equals("0")) {
            ToastTool.show(getActivity(), "自定义价格不能全为0！");
        }
        else if(max.equals("0")) {
            ToastTool.show(getActivity(), "自定义价格不能为0！");
        }
        else if(Double.valueOf(min)>= Double.valueOf(max)) {
            ToastTool.show(getActivity(), "自定义第二个价格不能小于等于第一个价格！");
        } else {
            minPrice = min;
            maxPrice = max;
            String text = min + "-" + max + "万";
            addLableEntity("", text, "3");
            moneyFilter=false;
            moneyLayout.setVisibility(View.GONE);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);

            onRefresh();
        }
    }

    private void customAgeFilter() {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }

        minAge=customAgeOne.getText().toString().trim();
        maxAge=customAgeTwo.getText().toString().trim();
        if (TextUtils.isEmpty(minAge) || TextUtils.isEmpty(maxAge)){
            ToastTool.show(getActivity(),"自定义车龄不能为空！");
            return;
        }else {
            int iMinAge=Integer.parseInt(minAge);
            int iMaxAge=Integer.parseInt(maxAge);
            if(iMinAge>=iMaxAge) {
                ToastTool.show(getActivity(), "自定义第二个车龄不能小于等于第一个车龄的值！");
                return;
            }
        }

        String text=minAge+"-"+maxAge+"年";
        addLableEntity("",text,"4");
        ageFilter=false;
        ageLayout.setVisibility(View.GONE);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        onRefresh();
    }

    private void setAgeData(View v) {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        selAgeColorList.clear();
        selAgeColorList.add(v);
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundResource(R.drawable.select_price);
        String text=textView.getText().toString().trim();
        String tag=String.valueOf(textView.getTag());
        if(tag.equals("0")){
            maxAge = "";
            minAge = "";
        }else if(tag.equals("1-in")){
            maxAge = "1";
            minAge = "";
        }else if(tag.equals("2-in")){
            maxAge = "2";
            minAge = "";
        }else if(tag.equals("3-in")){
            maxAge = "3";
            minAge = "";
        }else if(tag.equals("4-in")){
            maxAge = "4";
            minAge = "";
        }else if(tag.equals("5-in")){
            maxAge = "5";
            minAge = "";
        }else if(tag.equals("6-in")){
            maxAge = "6";
            minAge = "";
        }else if(tag.equals("6-on")){
            maxAge = "";
            minAge = "6";
        }

        ageFilter=false;
        ageLayout.setVisibility(View.GONE);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        if(!text.equals("不限")){
            addLableEntity("", text, "4");
        }else {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("4")) {
                        list.remove(i);
                        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                    }
                }
            }
        }

        onRefresh();

    }


    private void setMoneyData(View v) {
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        selMoneyColorList.clear();
        selMoneyColorList.add(v);
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundResource(R.drawable.select_price);
        String text=textView.getText().toString().trim();
        String moneyMode=String.valueOf(textView.getTag());
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if(moneyMode.equals("0")){
            minPrice="";
            maxPrice="";
        }else if(moneyMode.equals("1")){
            minPrice="0";
            maxPrice="30";
        }else if(moneyMode.equals("2")){
            minPrice="30";
            maxPrice="50";
        }else if(moneyMode.equals("3")){
            minPrice="50";
            maxPrice="70";
        }else if(moneyMode.equals("4")){
            minPrice="70";
            maxPrice="100";
        }else if(moneyMode.equals("5")){
            minPrice="100";
            maxPrice="";
        }
        moneyFilter=false;
        moneyLayout.setVisibility(View.GONE);
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if(!text.equals("不限")){
            addLableEntity("", text, "3");
        }else {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("3")) {
                        list.remove(i);
                        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                    }
                }
            }
        }

        onRefresh();
    }

    private void setSortMode(View v) {
        if(selOrderColorList.size()>0) {
            ((TextView) selOrderColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
            ((View)selOrderColorList.get(0)).setBackgroundColor(getResources().getColor(R.color.white));
            ((TextView) selOrderColorList.get(0)).setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

        }
        selOrderColorList.clear();
        selOrderColorList.add(v);

        TextView textView= (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundColor(getResources().getColor(R.color.gray_2));
        Drawable drawableright = getResources().getDrawable(
                R.mipmap.city_checked);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableright,null);
        textView.setPadding(GeneralUtils.dip2px(getActivity(),15),0,GeneralUtils.dip2px(getActivity(),15),0);

        sotrMode=String.valueOf(textView.getTag());
        tvOne.setText(textView.getText().toString().trim());
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        sortLayout.setVisibility(View.GONE);
        sotrFilter=false;

        onRefresh();
    }

    int BRANDREQUESTCODE = 10101;//品牌
    private void sortPanel(int i) {
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        sortLayout.setVisibility(View.GONE);
        moneyLayout.setVisibility(View.GONE);
        ageLayout.setVisibility(View.GONE);
        switch (i){
            case 0:
                moneyFilter=false;
                ageFilter=false;
                if(!sotrFilter) {
                    sotrFilter=true;
                    tvOne.setTextColor(getResources().getColor(R.color.orange_2));
                    ivOne.setImageResource(R.mipmap.sel_subscript);
                    sortLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    sotrFilter=false;
                    tvOne.setTextColor(getResources().getColor(R.color.black_1));
                    ivOne.setImageResource(R.mipmap.back_xb);
                    sortLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;
            case 1:
                sotrFilter=false;
                moneyFilter=false;
                ageFilter=false;
                startActivityForResult(new Intent(getActivity(), NewBrandAct.class).putExtra("type", 1), BRANDREQUESTCODE);
                break;
            case 2:
                sotrFilter=false;
                ageFilter=false;
                if(!moneyFilter) {
                    moneyFilter=true;
                    tvThree.setTextColor(getResources().getColor(R.color.orange_2));
                    ivThree.setImageResource(R.mipmap.sel_subscript);
                    moneyLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    moneyFilter=false;
                    tvThree.setTextColor(getResources().getColor(R.color.black_1));
                    ivThree.setImageResource(R.mipmap.back_xb);
                    moneyLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;

            case 3:
                sotrFilter=false;
                moneyFilter=false;
                if(!ageFilter) {
                    ageFilter=true;
                    tvFour.setTextColor(getResources().getColor(R.color.orange_2));
                    ivFour.setImageResource(R.mipmap.sel_subscript);
                    ageLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    ageFilter=false;
                    tvFour.setTextColor(getResources().getColor(R.color.black_1));
                    ivFour.setImageResource(R.mipmap.back_xb);
                    ageLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;
        }

    }

    /**
     * 设置下拉刷新是否可以点击item因为弹出的选择条件布局挡不住
     */
    private void setPullEnable() {
        if(sortLayout.getVisibility()==View.GONE&&moneyLayout.getVisibility()==View.GONE&&ageLayout.getVisibility()==View.GONE){
            pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
        }else {
            pullToRefreshRecyclerView.setVisibility(View.GONE);

        }
    }


    //type- 1:品牌，3-价格，4--车龄
    private void addLableEntity(String id,String text,String type)
    {
        LableEntity lableEntity=new LableEntity();
        lableEntity.setId(id);
        lableEntity.setLable(text);
        lableEntity.setType(type);
        if(list.size()>0) {
            for (int i=0;i<list.size();i++)
            {
                LableEntity entity=list.get(i);
                if(entity.getType().equals(type))
                    list.remove(i);
            }

        }
        list.add(lableEntity);
        lable.removeAllViews();
        lable.initData(list,SpUtils.getScreenWidth(getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == BRANDREQUESTCODE) {

                SortModel brand = (SortModel) data.getSerializableExtra("sortmodel");
                brandId = brand.getBrandId();
                if (!brand.getName().equals("不限品牌"))
                    addLableEntity(brand.getBrandId(), brand.getName(), "1");
                else {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LableEntity lableEntity = list.get(i);
                            if (lableEntity.getType().equals("1")) {
                                list.remove(i);
                                lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                            }
                        }
                    }
                }
                onRefresh();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

}

