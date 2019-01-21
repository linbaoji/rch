package com.rch.square;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.rch.R;
import com.rch.common.MyBitmapUtils;
import com.rch.common.ToastTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author wangbin
 * 
 */
public class CropImageActivity extends com.rch.square.MonitoredActivity {

	private static final boolean IN_MEMORY_CROP = Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1;
	private static final int SIZE_DEFAULT = 2048;
	private static final int SIZE_LIMIT = 4096;

	private final Handler handler = new Handler();

	private int type;
	private String filepath;

	private int aspectX;
	private int aspectY;

	// Output image size
	private int maxX;
	private int maxY;
	private int exifRotation;

	private Uri sourceUri;
	private Uri saveUri;

	private boolean isSaving;
	/** 传过来的图片 */
	private Bitmap bitmap;
	private int sampleSize;
	private ClipImageLayout clipImage;

	private TextView tvCancel, tvDone;

	private boolean isOrientationChanged;
	/** 切换锁定 */
	private boolean flag = true;
	private String path;
	private ProgressDialog loadingDialog;
	String pathfile;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// initViews();
		setContentView(R.layout.activity_crop);
		init();
	}



//	@Override
//	public void setLayout() {
//		setContentView(R.layout.activity_crop);
//	}


	public void init() {
		clipImage = (ClipImageLayout) findViewById(R.id.crop_image);
		tvCancel = (TextView) findViewById(R.id.btn_cancel);
		tvDone = (TextView) findViewById(R.id.btn_done);
		setupFromIntent();
		// 这步必须要加
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setTopTitle("裁剪照片");
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("请稍后...");
		path = getIntent().getStringExtra("path");
//		MyLogUtils.info("裁剪查找地址：" + path);
		if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
			ToastTool.show(this, "图片加载失败");
			return;
		}
		Bitmap bitmap = MyBitmapUtils.convertToBitmap(path,480,800);
		if (bitmap == null) {
			ToastTool.show(this, "图片加载失败");
			return;
		}
		clipImage.setImage(new BitmapDrawable(bitmap));
		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap bitmap = clipImage.clip();
						Log.e("宽度",bitmap.getWidth()+""+bitmap.getHeight());
						pathfile = Environment
								.getExternalStorageDirectory()
								+ "/ClipHeadPhoto/cache/"
								+ System.currentTimeMillis() + ".png";
//						MyBitmapUtils.savePhotoToSDCard(bitmap, path);
//						MyLogUtils.info("拍照图片地址是："+path);
						loadingDialog.dismiss();
						Intent intent = new Intent();
						intent.putExtra("path", path);
						setResult(RESULT_OK, intent);
						//info.setMemberpic(path);
						finish();
					}
				}).start();
			}
		});

	}


	private void setupFromIntent() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			type = extras.getInt(Crop.Extra.TYPE);
			aspectX = extras.getInt(Crop.Extra.ASPECT_X);
			aspectY = extras.getInt(Crop.Extra.ASPECT_Y);
			maxX = extras.getInt(Crop.Extra.MAX_X);
			maxY = extras.getInt(Crop.Extra.MAX_Y);
			saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
		}
		if (type == 0) {
			sourceUri = intent.getData();
			if (sourceUri != null) {
				exifRotation = CropUtil.getExifRotation(CropUtil
						.getFromMediaUri(getContentResolver(), sourceUri));
				InputStream is = null;
				try {
					sampleSize = calculateBitmapSampleSize();
					is = getContentResolver().openInputStream(sourceUri);
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = sampleSize;
					bitmap = BitmapFactory.decodeStream(is, null, option);
					// rotateBitmap = new
					// RotateBitmap(BitmapFactory.decodeStream(
					// is, null, option), exifRotation);
				} catch (IOException e) {
					Log.e("tag", "Error reading image: " + e.getMessage());
					setResultException(e);
				} catch (OutOfMemoryError e) {
					Log.e("tag", "OOM reading image: " + e.getMessage());
					setResultException(e);
				} finally {
					CropUtil.closeSilently(is);
				}
			}
		} else {
			filepath = Crop.file.getAbsolutePath();
			if (filepath != null) {
				exifRotation = CropUtil.getExifRotation(Crop.file
						.getAbsoluteFile());
				InputStream is = null;
				try {
					sampleSize = calculateBitmapSampleSize();
					is = new FileInputStream(Crop.file);
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = sampleSize;
					bitmap = BitmapFactory.decodeStream(is, null, option);
				} catch (IOException e) {
					setResultException(e);
				} catch (OutOfMemoryError e) {
					setResultException(e);
				} finally {
					CropUtil.closeSilently(is);
				}
			}
		}
	}

	private int calculateBitmapSampleSize() throws IOException {
		InputStream is = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			if (type == 0) {
				is = getContentResolver().openInputStream(sourceUri);
			} else {
				is = new FileInputStream(Crop.file);
			}
			// Just get image size
			BitmapFactory.decodeStream(is, null, options);
		} finally {
			CropUtil.closeSilently(is);
		}
		int maxSize = getMaxImageSize();
		int sampleSize = 1;
		while (options.outHeight / sampleSize > maxSize
				|| options.outWidth / sampleSize > maxSize) {
			sampleSize = sampleSize << 1;
		}
		return sampleSize;
	}

	private int getMaxImageSize() {
		int textureLimit = getMaxTextureSize();
		if (textureLimit == 0) {
			return SIZE_DEFAULT;
		} else {
			return Math.min(textureLimit, SIZE_LIMIT);
		}
	}

	private int getMaxTextureSize() {
		// The OpenGL texture size is the maximum size that can be drawn in an
		// ImageView
		int[] maxSize = new int[1];
		GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
		return maxSize[0];
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	@Override
	public boolean onSearchRequested() {
		return false;
	}

	public boolean isSaving() {
		return isSaving;
	}

	private void setResultUri(Uri uri) {
//		Bitmap bitmap = mClipImageLayout.clip();
//		pathfile = Environment
//				.getExternalStorageDirectory()
//				+ "/ClipHeadPhoto/cache/"
//				+ System.currentTimeMillis() + ".png";
//		MyBitmapUtils.savePhotoToSDCard(bitmap, path);
//		MyLogUtils.info("拍照图片地址是："+path);
		loadingDialog.dismiss();
		Intent intent = new Intent();
		intent.putExtra("path", path);
		setResult(RESULT_OK, intent);
//		setResult(RESULT_OK,
//				new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
	}

	private void setResultException(Throwable throwable) {
		setResult(Crop.RESULT_ERROR,
				new Intent().putExtra(Crop.Extra.ERROR, throwable));
	}

	private void saveImage(final Bitmap croppedImage) {
		if (croppedImage != null) {
			CropUtil.startBackgroundJob(this, null,
					"图片保存中",
					new Runnable() {
						public void run() {
							saveOutput(croppedImage);
						}
					}, handler);
		} else {
			finish();
		}
	}

	/**
	 * 保存截取的图片
	 * 
	 * @param croppedImage
	 */
	private void saveOutput(Bitmap croppedImage) {
		if (saveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = getContentResolver().openOutputStream(saveUri);
				if (outputStream != null) {
					croppedImage.compress(Bitmap.CompressFormat.JPEG, 90,
							outputStream);
				}
			} catch (IOException e) {
				setResultException(e);
				Log.e("tag", "Cannot open file: " + saveUri);
			} finally {
				CropUtil.closeSilently(outputStream);
			}
			if (!IN_MEMORY_CROP) {
				// In-memory crop negates the rotation
				CropUtil.copyExifRotation(CropUtil.getFromMediaUri(
						getContentResolver(), sourceUri), CropUtil
						.getFromMediaUri(getContentResolver(), saveUri));
			}
			setResultUri(saveUri);
		}

		final Bitmap b = croppedImage;
		handler.post(new Runnable() {
			public void run() {
				// imageView.clear();
				b.recycle();
			}
		});
		finish();
	}
	
	 private  float convertRationalLatLonToFloat(String rationalString, String ref) {
	        try {
	            String[] parts = rationalString.split(",");


	            String[] pair;
	            pair = parts[0].split("/");
	            int degrees = (int) (Float.parseFloat(pair[0].trim())
	                    / Float.parseFloat(pair[1].trim()));


	            pair = parts[1].split("/");
	            int minutes = (int) ((Float.parseFloat(pair[0].trim())
	                    / Float.parseFloat(pair[1].trim())));


	            pair = parts[2].split("/");
	            float seconds = Float.parseFloat(pair[0].trim())
	                    / Float.parseFloat(pair[1].trim());


	            float result = degrees + (minutes / 60F) + (seconds / (60F * 60F));
	            if ((ref.equals("S") || ref.equals("W"))) {
	                return -result;
	            }
	            return result;
	        } catch (RuntimeException ex) {
	            // if for whatever reason we can't parse the lat long then return
	            // null
	            return 0f;
	        }
	    }


}
