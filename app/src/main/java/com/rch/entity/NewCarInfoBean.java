package com.rch.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/24.
 */

public class NewCarInfoBean implements Serializable{
    private String definState;//1: 待上架-审核中,2: 待上架-审核失败,3: 待上架-审核通过,4: 在售中-审核通过,5: 已售罄-审核通过,6: 已下架-审核通过
    private String definStateName;//1：待上架-审核中,2: 待上架-审核失败,3: 待上架-审核通过,4: 在售中-审核通过,5: 已售罄-审核通过,6: 已下架-审核通过
    private DatailBean detail;

    public String getDefinState() {
        return definState;
    }

    public void setDefinState(String definState) {
        this.definState = definState;
    }

    public String getDefinStateName() {
        return definStateName;
    }

    public void setDefinStateName(String definStateName) {
        this.definStateName = definStateName;
    }

    public DatailBean getDetail() {
        return detail;
    }

    public void setDetail(DatailBean detail) {
        this.detail = detail;
    }

}
