package com.rch.common;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/31.
 */

public class RegTool {

    public static boolean isMobile(String mobiles)
    {
        String phone="^1[0|1|2|3|4|5|6|7|8|9][0-9]\\d{8}$";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(mobiles);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isEmail(String email)
    {

        String str="^[a-zA-Z0-9]{3,}@[a-zA-Z0-9]{2,6}.[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isValidateIdCard(String card)
    {
        String str="^\\d{17}(\\d|x|X)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(card);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isMoney(String money)
    {
        String str="^[0-9]*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(money);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isMileage(String mileage)
    {
        String str="^[1-9][0-9]*.[0-9]{1,2}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mileage);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isZjPhone(String phone) {
        String str="^(0[0-9]{2,3}\\\\-)?([1-9][0-9]{6,7})$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phone);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }

    public static boolean isLince(String phone) {
        String str="^([\\d\\-]){10,20}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phone);
        Log.e("Reg",String.valueOf(m.matches()));
        return m.matches();
    }
}
