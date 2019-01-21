package com.rch.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


import com.github.chrisbanes.photoview.PhotoView;
import com.rch.entity.ImageBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageBrowserAdapter extends PagerAdapter {

	private List<ImageBean> mPhotos = new ArrayList<ImageBean>();
	Context context;

	public ImageBrowserAdapter(Context context, List<ImageBean> photos) {
		this.context = context;
		if (photos != null) {
			mPhotos = photos;
		}
	}

	@Override
	public int getCount() {
		if (mPhotos.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());

		String path = mPhotos.get(position % mPhotos.size()).path;
		photoView.setImageDrawable(new BitmapDrawable(LoadBigImg(path,360, 360)));
//		if (path.startsWith("http://") || path.startsWith("https://")) {
//			// 这里进行图片的缓存操作
//			Picasso.with(context).load(path).into(photoView);
//		} else {
//			Picasso.with(context).load(new File(path)).into(photoView);
//		}
		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	/**
	 * 
	 * 加载大图片
	 * 
	 * @param
	 * @param newWidth
	 *            指定分辨率
	 * @param newHeight
	 * @return
	 */
	public static Bitmap LoadBigImg(String path, int newWidth, int newHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		int scale;

		scale = Math.max(bitmapWidth / newWidth, bitmapHeight / newHeight);

		// 缩放的比例
		options.inSampleSize = scale;

		options.inJustDecodeBounds = false;
		// 摆正
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		int degree = getExifOrientation(path);
		if (degree == 90 || degree == 180 || degree == 270) {
			// Roate preview icon according to exif orientation
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} else {
			// do not need roate the icon,default
			return bitmap;
		}

	}
	
	/**
	 * 获取图片的朝向
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			// MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
				}
			}
		}

		return degree;
	}
}
