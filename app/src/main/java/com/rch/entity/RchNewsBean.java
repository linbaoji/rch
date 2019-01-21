package com.rch.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/12/21.
 */

public class RchNewsBean implements Parcelable {

        /**
         * id : 5c19b1c617bc6cb24a03daa6
         * typeid : 1
         * title : asd
         * image : http://files.ubuyche.com/063400.png
         * source : asda
         * sourceurl : asdas
         * porder : 2
         * deploydate : 2018-12-19 00:00:00
         * author : asd
         * summary : asdasda
         * displaytype : 0
         * titleouturl :
         * notes : 内容啊啊啊
         */

        private String id;
        private String typeid;
        private String title;
        private String image;
        private String source;
        private String sourceurl;
        private int porder;
        private String deploydate;
        private String author;
        private String summary;
        private String displaytype;
        private String titleouturl;
        private String notes;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSourceurl() {
            return sourceurl;
        }

        public void setSourceurl(String sourceurl) {
            this.sourceurl = sourceurl;
        }

        public int getPorder() {
            return porder;
        }

        public void setPorder(int porder) {
            this.porder = porder;
        }

        public String getDeploydate() {
            return deploydate;
        }

        public void setDeploydate(String deploydate) {
            this.deploydate = deploydate;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDisplaytype() {
            return displaytype;
        }

        public void setDisplaytype(String displaytype) {
            this.displaytype = displaytype;
        }

        public String getTitleouturl() {
            return titleouturl;
        }

        public void setTitleouturl(String titleouturl) {
            this.titleouturl = titleouturl;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.typeid);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.source);
        dest.writeString(this.sourceurl);
        dest.writeInt(this.porder);
        dest.writeString(this.deploydate);
        dest.writeString(this.author);
        dest.writeString(this.summary);
        dest.writeString(this.displaytype);
        dest.writeString(this.titleouturl);
        dest.writeString(this.notes);
    }

    public RchNewsBean() {
    }

    protected RchNewsBean(Parcel in) {
        this.id = in.readString();
        this.typeid = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.source = in.readString();
        this.sourceurl = in.readString();
        this.porder = in.readInt();
        this.deploydate = in.readString();
        this.author = in.readString();
        this.summary = in.readString();
        this.displaytype = in.readString();
        this.titleouturl = in.readString();
        this.notes = in.readString();
    }

    public static final Parcelable.Creator<RchNewsBean> CREATOR = new Parcelable.Creator<RchNewsBean>() {
        @Override
        public RchNewsBean createFromParcel(Parcel source) {
            return new RchNewsBean(source);
        }

        @Override
        public RchNewsBean[] newArray(int size) {
            return new RchNewsBean[size];
        }
    };
}
