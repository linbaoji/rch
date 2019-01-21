package com.rch.common;

import android.content.Context;
import android.util.Log;

import com.rch.entity.BuyMonitorEntity;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/6.
 */

public class ZhuGeIOTool {

    public static void buyMonitor(Context context, BuyMonitorEntity entity)
    {
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("手机号", entity.getUserPhone());
            eventObject.put("用户姓名", entity.getUserName());
            eventObject.put("商品名称", entity.getCarShopName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //记录事件,以购买为例
        ZhugeSDK.getInstance().track(context, entity.getEventName(), eventObject);
    }


    public static void buyMonitor(Context context,String eventName, Map<String,String> map)
    {
        if(map.size()>1) {
            JSONObject eventObject = new JSONObject();
            try {
                for (Map.Entry<String ,String> entry:map.entrySet()) {
                    Log.e("asdasdasdas",entry.getKey()+"|||||"+entry.getValue());
                    eventObject.put(entry.getKey(), entry.getValue());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //记录事件,以购买为例
            ZhugeSDK.getInstance().track(context, eventName, eventObject);
        }
    }

}
