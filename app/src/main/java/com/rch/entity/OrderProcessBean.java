package com.rch.entity;

/**
 * Created by Administrator on 2018/8/1.
 */

public class OrderProcessBean {

    /**
     * code : 1
     * nodeName : 恭喜你，预约成功
     * ifCompleted : 1
     * nodeDesc :
     */

    private String code;
    private String nodeName;
    private String ifCompleted;
    private String nodeDesc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getIfCompleted() {
        return ifCompleted;
    }

    public void setIfCompleted(String ifCompleted) {
        this.ifCompleted = ifCompleted;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }
}
