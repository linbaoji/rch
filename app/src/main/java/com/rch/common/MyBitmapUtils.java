package com.rch.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.rch.base.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * 图片工具类
 *
 * @author wangbin
 *
 */
public class MyBitmapUtils {

	//图片文件的路径地址转化为Uri
	public static Uri pathToUri(Context context,String path){
		Uri uri=null;
		if (path != null) {
			path = Uri.decode(path);
			Log.d("==", "path2 is " + path);
			ContentResolver cr = context.getContentResolver();
			StringBuffer buff = new StringBuffer();
			buff.append("(")
					.append(MediaStore.Images.ImageColumns.DATA)
					.append("=")
					.append("'" + path + "'")
					.append(")");
			Cursor cur = cr.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Images.ImageColumns._ID},
					buff.toString(), null, null);
			int index = 0;
			for (cur.moveToFirst(); !cur.isAfterLast(); cur
					.moveToNext()) {
				index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
				// set _id value
				index = cur.getInt(index);
			}
			if (index == 0) {
				//do nothing
			} else {
				Uri uri_temp = Uri
						.parse("content://media/external/images/media/"
								+ index);
				Log.d("==", "uri_temp is " + uri_temp);
				if (uri_temp != null) {
					uri = uri_temp;
				}
			}
		}
		return uri;
	}
	/**
	 * 缩放图片--- 指定分辨率
	 *
	 * @param bm
	 * @param newWidth
	 *            指定分辨率
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 * 缩放图片--保持长宽比
	 *
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImgKeepWH(Bitmap bm, int newWidth, int newHeight, boolean isMax) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		float scale;
		if (isMax) {
			scale = Math.max(scaleWidth, scaleHeight);
		} else {
			scale = Math.min(scaleWidth, scaleHeight);
		}

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		int width1=newbm.getWidth();
		int height1=newbm.getHeight();
		return newbm;
	}

	/**
	 * bitmap转byte数组
	 *
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 *
	 * 加载大图片
	 *
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
		try{
			bitmap.getWidth();
		}catch(Exception e) {
//			MyLogUtils.info("图片有误！！！");
			return null;
		}
		int degree = getExifOrientation(path);//获取图片朝向
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
	 * Drawable 转 bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
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

	/**
	 * 根据路径加载bitmap
	 *
	 * @param path
	 *            路径
	 * @param w
	 *            款
	 * @param h
	 *            长
	 * @return
	 */
	public static final Bitmap convertToBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 设置为ture只获取图片大小
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			// 返回为空
			BitmapFactory.decodeFile(path, opts);
			int width = opts.outWidth;
			int height = opts.outHeight;
			float scaleWidth = 0.f, scaleHeight = 0.f;
			if (width > w || height > h) {
				// 缩放
				scaleWidth = ((float) width) / w;
				scaleHeight = ((float) height) / h;
			}
			opts.inJustDecodeBounds = false;
			float scale = Math.max(scaleWidth, scaleHeight);
			opts.inSampleSize = (int) scale;
			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));

			int degree = getExifOrientation(path);
			if (degree == 90 || degree == 180 || degree == 270) {
				// Roate preview icon according to exif orientation
				Matrix matrix = new Matrix();
				matrix.postRotate(degree);
				return Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), matrix, true);
			} else {
				// do not need roate the icon,default
				return Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), null, true);
			}
//			Bitmap bMapRotate = Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), null, true);
//			if (bMapRotate != null) {
//				return bMapRotate;
//			}
//			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void savePhotoToSDCard(Bitmap photoBitmap, String path){
		if (checkSDCardAvailable()) {
			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Check the SD card
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 根据uri获取bitmap
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		if (context == null || uri == null)
			return null;
		Bitmap bitmap;
		try {
			bitmap = getBitmapFormUri(context, uri);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 保存图片
	 * @param bitmap
	 * @param file
	 * @return
	 */
	public static File saveBitmap(final Bitmap bitmap, final String file){
		final File f = new File(Environment.getExternalStorageDirectory()+"/"+file);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}else{
			f.delete();
			scanFileAsync(MyApplication.getInstance().getApplicationContext(),f);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options=100;
		bitmap.compress(Bitmap.CompressFormat.JPEG,options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	//更新缩略图
	public static void scanFileAsync(Context ctx, File file) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(file));
		ctx.sendBroadcast(scanIntent);
	}
	/**
	 * 保存图片
	 * @param bitmap
	 * @param file
	 * @return
	 */
	public static File saveBitmap2(final Bitmap bitmap, final String file){
		final File f = new File(Environment.getExternalStorageDirectory()+"/"+file);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
		}else{
			f.delete();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int options=80;
		bitmap.compress(Bitmap.CompressFormat.PNG,90, baos);
//		while (baos.toByteArray().length / 1024 > 100) {
//			baos.reset();
//			options -= 10;
//			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//		}
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// 比例压缩
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		// 摆正
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		try{
			bitmap.getWidth();
		}catch(Exception e) {
//			Log.i().info("图片有误！！！");
			return null;
		}
		int degree = getExifOrientation(filePath);
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
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	/**
	 * 根据uri获取bitmap
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap2(Context context, Uri uri) {
		if (context == null || uri == null)
			return null;
		return getSmallBitmap(getRealFilePath(context, uri));
	}

	public static String getRealFilePath(final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 比例压缩
	 * @param ac
	 * @param uri
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Bitmap getBitmapFormUri(Context ac, Uri uri) throws FileNotFoundException, IOException {
		InputStream input = ac.getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;//加载图片，它只加载图片宽高，不加载图片真正信息。防止oom
		onlyBoundsOptions.inDither = true;//optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();

		int originalWidth = onlyBoundsOptions.outWidth;
		int originalHeight = onlyBoundsOptions.outHeight;

		Log.e("原图",originalWidth+""+originalHeight+"");

		if ((originalWidth == -1) || (originalHeight == -1))
			return null;
		//图片分辨率以480x800为标准
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (originalWidth / ww);
		} else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (originalHeight / hh);
		}
		if (be <= 0)
			be = 1;
		//比例压缩
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = be;//设置缩放比例
		bitmapOptions.inDither = true;//optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		input = ac.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();

		return compressImage(bitmap);//再进行质量压缩
	}

	/**
	 * 保存图片到图库
	 * @param context
	 * @param bmp
	 */
	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"desheng");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
//			MyToastUtils.showShortToast(context, "保存失败");
			e.printStackTrace();
		} catch (IOException e) {
//			MyToastUtils.showShortToast(context, "保存失败");
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
//			MyToastUtils.showShortToast(context, "保存成功");
		} catch (FileNotFoundException e) {
//			MyToastUtils.showShortToast(context, "保存失败");
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(new File(file.getPath()))));
	}

}
