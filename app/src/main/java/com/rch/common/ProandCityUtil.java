package com.rch.common;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.rch.entity.JsonBean;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/8.
 */

public class ProandCityUtil {

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

     Context context;

     public ProandCityUtil(Context context) {
        this.context = context;
     }

    public void initJsonData(ArrayList<JsonBean> options1Items, ArrayList<ArrayList<JsonBean.CityBean>> options2Items,Handler mhandler) {//解析数据

         options1Items.clear();
         options2Items.clear();
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "city.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData,mhandler);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
//        options1Items = jsonBean;
          options1Items.addAll(jsonBean);

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<JsonBean.CityBean> CityList = new ArrayList<>();//该省的城市列表（第二级）
//            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                JsonBean.CityBean CityBean = jsonBean.get(i).getCityList().get(c);
                CityList.add(CityBean);//添加城市

//                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
//                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
//                if (jsonBean.get(i).getCityList().get(c).getArea() == null
//                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
//                    City_AreaList.add("");
//                } else {
//                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
//                }
//                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                 }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
//            options3Items.add(Province_AreaList);
        }
        mhandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public static ArrayList<JsonBean> parseData(String result,Handler handler) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
