package com.rch.base;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.networkbench.agent.impl.NBSAppAgent;
import com.rch.activity.BookingorderAct;
import com.rch.activity.CarDetailActivity;
import com.rch.activity.CarHistoryActivity;
import com.rch.common.AppStatusTool;
import com.rch.common.JsonTool;
import com.rch.common.ToastTool;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;


import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/23.
 */

public class MyApplication extends Application {
    private List<Activity> oList;//用于存放所有启动的Activity的集合

    static   MyApplication  instance;
    public static Application getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        oList = new ArrayList<Activity>();
        instance = this;

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.init(this,"5b150bcaa40fa36fc8000056"
                ,"rch",UMConfigure.DEVICE_TYPE_PHONE,"3e8066bf7cb8d8e97547d69c8bfd388d");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        //boolean 默认为false，如需查看LOG设置为true
        UMConfigure.setLogEnabled(false);
        PlatformConfig.setWeixin("wxeb613c3cb76988a0", "66a4fbcd0a76c43c864d485cf3f3dbb4");
        NBSAppAgent.setLicenseKey("244ed5e3e7b048d4b2c9f6899e1cd2a2").withLocationServiceEnabled(true).start(this.getApplicationContext());//Appkey 请从官网获取
        //统计
        //MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        // MobclickAgent.setSecret(getApplicationContext(), "s10bacedtyz");


        initUpush();
    }

    /*友盟推送*/
    private void initUpush()
    {
         /*注册推送服务*/
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //自定义通知栏样式---自定义通知栏样式   ----------------------开启这个没有消息推送信息
       // mPushAgent.setMessageHandler(messageHandler);
        //自定义行为--自定义通知打开动作
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("deviceToken",deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });

        Log.e("device token",mPushAgent.getRegistrationId());

        //小米Push初始化
        MiPushRegistar.register(this, "2882303761517811635", "5731781143635");

        //华为Push初始化
        HuaWeiRegister.register(this);

      /*-----------------通知免打扰模式start----------------------*/
      /*为免过度打扰用户，SDK默认在“23:00”到“7:00”之间收到通知消息时不响铃，不振动，不闪灯。如果需要改变默认的静音时间
        mPushAgent.setNoDisturbMode(23, 0, 7, 0);
         可以通过下面的设置，来关闭免打扰模式：
        mPushAgent.setNoDisturbMode(0, 0, 0, 0);

        默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间
        mPushAgent.setMuteDurationSeconds(int seconds);*/
       /*-----------------通知免打扰模式end----------------------*/


        /*----------------- 通知栏按数量显示start----------------------*/
       /* 通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
        public void setDisplayNotificationNumber(int number);

        例如设置通知栏最多显示两条通知（当通知栏已经有两条通知，此时若第三条通知到达，则会把第一条通知隐藏）参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。：
        mPushAgent.setDisplayNotificationNumber(2);*/
       /*----------------- 通知栏按数量显示end----------------------*/


        /*-----------------应用在前台时否显示通知start----------------------*/
        // mPushAgent.setNotificaitonOnForeground(false);
          /*-----------------应用在前台时否显示通知end----------------------*/

            /*-----------------关闭推送start----------------------*/
    /*    mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });*/
        /*-----------------关闭推送ent----------------------*/

        /*-----------------若调用关闭推送后，想要再次开启推送，则需要调用以下代码（请在Activity内调用）start----------------------*/
     /*   mPushAgent.enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });*/
        /*-----------------若调用关闭推送后，想要再次开启推送，ent----------------------*/


    }

    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        /**
         * 通知的回调方法
         * @param context
         * @param msg
         */
        @Override
        public void dealWithNotificationMessage(Context context, UMessage msg) {
            //调用super则会走通知展示流程，不调用super则不展示通知
            Log.e("asdf",msg.toString());
            super.dealWithNotificationMessage(context, msg);
        }
    };


    /*自定义行为-----自定义行为的数据放在UMessage.custom字段*/
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
        String value="";
        String key="";
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
           /* for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                Log.e("UmengNotificationClick",key+"----------"+value);
                Log.e("UmengNotificationClick",key+"----------"+value);
            }*/

            String customJson=msg.custom;
            if(customJson!=null&&!customJson.isEmpty())
            {
                key=JsonTool.getResult(customJson,"action");
                value=JsonTool.getResult(customJson,"id");
                Intent intent1=new Intent();
                if(key.equals(UPustKey.toVehicleDetail))
                    intent1.setClass(context, CarDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",value);
                else if(key.equals(UPustKey.toOrderDeail))
                    intent1.setClass(context, BookingorderAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type",value);
//                    intent1.setClass(context, CarHistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type",value);
                startActivity(intent1);
            }



          /*  Intent intent1=new Intent(context, CarDetailActivity.class);
            if(key.equals(UPustKey.toVehicleDetail))
            {
                intent1.setClass(context, CarDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",value);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("id",value);

            }else if(key.equals(UPustKey.toOrderDeail)) {
                //Intent intent1=new Intent(context, CarHistoryActivity.class);
                intent1.setClass(context, CarHistoryActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("type",value);
                startActivity(intent1);
            }
            startActivity(intent1);*/
        }
    };


    //UmengMessageHandler类负责处理消息，包括通知和自定义消息。其中，成员函数getNotification负责定义通知栏样式。若SDK默认的消息展示样式不符合开发者的需求，可通过覆盖该方法来自定义通知栏展示样式。
  /*  UmengMessageHandler messageHandler = new UmengMessageHandler() {
       *//* @Override
        public Notification getNotification(Context context, UMessage msg) {
            switch (msg.builder_id) {
                case 1:
                    Notification.Builder builder = new Notification.Builder(context);
                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                            R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                            getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon,
                            getSmallIconId(context, msg));
                    builder.setContent(myNotificationView)
                            .setSmallIcon(getSmallIconId(context, msg))
                            .setTicker(msg.ticker)
                            .setAutoCancel(true);
                    return builder.getNotification();
                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
            }
        }*//*
    };*/



    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

    interface UPustKey{
     /*   String toVehicleDetail="toVehicleDetail";//车辆详情
        String toOrderDeail="toOrderDeail";//订单详情*/

        String toVehicleDetail="toVehicle";//车辆详情
        String toOrderDeail="toOrder";//订单详情
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }

}
