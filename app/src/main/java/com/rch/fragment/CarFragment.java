package com.rch.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.activity.CityLocationActivity;
import com.rch.activity.CustomerAct;
import com.rch.activity.NewBrandAct;
import com.rch.adatper.CarlibAdapter;
import com.rch.base.BaseFragment;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.EncryptionTools;
import com.rch.common.GPSTool;
import com.rch.common.GeneralUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.HomeLable;

import com.rch.custom.LoadingView;
import com.rch.custom.SortModel;
import com.rch.entity.CarEntity;
import com.rch.entity.CityInfoEntity;
import com.rch.entity.ComparatorImpl;
import com.rch.entity.LableEntity;
import com.rch.entity.SearchEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by Administrator on 2018/4/13.
 */
//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CarFragment extends BaseFragment implements View.OnClickListener, AMapLocationListener {
    boolean isVisible;
    View view;

    HomeLable lable;

    LinearLayout oneLayout, twoLayout, threeLayout, fourLayout, sortLayout, moneyLayout, areaLayout, ageLayout;
    TextView tvOne, tvTwo, tvThree, tvFour,
            sortOne, sortTwo, sortThree,
            moneyZero, moneyOne, moneyTwo, moneyThree, moneyFour, moneyFive,
            ageZero, ageOne, ageTwo, ageThree, ageFour, ageFive, ageSix, ageSeven,
            customAgeFilter, customMoneyFilter, tvLocation;
    ImageView ivOne, ivTwo, ivThree, ivFour, ivAreaIcon;
    EditText customMoneyOne, customMoneyTwo, customAgeOne, customAgeTwo;

    private TextView tv_totle;


    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    RecyclerView recyclerView;

    List<LableEntity> list = new ArrayList<>();
    List<CarEntity> shopList = new ArrayList<>();
    List<CarEntity> tempList;
    List<View> selMoneyColorList = new ArrayList<>();
    List<View> selOrderColorList = new ArrayList<>();
    List<View> selAgeColorList = new ArrayList<>();

    //    CarListAdapter adapter;
    CarlibAdapter adapter;
    private LoadingView load_view;
    private ImageView iv_customer;

    boolean sotrFilter, moneyFilter, ageFilter;
    String sotrMode = "1", minPrice = "", maxPrice = "", ageMode = "", brandId = "";

    int page = 1;
    int pageSize = 10;

    int BRANDREQUESTCODE = 10101;//品牌
    int CITYREQUESTCODE = 10102;//城市
    final int REQUESTGPSCODE = 10103;//GPS

    String gpsCity = "";
    String priceType = "0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
    private String totalSize;
    private Gson gson;
    private String isRecommend="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.car_fragment, container, false);
//        init();
        initControl();
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {
        gson=new Gson();
        setData();
        addTextWatcher();
    }
//    private void init() {
//        if(isVisible) {
//            if(view==null)
//                return;
//            initControl();
//            setData();
//        }
//
//       addTextWatcher();
//    }

    private void addTextWatcher() {

        InputFilter[] filters = {new EditInputFilter(99999, 2)};
        //自定义价格
        if (customMoneyOne != null) {
            customMoneyOne.setFilters(filters);
        }
        if (customMoneyTwo != null) {
            customMoneyTwo.setFilters(filters);
        }
    }

    private void initControl() {

        load_view = (LoadingView) view.findViewById(R.id.load_view);
        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                onRefresh();
            }
        });

        tv_totle= (TextView) view.findViewById(R.id.tv_totle);
        lable = (HomeLable) view.findViewById(R.id.home_lable_container);
        oneLayout = (LinearLayout) view.findViewById(R.id.home_title_one_layout);
        twoLayout = (LinearLayout) view.findViewById(R.id.home_title_two_layout);
        threeLayout = (LinearLayout) view.findViewById(R.id.home_title_three_layout);
        fourLayout = (LinearLayout) view.findViewById(R.id.home_title_four_layout);
        fourLayout.setVisibility(View.VISIBLE);//设置可见到
        sortLayout = (LinearLayout) view.findViewById(R.id.home_sort_layout);
        moneyLayout = (LinearLayout) view.findViewById(R.id.home_money_layout);
        ageLayout = (LinearLayout) view.findViewById(R.id.home_age_layout);

        areaLayout = (LinearLayout) view.findViewById(R.id.home_area_layout);

        tvOne = (TextView) view.findViewById(R.id.home_title_one_text);
        tvTwo = (TextView) view.findViewById(R.id.home_title_two_text);
        tvThree = (TextView) view.findViewById(R.id.home_title_three_text);
        tvFour = (TextView) view.findViewById(R.id.home_title_four_text);
        sortOne = (TextView) view.findViewById(R.id.sort_one);
        sortTwo = (TextView) view.findViewById(R.id.sort_two);
        sortThree = (TextView) view.findViewById(R.id.sort_three);

        moneyZero = (TextView) view.findViewById(R.id.money_zero);
        moneyOne = (TextView) view.findViewById(R.id.money_one);
        moneyTwo = (TextView) view.findViewById(R.id.money_two);
        moneyThree = (TextView) view.findViewById(R.id.money_three);
        moneyFour = (TextView) view.findViewById(R.id.money_four);
        moneyFive = (TextView) view.findViewById(R.id.money_five);

        ageZero = (TextView) view.findViewById(R.id.age_zero);
        ageOne = (TextView) view.findViewById(R.id.age_one);
        ageTwo = (TextView) view.findViewById(R.id.age_two);
        ageThree = (TextView) view.findViewById(R.id.age_three);
        ageFour = (TextView) view.findViewById(R.id.age_four);
        ageFive = (TextView) view.findViewById(R.id.age_five);
        ageSix = (TextView) view.findViewById(R.id.age_six);
        ageSeven = (TextView) view.findViewById(R.id.age_seven);

        customAgeFilter = (TextView) view.findViewById(R.id.custom_age_filter);
        customMoneyFilter = (TextView) view.findViewById(R.id.custom_money_filter);
        tvLocation = (TextView) view.findViewById(R.id.car_fragment_location);

        customAgeOne = (EditText) view.findViewById(R.id.et_custom_age_one);
        customAgeTwo = (EditText) view.findViewById(R.id.et_custom_age_two);
        customMoneyOne = (EditText) view.findViewById(R.id.et_custom_money_one);
        customMoneyTwo = (EditText) view.findViewById(R.id.et_custom_money_two);

        selOrderColorList.add(sortOne);
        selMoneyColorList.add(moneyZero);
        selAgeColorList.add(ageZero);

        ivOne = (ImageView) view.findViewById(R.id.home_title_one_img);
        ivTwo = (ImageView) view.findViewById(R.id.home_title_two_img);
        ivThree = (ImageView) view.findViewById(R.id.home_title_three_img);
        ivFour = (ImageView) view.findViewById(R.id.home_title_four_img);
        ivAreaIcon = (ImageView) view.findViewById(R.id.home_area_icon);


        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.home_recyclerView);
        recyclerView = pullToRefreshRecyclerView.getRefreshableView();

        iv_customer = (ImageView) view.findViewById(R.id.iv_customer);

        gpsCity = SpUtils.getCityid(getActivity());
        tvLocation.setText(gpsCity);
//        requestPermissions();

        oneLayout.setOnClickListener(this);
        twoLayout.setOnClickListener(this);
        threeLayout.setOnClickListener(this);
        fourLayout.setOnClickListener(this);
        sortOne.setOnClickListener(this);
        sortTwo.setOnClickListener(this);
        sortThree.setOnClickListener(this);

        moneyZero.setOnClickListener(this);
        moneyOne.setOnClickListener(this);
        moneyTwo.setOnClickListener(this);
        moneyThree.setOnClickListener(this);
        moneyFour.setOnClickListener(this);
        moneyFive.setOnClickListener(this);

        ageZero.setOnClickListener(this);
        ageOne.setOnClickListener(this);
        ageTwo.setOnClickListener(this);
        ageThree.setOnClickListener(this);
        ageFour.setOnClickListener(this);
        ageFive.setOnClickListener(this);
        ageSix.setOnClickListener(this);
        ageSeven.setOnClickListener(this);


        customAgeFilter.setOnClickListener(this);
        customMoneyFilter.setOnClickListener(this);
        areaLayout.setOnClickListener(this);
        iv_customer.setOnClickListener(this);


        recyclerView.addOnScrollListener(new MyRecyclerViewScrollListener());

    }

    private void setData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        onRefresh();
//        adapter=new CarListAdapter(getActivity(),shopList);
//        recyclerView.setAdapter(adapter);


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
                list = delList;
                List<String> lablelist = new ArrayList<>();
                lablelist.clear();
                lable.initData(list, width);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        lablelist.add(list.get(i).getType());
                    }

                    if (!lablelist.contains("4")) {//没有年
                        //清空自定义数据
                        customAgeOne.setText("");
                        customAgeTwo.setText("");
                        maxAge = "";
                        minAge = "";
//                        ageZero.setTextColor(getResources().getColor(R.color.orange_2));//清空时候要把价格筛选选中不限

                        if(selAgeColorList.size()>0) {
                            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                        }
                        selAgeColorList.clear();
                        ageZero.setTextColor(getResources().getColor(R.color.orange_2));
                        ageZero.setBackgroundResource(R.drawable.select_price);
                        selAgeColorList.add(ageZero);
                    }
                    if (!lablelist.contains("3")) {//没有金额
                        //清空自定义数据
                        customMoneyOne.setText("");
                        customMoneyTwo.setText("");
                        minPrice = "";
                        maxPrice = "";
//                        moneyZero.setTextColor(getResources().getColor(R.color.orange_2));//清空时候要把价格筛选选中不限

                        if(selMoneyColorList.size()>0) {
                            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                        }
                        selMoneyColorList.clear();
                        moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
                        moneyZero.setBackgroundResource(R.drawable.select_price);
                        selMoneyColorList.add(moneyZero);

                    }
                    if(!lablelist.contains("1")&&lablelist.contains("5")){//没有品牌,有标签
                        brandId=onlycxid;
                    }else if(lablelist.contains("1")&&!lablelist.contains("5")){//有品牌,没标签
                        brandId=onlybrandid;
                    }else if(!lablelist.contains("1")&&!lablelist.contains("5")){//没有品牌,没有标签
                        brandId="";
                    }

                } else {
                    minPrice = "";
                    maxPrice = "";
                    brandId = "";
                    minAge = "";
                    maxAge = "";
                }

                onRefresh();
                load_view.loading();
            }

            @Override
            public void onReset() {
                list.clear();
                lable.initData(list, width);
                minPrice = "";
                maxPrice = "";
                brandId = "";
                minAge = "";
                maxAge = "";
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
    }


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCoarseLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasFineLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED || hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTGPSCODE);
                return;
            }
        }
        GPSTool.initGPS(getActivity(), this);
    }

    @Override
    protected void onVisible() {
        isVisible = true;
//        init();
    }

    @Override
    protected void onInvisible() {
        isVisible = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_one_layout:
                sortPanel();
                break;
            case R.id.home_title_two_layout:
                startActivityForResult(new Intent(getActivity(), NewBrandAct.class).putExtra("type", 1), BRANDREQUESTCODE);

                sotrFilter = false;
                moneyFilter = false;
                ageFilter = false;
                tvOne.setTextColor(getResources().getColor(R.color.black_1));
                ivOne.setImageResource(R.mipmap.back_xb);
                tvThree.setTextColor(getResources().getColor(R.color.black_1));
                ivThree.setImageResource(R.mipmap.back_xb);
                tvFour.setTextColor(getResources().getColor(R.color.black_1));
                ivFour.setImageResource(R.mipmap.back_xb);

//                if (sortLayout.getVisibility() != View.GONE)
//                    showFilterPanel(sotrFilter, oneLayout, sortLayout);
//                else if (moneyLayout.getVisibility() != View.GONE)
//                    showFilterPanel(moneyFilter, threeLayout, moneyLayout);
//                else
//                    showFilterPanel(ageFilter, fourLayout, ageLayout);
                sortLayout.setVisibility(View.GONE);
                moneyLayout.setVisibility(View.GONE);
                ageLayout.setVisibility(View.GONE);

                break;
            case R.id.home_title_three_layout:
               /* tvOne.setTextColor(getResources().getColor(R.color.black_1));
                ivOne.setImageResource(R.mipmap.back_xb);
                tvThree.setTextColor(getResources().getColor(R.color.orange_2));
                ivThree.setImageResource(R.mipmap.sel_subscript);*/
                moneyPanel();
                break;
            case R.id.home_title_four_layout:
                agePanel();
                break;
            case R.id.sort_one:
            case R.id.sort_two:
            case R.id.sort_three:
                isRecommend="";
                setSortMode(v);
                break;
            case R.id.money_zero:
            case R.id.money_one:
            case R.id.money_two:
            case R.id.money_three:
            case R.id.money_four:
            case R.id.money_five:
                isRecommend="";
                setMoneyData(v);
                //清空自定义数据
                customMoneyOne.setText("");
                customMoneyTwo.setText("");
                break;

            case R.id.age_zero:
            case R.id.age_one:
            case R.id.age_two:
            case R.id.age_three:
            case R.id.age_four:
            case R.id.age_five:
            case R.id.age_six:
            case R.id.age_seven:
                isRecommend="";
                setAgeData(v);
                //清空自定义数据
                customAgeOne.setText("");
                customAgeTwo.setText("");
                break;

            case R.id.custom_age_filter:
                isRecommend="";
                customAgeFilter();
                break;

            case R.id.custom_money_filter:
                isRecommend="";
                customFilter();
                break;
            case R.id.home_area_layout:
                startActivityForResult(new Intent(getActivity(), CityLocationActivity.class).putExtra("request", CITYREQUESTCODE), CITYREQUESTCODE);
                break;
            case R.id.iv_customer:
                startActivity(new Intent(getActivity(), CustomerAct.class));
                break;


        }
    }


    private void onRefresh() {
        page=1;
        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);//这里是打开筛选条件时候不让点击item,关闭后可以点击
        requestShopListData();
        load_view.loading();
    }

    public void requestShopListData() {
        RequestParam param = new RequestParam();
        param.add("currentPage",String.valueOf(page));
        param.add("pageSize",String.valueOf(pageSize));
        if(!TextUtils.isEmpty(gpsCity)) {
            param.add("city", gpsCity);
        }
        param.add("brandId", brandId);
        param.add("salesMinPrice", minPrice);
        param.add("salesMaxPrice", maxPrice);
        param.add("vehicleMinYear", minAge);
        param.add("vehicleMaxYear", maxAge);
        param.add("orderType", sotrMode);
        param.add("ifNew","0");
        if(!TextUtils.isEmpty(isRecommend)) {
            param.add("isRecommend", isRecommend);
        }
        OkHttpUtils.post(Config.CARLIST, param, new OkHttpCallBack(getActivity(), "加载中...") {

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
                            load_view.setNoContentTxt("暂无数据");
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
//                iv_customer.setAnimation(AnimationUtils.makeInAnimation(getActivity(),false));
//                iv_customer.setVisibility(View.VISIBLE);
                load_view.loadError();
                pullToRefreshRecyclerView.onRefreshComplete();
                pullToRefreshRecyclerView.setVisibility(View.GONE);


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


    /*选择排序模式*/
    private void setSortMode(View v) {

        if (selOrderColorList.size() > 0) {
            ((TextView) selOrderColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((View)selOrderColorList.get(0)).setBackgroundColor(getResources().getColor(R.color.white));
            ((TextView) selOrderColorList.get(0)).setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
        selOrderColorList.clear();
        selOrderColorList.add(v);
        sortPanel();
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundColor(getResources().getColor(R.color.gray_2));
        Drawable drawableright = getResources().getDrawable(
                R.mipmap.city_checked);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableright,null);
        textView.setPadding(GeneralUtils.dip2px(getActivity(),15),0,GeneralUtils.dip2px(getActivity(),15),0);

        sotrMode = String.valueOf(textView.getTag());
        tvOne.setText(textView.getText().toString().trim());
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        isRecommend="";
        onRefresh();
        load_view.loading();

    }

    /*排序筛选面板*/
    private void sortPanel() {
        sotrFilter = !sotrFilter;
        if (sotrFilter) {
            tvOne.setTextColor(getResources().getColor(R.color.orange_2));
            ivOne.setImageResource(R.mipmap.sel_subscript);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            tvFour.setTextColor(getResources().getColor(R.color.black_1));
            ivFour.setImageResource(R.mipmap.back_xb);
        } else {
            tvOne.setTextColor(getResources().getColor(R.color.black_1));
            ivOne.setImageResource(R.mipmap.back_xb);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            tvFour.setTextColor(getResources().getColor(R.color.black_1));
            ivFour.setImageResource(R.mipmap.back_xb);
        }
        showFilterPanel(sotrFilter, oneLayout, sortLayout);
        if (moneyFilter) {
            moneyFilter = !moneyFilter;
            showFilterPanel(moneyFilter, threeLayout, moneyLayout);
        } else if (ageFilter) {
            ageFilter = !ageFilter;
            showFilterPanel(ageFilter, fourLayout, ageLayout);
        }
    }

    /*车龄筛选面板*/
    private void agePanel() {
        ageFilter = !ageFilter;
        if (ageFilter) {
            tvOne.setTextColor(getResources().getColor(R.color.black_1));
            ivOne.setImageResource(R.mipmap.back_xb);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            tvFour.setTextColor(getResources().getColor(R.color.orange_2));
            ivFour.setImageResource(R.mipmap.sel_subscript);

        } else {
            tvOne.setTextColor(getResources().getColor(R.color.black_1));
            ivOne.setImageResource(R.mipmap.back_xb);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            tvFour.setTextColor(getResources().getColor(R.color.black_1));
            ivFour.setImageResource(R.mipmap.back_xb);
        }
        showFilterPanel(ageFilter, fourLayout, ageLayout);
        if (sotrFilter) {
            sotrFilter = !sotrFilter;
            showFilterPanel(sotrFilter, oneLayout, sortLayout);
        } else if (moneyFilter) {
            moneyFilter = !moneyFilter;
            showFilterPanel(moneyFilter, threeLayout, moneyLayout);
        }
    }

    /*选择标识--车龄*/
    private void setAgeData(View v) {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        selAgeColorList.clear();
        selAgeColorList.add(v);
        agePanel();
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundResource(R.drawable.select_price);
        String text = textView.getText().toString().trim();
        String tag = String.valueOf(textView.getTag());
        if (tag.indexOf("-") > 0) {
            String values = String.valueOf(textView.getTag()).split("-")[0];
            String carType = String.valueOf(textView.getTag()).split("-")[1];
            if (carType.equals("on")) {
                maxAge = "";
                minAge = values;
            } else {
                maxAge = values;
                minAge = "";
            }
        } else {//不限
            maxAge = "";
            minAge = "";
        }
        // ageMode=;
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        if (!text.equals("不限")) {
            addLableEntity("", text, "4");
        } else {//点击不限就把它去掉
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("4")) {
                        list.remove(i);
                        lable.initData(list, width);
                    }
                }
            }
        }
        onRefresh();
        load_view.loading();

    }

    String minAge = "", maxAge = "";

    /*自定义车龄*/
    private void customAgeFilter() {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selAgeColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        minAge = customAgeOne.getText().toString().trim();
        maxAge = customAgeTwo.getText().toString().trim();
        if (TextUtils.isEmpty(minAge) || TextUtils.isEmpty(maxAge)) {
            ToastTool.show(getActivity(), "自定义车龄不能为空！");
            return;
        }
        int iMinAge = Integer.parseInt(minAge);
        int iMaxAge = Integer.parseInt(maxAge);
        if (minAge.isEmpty() || maxAge.isEmpty())
            ToastTool.show(getActivity(), "自定义车龄不能为空！");
        else if (iMinAge >= iMaxAge)
            ToastTool.show(getActivity(), "自定义第二个车龄不能小于等于第一个车龄的值！");
        else {
            String text = minAge + "-" + maxAge + "年";
            addLableEntity("", text, "4");
            agePanel();
            onRefresh();
            load_view.loading();
        }
    }

    /*分割金额了，取最小，与最大*/
    private void getAmount(String moneyStr) {
        if (moneyStr.isEmpty()) {
            minPrice = "";
            maxPrice = "";
        } else if (moneyStr.indexOf("-") > -1) {
            minPrice = moneyStr.split("-")[0];
            maxPrice = moneyStr.split("-")[1];
        } else {
            minPrice = moneyStr;
            maxPrice = "";
        }
        onRefresh();
        load_view.loading();
    }

    /*选择标示-金额*/
    private void setMoneyData(View v) {
        isRecommend="";
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        selMoneyColorList.clear();
        selMoneyColorList.add(v);

        moneyPanel();
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundResource(R.drawable.select_price);
        String text = textView.getText().toString().trim();
        String moneyMode = String.valueOf(textView.getTag());
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if (!text.equals("不限")) {
            addLableEntity("", text, "3");
        } else {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("3")) {
                        list.remove(i);
                        lable.initData(list, width);
                    }
                }
            }
        }
        switch (moneyMode) {
            case MONEYMODE.ZERO:
                getAmount(MONEYTEXT.ZERO);
                break;
            case MONEYMODE.ONE:
                getAmount(MONEYTEXT.ONE);
                break;
            case MONEYMODE.TWO:
                getAmount(MONEYTEXT.TWO);
                break;
            case MONEYMODE.THREE:
                getAmount(MONEYTEXT.THREE);
                break;
            case MONEYMODE.FOUR:
                getAmount(MONEYTEXT.FOUR);
                break;
            case MONEYMODE.FIVE:
                getAmount(MONEYTEXT.FIVE);
                break;
        }

    }

    /*价格筛选面板*/
    private void moneyPanel() {
        moneyFilter = !moneyFilter;
        if (moneyFilter) {
            tvOne.setTextColor(getResources().getColor(R.color.black_1));
            ivOne.setImageResource(R.mipmap.back_xb);
            tvThree.setTextColor(getResources().getColor(R.color.orange_2));
            ivThree.setImageResource(R.mipmap.sel_subscript);
            tvFour.setTextColor(getResources().getColor(R.color.black_1));
            ivFour.setImageResource(R.mipmap.back_xb);
        } else {
            tvOne.setTextColor(getResources().getColor(R.color.black_1));
            ivOne.setImageResource(R.mipmap.back_xb);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            tvFour.setTextColor(getResources().getColor(R.color.black_1));
            ivFour.setImageResource(R.mipmap.back_xb);
        }
        showFilterPanel(moneyFilter, threeLayout, moneyLayout);
        if (sotrFilter) {
            sotrFilter = !sotrFilter;
            showFilterPanel(sotrFilter, oneLayout, sortLayout);
        } else if (ageFilter) {
            ageFilter = !ageFilter;
            showFilterPanel(ageFilter, fourLayout, ageLayout);
        }
    }

    /*自定义金额*/
    private void customFilter() {
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        String min = customMoneyOne.getText().toString().trim();
        String max = customMoneyTwo.getText().toString().trim();
        if (min.isEmpty() || max.isEmpty())
            ToastTool.show(getActivity(), "自定义价格不能为空！");
        else if (min.equals("0") && max.equals("0"))
            ToastTool.show(getActivity(), "自定义价格不能全为0！");
        else if (max.equals("0"))
            ToastTool.show(getActivity(), "自定义价格不能为0！");
        else if (Double.valueOf(min) >= Double.valueOf(max))
            ToastTool.show(getActivity(), "自定义第二个价格不能小于等于第一个价格！");
        else {
            minPrice = min;
            maxPrice = max;
            String text = min + "-" + max + "万";
            addLableEntity("", text, "3");
            moneyPanel();
            onRefresh();
            load_view.loading();
        }
    }

    //type- 1:品牌，3-价格，4--车龄
    private void addLableEntity(String id, String text, String type) {
        LableEntity lableEntity = new LableEntity();
        lableEntity.setId(id);
        lableEntity.setLable(text);
        lableEntity.setType(type);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LableEntity entity = list.get(i);
                if (entity.getType().equals(type))
                    list.remove(i);
            }

        }
        list.add(lableEntity);
        lable.removeAllViews();
        lable.initData(list, width);

        isRecommend="";//只要筛选了条件就不要推荐
    }


    /*打开关闭动画*/
    public void showFilterPanel(final boolean isAnim, final View clickView, final View animView) {
        clickView.setEnabled(false);
        Animation animation;
        if (isAnim) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.record_open);
            animView.setClickable(true);
            pullToRefreshRecyclerView.setClickable(false);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.record_close);
            animView.setClickable(false);
            pullToRefreshRecyclerView.setClickable(true);

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
                if (isAnim) {
                    animView.setVisibility(View.VISIBLE);
                } else {
                    animView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            if (requestCode == BRANDREQUESTCODE) {
//                BrandEntity entity = (BrandEntity) data.getExtras().getSerializable("BrandEntity");
//                brandId = entity.getId();
                SortModel brand = (SortModel) data.getSerializableExtra("sortmodel");
                brandId = brand.getBrandId();
                if (!brand.getName().equals("不限品牌")) {
                    addLableEntity(brand.getBrandId(), brand.getName(), "1");
                } else {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LableEntity lableEntity = list.get(i);
                            if (lableEntity.getType().equals("1")) {
                                list.remove(i);
                                lable.initData(list, width);
                            }
                        }
                    }
                }

                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        LableEntity lableEntity = list.get(i);
                        if (lableEntity.getType().equals("5")) {
                            list.remove(i);
                            lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                        }
                    }
                }
                onRefresh();
                load_view.loading();

            } else if (requestCode == CITYREQUESTCODE) {
                int type = data.getExtras().getInt("type");
                if (type == 1) {
                    List<CityInfoEntity> cityList = data.getExtras().getParcelableArrayList("CityInfoEntity");
                    //对获取到的城市列表排序
                    Collections.sort(cityList, new ComparatorImpl());
                    Log.e("=====list=", cityList.toString());
                    gpsCity = cityList.get(0).getCityName().equals("全国") ? "" : cityList.get(0).getCityName();
                    if (cityList.size() > 1)
                        gpsCity = gpsCity + "等";
                } else {
                    gpsCity = data.getExtras().getString("CityInfoEntity");
                    setLocationCity(gpsCity);
                }
                tvLocation.setText(gpsCity.isEmpty() ? "全国" : gpsCity);
                onRefresh();
                load_view.loading();

            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        Log.e("123456", "asdasd");
//        if(getLocationCity().isEmpty()) {
//            if (aMapLocation != null) {
//                if (aMapLocation.getErrorCode() == 0) {
//                    //可在其中解析amapLocation获取相应内容。
//                    Log.e("city", aMapLocation.getCity());
//                    gpsCity = aMapLocation.getCity();
//                    tvLocation.setText(gpsCity);
//                    setLocationCity(gpsCity);
//                    onRefresh();
//                    load_view.loading();
//
//                } else {
//                    tvLocation.setText("全国");
//                    gpsCity="";
////                    onRefresh();
//                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + aMapLocation.getErrorCode() + ", errInfo:"
//                            + aMapLocation.getErrorInfo());
//
//                    onRefresh();
//                    load_view.loading();
//
//                }
//            }
//
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode)
//        {
//            case REQUESTGPSCODE:
//                if(permissions.length>0) {
//                    if (!permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                        //ToastTool.show(getActivity(), "获取位置权限失败，请手动开启");
//                        tvLocation.setText("全国");
//                        gpsCity = "";
//                        onRefresh();
//                        load_view.loading();
//                        return;
//                    }
//                    requestPermissions();
//                }
//
//                break;
//        }
    }



  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case REQUESTGPSCODE:
                if (!permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)||grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    ToastTool.show(getActivity(),"获取位置权限失败，请手动开启");
                    tvLocation.setText("全国");
                    gpsCity="";
                    onRefresh();
                    return;
                }
                requestPermissions();
                break;
        }
    }*/

    /*价格-金额*/
    public interface MONEYTEXT {
        String ZERO = "";
        String ONE = "0-30";
        String TWO = "30-50";
        String THREE = "50-70";
        String FOUR = "70-100";
        String FIVE = "100";
    }

    /*价格Key-模式*/
    public interface MONEYMODE {
        String ZERO = "0";
        String ONE = "1";
        String TWO = "2";
        String THREE = "3";
        String FOUR = "4";
        String FIVE = "5";
          /*价格与车龄共用一个Key-模式*/
        /*String SIX="6";
        String SEVEN="7";*/
    }

    public class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                ((NewMainActivity) getActivity()).setIvCustomerVisiable(true);
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                ((NewMainActivity) getActivity()).setIvCustomerVisiable(false);
            }
        }
    }

    public void upDateUi() {
//        gpsCity = SpUtils.getLoacationCity(getActivity());
        gpsCity=SpUtils.getCityid(getActivity());
        onRefresh();
    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            gpsCity = SpUtils.getCityid(getActivity());
            onRefresh();
        }
    }

    private String onlybrandid="";
    private String onlycxid="";
    /**
     * 从搜索也过来带车系的
     * @param entity
     */
    public void upTable(SearchEntity entity) {//从外面送进来的品牌
        if(entity!=null){

            if(!TextUtils.isEmpty(entity.getBrandName())){
                addLableEntity(entity.getSearchId(), entity.getBrandName(), "1");
                onlybrandid=entity.getBrandId();
            }else {
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

            if(!TextUtils.isEmpty(entity.getSeriesName())){
                addLableEntity(entity.getSeriesId(), entity.getSeriesName(), "5");
                onlycxid=","+entity.getSeriesId();
//                onlycxid=entity.getSeriesId();
            }else {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        LableEntity lableEntity = list.get(i);
                        if (lableEntity.getType().equals("5")) {
                            list.remove(i);
                            lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                        }
                    }
                }
            }

            if(!TextUtils.isEmpty(entity.getSearchId())){
                brandId=entity.getSearchId();
            }

            onRefresh();
        }
    }

    /**
     * 从推荐直接进来所有已经的筛选条件都关掉
     */
    public void upDateIsRec() {
        list.clear();
        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
        minPrice="";
        maxPrice="";
        brandId="";
        minAge="";
        maxAge="";

//        isRecommend="1";
        isRecommend="";
        onRefresh();
    }
}
