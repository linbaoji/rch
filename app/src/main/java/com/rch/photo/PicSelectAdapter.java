package com.rch.photo;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rch.R;
import com.rch.common.ToastTool;
import com.rch.entity.ImageBean;
import com.rch.view.MyImageView;


public class PicSelectAdapter extends BaseAdapter {

	Context context;
	private Point mPoint = new Point(0, 0);
	AlbumBean bean;
	private GridView mGridView;
	PicSelectActivity.OnImageSelectedListener onImageSelectedListener;
	PicSelectActivity.OnImageSelectedCountListener onImageSelectedCountListener;
    private int totalCount;
	public PicSelectAdapter(Context context, GridView mGridView,
			PicSelectActivity.OnImageSelectedCountListener onImageSelectedCountListener,int totalCount) {
		this.context = context;
		this.mGridView = mGridView;
		this.onImageSelectedCountListener = onImageSelectedCountListener;
		this.totalCount=totalCount;
	}

	public void taggle(AlbumBean bean) {
		this.bean = bean;
		notifyDataSetChanged();
	}

	public void setOnImageSelectedListener(
			PicSelectActivity.OnImageSelectedListener onImageSelectedListener) {
		this.onImageSelectedListener = onImageSelectedListener;
	}

	@Override
	public int getCount() {
		return bean == null || bean.count == 0 ? 0 : bean.count;
	}

	@Override
	public Object getItem(int position) {
		return bean == null ? null : bean.sets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		final ImageBean ib = (ImageBean) getItem(index);
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.the_picture_selection_item, null);
			viewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.child_checkbox);
			viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		viewHolder.mImageView.setTag(ib.path);

		if (index == 0) {
			viewHolder.mImageView.setImageResource(R.drawable.tk_photo);
			viewHolder.mCheckBox.setVisibility(View.GONE);
		} else {
			viewHolder.mCheckBox.setVisibility(View.VISIBLE);
			viewHolder.mCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int count = onImageSelectedCountListener
									.getImageSelectedCount();
							if (count == totalCount && isChecked) {
								ToastTool.show(context,
										"只能添加" +totalCount + "张");
								viewHolder.mCheckBox.setChecked(ib.isChecked);
							} else {
								if (!ib.isChecked && isChecked) {
									addAnimation(viewHolder.mCheckBox);
								}
								ib.isChecked = isChecked;
							}
							onImageSelectedListener.notifyChecked();
						}
					});
			if (ib.isChecked) {
				viewHolder.mCheckBox.setChecked(true);
			} else {
				viewHolder.mCheckBox.setChecked(false);
			}

			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
					ib.path, mPoint, new NativeImageLoader.NativeImageCallBack() {

						@Override
						public void onImageLoader(Bitmap bitmap, String path) {
							ImageView mImageView = (ImageView) mGridView
									.findViewWithTag(ib.path);
							if (bitmap != null && mImageView != null) {
								mImageView.setImageBitmap(bitmap);
							}
						}
					});

			if (bitmap != null) {
				viewHolder.mImageView.setImageBitmap(bitmap);
			} else {
				viewHolder.mImageView
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}
		}
		return convertView;
	}

	/**
	 * 
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	public static class ViewHolder {
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}

}
