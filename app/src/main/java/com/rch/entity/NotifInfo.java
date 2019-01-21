package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/4.
 */

public class NotifInfo implements Serializable{
    private String id;//	公告主键ID
    private String noticeTitle;//	公告标题
    private String releaseTime;//	发布时间
    private String noticeUrl;//	公告链接
    private String noticeContent;//	公告内容
    private String content;//去标签	公告内容
    private String createTime;//创建时间

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }
}
