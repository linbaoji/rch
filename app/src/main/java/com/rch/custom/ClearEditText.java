package com.rch.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.rch.R;

@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText {

	Drawable delImage,searchImage;
	
	public ClearEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ClearEditText(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	
	private void init()
	{
		delImage=getResources().getDrawable(R.mipmap.search_del);
		searchImage=getResources().getDrawable(R.mipmap.search_icon);
		setCompoundDrawablesWithIntrinsicBounds(searchImage, null, null, null);
		addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				//Log.e("t", "1");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {	//在文本改变之前.
				// TODO Auto-generated method stub
				//Log.e("t", "2");
			}
			
			@Override
			public void afterTextChanged(Editable s) { //在文本改变以后.
				// TODO Auto-generated method stub
				drawableEditTextImage();
				//Log.e("t", "3");
			}
			
		});
	}
	
	/***
	 * 画右边删除图标
	 */
	private void drawableEditTextImage()
	{
		if(length()<1)
			setCompoundDrawablesWithIntrinsicBounds(searchImage, null, null, null);
		else
			setCompoundDrawablesWithIntrinsicBounds(searchImage, null, delImage, null);
	}
	
	/***
	 * 点击删除图标逻辑
	 * Rect:矩形
	 * event.getRawX();//获取控件距离屏幕有多远,X坐标
	 * event.getRawY();//获取控件距离屏幕有多远,Y坐标
	 * getGlobalVisibleRect(rect);//获取焦点控件的left,right,top,bottom放在rect里面,怀疑left,right,top,bottom是控件离屏幕的距离
	 * rect.left = rect.right - delImage.getMinimumWidth(); //设置左边的值，delImage.getMinimumHeight()是图片的高度
	 * rect.contains(x, y)//X坐标是否在left、right值的范围，y坐标是否在top、bottom值的范围
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Rect rect=new Rect();
			int x=(int) event.getRawX();//获取控件距离屏幕有多远,X坐标
			int y=(int) event.getRawY();//获取控件距离屏幕有多远,Y坐标
			getGlobalVisibleRect(rect);//获取焦点控件的left,right,top,bottom放在rect里面,怀疑left,right,top,bottom是控件离屏幕的距离
			rect.left = rect.right - delImage.getMinimumWidth(); //设置左边的值，delImage.getMinimumHeight()是图片的高度
			if(rect.contains(x, y))//X坐标是否在left、right值的范围，y坐标是否在top、bottom值的范围
				setText("");
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
}
