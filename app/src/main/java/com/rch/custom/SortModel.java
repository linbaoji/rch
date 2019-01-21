package com.rch.custom;

import java.io.Serializable;

public class SortModel implements Serializable{

	private String name;
	private String sortLetters;
	private String brandId;
	private String brandImage;
	private String brandImagePath;
	private String remark;



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandImage() {
		return brandImage;
	}

	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}

	public String getBrandImagePath() {
		return brandImagePath;
	}

	public void setBrandImagePath(String brandImagePath) {
		this.brandImagePath = brandImagePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "SortModel{" +
				"name='" + name + '\'' +
				", sortLetters='" + sortLetters + '\'' +
				", brandId='" + brandId + '\'' +
				", brandImage='" + brandImage + '\'' +
				", brandImagePath='" + brandImagePath + '\'' +
				", remark='" + remark + '\'' +
				'}';
	}
}
