package com.rch.square;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * 
 * @author 王斌
 *
 */
public class Crop {
	public static final String PicturePath="image_crop";
	public static final int REQUEST_CROP = 6709;
	public static final int REQUEST_PICK = 9162;
	public static final int REQUEST_CAMERA = 9164;
	public static final int RESULT_ERROR = 404;
	public static File file;

	public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/education";

	static interface Extra {
		String TYPE = "type";
		String ASPECT_X = "aspect_x";
		String ASPECT_Y = "aspect_y";
		String MAX_X = "max_x";
		String MAX_Y = "max_y";
		String ERROR = "error";
	}

	private Intent cropIntent;

	/**
	 * Create a crop Intent builder with source image
	 * 
	 * @param source
	 *            Source image URI
	 */
	public Crop(Uri source) {
		cropIntent = new Intent();
		cropIntent.setData(source);
	}

	/**
	 * type
	 * 
	 * @param type
	 * @return
	 */
	public com.rch.square.Crop setType(int type) {
		cropIntent.putExtra(Extra.TYPE, type);
		return this;
	}

	/**
	 * Set output URI where the cropped image will be saved
	 * 
	 * @param output
	 *            Output image URI
	 */
	public com.rch.square.Crop output(Uri output) {
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		return this;
	}

	/**
	 * Set fixed aspect ratio for crop area
	 * 
	 * @param x
	 *            Aspect X
	 * @param y
	 *            Aspect Y
	 */
	public com.rch.square.Crop withAspect(int x, int y) {
		cropIntent.putExtra(Extra.ASPECT_X, x);
		cropIntent.putExtra(Extra.ASPECT_Y, y);
		return this;
	}

	/**
	 * Crop area with fixed 1:1 aspect ratio
	 */
	public com.rch.square.Crop asSquare() {
		cropIntent.putExtra(Extra.ASPECT_X, 1);
		cropIntent.putExtra(Extra.ASPECT_Y, 1);
		return this;
	}

	/**
	 * Set maximum crop size
	 * 
	 * @param width
	 *            Max width
	 * @param height
	 *            Max height
	 */
	public com.rch.square.Crop withMaxSize(int width, int height) {
		cropIntent.putExtra(Extra.MAX_X, width);
		cropIntent.putExtra(Extra.MAX_Y, height);
		return this;
	}

	/**
	 * Send the crop Intent!
	 * 
	 * @param activity
	 *            Activity that will receive result
	 */
	public void start(Activity activity) {
		activity.startActivityForResult(getIntent(activity), REQUEST_CROP);
	}

	/**
	 * Send the crop Intent!
	 * 
	 * @param context
	 *            Context
	 * @param fragment
	 *            Fragment that will receive result
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void start(Context context, Fragment fragment) {
		fragment.startActivityForResult(getIntent(context), REQUEST_CROP);
	}

	Intent getIntent(Context context) {
		cropIntent.setClass(context, CropImageActivity.class);
		return cropIntent;
	}

	/**
	 * Retrieve URI for cropped image, as set in the Intent builder
	 * 
	 * @param result
	 *            Output Image URI
	 */
	public static Uri getOutput(Intent result) {
		return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
	}

	/**
	 * Retrieve error that caused crop to fail
	 * 
	 * @param result
	 *            Result Intent
	 * @return Throwable handled in CropImageActivity
	 */
	public static Throwable getError(Intent result) {
		return (Throwable) result.getSerializableExtra(Extra.ERROR);
	}

	/**
	 * 
	 * @param fragment
	 * @param context
	 */
	 
	public static void pickAlbumsImage(final Fragment fragment, final Context context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
		try {
			if (fragment!=null) {
				fragment.startActivityForResult(intent,REQUEST_PICK);
			}else {
				((Activity)context).startActivityForResult(intent,REQUEST_PICK);
			}
		} catch (ActivityNotFoundException e) {
//			Toast.makeText(context, R.string.crop__pick_error,Toast.LENGTH_SHORT).show();
		}
	}
    
	/**
	 * 发送拍照请求
	 * @param fragment
	 * @param context
	 */
	public static void pickCameraImage(final Fragment fragment, final Context context) {
		try {
			Intent intent = new Intent();
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 指定开启系统相机的Action
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				// 根据文件地址创建文件   先创建png格式的图片做过渡  
				file = new File(FILE_PATH);
				if (file.exists()) {
					file.delete();
				}
				// 把文件地址转换成Uri格式
				Uri uri = Uri.fromFile(file);
				// 设置系统相机拍摄照片完成后图片文件的存放地址
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			if (fragment!=null) {
				fragment.startActivityForResult(intent,REQUEST_CAMERA);
			}else {
				((Activity)context).startActivityForResult(intent, REQUEST_CAMERA);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			Toast.makeText(context, R.string.crop__pick_error,Toast.LENGTH_SHORT).show();
		}
	}
}
