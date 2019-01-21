package com.rch.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rch.LanternRoll.XMarqueeView;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.activity.AuthenticateActivity;
import com.rch.activity.CustomerAct;
import com.rch.activity.LoginActivity;

import com.rch.activity.NewInofActivity;
import com.rch.activity.NewsconsultingActivity;
import com.rch.activity.SeachCarServerActivity;
import com.rch.activity.SearchActivity;
import com.rch.activity.SellCarActivity;
import com.rch.activity.StartCarActivity;
import com.rch.activity.VehicleActivity;
import com.rch.adatper.CarStarAdapter;
import com.rch.adatper.CarlibAdapter;
import com.rch.adatper.SpaceItemMiddleDecoration;
import com.rch.base.BaseFrag;

import com.rch.base.MyApplication;
import com.rch.common.Config;

import com.rch.common.GeneralUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.CertifiedPromptDialog;
import com.rch.custom.FlyBanner;
import com.rch.custom.FullyGridLayoutManager;
import com.rch.custom.HomeCarLayout;
import com.rch.custom.MyListview;
import com.rch.entity.CarEntity;
import com.rch.entity.HomeDateBean;
import com.rch.entity.RchNewsBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.rch.view.NoScrollRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class HomeFragment extends BaseFrag implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    LinearLayout lSecondHandl,lNewCar,lSearchLayout,lBuyModule,lSellModule,lSeekModule,lDetectModule,ll_top;


    LinearLayout new_car_module,old_car_module;//新车，高端二手车
    TextView tv_sports_car;//明星车款查看更多车源
    RelativeLayout rl_star_new_car,rl_star_old_car;//明星车款 新车，二手车
    TextView tv_star_new_car,tv_star_old_car;//明星车款 新车，二手车
    View view_star_new_car,view_star_old_car;//明星车款 新车，二手车
    private boolean isStarNewCar = true;//当前选中明星车款 新车
    NoScrollRecyclerView rcv_star_list;//明星车款列表

    TextView tv_more_cars,tv_look_financial_detail;//查看更多车源,查看金融详情
    LinearLayout ll_lixi,ll_rongzi,ll_changhuan;//融资利息超低,融资金额超高,偿还方式多样
    TextView tv_car_finance,tv_stock_finance,tv_claim_finance,tv_car_credit;//单车融资,库存融资,应收账款保理融资,车抵贷
    RelativeLayout rl_new_car,rl_old_car;//新车，二手车
    TextView tv_new_car,tv_old_car;//新车，二手车
    View view_new_car,view_old_car;//新车，二手车
    private boolean isNewCar = true;//当前选中新车
    LinearLayout ll_kefu;//客服
    TextView tv_kefu;//客服

    private SwipeRefreshLayout swiper;

    List<String> strUrl=new ArrayList<>();
    List<CarEntity> listCar=new ArrayList<>();
    List<String> reservList = new ArrayList<>();
    // List<ReserveOrderEntity> reserveOrderList=new ArrayList<>();

    HomeCarLayout homeCarLayout;
    XMarqueeView marqueeviewone;

    String priceType="0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
    String userHasAgentRemind="";//用户成为分销商弹框提醒，1-企业分销商，2-个人分销商，这个字段只会出现一次，调用一次接口后将不会出现。

    boolean isVisible;


    FlyBanner mBannerNet;//无限轮播
    FlyBanner fly_one,fly_two;//第一个广告位置，第二个广告位置
    RelativeLayout rl_adone,rl_adtwo;//第一个广告位置，第二个广告位置
    TextView tv_adinfo;

    private NestedScrollView nd;
    private ImageView iv_logo,iv_customer,ivMsg;
    private int screen_height;//图片控件高度
    private Context context;
    private MyListview lv_my;
    private CarlibAdapter adapter;

    private CarStarAdapter starAdapter;//明星车款适配器
    private List<CarEntity> starCarList = new ArrayList<>();//

    private int from=0;//精选好车 新车/二手车
    private int starType=0;//明星车款 新车/二手车
    private View view;

    private Gson gson;
    private HomeDateBean homebean;
    private RchNewsBean rchbean;

    View view_include;

    Toolbar toolbar;

    @Override
    public View initView(LayoutInflater inflater) {
        view= inflater.inflate(R.layout.home_fragment,null);
        initControl();
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initListeners();
        setData();
        gson=new Gson();
        if (isNewCar){
            setNewCarChecked();
        }else {
            setOldCarChecked();
        }

        if (isStarNewCar){
            setStarNewCarChecked();
        }else {
            setStarOldCarChecked();
        }
    }
    private GridLayoutManager mGridLayoutManager = new GridLayoutManager(mActivity,2){
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    };
    private void initControl() {
        context = MyApplication.getInstance();
        screen_height = SpUtils.get_View_heigth(SpUtils.getScreenWith(context), 388, 750);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
      /*  lSecondHandl= (LinearLayout) view.findViewById(R.id.home_second_hand);
        lNewCar= (LinearLayout) view.findViewById(R.id.home_new_car);*/
        iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
        new_car_module = (LinearLayout) view.findViewById(R.id.new_car_module);
        old_car_module = (LinearLayout) view.findViewById(R.id.old_car_module);
        tv_sports_car = (TextView) view.findViewById(R.id.tv_sports_car);
        new_car_module.setOnClickListener(this);
        old_car_module.setOnClickListener(this);
        tv_sports_car.setOnClickListener(this);
        tv_more_cars = (TextView) view.findViewById(R.id.tv_more_cars);
        tv_look_financial_detail = (TextView) view.findViewById(R.id.tv_look_financial_detail);
        ll_lixi = (LinearLayout) view.findViewById(R.id.ll_lixi);
        ll_rongzi = (LinearLayout) view.findViewById(R.id.ll_rongzi);
        ll_changhuan = (LinearLayout) view.findViewById(R.id.ll_changhuan);
        tv_car_finance = (TextView) view.findViewById(R.id.tv_car_finance);
        tv_stock_finance = (TextView) view.findViewById(R.id.tv_stock_finance);
        tv_claim_finance = (TextView) view.findViewById(R.id.tv_claim_finance);
        tv_car_credit = (TextView) view.findViewById(R.id.tv_car_credit);
        tv_more_cars.setOnClickListener(this);
        tv_look_financial_detail.setOnClickListener(this);
        ll_lixi.setOnClickListener(this);
        ll_rongzi.setOnClickListener(this);
        ll_changhuan.setOnClickListener(this);
        tv_car_finance.setOnClickListener(this);
        tv_stock_finance.setOnClickListener(this);
        tv_claim_finance.setOnClickListener(this);
        tv_car_credit.setOnClickListener(this);
        rl_new_car = (RelativeLayout) view.findViewById(R.id.rl_new_car);
        rl_old_car = (RelativeLayout) view.findViewById(R.id.rl_old_car);
        rl_new_car.setOnClickListener(this);
        rl_old_car.setOnClickListener(this);
        tv_new_car = (TextView) view.findViewById(R.id.tv_new_car);
        tv_old_car = (TextView) view.findViewById(R.id.tv_old_car);
        view_new_car = view.findViewById(R.id.view_new_car);
        view_old_car = view.findViewById(R.id.view_old_car);

        //明星车款
        rl_star_new_car = (RelativeLayout) view.findViewById(R.id.rl_star_new_car);
        rl_star_old_car = (RelativeLayout) view.findViewById(R.id.rl_star_old_car);
        rl_star_new_car.setOnClickListener(this);
        rl_star_old_car.setOnClickListener(this);
        tv_star_new_car = (TextView) view.findViewById(R.id.tv_star_new_car);
        tv_star_old_car = (TextView) view.findViewById(R.id.tv_star_old_car);
        view_star_new_car = view.findViewById(R.id.view_star_new_car);
        view_star_old_car = view.findViewById(R.id.view_star_old_car);
        rcv_star_list = (NoScrollRecyclerView) view.findViewById(R.id.rcv_star_list);
//        rcv_star_list.addItemDecoration(new SpaceItemMiddleDecoration(GeneralUtils.dip2px(mActivity,6)));
        rcv_star_list.setHasFixedSize(true);
        rcv_star_list.setFocusable(false);
        rcv_star_list.setNestedScrollingEnabled(false);
        rcv_star_list.setLayoutManager(mGridLayoutManager);
        starAdapter=new CarStarAdapter(mActivity,starCarList);
        rcv_star_list.setAdapter(starAdapter);

        ll_kefu = (LinearLayout) view.findViewById(R.id.ll_kefu);
        ll_kefu.setOnClickListener(this);
        tv_kefu= (TextView) view.findViewById(R.id.tv_kefu);
        tv_kefu.setOnClickListener(this);

        lSearchLayout = (LinearLayout) view.findViewById(R.id.home_search_layout);

        lBuyModule = (LinearLayout) view.findViewById(R.id.home_buy_module);
        lSellModule = (LinearLayout) view.findViewById(R.id.home_sell_module);
        lSeekModule = (LinearLayout) view.findViewById(R.id.home_seek_module);
        lDetectModule = (LinearLayout) view.findViewById(R.id.home_detect_module);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);

        ivMsg = (ImageView) view.findViewById(R.id.home_msg);

        mBannerNet = (FlyBanner) view.findViewById(R.id.banner_1);
        ViewGroup.LayoutParams params = mBannerNet.getLayoutParams();
        params.height = screen_height;
        mBannerNet.setLayoutParams(params);
        mBannerNet.setOnItemClickListener(new MyBannerItem());

        homeCarLayout = (HomeCarLayout) view.findViewById(R.id.home_car_layout);
        marqueeviewone = (XMarqueeView) view.findViewById(R.id.upview2);

        view_include= view.findViewById(R.id.view_include);



//        if (getUserInfo().getUserType().equals("1"))
//            lSellModule.setVisibility(View.GONE);
//        else
//            lSellModule.setVisibility(View.GONE);

      /*  lSecondHandl.setOnClickListener(this);
        lNewCar.setOnClickListener(this);*/
        lSearchLayout.setOnClickListener(this);
        lBuyModule.setOnClickListener(this);
        lSellModule.setOnClickListener(this);
        lSeekModule.setOnClickListener(this);
        lDetectModule.setOnClickListener(this);
        ivMsg.setOnClickListener(this);

        //初始化SwipeRefreshLayout
        swiper = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        nd= (NestedScrollView) view.findViewById(R.id.ns);
        iv_customer= (ImageView) view.findViewById(R.id.iv_customer);
        iv_customer.setOnClickListener(this);



//        nd.setOnTouchListener(new View.OnTouchListener() {
//
//            private int lastY = 0;
//            private int touchEventId = -9983761;
//
//            Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    View nd = (View) msg.obj;
//                    if (msg.what == touchEventId) {
//                        if (lastY == nd.getScrollY()) {
//                            handleStop(nd);
//                        } else {
//                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, nd), 20);
//                            lastY = nd.getScrollY();
////                            iv_customer.setAnimation(AnimationUtils.makeOutAnimation(getActivity(),true));
////                            iv_customer.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            };
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 20);
////                }
//
////                if (event.getAction() == MotionEvent.ACTION_DOWN) {
////                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 20);
////                }
//                handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 20);
//
//
//                return false;
//            }
//
//            //这里写真正的事件
//            private void handleStop(Object view) {
////                iv_customer.setAnimation(AnimationUtils.makeInAnimation(getActivity(),false));
////                iv_customer.setVisibility(View.VISIBLE);
//
//            }
//        });

        rl_adone= (RelativeLayout) view.findViewById(R.id.rl_adone);
        rl_adtwo= (RelativeLayout) view.findViewById(R.id.rl_adtwo);

        fly_one = (FlyBanner) view.findViewById(R.id.fly_one);
        fly_one.setType(1);
        fly_one.setOnItemClickListener(new MyAdoneItem());
        fly_two = (FlyBanner) view.findViewById(R.id.fly_two);
        fly_two.setType(2);
        fly_two.setOnItemClickListener(new MyAdtwoItem());

        tv_adinfo= (TextView) view.findViewById(R.id.tv_adinfo);
        tv_adinfo.setOnClickListener(this);

        nd.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY==0 || scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){
                    ( (NewMainActivity)getActivity()).setIvCustomerVisiable(true);
                }else {
                    ( (NewMainActivity)getActivity()).setIvCustomerVisiable(false);
                }
                if (scrollY <= 0) {
                    toolbar.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或者美工提供
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    iv_logo.setImageResource(R.mipmap.home_logo_white);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    lSearchLayout.setBackgroundResource(R.drawable.home_white_bg);
                } else if (scrollY > 0 && scrollY <= imageHeight) {
                    float scale = (float) scrollY / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    toolbar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    iv_logo.setImageResource(R.mipmap.home_logo_white);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    lSearchLayout.setBackgroundResource(R.drawable.home_white_bg);
                } else {
                    toolbar.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    ivMsg.setImageResource(R.mipmap.kefuhei);
                    iv_logo.setImageResource(R.mipmap.home_logo2);
                    tv_kefu.setTextColor(getResources().getColor(R.color.kf_info));
                    lSearchLayout.setBackgroundResource(R.drawable.home_gray_bg);
                }


            }
        });


    }

    private int imageHeight;

    private void initListeners() {
        // 获取顶部图片高度
        ViewTreeObserver vto = mBannerNet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBannerNet.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = mBannerNet.getHeight()-150;
                Log.e("===",imageHeight+"]]]");
            }
        });
    }


    private void setData()
    {
        clearGgLb();//先清空广告轮播
        requestHomeData();
        //testData();
    }

    private void requestHomeData()
    {
        RequestParam param = new RequestParam();
        OkHttpUtils.post(Config.HOMEINFO, param, new OkHttpCallBack(getActivity(),"加载中...") {

            @Override
            public void onSuccess(String data) {
                swiper.setRefreshing(false);
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    if(!TextUtils.isEmpty(result.getString("rchNews").toString())){
                       rchbean=gson.fromJson(result.getJSONObject("rchNews").toString(),RchNewsBean.class);
                    }
                    homebean=gson.fromJson(result.toString(),HomeDateBean.class);
                    if(!TextUtils.isEmpty(homebean.getRemindType())){
                        isShowUserHasAgentRemind(homebean.getRemindType());//认证通过弹框
                    }
                    if(!TextUtils.isEmpty(homebean.getHxUrl())){
                       SpUtils.setHxurl(mActivity,homebean.getHxUrl());//hx地址
                    }

                    if(homebean.getBannerAList()!=null&&homebean.getBannerAList().size()>0){
                        mBannerNet.setImagesUrl(homebean.getBannerAList());//显示首页轮播图
                    }
                    if (homebean.getBannerBList()!=null && homebean.getBannerBList().size()>0){
                        rl_adone.setVisibility(View.VISIBLE);
                        fly_one.setImagesUrl(homebean.getBannerBList());
                    }else {
                        rl_adone.setVisibility(View.GONE);
                    }
                    if (homebean.getBannerCList()!=null && homebean.getBannerCList().size()>0){
                        rl_adtwo.setVisibility(View.VISIBLE);
                        fly_two.setImagesUrl(homebean.getBannerCList());
                    }else {
                        rl_adtwo.setVisibility(View.GONE);
                    }
                    if (rchbean!=null)
                        tv_adinfo.setText(rchbean.getTitle());
                    if(homebean.getOrderList()!=null&&homebean.getOrderList().size()>0){
                        marqueeviewone.removeAllViews();//防止重叠
                        marqueeviewone.setData(homebean.getOrderList());
                    }

                        if(from==0){
                            homeCarLayout.initData(homebean.getNewVehicleList(),from);
                        }
                        if(from==1){
                            homeCarLayout.initData(homebean.getOldVehicleList(),from);
                        }

                        if (starType == 0){
                            starCarList = homebean.getStarNewVehicleList();
                        }else {
                            starCarList = homebean.getStarOldVehicleList();
                        }
                    starAdapter.updateData(starCarList,starType);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swiper.setRefreshing(false);
                }



            }

            @Override
            public void onError(int code, String error) {
                swiper.setRefreshing(false);
                ToastTool.show(getActivity(),error);
            }
        });
    }


    CertifiedPromptDialog dialog;
    /**
     * 认证通过弹框
     * @param userHasAuditRemind
     */
    private void isShowCertifiedPrompt(String userHasAuditRemind) {
        if (userHasAuditRemind.isEmpty()) {
            return;
        } else {
            dialog = new CertifiedPromptDialog(getActivity());
      /*  if(userResultState.isEmpty())
            return;*/
     /*   if(getCertified())
            return;*/
            // if(userResultState.equals("102")||userResultState.equals("105")) {
            if (userHasAuditRemind.equals("1")) {
                //setCertified(true);
//                dialog.setBgImage(R.mipmap.enterpriserz);
                dialog.setBgImage(R.mipmap.enterpriseno);
                dialog.setBtnImage(R.mipmap.goshare, new CertifiedPromptDialog.OnClickListener() {
                    @Override
                    public void onClick() {
                        if (dialog != null)
                            dialog.cancel();
                           getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                    }
                });


            }else if(userHasAuditRemind.equals("2")){
//                dialog.setBgImage(R.mipmap.personalrz); (R.mipmap.gofinsh
                dialog.setBgImage(R.mipmap.rzper);
                dialog.setBtnImage(R.mipmap.inow, new CertifiedPromptDialog.OnClickListener() {
                    @Override
                    public void onClick() {//做任务
//                       ToastTool.show(getActivity(),"去做任务");
                        if (dialog != null) {
                            dialog.cancel();
                        }

//                        startActivity(new Intent(getActivity(), TaskcenterActivity.class));
                    }
                });
            }

            dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
            dialog.show();
        }
    }

    /*用户成为分销商弹框提醒*/
    Dialog distributionDalog;
    TextView tv_serach_car,tv_zbrz,content_one,tv_decc;
    ImageView iv_close,iv_rz;
    private void isShowUserHasAgentRemind(String type){

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.distribution_dialog, null, false);
            distributionDalog=new Dialog(getActivity());
            distributionDalog.setContentView(view);
            distributionDalog.getWindow().setBackgroundDrawable(new BitmapDrawable());
            tv_serach_car= (TextView) view.findViewById(R.id.tv_serach_car);
            tv_serach_car.setOnClickListener(this);
            tv_zbrz= (TextView) view.findViewById(R.id.tv_zbrz);
            tv_zbrz.setOnClickListener(this);
            iv_rz= (ImageView) view.findViewById(R.id.iv_rz);
            content_one= (TextView) view.findViewById(R.id.content_one);
            tv_decc= (TextView) view.findViewById(R.id.tv_decc);
            iv_close = (ImageView) view.findViewById(R.id.iv_close);
            iv_close.setOnClickListener(this);

            if(type.equals("1")){//成功
                iv_rz.setImageResource(R.mipmap.tj_icon);
                content_one.setText("认证成功");
                tv_decc.setText("恭喜您，已认证成功");
                tv_serach_car.setText("去选车");
                tv_zbrz.setVisibility(View.GONE);
            }else if(type.equals("2")){
                iv_rz.setImageResource(R.mipmap.failure_icon);
                content_one.setText("认证失败");
                tv_decc.setText(isNull(homebean.getRemindDesc()));
                tv_serach_car.setText("重新认证");
                tv_zbrz.setVisibility(View.VISIBLE);
            }

            distributionDalog.show();


    }

    private String isNull(String tex) {
        if(!TextUtils.isEmpty(tex)){
            return tex;
        }else {
            return "--";
        }
    }


    //选中新车
    private void setNewCarChecked(){
        tv_new_car.setTextColor(getResources().getColor(R.color.btn_org1));
        view_new_car.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        tv_old_car.setTextColor(getResources().getColor(R.color.gray_3));
        view_old_car.setBackgroundColor(getResources().getColor(R.color.white));
        isNewCar = true;
        from=0;
        setData();
    }
    //选中二手车
    private void setOldCarChecked(){
        tv_new_car.setTextColor(getResources().getColor(R.color.gray_3));
        view_new_car.setBackgroundColor(getResources().getColor(R.color.white));
        tv_old_car.setTextColor(getResources().getColor(R.color.btn_org1));
        view_old_car.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        isNewCar = false;
        from=1;
        setData();
    }

    //选中明星车款新车
    private void setStarNewCarChecked(){
        tv_star_new_car.setTextColor(getResources().getColor(R.color.btn_org1));
        view_star_new_car.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        tv_star_old_car.setTextColor(getResources().getColor(R.color.gray_3));
        view_star_old_car.setBackgroundColor(getResources().getColor(R.color.white));
        isStarNewCar = true;
        starType = 0;
        setData();
    }
    //选中明星车款二手车
    private void setStarOldCarChecked(){
        tv_star_new_car.setTextColor(getResources().getColor(R.color.gray_3));
        view_star_new_car.setBackgroundColor(getResources().getColor(R.color.white));
        tv_star_old_car.setTextColor(getResources().getColor(R.color.btn_org1));
        view_star_old_car.setBackgroundColor(getResources().getColor(R.color.btn_org1));
        isStarNewCar = false;
        starType = 1;
        setData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_new_car://新车列表
                if (!isNewCar) {
                    setNewCarChecked();
                }
                break;
            case R.id.rl_old_car://二手车列表
                if (isNewCar) {
                    setOldCarChecked();
                }
                break;
            case R.id.tv_more_cars://查看更多车源
                if (isNewCar) {
                    ((NewMainActivity) getActivity()).setNavigationBackground(1, 0);
                }else {
                    ((NewMainActivity) getActivity()).setNavigationBackground(1, 1);
                }
                break;
            case R.id.tv_look_financial_detail://查看金融详情

                break;
            case R.id.ll_lixi://融资利息超低

                break;
            case R.id.ll_rongzi://融资金额超高

                break;
            case R.id.ll_changhuan://偿还方式多样

                break;
            case R.id.tv_car_finance://单车融资

                break;
            case R.id.tv_stock_finance://库存融资

                break;
            case R.id.tv_claim_finance://应收账款保理融资

                break;
            case R.id.tv_car_credit://车抵贷

                break;
            case R.id.new_car_module://新车

                ( (NewMainActivity)getActivity()).setNavigationBackground(1,0);

                break;
            case R.id.old_car_module://高端二手车

                ( (NewMainActivity)getActivity()).setNavigationBackground(1,1);

                break;
            case R.id.rl_star_new_car://明星车款 新车列表
                if (!isStarNewCar) {
                    setStarNewCarChecked();
                }
                break;
            case R.id.rl_star_old_car://明星车款 二手车列表
                if (isStarNewCar) {
                    setStarOldCarChecked();
                }
                break;
            case R.id.tv_sports_car:// 明星车款 查看更多车源
                startActivity(new Intent(mActivity, StartCarActivity.class).putExtra("isStarNewCar",isStarNewCar));
                break;
            case R.id.iv_close:
                if (distributionDalog!=null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();
                }
                break;
            case R.id.tv_serach_car:
                if (distributionDalog!=null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();}
                if(!TextUtils.isEmpty(homebean.getRemindType())&&homebean.getRemindType().equals("1")) {//认证成功
                          getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));}

                if(!TextUtils.isEmpty(homebean.getRemindType())&&homebean.getRemindType().equals("2")) {//认证失败
                    startActivity(new Intent(getActivity(), AuthenticateActivity.class));
                }
                break;

            case R.id.tv_zbrz:
                if (distributionDalog!=null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();
                }
                break;
        /*    case R.id.home_new_car:
            case R.id.home_second_hand:
                getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                if(distributionDalog!=null)
                    distributionDalog.cancel();
                break;*/
            case R.id.home_search_layout:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            case R.id.iv_customer:
                startActivity(new Intent(getActivity(), CustomerAct.class));
                break;

            case R.id.home_buy_module:
                getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                break;

            case R.id.home_sell_module:
                startActivity(new Intent(getActivity(), SellCarActivity.class));
                break;

            case R.id.home_seek_module:
                if(!SpUtils.getIsLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                else {
                    startActivity(new Intent(getActivity(), SeachCarServerActivity.class));
                }
                break;

            case R.id.home_detect_module:
                startActivity(new Intent(getActivity(),VehicleActivity.class));
                break;
            case R.id.home_msg:

            case R.id.tv_kefu:

            case R.id.ll_kefu:
//                if(!SpUtils.getIsLogin(getActivity()))
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                else {
                    startActivity(new Intent(getActivity(), CustomerAct.class));
//                }
//                    startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;

            case R.id.tv_adinfo:
                startActivity(new Intent(getActivity(), NewsconsultingActivity.class));
                break;
        }
    }


    @Override
    public void onRefresh() {
//        //刷新
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //结束后停止刷新
//                swiper.setRefreshing(false);
//            }
//        }, 3000);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                requestHomeData();
//                swiper.setRefreshing(false);
            }
        });
    }


    public void upDateUi() {
        clearGgLb();//清空广告轮播
        setData();
    }

    private void clearGgLb() {
        List<String>gglist=new ArrayList<>();
        gglist.clear();
        gglist.add("");
        gglist.add("");
        gglist.add("");
        gglist.add("");
        marqueeviewone.removeAllViews();//防止重叠
        marqueeviewone.setData(gglist);
    }

    private class MyBannerItem implements FlyBanner.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
//         ToastTool.show(getActivity(),"点击了banner的第"+position+"位置");
            if (!TextUtils.isEmpty(homebean.getBannerAList().get(position).getNoteurl())){
                startActivity(new Intent(context,NewInofActivity.class).putExtra(NewInofActivity.NOTE_URL,homebean.getBannerAList().get(position).getNoteurl()).putExtra("from","1"));
            }
        }
    }

    private class MyAdoneItem implements FlyBanner.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
//            ToastTool.show(getActivity(),"点击了第一个广告的第"+position+"位置");
            if (!TextUtils.isEmpty(homebean.getBannerBList().get(position).getNoteurl())){
                startActivity(new Intent(context,NewInofActivity.class).putExtra(NewInofActivity.NOTE_URL,homebean.getBannerBList().get(position).getNoteurl()).putExtra("from","1"));
            }
        }
    }

    private class MyAdtwoItem implements FlyBanner.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
//            ToastTool.show(getActivity(),"点击了第二个广告的第"+position+"位置");
            if (!TextUtils.isEmpty(homebean.getBannerCList().get(position).getNoteurl())){
                startActivity(new Intent(context,NewInofActivity.class).putExtra(NewInofActivity.NOTE_URL,homebean.getBannerCList().get(position).getNoteurl()).putExtra("from","1"));
            }
        }
    }
}
