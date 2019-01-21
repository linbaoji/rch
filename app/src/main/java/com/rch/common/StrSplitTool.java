package com.rch.common;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Administrator on 2018/2/27.
 */

public class StrSplitTool {

    public static int splitSymbol(String str)
    {
        if(str.isEmpty())
            return 0;
        else if(str.indexOf(".")>-1)
            return Integer.parseInt(str.split("\\.")[0]);
        else
            return Integer.parseInt(str);

    }

    /*保留一位小数，如果一位小数为0，则整数，否则浮点数*/
    public static String retainOneNumber(String str)
    {
        if(str!=null&&!str.isEmpty())
        {
            String strShowMileage=String.valueOf(Float.parseFloat(str)/10000);
            String convertStr=new BigDecimal(strShowMileage).setScale(1,BigDecimal.ROUND_DOWN).toString();
            String startNumber=convertStr.split("\\.")[0];
            String endNumber=convertStr.split("\\.")[1];
            if(endNumber.equals("0"))
                return startNumber;
            else
                return convertStr;
        }
        return "0";
    }

    public static String removeForeComma(String str)
    {
        if(str.isEmpty())
            return str;
        if(str.indexOf(",")>-1)
            str=str.substring(1,str.length());
        return str;
    }

    public static String addZero(String str)
    {
        if(str==null||str.isEmpty())
            return "";
        else if(str.indexOf(".0")>-1)
            return str.split("\\.")[0]+".00";
        else
            return str+".00";
    }


    /*4位间隔*/
    public static String intervalFour(String str)
    {
        if(str==null||str.isEmpty())
            return "";
        int j=0;
        int len=str.length();
        int stitchingParams=4;
        int remaining=len%stitchingParams;
        String tempStitching="",resultStr="";
        for (int i=0;i<len;i++)
        {
            j++;
            tempStitching+=str.charAt(i);
            if((i+1)==remaining||j%stitchingParams==0) {
                resultStr+= tempStitching + " ";
                tempStitching="";
                j=0;
            }
        }
        return resultStr;
    }

}
