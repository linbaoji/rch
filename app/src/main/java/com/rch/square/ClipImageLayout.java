package com.rch.square;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.rch.R;


/**
 * @author wangbin
 * 
 * 
 */
public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	private Drawable image;
	boolean dispath =false;
	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 0;

	public ClipImageLayout(Context context) {
		this(context, null);
	}

	public ClipImageLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ClipImageLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ClipImageLayout);

		// image=mTypedArray.getDrawable(R.styleable.ClipImageLayout_clip_image);

		mTypedArray.recycle();
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */

		setImage(image);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

	public void setImage(Drawable image) {
//		 int w= View.MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
//		 int h=View.MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
//		 mClipImageView.measure(w, h);
		if (image != null) {
			// Bitmap
			// bitmap=BitmapUtils.zoomImg(BitmapUtils.drawable2Bitmap(image),GlobalParams.screenWidth,GlobalParams.screenWidth);
			mZoomImageView.setImageDrawable(image);
		}
	}

	/**
	 * 根据方框设置图片
	 */
	public void setImageWH() {//480  480
			
			mZoomImageView.setImageWH();
			
	}
	
	/**
	 * 恢复根据方框设置图片
	 */
	public void reSetImageWH(){
		mZoomImageView.reSetImageWH();
	}

	public void setImageFocus(boolean isFocus) {
		mZoomImageView.isAutoScale = true;
		mZoomImageView.setFocusable(isFocus);
		mZoomImageView.setEnabled(isFocus);
		invalidate();
	}


	/**设置图片是否可触摸
	 * @param can
	 */
	public void setCanTouch(boolean can) {
		mZoomImageView.setcantTouch(can);
	}
}
