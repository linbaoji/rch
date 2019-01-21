package com.rch.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * TODO<json数据源>
 *
 * @author: 小嵩
 * @date: 2017/3/16 15:36
 */

public class JsonBean  implements IPickerViewData{


//    {
//        "areaId": "1",
//            "name": "北京",
//            "level": "1",
//            "pid": "",
//            "spellInit": "B",
//            "spellFullinit": "BJ",
//            "city": [{
//                "areaId": "1",
//                "name": "北京",
//                "level": "2",
//                "pid": "1",
//                "spellInit": "B",
//                "spellFullinit": "BJ"
//                                       }]
//     }



    private String name;
    private String areaId;
    private String level;
    private String pid;
    private String spellInit;
    private String spellFullinit;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSpellInit() {
        return spellInit;
    }

    public void setSpellInit(String spellInit) {
        this.spellInit = spellInit;
    }

    public String getSpellFullinit() {
        return spellFullinit;
    }

    public void setSpellFullinit(String spellFullinit) {
        this.spellFullinit = spellFullinit;
    }

    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCityList() {
        return city;
    }

    public void setCityList(List<CityBean> city) {
        this.city = city;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }



    public static class CityBean implements IPickerViewData{
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */


        private String name;
        private String areaId;
        private String level;
        private String pid;
        private String spellInit;
        private String spellFullinit;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getSpellInit() {
            return spellInit;
        }

        public void setSpellInit(String spellInit) {
            this.spellInit = spellInit;
        }

        public String getSpellFullinit() {
            return spellFullinit;
        }

        public void setSpellFullinit(String spellFullinit) {
            this.spellFullinit = spellFullinit;
        }

        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }
}
