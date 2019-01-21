package com.rch.custom;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.cunoraz.gifview.library.GifView;
import com.rch.R;
import com.rch.base.MyApplication;




/**
 * 加载类
 * @author wangbin
 *
 */
public class LoadingView extends RelativeLayout {
	private LinearLayout mNoContent;
	private TextView mNoContentTxt;
	private TextView mNoContentTxtLeft;
	private TextView mNoContentTxtRight;
	private ImageView mNoContentIco;
	private LinearLayout mLoading;
	private TextView mLoadingTxt;
	private GifView mLoadingIco;
	private LinearLayout mLoadError;
	private OnRetryListener mOnRetryListener;
	private OnClickGoListener mOnClickGoListener;
	private ImageView imageView1;
	private TextView tv_content,tv_refresh;
	private TextView tv_ok;//底部新增加按钮（发布车辆，添加员工等）

	public LoadingView(Context context) {
		super(context, null);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr,
                       int defStyleRes) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.loading_view, this);

		mNoContent = (LinearLayout) findViewById(R.id.nocontent);//暂无类容布局
		mNoContentTxt = (TextView) findViewById(R.id.nocontent_txt);//暂无类容字体
		mNoContentTxtLeft = (TextView) findViewById(R.id.nocontent_txt_left);//暂无类容字体
		mNoContentTxtRight = (TextView) findViewById(R.id.nocontent_txt_right);//暂无类容字体
		mNoContentIco = (ImageView) findViewById(R.id.nocontent_ico);//暂无内龙icon

		mLoading = (LinearLayout) findViewById(R.id.loading);//加载
		mLoadingTxt = (TextView) findViewById(R.id.loading_txt);//加载字体

		mLoadingIco= (GifView) findViewById(R.id.loading_ico);//加载动图
//		mLoadingIco.setGifResource(R.drawable.animt);
		mLoadingIco.setGifResource(R.drawable.load);

		imageView1 = (ImageView) findViewById(R.id.imageView1);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_refresh = (TextView) findViewById(R.id.tv_refresh);

		tv_ok = (TextView) findViewById(R.id. tv_go);



		mLoadError = (LinearLayout) findViewById(R.id.loaderror);//失败的
		mLoadError.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mOnRetryListener != null){
					mOnRetryListener.OnRetry();
				}
				loading();
			}
		});

		loading();
	}

	//点击底部按钮
	public void clickButton(String content,View.OnClickListener listener){
		tv_ok.setVisibility(View.VISIBLE);
		tv_ok.setText(content);
		if(listener!=null){
			tv_ok.setOnClickListener(listener);
		}
	}
	public void noContent() {
		mLoadingIco.pause();
		setVisibility(View.VISIBLE);
		mNoContent.setVisibility(View.VISIBLE);
		mLoading.setVisibility(View.GONE);
		mLoadError.setVisibility(View.GONE);
		tv_ok.setVisibility(View.GONE);
	}

	public void setNoContentTxt(String s) {
		tv_ok.setVisibility(View.GONE);
		mNoContentTxt.setText(s);
	}

	public void setNoContentTxt(String left,String s,String right) {
		tv_ok.setVisibility(View.GONE);
		mNoContentTxtLeft.setVisibility(VISIBLE);
		mNoContentTxtRight.setVisibility(VISIBLE);
		mNoContentTxtLeft.setText(left);
		mNoContentTxt.setText(s);
		mNoContentTxt.setTextColor(getResources().getColor(R.color.orange_1));
		mNoContentTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnClickGoListener!=null){
					mOnClickGoListener.click();
				}
			}
		});
		mNoContentTxtRight.setText(right);
	}
	public void setNoContentTxt(int r) {
		mNoContentTxt.setText(r);
	}

	public void setNoContentIco(int r) {
		mNoContentIco.setImageResource(r);
	}

	public void loading() {
		setVisibility(View.VISIBLE);
		mLoadingIco.play();
		mNoContent.setVisibility(View.GONE);
		mLoading.setVisibility(View.VISIBLE);
		mLoadError.setVisibility(View.GONE);
		tv_ok.setVisibility(View.GONE);
	}

	public void setLoadingTxt(String s) {
		mLoadingTxt.setText(s);
	}

	public void setLoadingTxt(int r) {
		mLoadingTxt.setText(r);
	}

	public void setLoadingIco(int r) {
//		mLoadingIco.setImageResource(r);
	}

	public void loadComplete() {
		mLoadingIco.pause();
		setVisibility(View.GONE);
		tv_ok.setVisibility(View.GONE);
	}
	
	public void loadError(){
		mLoadingIco.pause();
		setVisibility(View.VISIBLE);
		mNoContent.setVisibility(View.GONE);
		mLoading.setVisibility(View.GONE);
		mLoadError.setVisibility(View.VISIBLE);
		tv_ok.setVisibility(View.GONE);
	}

	public void loadNodata(int res,String content){//下架
		mLoadingIco.pause();
		setVisibility(View.VISIBLE);
		mNoContent.setVisibility(View.GONE);
		mLoading.setVisibility(View.GONE);
		mLoadError.setVisibility(View.VISIBLE);
		imageView1.setImageResource(res);
		tv_content.setText(content);
		tv_refresh.setVisibility(GONE);
		tv_ok.setVisibility(View.GONE);
	}
	
	public interface OnRetryListener{
		public void OnRetry();
	}
	public void setOnRetryListener(OnRetryListener l){
		mOnRetryListener = l;
	}

	public interface OnClickGoListener{
		void click();
	}

	public void setOnClickGoListener(OnClickGoListener l){
		this.mOnClickGoListener = l;
	}
}
