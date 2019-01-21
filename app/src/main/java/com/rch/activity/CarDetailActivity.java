package com.rch.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loc.a;
import com.rch.R;
import com.rch.adatper.CarBannerAdatper;
import com.rch.base.BaseActivity;
import com.rch.base.MyApplication;
import com.rch.base.SecondBaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CalculationTool;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.SpUtils;
import com.rch.common.StartorSelectUtils;
import com.rch.common.StrSplitTool;
import com.rch.common.TimePareUtil;
import com.rch.common.TimeSplitTool;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.custom.CarImageDisplay;
import com.rch.custom.ItemLabelLayout;
import com.rch.custom.LoadingView;
import com.rch.custom.PromptDialog;
import com.rch.custom.ShareSuccessDialog;

import com.rch.entity.CarDetailEntity;
import com.rch.entity.CarEntity;
import com.rch.entity.DatailBean;
import com.rch.entity.DetailBrandEntity;
import com.rch.entity.ModelEntity;
import com.rch.entity.PicListBean;
import com.rch.entity.SupplierEntity;
import com.rch.entity.VehicleImageListEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.rch.view.ShareDialog;
import com.umeng.socialize.ShareAction;

import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhuge.analysis.stat.ZhugeSDK;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umeng.socialize.utils.ContextUtil.getContext;


/**
 * Created by Administrator on 2018/5/7.
 */

public class CarDetailActivity extends SecondBaseActivity implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.car_detail_title)
    private TextView tv_carname;
    @ViewInject(R.id.tv_carprice)
    private TextView tv_carprice;
    @ViewInject(R.id.car_detail_location)
    private TextView tv_location;
    @ViewInject(R.id.car_detail_land)
    private TextView tv_spd;

    @ViewInject(R.id.car_detail_factory_time)
    private TextView tv_cctime;//出厂日期
    @ViewInject(R.id.car_detail_land_time)
    private TextView tv_sptime;//上牌日期
    @ViewInject(R.id.car_detail_mileage)
    private TextView tv_bxlc;//表显里程
    @ViewInject(R.id.car_detail_emission)
    private TextView tv_pfbz;//排放标准
    @ViewInject(R.id.car_detail_transmission)
    private TextView tv_bsx;//变速箱
    @ViewInject(R.id.car_detail_amount)
    private TextView tv_pl;//排量

    @ViewInject(R.id.car_detail_vp)
    private ViewPager vp;
    @ViewInject(R.id.car_detail_index)
    private TextView tvIndex;
    @ViewInject(R.id.car_num)
    private TextView car_num;

    @ViewInject(R.id.rl_vp)
    private RelativeLayout rl_vp;
    @ViewInject(R.id.iv_sc)
    private ImageView iv_sc;
    @ViewInject(R.id.tv_sc)
    private TextView tv_sc;
    @ViewInject(R.id.ll_sc)
    private LinearLayout ll_sc;
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
    @ViewInject(R.id.ll_tab)
    private ItemLabelLayout ll_tab;
    @ViewInject(R.id.tv_dec)
    private TextView tv_dec;




    CarBannerAdatper adatper;
    List<PicListBean> vehicleImageList=new ArrayList<>();
    private String id;//车辆逐渐id
    private int screen_height;//图片控件高度


    String ifCollect="";//:"0",  是否收藏 0-否，1-是
    boolean isSave=false;//"0",  是否收藏false-否，true-是
    private DatailBean infobean;
    private Gson gson;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_car_detail);

    }

    @Override
    public void init(Bundle savedInstanceState) {
        gson=new Gson();
        screen_height= SpUtils.get_View_heigth(SpUtils.getScreenWith(CarDetailActivity.this),600,900);
        id=getIntent().getExtras().getString("id");
        ViewGroup.LayoutParams params = rl_vp.getLayoutParams();
        params.height = screen_height;
        rl_vp.setLayoutParams(params);

        setData();
    }


    private void setData(){
        RequestParam param = new RequestParam();
        param.add("id", id);
        OkHttpUtils.post(Config.GETVEHICLEDETAIL_URL, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    if (!result.isNull("ifCollect")) {
                        ifCollect = result.getString("ifCollect");
                        if (ifCollect.equals("0")) {
                            iv_sc.setImageResource(R.mipmap.sctwo);
                            tv_sc.setText("收藏");
                        } else {
                            iv_sc.setImageResource(R.mipmap.scsucess);
                            tv_sc.setText("已收藏");
                        }
                    }
                    JSONObject vehicleobject = result.getJSONObject("vehicle");
                    JSONArray picarray = result.getJSONArray("vehicleImageList");
                    infobean = gson.fromJson(vehicleobject.toString(), DatailBean.class);
                    vehicleImageList = gson.fromJson(picarray.toString(), new TypeToken<List<PicListBean>>() {
                    }.getType());

                    initData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(int code, String error) {
                if (code == 1017){
//                    load_view.loadNodata(R.mipmap.nocontent,"该车辆已下架");
                }else {
//                    load_view.loadError();
                    ToastTool.show(CarDetailActivity.this,error);
                }

            }
        });
    }
    String shareTitle="",shareImage="",shareDesc="",shareUrl="",agent="",auditState="",ifRemind="",orginid="";//是否分销商 0-否，1-是

    private void initData(){

        if (vehicleImageList != null && vehicleImageList.size() > 0) {//设置图片
            adatper = new CarBannerAdatper(this, vehicleImageList, infobean.getShelvesStatus());
            vp.setAdapter(adatper);
            vp.setCurrentItem(1);
            tvIndex.setText("1/"+vehicleImageList.size());
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

//        if (isSave) {
//            iv_sc.setImageResource(R.mipmap.sctwo);
//            tv_sc.setText("收藏");
//        } else {
//            iv_sc.setImageResource(R.mipmap.scsucess);
//            tv_sc.setText("已收藏");
//        }

        car_num.setText(isNull("车源号:"+infobean.getSourcecode()));

//        if(!TextUtils.isEmpty(infobean.getIsSelected())&&infobean.getIsSelected().equals("1")){
//            SpannableStringBuilder spannableString = new SpannableStringBuilder();
//            spannableString.append("占位");
//            spannableString.append(" ");
//            spannableString.append(isNull(infobean.getModelName()));
//            ImageSpan imageSpan = new ImageSpan(mContext, R.mipmap.jxcar);
//            //        也可以这样
//            //        Drawable drawable = context.getResources().getDrawable(R.mipmap.jxcar);
//            //        drawable.setBounds(0, 0, GeneralUtils.dip2px(context,28), GeneralUtils.dip2px(context,15));
//            //        ImageSpan imageSpan1 = new ImageSpan(drawable);
//            //        将index为6、7的字符用图片替代
//            spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            tv_carname.setText(spannableString);
//        }else {
//            tv_carname.setText(isNull(infobean.getModelName()));
//        }
        tv_carname.setText(isNull(infobean.getModelName()));

        if(StartorSelectUtils.isBoolean(infobean.getIsRecommend())||StartorSelectUtils.isBoolean(infobean.getIfStar())||StartorSelectUtils.isBoolean(infobean.getIsSelected())) {
            ll_tab.setVisibility(View.VISIBLE);
            ll_tab.initData(StartorSelectUtils.nullOrone(infobean.getIsRecommend()), StartorSelectUtils.nullOrone(infobean.getIfStar()), StartorSelectUtils.nullOrone(infobean.getIsSelected()));
        }

        shareTitle=isNull(infobean.getModelName());//分享的标题

        if(!TextUtils.isEmpty(infobean.getSalesPriceMinView()) &&!TextUtils.isEmpty(infobean.getSalesPriceMaxView())){
            if(infobean.getSalesPriceMinView().equals(infobean.getSalesPriceMaxView())){
                tv_carprice.setText(isNull(infobean.getSalesPriceMaxView())+"万");
                shareDesc="门店价:"+isNull(infobean.getSalesPriceMaxView())+"万";
            }else {
                tv_carprice.setText(isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万");
                shareDesc="门店价:"+isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万";
            }
        }else {
            tv_carprice.setText(isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万");
            shareDesc="门店价:"+isNull(infobean.getSalesPriceMinView()) + "万"+"-"+isNull(infobean.getSalesPriceMaxView())+"万";
        }


        if(TextUtils.isEmpty(infobean.getProvinceName())&&TextUtils.isEmpty(infobean.getCityName())){
            tv_location.setText(isNull(infobean.getProvinceName()));
        }else {
            if(!TextUtils.isEmpty(infobean.getProvinceName())&&!TextUtils.isEmpty(infobean.getCityName())){
                if(infobean.getProvinceName().equals(infobean.getCityName())) {
                    tv_location.setText(infobean.getProvinceName());
                }else {
                    tv_location.setText(infobean.getProvinceName() + infobean.getCityName());
                }
            }else {
                tv_location.setText(isNull(infobean.getProvinceName()) + isNull(infobean.getCityName()));
            }

        }



        if(TextUtils.isEmpty(infobean.getRegistrationProvName())&&TextUtils.isEmpty(infobean.getRegistrationCityName())){
            tv_spd.setText(isNull(infobean.getRegistrationProvName()));
        }else {
            if(!TextUtils.isEmpty(infobean.getRegistrationProvName())&&!TextUtils.isEmpty(infobean.getRegistrationCityName())){
                if(infobean.getRegistrationProvName().equals(infobean.getRegistrationCityName())) {
                    tv_spd.setText(infobean.getRegistrationCityName());
                }else {
                    tv_spd.setText(infobean.getRegistrationProvName() + infobean.getRegistrationCityName());
                }
            }else {
                tv_spd.setText(isNull(infobean.getRegistrationProvName()) + isNull(infobean.getRegistrationCityName()));
            }
        }


        if(!TextUtils.isEmpty(infobean.getProductionTime())){//出厂日期
            Date date= TimePareUtil.getDateTimeForTime("yyyy-MM",infobean.getProductionTime());
            String time=TimePareUtil.getTimeForDate("yyyy-MM",date);
//            tv_ccrq.setText(resultCarEntity.getProductionTime());
            tv_cctime.setText(time);
        }else {
            tv_cctime.setText("--");
        }


        if(!TextUtils.isEmpty(infobean.getRegistrationTime())){//上牌时间
            Date date= TimePareUtil.getDateTimeForTime("yyyy-MM",infobean.getRegistrationTime());
            String time=TimePareUtil.getTimeForDate("yyyy-MM",date);
//            tv_ccspsj.setText(resultCarEntity.getRegistrationTime());
            tv_sptime.setText(time);
        }else {
            tv_sptime.setText("--");
        }

        String lime="0公里";
        int iShowMileage=infobean.getShowMileage().isEmpty()?0: Integer.parseInt(infobean.getShowMileage());//由CarEntity改成
        if(iShowMileage<10000) {
            lime = String.valueOf(iShowMileage) + "公里";
        }
        else {
            lime = StrSplitTool.retainOneNumber(String.valueOf(iShowMileage)) + "万公里";
        }
        tv_bxlc.setText(isNull(lime));
        tv_dec.setText(tv_sptime.getText()+"/"+tv_bxlc.getText());//二手车这列是后面加的
        tv_pfbz.setText(isNull(infobean.getEmissionStandardName()));
        tv_bsx.setText(isNull(infobean.getGearboxTypeName()));
        tv_pl.setText(isNull(infobean.getStandardDelivery())+isNull(infobean.getDeliveryUnit()));

    }



    @OnClick({R.id.car_detail_more, R.id.ll_sc, R.id.ll_xdj, R.id.car_detail_store_share,
            R.id.car_detail_reserve, R.id.car_detail_reserve,R.id.car_detail_back,R.id.car_detail_advisory})
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.car_detail_store_share://分享
              if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_store_share)) {
//            case R.id.car_detail_share:
                  if (!isLogin()) {
                      startActivity(new Intent(this, LoginActivity.class));
                  } else {
                      httpShareLog("2");
                      tv_share.setEnabled(false);
                  }
              }
                break;
            case R.id.car_detail_more:
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_more)) {
                    if (infobean != null) {
                        startActivity(new Intent(this, CarMoreInfoActivity.class).putExtra("infobean", infobean).putExtra("type", 1));
                    }
                }
                break;
            case R.id.car_detail_advisory:
                if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_advisory)) {
//                callPhone();
//                if(!SpUtils.getIsLogin(mContext))
//                    startActivity(new Intent(mContext, LoginActivity.class));
//                else {
                    startActivity(new Intent(mContext, CustomerAct.class));
//                }
                }
                break;
            case R.id.car_detail_reserve://预约看车
                 if(!ButtonUtils.isFastDoubleClick(R.id.car_detail_reserve)) {
//                if(!isLogin()) {
//                    startActivity(new Intent(this, PreOrderActivity.class).
//                            putExtra("carId", id).putExtra("name",infobean.getVehicleFullName()));
//                }
//                else {
//                    startActivity(new Intent(this, PreOrderActivity.class).
//                            putExtra("carId", id).putExtra("name",infobean.getVehicleFullName()));
//                }
                     startActivity(new Intent(this, PreOrderActivity.class).
                             putExtra("carId", id).putExtra("name", infobean.getModelName()));
                 }
                break;
            case R.id.car_detail_back:
                finish();
                break;

            case R.id.ll_sc:
                if(!isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    goCollotion();//收藏和取消收藏
                    ll_sc.setEnabled(false);
                }
                break;
            case R.id.ll_xdj:
                if(!ButtonUtils.isFastDoubleClick(R.id.ll_xdj)) {
//                if(SpUtils.getIsLogin(mContext)){
//                    goSerchDj();
//                }else {
//                    startActivity(new Intent(CarDetailActivity.this, FindloweAct.class).putExtra("id",infobean.getId()));
//                }
                    startActivity(new Intent(CarDetailActivity.this, FindloweAct.class).putExtra("id", infobean.getId()));
                }
                break;
        }
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
            }
        });

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
                ToastTool.show(CarDetailActivity.this,error);
            }
        });
    }



    final int REQUESTCODE=1012;
    PromptDialog dialog;
    String number="";
    private void callPhone(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasPhonePermission =  checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasPhonePermission!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQUESTCODE);
                return;
            }
        }


//        number=String.valueOf(lAdvisory.getTag());
        dialog =new PromptDialog(this);
        dialog.setText(number);
        dialog.setLeftButtonText("取消", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        dialog.setRightButtonText("呼叫", new PromptDialog.OnClickListener() {
            @Override
            public void onClick() {
                // 拨号：激活系统的拨号组件
                Intent intent = new Intent(); // 意图对象：动作 + 数据
                intent.setAction(Intent.ACTION_CALL); // 设置动作
                Uri data = Uri.parse("tel:" + number); // 设置数据
                intent.setData(data);
                startActivity(intent); // 激活Activity组件
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("position",String.valueOf(position));
        if(position==0||position==vp.getAdapter().getCount() - 2)//右滑动
            tvIndex.setText(String.valueOf(vehicleImageList.size())+"/"+vehicleImageList.size());
        else if(position==1||position==vp.getAdapter().getCount()-1)//左滑动
            tvIndex.setText("1/"+vehicleImageList.size());
        else
            tvIndex.setText(String.valueOf(position%vehicleImageList.size())+"/"+vehicleImageList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state){
            case ViewPager.SCROLL_STATE_IDLE:
                if(vp.getCurrentItem()==0) {
                    vp.setCurrentItem(vp.getAdapter().getCount() - 2,false);
                }
                else if(vp.getCurrentItem()==vp.getAdapter().getCount()-1) {
                    vp.setCurrentItem(1,false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTCODE:
                if(!permissions[0].equals(Manifest.permission.CALL_PHONE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(this,"请先授权电话权限！");
                    return;
                }
                callPhone();
                break;
        }
    }

    UMWeb web,web1;
    UMImage image;
    private void sharePanle()
    {
          if(vehicleImageList!=null&&vehicleImageList.size()>0){
              shareImage=vehicleImageList.get(0).getVehicleImage();
          }
          if(!TextUtils.isEmpty(shareImage)) {
              image = new UMImage(CarDetailActivity.this, shareImage);
          }else {
               image = new UMImage(CarDetailActivity.this, R.mipmap.shareicon);//资源文件
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

        ShareDialog dialog=new ShareDialog(CarDetailActivity.this).build();
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
                new ShareAction(CarDetailActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        dialog.setWeixinPyq(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(CarDetailActivity.this)
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
            ZhuGeIOTool.buyMonitor(CarDetailActivity.this,"分享",map);

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
                ToastTool.show(CarDetailActivity.this,"您未安装微信APP,请先安装");
            }else {
                ToastTool.show(CarDetailActivity.this, t.getMessage());
            }
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastTool.show(CarDetailActivity.this,"取消！");
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


    private String isNull(String registrationArea) {
        if(!TextUtils.isEmpty(registrationArea)){
            return registrationArea;
        }else {
            return "--";
        }
    }


}
