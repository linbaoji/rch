package com.rch.adatper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rch.R;
import com.rch.common.MyBitmapUtils;
import com.rch.custom.MyAlertDialog;
import com.rch.entity.ImageBean;
import com.rch.photo.ImageBrowserActivity;
import com.rch.photo.PicSelectActivity;

import java.io.Serializable;
import java.util.List;

/**
 * 照片Adapter
 * 
 */
public class GvPhotoAdapter extends BaseAdapter {
	private List<ImageBean> list;
	private Activity context;
	public static final int PHOTO_CODE0 = 0x456;
	public static final int ADD_PHOTO_CODE = 0x300;

	private int READ_EXTERNAL_STORAGE=112;
	private int maxCount;

	private int photoSize=0;//如果为1则设置为60X60的
	private int pos=0;

	public GvPhotoAdapter(List<ImageBean> list,int maxCount,Activity context) {
		this.list = list;
		this.context = context;
		this.maxCount=maxCount;
	}

	public int getPhotoSize() {
		return photoSize;
	}

	public void setPhotoSize(int photoSize) {
		this.photoSize = photoSize;
	}

	public void notifyDataChange(List<ImageBean> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public int getListSize(){
		if (list!=null) {
			return list.size();
		}else {
			return 0;
		}
	}

	public List<ImageBean> getList(){
		return list;
	}

	public int getPos(){
		return pos;
	}
	@Override
	public int getCount() {
		return list.size() == maxCount? list.size() : list.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.photo_gv_item, null);
			viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
			viewHolder.ivLeftTop = (ImageView) convertView.findViewById(R.id.iv_fm);
			viewHolder.ivRightTop = (ImageView) convertView.findViewById(R.id.iv_fm_deleat);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		pos=position;
		ViewGroup.LayoutParams ps = viewHolder.ivPhoto.getLayoutParams();
		if (getPhotoSize()==1) {
//			ps.width=216;
			ps.height = 280;
			viewHolder.ivPhoto.setLayoutParams(ps);
		}else {
//			ps.width=216;
			ps.height = 280;
			viewHolder.ivPhoto.setLayoutParams(ps);
		}
		if (list.size() < maxCount) {
			if (list.size()>=1 && position == 0){
				viewHolder.ivLeftTop.setVisibility(View.VISIBLE);
				viewHolder.ivRightTop.setVisibility(View.VISIBLE);
			}else {
				viewHolder.ivLeftTop.setVisibility(View.GONE);
				viewHolder.ivRightTop.setVisibility(View.GONE);
			}
			if (position == list.size()) {
				viewHolder.ivPhoto.setImageBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.mipmap.shangc));
				viewHolder.ivPhoto.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if(Build.VERSION.SDK_INT>=23){
							if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
									!= PackageManager.PERMISSION_GRANTED) {
								ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
										READ_EXTERNAL_STORAGE);
							}else{
								Intent intent = new Intent(context,
										PicSelectActivity.class);
								intent.putExtra("select_count", list.size());
								intent.putExtra(PicSelectActivity.TOTAL_COUNT, maxCount);
								context.startActivityForResult(intent, ADD_PHOTO_CODE);
							}
						}else {
							try {
								Intent intent = new Intent(context,
										PicSelectActivity.class);
								intent.putExtra("select_count", list.size());
								intent.putExtra(PicSelectActivity.TOTAL_COUNT, maxCount);
								context.startActivityForResult(intent, ADD_PHOTO_CODE);
							}catch (Exception e){
								e.printStackTrace();
								MyAlertDialog dialog = new MyAlertDialog(context).builder();
								dialog.setTitle("提示").setMsg("获取文件权限未打开，请求开启权限").setCancelable(false)
										.setPositiveButton("立即开启", new OnClickListener() {
											@Override
											public void onClick(View v) {
												//跳到设置界面
												Intent intent = new Intent(Settings.ACTION_SETTINGS);
												context.startActivity(intent);
											}
										}).setNegativeButton("稍后再说", null).show();
//								MyToastUtils.showShortToast(context,"获取文件权限被取消");
							}
						}
					}
				});
			} else {

//				if (getPhotoSize()==1){
//					viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.LoadBigImg(list
//							.get(position).path, 108, 108));
//				}else {
//					viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.LoadBigImg(list
//							.get(position).path, 108, 108));
//				}
				viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.getSmallBitmap(list.get(position).path));
				viewHolder.ivPhoto.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								ImageBrowserActivity.class);
						intent.putExtra("images", (Serializable) list);
						intent.putExtra("position", position);
						intent.putExtra("isdel", true);
						context.startActivityForResult(intent, PHOTO_CODE0);
					}
				});
			}
		} else {
//			if (getPhotoSize()==1){
//				viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.LoadBigImg(list
//						.get(position).path, 108, 108));
//			}else {
//				viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.LoadBigImg(list
//						.get(position).path, 108, 108));
//			}
			viewHolder.ivPhoto.setImageBitmap(MyBitmapUtils.getSmallBitmap(list.get(position).path));
			viewHolder.ivPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							ImageBrowserActivity.class);
					intent.putExtra("images", (Serializable) list);
					intent.putExtra("position", position);
					intent.putExtra("isdel", true);
					context.startActivityForResult(intent, PHOTO_CODE0);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		ImageView ivPhoto;
		ImageView ivLeftTop;
		ImageView ivRightTop;
	}


//	private interface AddImage{
//
//	}
//
//	private AddImage addImage;
//
//	public AddImage getAddImage() {
//		return addImage;
//	}
//
//	public void setAddImage(AddImage addImage) {
//		this.addImage = addImage;
//	}
}
