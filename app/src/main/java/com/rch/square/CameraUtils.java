package com.rch.square;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Toast;


import com.rch.common.ToastTool;

import java.io.File;

public class CameraUtils {

	
	/**
	 * 获取图片的uri
	 * 
	 * @param context
	 * @param requestCode
	 * @param resultCode
	 * @param
	 * @return
	 */
	public static Uri getBitmapUri(Fragment fragment, Context context,
                                   int requestCode, int resultCode, Intent result) {
		if (requestCode == com.rch.square.Crop.REQUEST_PICK) {
			beginCrop(result.getData(), fragment, context);
		} else if (requestCode == com.rch.square.Crop.REQUEST_CAMERA) {
			if (fragment != null) {
				new com.rch.square.Crop(null).setType(1).output(Uri.fromFile(com.rch.square.Crop.file))
						.asSquare().start(context, fragment);
			} else {
				if(com.rch.square.Crop.file!=null){
				new com.rch.square.Crop(null).setType(1).output(Uri.fromFile(com.rch.square.Crop.file))
						.asSquare().start((Activity) context);
				}
			}
		} else if (requestCode == com.rch.square.Crop.REQUEST_CROP) {
			return handleCrop(resultCode, result, context);
		}
		return null;
	}

	/**
	 * 获取相册图片
	 *
	 * @param source
	 */
	private static void beginCrop(Uri source, Fragment fragment, Context context) {
		Uri outputUri = Uri
				.fromFile(new File(context.getCacheDir(), "cropped"));
		if (fragment != null) {
			new com.rch.square.Crop(source).setType(0).output(outputUri).asSquare()
					.start(context, fragment);
		} else {
			new com.rch.square.Crop(source).setType(0).output(outputUri).asSquare()
					.start((Activity) context);
		}

	}

	/**
	 * 跳转
	 *
	 * @param resultCode
	 * @param result
	 */
	private static Uri handleCrop(int resultCode, Intent result, Context context) {
		if (resultCode == Activity.RESULT_OK) {
			return com.rch.square.Crop.getOutput(result);
		} else if (resultCode == com.rch.square.Crop.RESULT_ERROR) {
			ToastTool.show(context, Crop.getError(result).getMessage());
		}
		return null;
	}
	
}
