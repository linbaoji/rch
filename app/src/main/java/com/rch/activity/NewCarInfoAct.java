package com.rch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.adatper.CarBannerAdatper;
import com.rch.base.SecondBaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.custom.ItemLabelLayout;
import com.rch.entity.DatailBean;
import com.rch.entity.PicListBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.rch.view.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从首页和选车进来的新车详情
 * Created by Administrator on 2018/10/12.
 */

public class NewCarInfoAct extends SecondBaseActivity implements ViewPager.OnPageChangeListener {
    //1.2.3新增字段
    @ViewInject(R.id.tv_brand)
    private TextView tv_brand;//品牌
    @ViewInject(R.id.tv_series)
    private TextView tv_series;//车系
    @ViewInject(R.id.tv_body_color)
    private TextView tv_body_color;//车身颜色
    @ViewInject(R.id.tv_interior_color)
    private TextView tv_interior_color;//内饰颜色
    @ViewInject(R.id.tv_config)
    private TextView tv_config;//配置说明
    @ViewInject(R.id.ll_configuration)
    private LinearLayout ll_configuration;//配置说明

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.car_detail_back)
    private ImageView car_detail_back;
    @ViewInject(R.id.tv_carName)
    private TextView tv_carName;//title车辆名称
    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;
    @ViewInject(R.id.iv_msg)
    private ImageView ivMsg;
    @ViewInject(R.id.tv_kefu)
    private TextView tv_kefu;
    @ViewInject(R.id.sc_view)
    private NestedScrollView sc_view;
    @ViewInject(R.id.car_detail_vp)
    private ViewPager vp;
    @ViewInject(R.id.car_num)
    private TextView car_num;
    @ViewInject(R.id.car_detail_index)
    private TextView tvIndex;
    @ViewInject(R.id.car_detail_title)
    private TextView tv_carname;
    @ViewInject(R.id.tv_dec)
    private TextView tv_dec;
    @ViewInject(R.id.tv_price)
    private TextView tv_price;

    @ViewInject(R.id.tv_cg)
    private TextView tv_cg;
    @ViewInject(R.id.tv_nk)
    private TextView tv_nk;
    @ViewInject(R.id.tv_pl)
    private TextView tv_pl;
    @ViewInject(R.id.tv_pfbz)
    private TextView tv_pfbz;
    @ViewInject(R.id.tv_bsxlx)
    private TextView tv_bsxlx;
    @ViewInject(R.id.tv_qdfs)
    private TextView tv_qdfs;
    @ViewInject(R.id.tv_zws)
    private TextView tv_zws;
    @ViewInject(R.id.tv_zzslx)
    private TextView tv_zzslx;


    @ViewInject(R.id.rl_vp)
    private RelativeLayout rl_vp;
    @ViewInject(R.id.iv_sc)
    private ImageView iv_sc;
    @ViewInject(R.id.tv_sc)
    private TextView tv_sc;
    @ViewInject(R.id.ll_xdj)
    private LinearLayout ll_xdj;
    @ViewInject(R.id.tv_xdj)
    private TextView tv_xdj;
    @ViewInject(R.id.iv_xdj)
    private ImageView iv_xdj;
    @ViewInject(R.id.car_detail_store_share)
    private TextView tv_share;
    @ViewInject(R.id.car_detail_reserve)
    private TextView tv_yykc;
    @ViewInject(R.id.tv_xj)
    private TextView tv_xj;
    @ViewInject(R.id.ll_sc)
    private LinearLayout ll_sc;
    @ViewInject(R.id.tv_city)
    private TextView tv_city;
    @ViewInject(R.id.ll_tab)
    private ItemLabelLayout ll_tab;

    private int screen_height;

    private DatailBean infobean;//车辆详情
    private List<PicListBean> vehicleimagelist;//车辆图片

    CarBannerAdatper adatper;
    private Gson gson;
    private boolean isSave = false;//默认没有收藏
    private String ifCollect;//是否收藏 是否收藏 0-否，1-是（用户登录时返回）
    private String id;



    @Override
    public void setLayout() {
        setContentView(R.layout.activity_newcarinfo);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        screen_height = SpUtils.get_View_heigth(SpUtils.getScreenWith(NewCarInfoAct.this), 600, 900);
        gson = new Gson();
        ViewGroup.LayoutParams params = rl_vp.getLayoutParams();
        params.height = screen_height;
        rl_vp.setLayoutParams(params);

        id=getIntent().getExtras().getString("id");
        initListeners();
        scrollChange();
        getDate();
    }

    private int imageHeight;

    private void initListeners() {
        // 获取顶部图片高度
        ViewTreeObserver vto = vp.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imageHeight = vp.getHeight() - 150;
                Log.e("===", imageHeight + "]]]");
            }
        });
    }

    private void scrollChange() {
        sc_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    toolbar.setBackgroundResource(R.mipmap.detail_shadow);;//AGB由相关工具获得，或者美工提供
                    car_detail_back.setImageResource(R.mipmap.back_white);
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    tv_carName.setVisibility(View.GONE);
                } else if (scrollY > 0 && scrollY <= imageHeight) {
                    float scale = (float) scrollY / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    toolbar.setBackgroundResource(R.mipmap.detail_shadow);
                    car_detail_back.setImageResource(R.mipmap.back_white);
                    ivMsg.setImageResource(R.mipmap.kefubai);
                    tv_kefu.setTextColor(getResources().getColor(R.color.white));
                    tv_carName.setVisibility(View.GONE);
                } else {
                    toolbar.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    car_detail_back.setImageResource(R.mipmap.back);
                    ivMsg.setImageResource(R.mipmap.kefuhei);
                    tv_kefu.setTextColor(getResources().getColor(R.color.kf_info));
                    tv_carName.setVisibility(View.VISIBLE);
                    tv_carName.setText("车辆详情");
                }


            }
        });
    }

    private void getDate() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.GETVEHICLEDETAIL_URL, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    if (!result.isNull("ifCollect")) {
                        ifCollect = result.getString("ifCollect");
                        if (ifCollect.equals("0")) {
//                            isSave = false;
                            iv_sc.setImageResource(R.mipmap.sctwo);
                            tv_sc.setText("收藏");
                        } else {
//                            isSave = true;
                            iv_sc.setImageResource(R.mipmap.scsucess);
                            tv_sc.setText("已收藏");
                        }
                    }
                    JSONObject vehicleobject = result.getJSONObject("vehicle");
                    JSONArray picarray = result.getJSONArray("vehicleImageList");
                    infobean = gson.fromJson(vehicleobject.toString(), DatailBean.class);
                    vehicleimagelist = gson.fromJson(picarray.toString(), new TypeToken<List<PicListBean>>() {
                    }.getType());
                    setUi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
            }
        });
    }

    private void setUi() {
        if (infobean != null) {
            if (vehicleimagelist != null && vehicleimagelist.size() > 0) {//设置图片
                adatper = new CarBannerAdatper(this, vehicleimagelist, infobean.getShelvesStatus());
                vp.setAdapter(adatper);
                vp.setCurrentItem(1);
                tvIndex.setText("1/"+vehicleimagelist.size());
                vp.setOnPageChangeListener(this);
            }

            if (infobean.getShelvesStatus().equals("2") || infobean.getShelvesStatus().equals("3")) {//下架售罄
                if (infobean.getShelvesStatus().equals("2")) {
                    tv_xj.setText("车辆已经下架了~");
                } else {
                    tv_xj.setText("车辆已经售罄了~");
                }
                tv_xj.setVisibility(View.VISIBLE);
                ll_xdj.setEnabled(false);
                tv_share.setEnabled(false);
                tv_yykc.setEnabled(false);
                tv_share.setTextColor(Color.parseColor("#33ffffff"));
                tv_yykc.setTextColor(Color.parseColor("#33ffffff"));
                tv_xdj.setTextColor(Color.parseColor("#dbdbdb"));
                iv_xdj.setImageResource(R.mipmap.noxundijia);
            } else {
                tv_xj.setVisibility(View.GONE);
                ll_xdj.setEnabled(true);
                tv_share.setEnabled(true);
                tv_yykc.setEnabled(true);
                tv_share.setTextColor(getResources().getColor(R.color.white));
                tv_yykc.setTextColor(getResources().getColor(R.color.white));
                tv_xdj.setTextColor(Color.parseColor("#444444"));
                iv_xdj.setImageResource(R.mipmap.xundijia);
            }

//            if (isSave) {
//                iv_sc.setImageResource(R.mipmap.sctwo);
//                tv_sc.setText("收藏");
//            } else {
//                iv_sc.setImageResource(R.mipmap.scsucess);
//                tv_sc.setText("已收藏");
//            }


            car_num.setText("车源号:" + isNull(infobean.getSourcecode()));

//            if (!TextUtils.isEmpty(infobean.getIsSelected()) && infobean.getIsSelected().equals("1")) {
//                SpannableStringBuilder spannableString = new SpannableStringBuilder();
//                spannableString.append("占位");
//                spannableString.append(" ");
//                spannableString.append(isNull(infobean.getModelName()));
//                ImageSpan imageSpan = new ImageSpan(mContext, R.mipmap.jxcar);
//                //        也可以这样
//                //        Drawable drawable = context.getResources().getDrawable(R.mipmap.jxcar);
//                //        drawable.setBounds(0, 0, GeneralUtils.dip2px(context,28), GeneralUtils.dip2px(context,15));
//                //        ImageSpan imageSpan1 = new ImageSpan(drawable);
//                //        将index为6、7的字符用图片替代
//                spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tv_carname.setText(spannableString);
//            } else {
//                tv_carname.setText(isNull(infobean.getModelName()));
//            }

            tv_carname.setText(isNull(infobean.getModelName()));

            if(StartorSelectUtils.isBoolean(infobean.getIsRecommend())||StartorSelectUtils.isBoolean(infobean.getIfStar())||StartorSelectUtils.isBoolean(infobean.getIsSelected())) {
                ll_tab.setVisibility(View.VISIBLE);
                ll_tab.initData(StartorSelectUtils.nullOrone(infobean.getIsRecommend()), StartorSelectUtils.nullOrone(infobean.getIfStar()), StartorSelectUtils.nullOrone(infobean.getIsSelected()));
            }

            shareTitle=isNull(infobean.getModelName());//分享的标题

            tv_dec.setText(isNull(infobean.getVehicleStandardName() + "/") +
                    isNull(infobean.getStandardDelivery() +
                            isNull(infobean.getDeliveryUnit()) + "/"
                            + isNull(infobean.getDriverType())));

            if (!TextUtils.isEmpty(infobean.getSalesPriceMinView()) && !TextUtils.isEmpty(infobean.getSalesPriceMaxView())) {
                if (infobean.getSalesPriceMinView().equals(infobean.getSalesPriceMaxView())) {
                    tv_price.setText(isNull(infobean.getSalesPriceMinView()) + "万");
                    shareDesc="门店价:"+isNull(infobean.getSalesPriceMaxView())+"万";

                } else {
                    tv_price.setText(isNull(infobean.getSalesPriceMinView())  + "万"+"-"+isNull(infobean.getSalesPriceMaxView()) + "万");
                    shareDesc="门店价:"+isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万";

                }
            } else {
                tv_price.setText(isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView()) + "万");
                shareDesc="门店价:"+isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万";

            }

            if(TextUtils.isEmpty(infobean.getProvinceName())&&TextUtils.isEmpty(infobean.getCityName())){
                tv_city.setText(isNull(infobean.getProvinceName()));
            }else {
                if(!TextUtils.isEmpty(infobean.getProvinceName())&&!TextUtils.isEmpty(infobean.getCityName())){
                    if(infobean.getProvinceName().equals(infobean.getCityName())) {
                        tv_city.setText(infobean.getProvinceName());
                    }else {
                        tv_city.setText(infobean.getProvinceName() +" "+infobean.getCityName());
                    }
                }else {
                    tv_city.setText(isNull(infobean.getProvinceName()) +" "+isNull(infobean.getCityName()));
                }

            }


            tv_cg.setText(isNull(infobean.getVehicleStandardName()));
            tv_nk.setText(isNull(infobean.getModelYear()));
            tv_pl.setText(isNull(infobean.getStandardDelivery() + isNull(infobean.getDeliveryUnit())));
            tv_pfbz.setText(isNull(infobean.getEmissionStandardName()));
            tv_bsxlx.setText(isNull(infobean.getGearboxTypeName()));
            tv_qdfs.setText(isNull(infobean.getDriverType()));
            tv_zws.setText(isNull(infobean.getCarrierNumber()));
            tv_zzslx.setText(isNull(infobean.getMakerType()));

            if (!TextUtils.isEmpty(infobean.getModified())) {
                ll_configuration.setVisibility(View.VISIBLE);
                tv_config.setText(isNull(infobean.getModified()));
            }else {
                ll_configuration.setVisibility(View.GONE);
            }
            tv_brand.setText(isNull(infobean.getBrandName()));
            tv_series.setText(isNull(infobean.getSeriesName()));
//            if (!TextUtils.isEmpty(infobean.getVehicleColor())) {
//                tv_body_color.setBackgroundResource(R.drawable.money_frame);
//                if (infobean.getVehicleColor().length()>18)
//                    tv_body_color.setText(infobean.getVehicleColor().substring(0,17)+"...");
//                else
//                    tv_body_color.setText(isNull(infobean.getVehicleColor()));
//            }else {
//                tv_body_color.setText(isNull(infobean.getVehicleColor()));
//            }
            tv_body_color.setText(isNull(infobean.getVehicleColor()));

//            if (!TextUtils.isEmpty(infobean.getInsideColor())){
//                tv_interior_color.setBackgroundResource(R.drawable.money_frame);
//                if (infobean.getInsideColor().length()>18)
//                    tv_interior_color.setText(infobean.getInsideColor().substring(0,17)+"...");
//                else
//                    tv_interior_color.setText(isNull(infobean.getInsideColor()));
//            }else {
//                tv_interior_color.setText(isNull(infobean.getInsideColor()));
//            }
            tv_interior_color.setText(isNull(infobean.getInsideColor()));
        }

    }




    @OnClick({R.id.car_detail_more, R.id.ll_sc, R.id.ll_xdj, R.id.car_detail_store_share, R.id.car_detail_reserve,R.id.ll_kefu,R.id.car_detail_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.car_detail_more://跳h5的新车详情界面
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more))
                startActivity(new Intent(mContext,MorNewCarActivity.class).putExtra("id",infobean.getId()));
                break;
            case R.id.ll_sc://收藏
                if(!isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    goCollotion();//收藏和取消收藏
                    ll_sc.setEnabled(false);
                }
                break;

            case R.id.ll_xdj:
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more)) {
//                if(SpUtils.getIsLogin(mContext)){
//                    goSerchDj();
//                }else {
//                    startActivity(new Intent(mContext, FindloweAct.class).putExtra("id",infobean.getId()));
//                }
                    startActivity(new Intent(mContext, FindloweAct.class).putExtra("id", infobean.getId()));
                }
                break;

            case R.id.car_detail_store_share://分享
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more)) {
                    if (!isLogin()) {
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        httpShareLog("2");
                        tv_share.setEnabled(false);
                    }
                }
                break;

            case R.id.car_detail_reserve://预约看车
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more)) {
                    if (!isLogin()) {
                        startActivity(new Intent(this, PreOrderActivity.class).
                                putExtra("carId", id).putExtra("name", infobean.getModelName()));
                    } else {
                        startActivity(new Intent(this, PreOrderActivity.class).
                                putExtra("carId", id).putExtra("name", infobean.getModelName()));
                    }
                }
                break;
            case R.id.car_detail_back:
                finish();
                break;
            case R.id.ll_kefu:
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more))
//                if(!SpUtils.getIsLogin(mContext))
//                    startActivity(new Intent(mContext, LoginActivity.class));
//                else {
                    startActivity(new Intent(mContext, CustomerAct.class));
//                }
                break;
        }
    }

    /*生成分享日志*/
    private void httpShareLog(final String type)
    {
        RequestParam param = new RequestParam();
        param.add("id", id);
        param.add("optType",type);//日志类型  1-浏览，2-分享，3-收藏
        param.add("version",infobean.getVersion());
        OkHttpUtils.post(Config.SHARELOG, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                tv_share.setEnabled(true);
                shareUrl = JsonTool.getResultStr(data, "shareUrl");
                orginid = JsonTool.getResultStr(data, "orginid");
//                    ifRemind = JsonTool.getResultStr(data, "ifRemind") + "";
                sharePanle();

            }

            @Override
            public void onError(int code, String error) {
                tv_share.setEnabled(true);
                ToastTool.show(mContext,error);
            }
        });
    }

    private void goCollotion() {
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.COLLECTION_URL, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                ll_sc.setEnabled(true);
                try {
                    JSONObject object=new JSONObject(data.toString());
                    String result=object.getString("result");
                    if(result.equals("0")){//取消收藏成功
                        tv_sc.setText("收藏");
                        iv_sc.setImageResource(R.mipmap.sctwo);
                        isSave=false;
                    }else {//收藏成功
                        iv_sc.setImageResource(R.mipmap.scsucess);
                        tv_sc.setText("已收藏");
                        isSave=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ll_sc.setEnabled(true);
                ToastTool.show(mContext,error);
            }
        });

    }

    private void goSerchDj() {
        RequestParam param = new RequestParam();
        param.add("id",id);
        OkHttpUtils.post(Config.SUBMITVEHICLEINQUIRY_URL, param, new OkHttpCallBack(mContext,"加载中") {
            @Override
            public void onSuccess(String data) {
                startActivity(new Intent(mContext,FinancialSubmitSuccessActivity.class));
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("position", String.valueOf(position));
        if (position == 0 || position == vp.getAdapter().getCount() - 2)//右滑动
            tvIndex.setText(String.valueOf(vehicleimagelist.size()) + "/" + vehicleimagelist.size());
        else if (position == 1 || position == vp.getAdapter().getCount() - 1)//左滑动
            tvIndex.setText("1/" + vehicleimagelist.size());
        else
            tvIndex.setText(String.valueOf(position % vehicleimagelist.size()) + "/" + vehicleimagelist.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if (vp.getCurrentItem() == 0) {
                    vp.setCurrentItem(vp.getAdapter().getCount() - 2, false);
                } else if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                    vp.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }


    UMWeb web,web1;
    UMImage image;
    String shareUrl="",orginid="",shareImage,shareTitle,shareDesc;
    private void sharePanle()
    {
        if(vehicleimagelist!=null&&vehicleimagelist.size()>0){
            shareImage=vehicleimagelist.get(0).getVehicleImage();
        }
        if(!TextUtils.isEmpty(shareImage)) {
            image = new UMImage(NewCarInfoAct.this, shareImage);
        }else {
            image = new UMImage(NewCarInfoAct.this, R.mipmap.shareicon);//资源文件
        }
        image.compressStyle=UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(shareDesc);//描述

        String pyqtitle=shareTitle+" "+shareDesc;
        web1 = new UMWeb(shareUrl);
        web1.setTitle(pyqtitle);
        web1.setThumb(image);  //缩略图
        ShareDialog dialog=new ShareDialog(mContext).build();
        String type = "";

        if(getUserInfo()!=null&&!TextUtils.isEmpty(getUserInfo().getUserRoleType())){
            if(getUserInfo().getUserRoleType().equals("101")){
//              type="自营车商管理员";
                type="车商";
            }else if(getUserInfo().getUserRoleType().equals("102")){
//                type="自营车商员工";
                type="车商";
            } else if(getUserInfo().getUserRoleType().equals("201")){
//                type="全网车商管理员";
                type="车商";
            }else if(getUserInfo().getUserRoleType().equals("202")){
//               type="全网车商员工";
                type="车商";
            }else if(getUserInfo().getUserRoleType().equals("301")){
//               type="企业分销商管理员";
                type="分销商";
            }else if(getUserInfo().getUserRoleType().equals("302")){
//               type="企业分销商员工";
                type="分销商";
            }else if(getUserInfo().getUserRoleType().equals("401")){
//               type="个人分销商";
                type="分销商";
            }else if(getUserInfo().getUserRoleType().equals("501")){
//               type="普通用户";
                type="用户";
            }
        }else {
            type="--";
        }
        dialog.setName(isNull(getUserInfo().getShowName())+"("+type+")");
        dialog.setWeixin(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(NewCarInfoAct.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        dialog.setWeixinPyq(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(NewCarInfoAct.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener)
                        .withMedia(web1)
                        .share();
            }
        });
        dialog.show();
    }

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

            Map<String,String> map=new HashMap<>();
            map.put("用户手机",getUserInfo().getMobile());
            map.put("用户名称",getUserInfo().getUserName());
            map.put("商品名称",shareTitle);
            ZhuGeIOTool.buyMonitor(mContext,"分享",map);

            showDialogSu();
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t.getMessage().contains("没有安装应用")){
                ToastTool.show(mContext,"您未安装微信APP,请先安装");
            }else {
                ToastTool.show(mContext, t.getMessage());
            }
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastTool.show(mContext,"取消！");
        }
    };

    private void showDialogSu() {

//        final ShareSuccessDialog sharedialog = new ShareSuccessDialog(CarDetailActivity.this);
//        sharedialog.setCanceledOnTouchOutside(false);
//        if (!TextUtils.isEmpty(ifRemind)) {
//            if (ifRemind.equals("1")) {//显示
////                sharedialog.setBagRes(R.mipmap.jxssucess);原来是这个后来改了需求
//                sharedialog.setBagRes(R.mipmap.sharesucess);
//            } else if (ifRemind.equals("0"))//不显示
//            {//是分销商
//                sharedialog.setBagRes(R.mipmap.sharesucess);
//            }
//        }
//        sharedialog.getWindow().setBackgroundDrawable(new BitmapDrawable());//
//        sharedialog.setDeleatClick(new ShareSuccessDialog.OnClickListener() {
//            @Override
//            public void onClick() {
//                sharedialog.dismiss();
////                dex=0;//微信
//            }
//        });
//        sharedialog.setContinuClick(new ShareSuccessDialog.OnClickListener() {
//            @Override
//            public void onClick() {
//                sharedialog.dismiss();
////                dex=0;//微信
//                sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
//                finish();
//            }
//        });
//        sharedialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存泄漏解决方案
        UMShareAPI.get(this).release();
        ZhugeSDK.getInstance().flush(getApplicationContext());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    private String isNull(String text) {
        if (TextUtils.isEmpty(text)) {
            return "--";
        } else {
            return text;
        }
    }

}

