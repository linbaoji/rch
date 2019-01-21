package com.rch.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.NewMainActivity;
import com.rch.R;
import com.rch.activity.AuthenticateActivity;
import com.rch.activity.BookingAct;
import com.rch.activity.BookingorderAct;
import com.rch.activity.CarDealerActivity;
import com.rch.activity.CarHistoryActivity;
import com.rch.activity.CarManagerNewActivity;
import com.rch.activity.CollectionAct;
import com.rch.activity.CorporateInfoActivity;
import com.rch.activity.CustomerManagementActivity;
import com.rch.activity.DistributionActivity;
import com.rch.activity.EmployeesAct;
import com.rch.activity.FeedbackActivity;
import com.rch.activity.LoginActivity;
import com.rch.activity.MyCollectionAct;
import com.rch.activity.MyInfoActivity;
import com.rch.activity.NewBrandAct;
import com.rch.activity.ReleaseAct;
import com.rch.activity.SeachCarServerActivity;
import com.rch.activity.SetActivity;
import com.rch.activity.SigninAct;
import com.rch.activity.SoldcarAct;
import com.rch.activity.VehicleActivity;
import com.rch.activity.VehicleMnageAct;
import com.rch.activity.VerifyFailureActivity;
import com.rch.base.BaseFrag;
import com.rch.base.BaseFragment;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GeneralUtils;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.CertifiedPromptDialog;
import com.rch.custom.CommonView;
import com.rch.custom.PromptDialog;
import com.rch.entity.UserInfoEntity;
import com.rch.entity.UserOrderEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/4/13.
 */
public class MyFragment extends BaseFrag {
    @ViewInject(R.id.view_unlogin)
    private View view_unlogin;
    @ViewInject(R.id.iv_nologinset)
    private ImageView iv_nologinset;
    @ViewInject(R.id.ll_unlogin)
    private LinearLayout ll_unlogin;
    @ViewInject(R.id.tv_name)
    private TextView tv_personname;
    @ViewInject(R.id.tv_phone)
    private TextView tvPersonalPhone;
    @ViewInject(R.id.iv_sex)
    private ImageView iv_personsex;
    @ViewInject(R.id.my_fragment_authen)
    private ImageView iv_personstute;
    @ViewInject(R.id.iv_sqd)
    private ImageView iv_perqd;
    @ViewInject(R.id.tv_rcglr)
    private TextView tv_rcglr;
    @ViewInject(R.id.cv_gocerti)
    private CommonView cv_gocerti;
    @ViewInject(R.id.iv_identify)
    private ImageView iv_identify;

    @ViewInject(R.id.ll_work_item)
    private LinearLayout ll_work_item;//工作台四项
    @ViewInject(R.id.ll_work)
    private LinearLayout ll_work;//工作台
    @ViewInject(R.id.ll_new_car_manage)
    private LinearLayout ll_new_car_manage;
    @ViewInject(R.id.ll_old_car_manage)
    private LinearLayout ll_old_car_manage;
    @ViewInject(R.id.ll_member_manage)
    private LinearLayout ll_member_manage;
    @ViewInject(R.id.ll_customer_manage)
    private LinearLayout ll_customer_manage;

    String remindDesc = "", remindType = "";
    String auditDesc = " ";//失败原因是要带到认证失败页面的

    private String sex;//性别
    private UserInfoEntity entity;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.my_fragment, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setData();
    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
    }


    public void setData() {
        if (SpUtils.getIsLogin(mActivity)) {
            onRefresh();
        } else {//未登陆
            iv_personsex.setImageResource(R.mipmap.img_boy);
            tv_personname.setText("注册/登录");
            tvPersonalPhone.setText("马上登录，体验U买车完整功能");
            iv_personstute.setVisibility(View.GONE);
            tv_rcglr.setVisibility(View.GONE);
            cv_gocerti.setVisibility(View.GONE);
            iv_identify.setVisibility(View.GONE);
            setWorkbenchIsVisiable();
        }
    }

    private void onRefresh() {
        RequestParam param = new RequestParam();
        OkHttpUtils.post(Config.REFRESHUSERRESULT, param, new OkHttpCallBack(getActivity(), "加载中...") {
            @Override
            public void onSuccess(String data) {
                setLoginJsonStr(data);//保持用户信息
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject json = jsonObject.getJSONObject("result");

                    if (!json.isNull("remindDesc")) {//认证失败原因只会出现一次弹框内容
                        remindDesc = json.getString("remindDesc");//弹框的
                    }

                    if (!json.isNull("remindType")) {//认证状态
                        remindType = json.getString("remindType");//认证原因
                        if (!TextUtils.isEmpty(remindType)) {
                            isShowUserHasAgentRemind(remindType);//认证通过弹框
                        }
                    }

                    if (!json.isNull("auditDesc")) {//失败原因
                        auditDesc = json.getString("auditDesc");//带到失败页面的
                    }

                    JSONObject loginresultuser = json.getJSONObject("loginResultUser");
                    entity = GsonUtils.gsonToBean(loginresultuser.toString(), UserInfoEntity.class);
                    SpUtils.setToken(getActivity(), entity.getToken());//保存登录token
                    SpUtils.setIsLogin(getActivity(), true);//保存是否登录状态

                    initLoginInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                initLoginInfo();
                if (code == 1 || code == 2 || code == 3) {
//                    clearUser();
                    SpUtils.clearSp(getActivity());
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                ToastTool.show(getActivity(), error);
            }
        });
    }

    /*用户成为分销商弹框提醒*/
    Dialog distributionDalog;
    TextView tv_serach_car, tv_zbrz, content_one, tv_decc;
    ImageView iv_close, iv_rz;

    private void isShowUserHasAgentRemind(String type) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.distribution_dialog, null, false);
        distributionDalog = new Dialog(getActivity());
        distributionDalog.setContentView(view);
        distributionDalog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        tv_serach_car = (TextView) view.findViewById(R.id.tv_serach_car);
        tv_zbrz = (TextView) view.findViewById(R.id.tv_zbrz);

        iv_rz = (ImageView) view.findViewById(R.id.iv_rz);
        content_one = (TextView) view.findViewById(R.id.content_one);
        tv_decc = (TextView) view.findViewById(R.id.tv_decc);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);


        tv_serach_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distributionDalog != null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();
                }
                if (!TextUtils.isEmpty(remindType) && remindType.equals("1")) {//认证成功
                    getActivity().sendBroadcast(new Intent(ReceiverTool.REFRESHCARFRAGMENTMODULE));
                }

                if (!TextUtils.isEmpty(remindType) && remindType.equals("2")) {//认证失败
                    startActivity(new Intent(getActivity(), AuthenticateActivity.class));
                }
            }
        });
        tv_zbrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distributionDalog != null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distributionDalog != null && distributionDalog.isShowing()) {
                    distributionDalog.dismiss();
                }
            }
        });

        if (type.equals("1")) {//成功
            iv_rz.setImageResource(R.mipmap.tj_icon);
            content_one.setText("认证成功");
            tv_decc.setText("恭喜您，已认证成功");
            tv_serach_car.setText("去选车");
            tv_zbrz.setVisibility(View.GONE);
        } else if (type.equals("2")) {
            iv_rz.setImageResource(R.mipmap.failure_icon);
            content_one.setText("认证失败");
            tv_decc.setText(isNull(remindDesc));
            tv_serach_car.setText("重新认证");
            tv_zbrz.setVisibility(View.VISIBLE);
        }

        distributionDalog.show();

    }


    String sCertifiedStatus = "";

    private void initLoginInfo() {

        ((NewMainActivity) getActivity()).setWorkbenchIsVisiable();

        setWorkbenchIsVisiable();

        if (entity != null) {
            sex = isNull(entity.getUserSex());//性别
            String phonenum = isNull(entity.getMobile());
//            String str = String.format("%s****%s", phonenum.substring(0, 3), phonenum.substring(7, 11));//手机号码
            tvPersonalPhone.setText(phonenum);

//            if (entity.getShowName().length() > 12) {//名字
//                tv_personname.setText(entity.getShowName().substring(0,12) + "...");
//            } else {
//                tv_personname.setText(entity.getShowName());
//            }

            tv_personname.setText(entity.getShowName());

//            if (sex.equals("1")) {//图片
//                iv_personsex.setImageResource(R.mipmap.img_boy);
//            } else if (sex.equals("2")) {
//                iv_personsex.setImageResource(R.mipmap.img_girl);
//            } else {
//                iv_personsex.setImageResource(R.mipmap.img_moren);
//            }

            iv_personsex.setImageResource(R.mipmap.img_boy);

            if (entity.getIfDailyadmin().equals("0")) {//不是日常管理员
                iv_identify.setVisibility(View.GONE);
                tv_rcglr.setVisibility(View.GONE);
            } else {//判断是否认证
                if (entity.getUserAuditState().equals("1")) {//未认证
                    iv_identify.setVisibility(View.VISIBLE);
                    tv_rcglr.setVisibility(View.GONE);
                } else {
                    iv_identify.setVisibility(View.VISIBLE);
                    tv_rcglr.setVisibility(View.GONE);
                }
            }
            sCertifiedStatus = isNull(entity.getUserAuditState());//用户状态标签 1-未认证 2-审核中，3-认证车商，4-认证分销商，5-认证失败
            if (sCertifiedStatus.equals("2") || sCertifiedStatus.equals("3") || sCertifiedStatus.equals("4")) {
                cv_gocerti.setVisibility(View.GONE);
            } else {
                cv_gocerti.setVisibility(View.VISIBLE);
            }
            showStatue();
        }
    }

    private String isNull(String tex) {
        if (TextUtils.isEmpty(tex)) {
            return "--";
        } else {
            return tex;
        }
    }

    private void showStatue() {

        if (sCertifiedStatus.equals("1")) {
            iv_personstute.setBackgroundResource(R.mipmap.noscer);
        } else if (sCertifiedStatus.equals("2")) {
            iv_personstute.setBackgroundResource(R.mipmap.scerwait);
        } else if (sCertifiedStatus.equals("3")) {
            iv_personstute.setBackgroundResource(R.mipmap.scercs);
        } else if (sCertifiedStatus.equals("4")) {//认证分销商要给图
            iv_personstute.setBackgroundResource(R.mipmap.scerfxs);
        } else if (sCertifiedStatus.equals("5")) {//认证失败
            iv_personstute.setBackgroundResource(R.mipmap.scerfail);
        }

        iv_personstute.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.ll_unlogin, R.id.cv_sc, R.id.cv_fk, R.id.cv_gocerti, R.id.cv_sz,R.id.ll_new_car_manage,R.id.ll_old_car_manage,R.id.ll_member_manage,R.id.ll_customer_manage})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_car_manage://新车管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), CarManagerNewActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_old_car_manage://二手车管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), VehicleMnageAct.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_member_manage://员工管理
                if(isLogin()) {
                    startActivity(new Intent(getActivity(), EmployeesAct.class));
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_customer_manage://客户管理
                if(isLogin()) {
                    //判断是车商（进入客户管理）还是日常管理人（进入选择车商）
                    if(getUserInfo().getIfDailyadmin().equals("1")){//日常管理人
                        startActivity(new Intent(getActivity(), CarDealerActivity.class));//选择车商
                    }else if (getUserInfo().getUserRoleType().equals("201")) {
                        startActivity(new Intent(getActivity(), CustomerManagementActivity.class));//客户管理
                    }

                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;

            case R.id.ll_unlogin:
                if (SpUtils.getIsLogin(mActivity)) {
                    selectOpen();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;


            case R.id.cv_sc://收场
                if (SpUtils.getIsLogin(mActivity)) {
//                    startActivity(new Intent(getActivity(), CollectionAct.class));//进入收藏页面
                    startActivity(new Intent(getActivity(), MyCollectionAct.class));//进入收藏页面
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;

            case R.id.cv_fk://意见反馈
                if (SpUtils.getIsLogin(mActivity)) {
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.cv_sz://设置
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.cv_gocerti://认证
                if (SpUtils.getIsLogin(mActivity)) {
                    selectOpen();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;


//            case R.id.tv_clgl://车辆管理
//                startActivity(new Intent(getActivity(), VehicleMnageAct.class));
//                break;

//            case R.id.tv_ddgl://订单管理列表
//                startActivity(new Intent(getActivity(), BookingorderAct.class));
//                break;

//            case R.id.tv_yggl://员工
//                startActivity(new Intent(getActivity(), EmployeesAct.class));
//                break;

//            case R.id.tv_yydsh://预约单审核列表
//                startActivity(new Intent(getActivity(), BookingAct.class));
//                break;

//            case R.id.tv_wdyy://我的预约（写好了）
//                if(isLogin()) {
//                    startActivity(new Intent(getActivity(), CarHistoryActivity.class));
//                }else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//                break;

//            case R.id.tv_wmdc://我卖的车(写好了)
//                if(isLogin()) {
//                    startActivity(new Intent(getActivity(), SoldcarAct.class));
//                }else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//                break;

//            case R.id.tv_zcfw://找车服务
//                if(isLogin()) {
//                    startActivity(new Intent(getActivity(), SeachCarServerActivity.class));
//                }else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//                break;

//            case R.id.tv_qcjc://汽车检测
//                startActivity(new Intent(getActivity(), VehicleActivity.class));
//                break;

//            case R.id.tv_yjfk://意见反馈
//                if (isLogin()) {
//                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//                break;

//            case R.id.tv_dyyjfk://意见反馈
//                if (isLogin()) {
//                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//                break;

//            case R.id.tv_kfdh://客服电话
//                callPhone();
////                startActivity(new Intent(getActivity(), NewBrandAct.class));
//                break;

//            case R.id.tv_fb://发布车辆
//                Intent intent=new Intent(getActivity(),ReleaseAct.class);
//                intent.putExtra("fromtype","1");
//                startActivity(intent);
//                break;

        }
    }
    //用户角色类型   101-自营车商管理员，102-自营车商员工，201-全网车商管理员，202-全网车商员工 ,301-企业分销商管理员,302-企业分销商员工,401-个人分销商,501-普通用户
    private void setPermission(){
        setGone();
        //判断是否是日常管理人，如果是，显示客户管理
        String roleType = getUserInfo().getUserRoleType();
        if (TextUtils.isEmpty(roleType))
            return;
        switch (roleType){
            case "101":
                setSelfEmployed();
                break;
            case "201":
                setAllNetEmployed();
                break;
            case "301":
                setCorporateDistributors();
                break;
            default://日常管理员
                setDailyManager();
                break;
        }

    }
    //设置工作台是否可见
    public void setWorkbenchIsVisiable(){
        //判断是否登录，未登录，工作台不显示，登录判断登陆者身份(未认证,普通用户，员工，工作台不显示)
        if (isLogin()){
            if(getUserInfo().getUserRoleType().equals("102")||getUserInfo().getUserRoleType().equals("202")||
                    getUserInfo().getUserRoleType().equals("302")||
                    getUserInfo().getUserRoleType().equals("401")||getUserInfo().getUserRoleType().equals("501")) {
                ll_work.setVisibility(View.GONE);
                if(getUserInfo().getIfDailyadmin().equals("1")){
                    ll_work.setVisibility(View.VISIBLE);
                    setPermission();
                }
            }else {
                ll_work.setVisibility(View.VISIBLE);
                setPermission();
            }
        }else {
            ll_work.setVisibility(View.GONE);
        }

    }
    private void setGone(){
        ll_work.setVisibility(View.GONE);
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.GONE);
        ll_customer_manage.setVisibility(View.GONE);
    }
    //自营车商  管理员：新车，二手车，员工管理
    private void setSelfEmployed(){
        if (!getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            int screenWith = SpUtils.getScreenWith(mActivity);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
            params.width = (3*screenWith)/4;
            ll_work_item.setLayoutParams(params);
        }else {
            int screenWith = SpUtils.getScreenWith(mActivity);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
            params.width = screenWith;
            ll_work_item.setLayoutParams(params);
        }
        ll_work.setVisibility(View.VISIBLE);
        ll_new_car_manage.setVisibility(View.VISIBLE);
        ll_old_car_manage.setVisibility(View.VISIBLE);
        ll_member_manage.setVisibility(View.VISIBLE);
        if (getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            ll_customer_manage.setVisibility(View.VISIBLE);
        }else {

            ll_customer_manage.setVisibility(View.GONE);
        }
    }
    //全网车商  管理员：新车，二手车，员工管理，客户管理
    private void setAllNetEmployed(){
        int screenWith = SpUtils.getScreenWith(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
        params.width = screenWith ;
        ll_work_item.setLayoutParams(params);
        ll_work.setVisibility(View.VISIBLE);
        ll_new_car_manage.setVisibility(View.VISIBLE);
        ll_old_car_manage.setVisibility(View.VISIBLE);
        ll_member_manage.setVisibility(View.VISIBLE);
        ll_customer_manage.setVisibility(View.VISIBLE);
    }
    //企业分销商 管理员：员工管理
    private void setCorporateDistributors(){
        if (getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            int screenWith = SpUtils.getScreenWith(mActivity);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
            params.width = screenWith / 2;
            ll_work_item.setLayoutParams(params);
        }else {
            int screenWith = SpUtils.getScreenWith(mActivity);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
            params.width = screenWith / 4;
            ll_work_item.setLayoutParams(params);
        }
        ll_work.setVisibility(View.VISIBLE);
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.VISIBLE);
        if (getUserInfo().getIfDailyadmin().equals("1")) {//日常管理员
            ll_customer_manage.setVisibility(View.VISIBLE);
        }else {
            ll_customer_manage.setVisibility(View.GONE);
        }
    }
    //日常管理人
    private void setDailyManager(){
        int screenWith = SpUtils.getScreenWith(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_work_item.getLayoutParams();
        params.width = screenWith/4;
        ll_work_item.setLayoutParams(params);
        ll_work.setVisibility(View.VISIBLE);
        ll_new_car_manage.setVisibility(View.GONE);
        ll_old_car_manage.setVisibility(View.GONE);
        ll_member_manage.setVisibility(View.GONE);

        ll_customer_manage.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setWorkbenchIsVisiable();
    }

    private void selectOpen() {
        if (entity.getToDetail().equals("1"))//未认证
        {
            if (entity.getIfDailyadmin().equals("1")) {//是日常管理员
                   showDialog();
            } else {//不是日常管理员
                startActivity(new Intent(getActivity(), AuthenticateActivity.class));
            }
        } else if (entity.getToDetail().equals("5")) {//认证失败
            startActivity(new Intent(getActivity(), VerifyFailureActivity.class).putExtra("auditDesc", auditDesc));
        } else {//资料头部
            startActivity(new Intent(getActivity(), MyInfoActivity.class).putExtra("type", entity.getToDetail()));
        }
    }

    /*用户成为分销商弹框提醒*/
    Dialog adminDalog;//日常管理员未认证时候点击认证弹框
    private void showDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.admin_dialog, null, false);
        adminDalog=new Dialog(getActivity());
        adminDalog.setContentView(view);
        adminDalog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        tv_zbrz= (TextView) view.findViewById(R.id.tv_zbrz);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        tv_zbrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminDalog!=null && adminDalog.isShowing()) {
                    adminDalog.dismiss();
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminDalog!=null && adminDalog.isShowing()) {
                    adminDalog.dismiss();
                }
            }
        });
        adminDalog.show();

    }

    private void goSign() {
        startActivity(new Intent(getActivity(), SigninAct.class));

    }

    final int REQUESTCODE = 1012;
    PromptDialog dialog;
    String number = "";

    private void callPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPhonePermission = getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasPhonePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUESTCODE);
                return;
            }
        }


        dialog = new PromptDialog(getActivity());
        dialog.setText("400 189 0079");
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
                Uri data = Uri.parse("tel:" + "4001890079"); // 设置数据
                intent.setData(data);
                startActivity(intent); // 激活Activity组件
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE:
                if (!permissions[0].equals(Manifest.permission.CALL_PHONE) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastTool.show(getActivity(), "请先授权电话权限！");
                    return;
                }
                callPhone();
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setWorkbenchIsVisiable();

        if (hidden) {

        } else {
            setData();
        }
    }
}
