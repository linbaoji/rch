package com.rch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.adatper.EmployeesAdapter;
import com.rch.adatper.EmployeesManagerAdapter;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.ToastTool;
import com.rch.custom.LoadingView;
import com.rch.custom.MyAlertDialog;
import com.rch.custom.MySelfSheetDialog;
import com.rch.entity.EmployBean;
import com.rch.entity.UserInfoEntity;
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
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class EmployeesAct extends BaseActivity implements EmployeesAdapter.upDate{
    @ViewInject(R.id.et_search)
    private EditText et_search;
    @ViewInject(R.id.rl_search)
    private RelativeLayout rl_search;
    @ViewInject(R.id.tv_member)
    private TextView tv_member;
    @ViewInject(R.id.plv_gly)
    private SwipeMenuRecyclerView plv_gly;
    @ViewInject(R.id.plv_yg)
    private SwipeMenuRecyclerView plv_yg;
    @ViewInject(R.id.ld_view)
    private LoadingView ld_view;
    @ViewInject(R.id.iv_tianjia)
    private ImageView iv_tianjia;
    private List<EmployBean> memberList=new ArrayList<>();
    private List<EmployBean> managerList=new ArrayList<>();
    private List<EmployBean> searchList=new ArrayList<>();
    String userId;
    String userName;
    String enterpriseName;
    String enterpriseId;
    String uuId;
    String shareUrl;


    private RecyclerView recyclerView;
    private EmployeesManagerAdapter adapter;//成员
    private EmployeesManagerAdapter adapterManager;//管理员
    private UserInfoEntity user;
//    private int isAdmin=0;
    private boolean isshow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_empoloyess);
        initToolBar();
        ViewUtils.inject(this);

        plv_gly.setLayoutManager(new LinearLayoutManager(EmployeesAct.this));
        adapterManager = new EmployeesManagerAdapter(EmployeesAct.this,managerList);
        plv_gly.setAdapter(adapterManager);

        plv_yg.useDefaultLoadMore(); // 使用默认的加载更多的View。
//        plv_yg.setItemViewSwipeEnabled(true); // 策划删除，默认关闭。
        plv_yg.setLayoutManager(new LinearLayoutManager(EmployeesAct.this));
        plv_yg.setSwipeMenuCreator(swipeMenuCreator);
        plv_yg.setSwipeMenuItemClickListener(mMenuItemClickListener);
//        name=getUserInfo().getUserName();
//        phone=getUserInfo().getMobile();
        user=getUserInfo();
        adapter=new EmployeesManagerAdapter(EmployeesAct.this,memberList);
        plv_yg.setAdapter(adapter);

        isshow=false;
        getDate();


        ld_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                getDate();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(EmployeesAct.this)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("移除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };

    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                showAlet(memberList.get(menuPosition).getId());
//                Toast.makeText(EmployeesAct.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(EmployeesAct.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };
    //弹框确认
    private void showAlet(final String id) {
        MyAlertDialog dialog = new MyAlertDialog(EmployeesAct.this).builder();
        dialog.setMsg("确认移除该员工？");

        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delet(id);
            }
        });

        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.show();
    }
    //移除员工
    private void delet(String id) {
        String type = "2";
        RequestParam param = new RequestParam();
        EncryptionTools.initEncrypMD5(user.getToken() == null ? "" : user.getToken());
        param.add("token", user.getToken() == null ? "" : user.getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("operType", type);
        param.add("id", id);
        OkHttpUtils.post(Config.OPERUSER, param, new OkHttpCallBack(EmployeesAct.this, "加载中...") {
            @Override
            public void onSuccess(String data) {
                getDate();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(EmployeesAct.this, error);
            }
        });
    }

    private void getDate() {
        RequestParam param = new RequestParam();
        ld_view.loading();
        OkHttpUtils.post(Config.QUERYOWNERUSER, param, new OkHttpCallBack(EmployeesAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                ld_view.loadComplete();
                try {
                    Gson gson=new Gson();
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    JSONArray admList=result.getJSONArray("admList");
                    managerList= gson.fromJson(admList.toString(),new TypeToken<List<EmployBean>>(){}.getType());
                    JSONArray userList=result.getJSONArray("userList");
                    memberList= gson.fromJson(userList.toString(),new TypeToken<List<EmployBean>>(){}.getType());


                    if(memberList.size()>0 || managerList.size()>0){
//                        if (memberList!=null && memberList.size()==0){//成员列表为空
////                            rl_search.setVisibility(View.GONE);
//                            ld_view.noContent();
//                            ld_view.setNoContentTxt("您还没有任何员工噢~");
//                            ld_view.clickButton("添加员工", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    httpYqhy();//邀请好友接口
//                                }
//                            });
//                        }else {
                            if (memberList.size()==0){
                                tv_member.setVisibility(View.GONE);
                            }else {
                                tv_member.setVisibility(View.VISIBLE);
                            }
                            adapter.ChageDate(memberList);
                            adapterManager.ChageDate(managerList);

//                        }
                        // 数据完更多数据，一定要调用这个方法。
                        // 第一个参数：表示此次数据是否为空。
                        // 第二个参数：表示是否还有更多数据。

//                        plv_gly.loadMoreFinish(false, false);
//                        plv_yg.loadMoreFinish(false, false);




                    }else {
                        plv_gly.loadMoreFinish(true, false);
                        plv_yg.loadMoreFinish(true, false);
                        adapter.ChageDate(new ArrayList<EmployBean>());
                        adapterManager.ChageDate(new ArrayList<EmployBean>());
                        ld_view.noContent();
                        ld_view.setNoContentIco(R.mipmap.wukehu);
                        ld_view.setNoContentTxt("您还没有任何员工噢~");
                        ld_view.clickButton("添加员工", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                httpYqhy();//邀请好友接口
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    plv_gly.loadMoreFinish(false, true);
                    plv_yg.loadMoreFinish(false, true);
                }

            }

            @Override
            public void onError(int code, String error) {
                 ToastTool.show(EmployeesAct.this,error);
                 plv_yg.loadMoreError(code, error);
                 ld_view.loadError();
            }
        });

    }


    @OnClick({R.id.iv_finsh,R.id.iv_tianjia})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_finsh:
                finish();
                break;

            case R.id.iv_tianjia://添加用户
                httpYqhy();//邀请好友接口
                break;
        }
    }

    private void httpYqhy() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("type", "1");
        OkHttpUtils.post(Config.INVITEUSER, param, new OkHttpCallBack(EmployeesAct.this,"加载中") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject=new JSONObject(data.toString());
                    JSONObject result=jsonObject.getJSONObject("result");
                     userId=result.getString("userId");
                     userName=result.getString("userName");
                     enterpriseName=result.getString("enterpriseName");
                     enterpriseId=result.getString("enterpriseId");
                     uuId=result.getString("uuId");
                     shareUrl=result.getString("shareUrl");

                    showShelfDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

            }
        });


    }


    /**
     * 显示弹框
     */
    private void showShelfDialog() {
        final MySelfSheetDialog dialog=new MySelfSheetDialog(this);
        dialog.builder().setTitle("邀请您的同事加入企业").addSheetItem("微信邀请同事", MySelfSheetDialog.SheetItemColor.Gray, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                share();

            }
        }).show();
    }

    UMWeb web;
    UMImage image;
    String wxshartitle;
    String shareDesc;


    private void share() {
        image = new UMImage(EmployeesAct.this, R.mipmap.shareicon);
        image.compressStyle=UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        wxshartitle=userName+"邀请您加入企业";
        shareDesc=userName+"邀请你加入"+enterpriseName+"，快来加入吧！";

                    //微信
        web = new UMWeb(shareUrl);//分享路径
        web.setTitle(wxshartitle);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(shareDesc);//描述


        new ShareAction(EmployeesAct.this)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .withMedia(web)
                .share();

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

        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t.getMessage().contains("没有安装应用")){
                ToastTool.show(EmployeesAct.this,"您未安装微信APP,请先安装");

            }else {
                ToastTool.show(EmployeesAct.this, t.getMessage());
            }
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastTool.show(EmployeesAct.this,"取消！");
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void update() {
//        currentPage=1;
//        getDate();
        finish();//结束当前界面，并将我的界面员工管理模块隐藏
    }

    public void setMemberGone(){
        tv_member.setVisibility(View.GONE);
    }
}
