package com.rch.common;

/**
 * Created by Administrator on 2018/5/11.
 */

public class TimeSplitTool {

    /*获取年月日*/
    public static String getYMD(String time)
    {
        if(time!=null&&!time.isEmpty())
        {
            if(time.indexOf(" ")>-1)
                return time.split(" ")[0];
        }
        return "";
    }

    /*获取年月*/
    public static String getYM(String time)
    {
        if(time!=null&&!time.isEmpty())
        {
            if(time.indexOf(" ")>-1) {
                String ymd = time.split(" ")[0];
                String y=ymd.split("-")[0];
                String d=ymd.split("-")[1];
                return y+"-"+d;
            }
        }
        return "--";
    }

    /*获取年月*/
    public static String getY(String time)
    {
        if(time!=null&&!time.isEmpty())
        {
            if(time.indexOf(" ")>-1) {
                String ymd = time.split(" ")[0];
                String y=ymd.split("-")[0];
                String d=ymd.split("-")[1];
                return y+"年";
            }
        }
        return "--";
    }

}
