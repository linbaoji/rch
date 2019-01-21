package com.rch.photo;

/**
 * 
 * @author join
 * 
 */
public class ConfigPhoto {

//	public static int limit;
	static String savePathString;

	static {
//		limit = 5;
		savePathString = "/temp";
	}

//	public static void setLimit(int limit) {
//		Config.limit = limit;
//	}
//
//	public static int getLimit() {
//		return limit;
//	}

	public static void setSavePath(String path) {
		ConfigPhoto.savePathString = path;
	}

	public static String getSavePath() {
		return savePathString;
	}
}
