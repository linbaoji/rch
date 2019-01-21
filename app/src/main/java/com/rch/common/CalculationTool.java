package com.rch.common;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/3/23.
 */

public class CalculationTool {


    /*以万为单位*/
    public static String getCaluculationUnit(String money)
    {
        float tempMoney=0;
        if(money.isEmpty())
            tempMoney=0;
        else if(money.indexOf(".")>-1)
            tempMoney=Float.parseFloat(money.split("\\.")[0]);
        else
            tempMoney=Float.parseFloat(money);
        String str=String.valueOf(tempMoney/10000);
        return  new BigDecimal(str).setScale(2,4).toString()+"";
    }

    /*以万为单位*/
    public static String getCaluculationIntUnit(String money)
    {
        float tempMoney=0;
        if(money.isEmpty())
            tempMoney=0;
        else if(money.indexOf(".")>-1)
            tempMoney=Float.parseFloat(money.split("\\.")[0]);
        else
            tempMoney=Float.parseFloat(money);
        String str=String.valueOf(tempMoney/10000);
        return  new BigDecimal(str).setScale(0,BigDecimal.ROUND_DOWN).toString()+"";
    }


    public static String getCaluculationUnit(String money,int unit)
    {
        float tempMoney=0;
        if(money.isEmpty())
            tempMoney=0;
        else if(money.indexOf(".")>-1)
            tempMoney=Float.parseFloat(money.split("\\.")[0]);
        else
            tempMoney=Float.parseFloat(money);
        return  new BigDecimal(tempMoney/unit).setScale(2,4).toString();
    }


    /*public static String getPeriodCaluculation(String money)
    {
        float tempMoney=0;
        if(money.isEmpty())
            tempMoney=0;
        else if(money.indexOf(".")>-1)
            tempMoney=Float.parseFloat(money.split("\\.")[0]);
        else
            tempMoney=Float.parseFloat(money);
       return  new BigDecimal(tempMoney/12).setScale(2,4).toString()+"x12期";
    }

    public static String getPeriodCaluculation(String money,String period)
    {
        float tempMoney=0;
        if(money.isEmpty())
            tempMoney=0;
        else if(money.indexOf(".")>-1)
            tempMoney=Float.parseFloat(money.split("\\.")[0]);
        else
            tempMoney=Float.parseFloat(money);
        if(period.isEmpty())
            return "";
        else
            return  new BigDecimal(tempMoney/Float.parseFloat(period)).setScale(2,4).toString()+"x"+period+"期";
    }*/
}
