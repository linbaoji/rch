package com.rch.common;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/12/10.
 */

public class StartorSelectUtils {

    /**
     * 如果不为空或者不为0返回true
     * @param tex
     * @return
     */
    public static boolean isBoolean(String tex) {
        if(!TextUtils.isEmpty(tex)) {
            if(tex.equals("1")){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 如果为空或者为0返回0
     * @param tex
     * @return
     */
    public static String nullOrone(String tex) {
        if(!TextUtils.isEmpty(tex)){
            if(tex.equals("1")){
                return "1";
            }else {
                return "0";
            }
        }else {
            return "0";
        }
    }
}
