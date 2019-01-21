package com.rch.common;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/2/26.
 */

public class EncryptionTools {

    //public static String TOKEN="";
    public static String TIMESTAMP="";
    public static String NONCE="";
    public static String SIGNATURE="";

    public static void initEncrypMD5(String Token)
    {
        TIMESTAMP=getDateToString("yyyyMMddHHmmss");
        NONCE=String.valueOf( new Random().nextInt(10000));
        String stitching=Token+TIMESTAMP+NONCE;
        String str=sort(stitching)+Config.key;
        SIGNATURE=MD5Util.encrypt(str);
       // Log.e("SIGNATURE", SIGNATURE);
    }

    public static String getDateToString(String pattern) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    // 字符升序
    public static String sort(String str) {
        char[] ch = str.toCharArray() ;
        List<Character> list = new ArrayList<Character>() ;
        for (int i=0;i<ch.length;i++) {
            list.add(ch[i]) ;
        }
        Collections.sort(list);
        StringBuffer sb = new StringBuffer() ;
        for (int i=0;i<ch.length;i++) {
            sb.append(list.get(i)) ;
        }
        return sb.toString() ;
    }

}
