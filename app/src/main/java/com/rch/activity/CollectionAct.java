package com.rch.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.NewMainActivity;
import com.rch.R;


import com.rch.adatper.CollectAdapter;
import com.rch.base.BaseActivity;
import com.rch.base.SoftKeyBoardListener;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.custom.HomeLable;
import com.rch.custom.LoadingView;
import com.rch.custom.SortModel;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarEntity;
import com.rch.entity.CollectionBean;
import com.rch.entity.LableEntity;
import com.rch.fragment.CarFragment;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的收藏
 * Created by Administrator on 2018/7/18.
 */

public class CollectionAct extends BaseActivity implements View.OnClickListener,SoftKeyBoardListener.OnSoftKeyBoardChangeListener{

    private View view_zsz;
    private View view_ysq;
    private View view_yxj;
    private LinearLayout ll_zsz;
    private LinearLayout ll_ysq;
    private LinearLayout ll_yxj;

    private LinearLayout ll_nocontent;
    private SoftKeyBoardListener listener;




    private String isSale;
    private List<CollectionBean>datalist=new ArrayList<>();
    boolean isVisible;
    View view;

    HomeLable lable;

    LinearLayout oneLayout, twoLayout, threeLayout, fourLayout, sortLayout, moneyLayout, areaLayout, ageLayout;
    TextView tvOne, tvTwo, tvThree, tvFour,
            sortOne, sortTwo, sortThree,
            moneyZero, moneyOne, moneyTwo, moneyThree, moneyFour, moneyFive,
            ageZero, ageOne, ageTwo, ageThree, ageFour, ageFive, ageSix, ageSeven,
            customAgeFilter, customMoneyFilter, tvLocation;
    ImageView ivOne, ivTwo, ivThree, ivFour, ivAreaIcon, ivBgImg;
    EditText customMoneyOne, customMoneyTwo, customAgeOne, customAgeTwo;
    private TextView tv_no;
    private TextView tv_select;

    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    RecyclerView recyclerView;

    List<LableEntity> list = new ArrayList<>();

    List<CollectionBean>infolist;
    List<View> selMoneyColorList = new ArrayList<>();
    List<View> selOrderColorList = new ArrayList<>();
    List<View> selAgeColorList = new ArrayList<>();

    CollectAdapter adapter;
    private LoadingView ld_view;


    boolean sotrFilter, moneyFilter, ageFilter;
    String sotrMode = "1", minPrice = "", maxPrice = "", brandId = "";
    String minAge = "", maxAge = "";

    int page = 1;
    int pageSize = 10;
    private boolean isshow;//刷新和加载

    int BRANDREQUESTCODE = 10101;//品牌
    private RelativeLayout rl_back;

    String priceType = "0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
    String userType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_conllect);
        initToolBar();
        listener=new SoftKeyBoardListener(this);
        listener.setListener(CollectionAct.this,this);

        initControl();
        selectCol(0);
//        getData();

        addTextWatcher();
    }

    private void addTextWatcher(){

        InputFilter[] filters = {new EditInputFilter(99999,2)};
        //自定义价格
        if (customMoneyOne!=null) {
            customMoneyOne.setFilters(filters);
        }
        if (customMoneyTwo!=null) {
            customMoneyTwo.setFilters(filters);
        }
    }



    private void getData() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("userId", getUserInfo().getId());
        if(!TextUtils.isEmpty(isSale)){
            param.add("isSale",isSale);
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
        param.add("currentPage",page+"");
        param.add("pageSize",pageSize+"");
        OkHttpUtils.post(Config.COLLECTVEHICLE_URL, param, new OkHttpCallBack(CollectionAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                pullToRefreshRecyclerView.onRefreshComplete();
                ld_view.loadComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                    String priceType=result.getString("priceType");//价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
                    String totalSize=result.getString("totalSize");
                     userType=result.getString("userType");
                    if(userType.equals("2")){
                        tv_no.setVisibility(View.GONE);
                    }else {//企业现实
                        tv_no.setVisibility(View.VISIBLE);
                    }

                    infolist= gson.fromJson(result.getJSONArray("list").toString(),new TypeToken<List<CollectionBean>>(){}.getType());

                    if(infolist!=null&&infolist.size()>0){
                        if(page==1){
                            datalist.clear();
                        }
                        datalist.addAll(infolist);
                        adapter.update(datalist,priceType);
                        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);

                    }else {
                       if(!isshow){//说明是切换条件，界面变化
                           adapter.update(new ArrayList<CollectionBean>(),priceType);
                           ld_view.noContent();
                           ld_view.setNoContentTxt("您还没有收藏的车辆，","快去选车页","进行收藏吧~");
                           ld_view.setOnClickGoListener(new LoadingView.OnClickGoListener() {
                               @Override
                               public void click() {
                                   Intent intent = new Intent(CollectionAct.this, NewMainActivity.class);
                                   intent.putExtra("from_collection",true);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                   startActivity(intent);
                                   finish();
                               }
                           });
                           pullToRefreshRecyclerView.setVisibility(View.GONE);
                       }else {//说明是下拉和刷新界面不变
                           pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                       }
                    }

                    if(datalist!=null&&datalist.size()>0){
                        tv_no.setText("分享我收藏的店铺");
                        tv_no.setEnabled(true);
                        tv_no.setBackgroundColor(Color.parseColor("#ed702d"));
                        ll_nocontent.setVisibility(View.GONE);
                        tv_no.setTextColor(Color.parseColor("#ffffff"));
                    }else {
                        ll_nocontent.setVisibility(View.VISIBLE);
                        tv_no.setText("您还没有收藏车辆，无法分享");
                        tv_no.setEnabled(false);
                        tv_no.setBackgroundColor(Color.parseColor("#ffb38a"));
                        tv_no.setTextColor(Color.parseColor("#ffffff"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                pullToRefreshRecyclerView.onRefreshComplete();
                ld_view.loadError();
            }
        });
    }

    private void initControl() {
        ll_nocontent= (LinearLayout) findViewById(R.id.ll_nocontent);
        ll_nocontent.setVisibility(View.GONE);

        ld_view= (LoadingView) findViewById(R.id.ld_view);

        ld_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                getData();
            }
        });

        tv_select=(TextView) findViewById(R.id.tv_select_car);
        tv_select.setOnClickListener(this);
        rl_back= (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        tv_no= (TextView) findViewById(R.id.tv_no);
        ll_zsz= (LinearLayout) findViewById(R.id.ll_zsz);
        ll_ysq= (LinearLayout) findViewById(R.id.ll_ysq);
        ll_yxj= (LinearLayout) findViewById(R.id.ll_yxj);
        view_zsz=findViewById(R.id.view_zsz);
        view_ysq=findViewById(R.id.view_ysq);
        view_yxj=findViewById(R.id.view_yxj);

        lable = (HomeLable)findViewById(R.id.home_lable_container);
        oneLayout = (LinearLayout) findViewById(R.id.home_title_one_layout);
        twoLayout = (LinearLayout)findViewById(R.id.home_title_two_layout);
        threeLayout = (LinearLayout)findViewById(R.id.home_title_three_layout);
        fourLayout = (LinearLayout)findViewById(R.id.home_title_four_layout);
        sortLayout = (LinearLayout)findViewById(R.id.home_sort_layout);
        moneyLayout = (LinearLayout)findViewById(R.id.home_money_layout);
        ageLayout = (LinearLayout)findViewById(R.id.home_age_layout);

        areaLayout = (LinearLayout)findViewById(R.id.home_area_layout);

        tvOne = (TextView)findViewById(R.id.home_title_one_text);
        tvTwo = (TextView)findViewById(R.id.home_title_two_text);
        tvThree = (TextView)findViewById(R.id.home_title_three_text);
        tvFour = (TextView) findViewById(R.id.home_title_four_text);
        sortOne = (TextView) findViewById(R.id.sort_one);
        sortTwo = (TextView) findViewById(R.id.sort_two);
        sortThree = (TextView) findViewById(R.id.sort_three);

        moneyZero = (TextView) findViewById(R.id.money_zero);
        moneyOne = (TextView) findViewById(R.id.money_one);
        moneyTwo = (TextView) findViewById(R.id.money_two);
        moneyThree = (TextView) findViewById(R.id.money_three);
        moneyFour = (TextView) findViewById(R.id.money_four);
        moneyFive = (TextView) findViewById(R.id.money_five);

        ageZero = (TextView) findViewById(R.id.age_zero);
        ageOne = (TextView)findViewById(R.id.age_one);
        ageTwo = (TextView) findViewById(R.id.age_two);
        ageThree = (TextView) findViewById(R.id.age_three);
        ageFour = (TextView) findViewById(R.id.age_four);
        ageFive = (TextView) findViewById(R.id.age_five);
        ageSix = (TextView) findViewById(R.id.age_six);
        ageSeven = (TextView) findViewById(R.id.age_seven);

        customAgeFilter = (TextView) findViewById(R.id.custom_age_filter);
        customMoneyFilter = (TextView)findViewById(R.id.custom_money_filter);
        tvLocation = (TextView) findViewById(R.id.car_fragment_location);

        customAgeOne = (EditText) findViewById(R.id.et_custom_age_one);
        customAgeTwo = (EditText) findViewById(R.id.et_custom_age_two);
        customMoneyOne = (EditText) findViewById(R.id.et_custom_money_one);
        customMoneyTwo = (EditText) findViewById(R.id.et_custom_money_two);

        selOrderColorList.add(sortOne);
        selMoneyColorList.add(moneyZero);
        selAgeColorList.add(ageZero);

        ivOne = (ImageView) findViewById(R.id.home_title_one_img);
        ivTwo = (ImageView) findViewById(R.id.home_title_two_img);
        ivThree = (ImageView) findViewById(R.id.home_title_three_img);
        ivFour = (ImageView) findViewById(R.id.home_title_four_img);
        ivAreaIcon = (ImageView) findViewById(R.id.home_area_icon);



        tv_no.setOnClickListener(this);

        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.home_recyclerView);
        recyclerView = pullToRefreshRecyclerView.getRefreshableView();




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

        ll_zsz.setOnClickListener(this);
        ll_ysq.setOnClickListener(this);
        ll_yxj.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionAct.this));
        adapter=new CollectAdapter(CollectionAct.this,datalist);
        recyclerView.setAdapter(adapter);

        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                onRefresh();
                isshow=true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                isshow=true;
                getData();
            }
        });


        lable.setOnTtemListener(new HomeLable.OnTtemListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDel(List<LableEntity> delList) {
                list=delList;
                List<String>lablelist=new ArrayList<>();
                lablelist.clear();
                lable.initData(list,width);
                if(list.size()>0)
                {
                    for(int i=0;i<list.size();i++){
                        lablelist.add(list.get(i).getType());
                    }

                    if(!lablelist.contains("4")){//没有年
                       maxAge="";
                       minAge="";
                    }
                    if(!lablelist.contains("3")){//没有金额
                     minPrice="";
                     maxPrice="";
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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReset() {
                list.clear();
                lable.initData(list,width);
                minPrice="";
                maxPrice="";
                brandId="";
                minAge="";
                maxAge="";

                onRefresh();
            }
        });
    }






    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_title_one_layout://最新
                sortPanel();
                break;
            case R.id.home_title_two_layout://品牌
                sotrFilter=false;
                moneyFilter=false;
                ageFilter=false;
                tvOne.setTextColor(getResources().getColor(R.color.black_1));
                ivOne.setImageResource(R.mipmap.back_xb);
                tvThree.setTextColor(getResources().getColor(R.color.black_1));
                ivThree.setImageResource(R.mipmap.back_xb);
                tvFour.setTextColor(getResources().getColor(R.color.black_1));
                ivFour.setImageResource(R.mipmap.back_xb);
                if(sortLayout.getVisibility()!=View.GONE)
                    showFilterPanel(sotrFilter,oneLayout,sortLayout);
                else if(moneyLayout.getVisibility()!=View.GONE)
                    showFilterPanel(moneyFilter,threeLayout,moneyLayout);
                else if(ageLayout.getVisibility()!=View.GONE)
                    showFilterPanel(ageFilter,fourLayout,ageLayout);
//                startActivityForResult(new Intent(CollectionAct.this, BrandActivity.class).putExtra("Type",1), BRANDREQUESTCODE);
                startActivityForResult(new Intent(CollectionAct.this, NewBrandAct.class).putExtra("type",1), BRANDREQUESTCODE);
                break;
            case R.id.home_title_three_layout://价格
                moneyPanel();
                break;
            case R.id.home_title_four_layout://车龄
                agePanel();
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

            case R.id.custom_age_filter:
                customAgeFilter();
                break;

            case R.id.custom_money_filter:
                customFilter();
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
            case R.id.tv_no:
            share();//分享
            break;

            case R.id.rl_back:
            finish();
            break;

            case R.id.tv_select_car:
                sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                finish();
                break;


        }
    }

    private void onRefresh()
    {
        isshow=false;
        page=1;
        ld_view.loading();
        getData();
    }


    /*选择排序模式*/
    private void setSortMode(View v) {

        if (selOrderColorList.size() > 0)
            ((TextView) selOrderColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        selOrderColorList.clear();
        selOrderColorList.add(v);
        sortPanel();//关闭
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        sotrMode = String.valueOf(textView.getTag());
        tvOne.setText(textView.getText().toString().trim());
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        onRefresh();
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setAgeData(View v) {
        if (selAgeColorList.size() > 0)
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        selAgeColorList.clear();
        selAgeColorList.add(v);
        agePanel();
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        String text = textView.getText().toString().trim();
        String tag = String.valueOf(textView.getTag());
        if (tag.indexOf("-") > 0) {
            String values = String.valueOf(textView.getTag()).split("-")[0];
            String carType = String.valueOf(textView.getTag()).split("-")[1];
            if (carType.equals("on")) {//6年以上
                maxAge = "";
                minAge = values;
            }
            else {//0-6年内
                maxAge = values;
                minAge = "";
            }
        }else {//不限
            maxAge = "";
            minAge = "";
        }
        // ageMode=;
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        if (!text.equals("不限")) {
            addLableEntity("", text, "4");
        }else {//点击不限就把它去掉
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
    }



    /*自定义车龄*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void customAgeFilter() {
        if (selAgeColorList.size() > 0)
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        selAgeColorList.clear();
        minAge = customAgeOne.getText().toString().trim();
        maxAge = customAgeTwo.getText().toString().trim();
        if (TextUtils.isEmpty(minAge) || TextUtils.isEmpty(maxAge)){
            ToastTool.show(this,"自定义车龄不能为空！");
            return;
        }
        int iMinAge=Integer.parseInt(minAge);
        int iMaxAge=Integer.parseInt(maxAge);
        if (minAge.isEmpty() || maxAge.isEmpty())
            ToastTool.show(CollectionAct.this, "自定义车龄不能为空！");
        else if(iMinAge>=iMaxAge)
            ToastTool.show(this,"自定义第二个车龄不能小于等于第一个车龄的值！");
        else{
            String text = minAge + "-" + maxAge + "年";
            addLableEntity("", text, "4");
            agePanel();
            onRefresh();

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

    }

    /*选择标示-金额*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMoneyData(View v) {
        if (selMoneyColorList.size() > 0)
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        selMoneyColorList.clear();
        selMoneyColorList.add(v);
        moneyPanel();
        TextView textView = (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        String text = textView.getText().toString().trim();
        String moneyMode = String.valueOf(textView.getTag());
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if (!text.equals("不限")) {
            addLableEntity("", text, "3");
        }else {
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
            case CarFragment.MONEYMODE.ZERO:
                getAmount(CarFragment.MONEYTEXT.ZERO);
                break;
            case CarFragment.MONEYMODE.ONE:
                getAmount(CarFragment.MONEYTEXT.ONE);
                break;
            case CarFragment.MONEYMODE.TWO:
                getAmount(CarFragment.MONEYTEXT.TWO);
                break;
            case CarFragment.MONEYMODE.THREE:
                getAmount(CarFragment.MONEYTEXT.THREE);
                break;
            case CarFragment.MONEYMODE.FOUR:
                getAmount(CarFragment.MONEYTEXT.FOUR);
                break;
            case CarFragment.MONEYMODE.FIVE:
                getAmount(CarFragment.MONEYTEXT.FIVE);
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void customFilter() {
        if (selMoneyColorList.size() > 0)
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        selMoneyColorList.clear();
        String min = customMoneyOne.getText().toString().trim();
        String max = customMoneyTwo.getText().toString().trim();
        if(min.isEmpty()||max.isEmpty())
            ToastTool.show(this,"自定义价格不能为空！");
        else if(min.equals("0")&&max.equals("0"))
            ToastTool.show(this,"自定义价格不能全为0！");
        else if(max.equals("0"))
            ToastTool.show(this,"自定义价格不能为0！");
        else if(Double.valueOf(min)>= Double.valueOf(max))
            ToastTool.show(this,"自定义第二个价格不能小于等于第一个价格！");
//        else if (min.equals("0") && max.equals("0"))
//            ToastTool.show(CollectionAct.this, "自定义价格不能全为0！");
//        else if (max.equals("0"))
//            ToastTool.show(CollectionAct.this, "自定义价格不能为0！");
        else {
            minPrice = min ;
            maxPrice = max;
            String text = min + "-" + max + "万";
            addLableEntity("", text, "3");
            moneyPanel();
            onRefresh();

        }
    }

    //type- 1:品牌，3-价格，4--车龄
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addLableEntity(String id, String text, String type) {
        LableEntity lableEntity = new LableEntity();
        lableEntity.setId(id);
        lableEntity.setLable(text);
        lableEntity.setType(type);
        if (list.size() > 0) {
//            if (list.contains(lableEntity)) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity entity = list.get(i);
                    if (entity.getType().equals(type))
                        list.remove(i);
//                }
            }
        }
        list.add(lableEntity);
        lable.removeAllViews();
        lable.initData(list,width);
    }


    /*打开关闭动画*/
    public void showFilterPanel(final boolean isAnim, final View clickView, final View animView) {
        clickView.setEnabled(false);
        Animation animation;
        if (isAnim) {
            animation = AnimationUtils.loadAnimation(CollectionAct.this, R.anim.record_open);
            animView.setClickable(true);
            pullToRefreshRecyclerView.setClickable(false);
        } else {
            animation = AnimationUtils.loadAnimation(CollectionAct.this, R.anim.record_close);
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




    private void selectCol(int i) {
        view_zsz.setBackgroundColor(getResources().getColor(R.color.white));
        view_ysq.setBackgroundColor(getResources().getColor(R.color.white));
        view_yxj.setBackgroundColor(getResources().getColor(R.color.white));

        if(i==0){
            view_zsz.setBackgroundColor(getResources().getColor(R.color.orange_2));
            isSale="1";
        }else if(i==1){
            view_ysq.setBackgroundColor(getResources().getColor(R.color.orange_2));
            isSale="3";
        }else if(i==2){
            isSale="2";
            view_yxj.setBackgroundColor(getResources().getColor(R.color.orange_2));
        }
        onRefresh();
    }


    UMWeb web,web1;
    UMImage image;
    String wxshartitle;
    String shareDesc;


    private void share() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("type", "2");
        OkHttpUtils.post(Config.INVITEUSER, param, new OkHttpCallBack(CollectionAct.this,"加载中") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject=new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                    String userId=result.getString("userId");
                    String userName=result.getString("userName");
                    String enterpriseName=result.getString("enterpriseName");
                    String enterpriseId=result.getString("enterpriseId");
                    String uuId=result.getString("uuId");
                    String shareUrl=result.getString("shareUrl");


                    image = new UMImage(CollectionAct.this, R.mipmap.shareicon);
                    image.compressStyle=UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图

                    wxshartitle=userName+"向你分享了TA的店铺";
                    shareDesc="快来看看我的店铺吧，百万豪车等你来拿~";

                    //微信
                    web = new UMWeb(shareUrl);//分享路径
                    web.setTitle(wxshartitle);//标题
                    web.setThumb(image);  //缩略图
                    web.setDescription(shareDesc);//描述

                    //朋友圈
                    web1 = new UMWeb(shareUrl);
                    web1.setTitle(shareDesc);//类容
                    web1.setThumb(image);  //缩略图

                    new ShareAction(CollectionAct.this)
                            .setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
//                .withMedia(web)
                            .setShareboardclickCallback(shareBoardlistener)//面板点击监听器
                            .open();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(CollectionAct.this,error);
            }
        });


    }


    private ShareBoardlistener shareBoardlistener = new  ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media==null){
                //根据key来区分自定义按钮的类型，并进行对应的操作
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")){
                    ToastTool.show(CollectionAct.this,"add button  success");
                }
            }
            else {//社交平台的分享行为
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    new ShareAction(CollectionAct.this)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .withMedia(web)
                            .share();
                }else if(share_media==SHARE_MEDIA.WEIXIN_CIRCLE){
                    new ShareAction(CollectionAct.this)
                            .setPlatform(share_media)
                            .setCallback(umShareListener)
                            .withMedia(web1)
                            .share();
                }
            }
        }
    };


    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {

        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t.getMessage().contains("没有安装应用")){
                ToastTool.show(CollectionAct.this,"您未安装微信APP,请先安装");

            }else {
                ToastTool.show(CollectionAct.this, t.getMessage());
            }
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastTool.show(CollectionAct.this,"取消！");
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        if (resultCode == -1) {
            if (requestCode == BRANDREQUESTCODE) {
//                BrandEntity entity = (BrandEntity) data.getExtras().getSerializable("BrandEntity");
//                brandId = entity.getId();
                SortModel brand= (SortModel) data.getSerializableExtra("sortmodel");
                brandId=brand.getBrandId();
                if (!brand.getName().equals("不限品牌"))
                    addLableEntity(brand.getBrandId(), brand.getName(), "1");
                else {
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
                onRefresh();
//                load_view.loading();
            }
        }

    }


    @Override
    public void keyBoardShow(int height) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!userType.equals("1")){
                            tv_no.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public void keyBoardHide(int height) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!userType.equals("1")){
                            tv_no.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (broadcastReceiver==null){
            broadcastReceiver = new MyBroadcastReceiver();
            registerReceiver(broadcastReceiver,new IntentFilter(UPDATE_CO_LIST));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver =null;
        }
    }

    private MyBroadcastReceiver broadcastReceiver;
    public static final String UPDATE_CO_LIST="update_co_list";

    public class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    }
}



