package com.rch.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rch.R;


public class CustomContactViewControl extends View {

	Context context;

	//private String[] indexs = {"*","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private String[] indexs = new String[0];

	public void setIndexs(String[] indexs) {
		this.indexs = indexs;
		invalidate();
	}

	int choosed=-1;

	public CustomContactViewControl(Context context) {
		super(context);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	public CustomContactViewControl(Context context,AttributeSet attrs) {
		super(context,attrs);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	public CustomContactViewControl(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		this.context=context;
		// TODO Auto-generated constructor stub
	}

	int fixed=50;
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onDraw(Canvas canvas) {

		if(indexs==null||indexs.length==0)
			return;

		int h=getHeight();
		int w=getWidth();
		int singleH=0;
		if(indexs.length>1)
			singleH=h/indexs.length;//单个的高度

		for (int i = 0; i < indexs.length; i++) {
			Paint paint=new Paint();
//			paint.setColor(context.getColor(R.color.orange_2));
			paint.setColor(getResources().getColor(R.color.orange_2));
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(25);
		/*	if(choosed==i)
			{
				paint.setColor(Color.CYAN);
			}
*/
			int x=(int) (w/2-paint.measureText(indexs[i])/2);
			//int y=singleH*i+singleH;
			int y=0;
			if(indexs.length*fixed>h)
				y=singleH*i+singleH;
			else
				y=fixed*i+singleH;
			canvas.drawText(indexs[i], x, y, paint);
		}
		Log.e("onDraw", "Draw");
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int touchSingle;
		int oldChoosed=choosed;
		Log.e("Y",String.valueOf(event.getY()));
		if(indexs.length*fixed>getHeight()) {
			choosed = (int) (event.getY() / getHeight() * indexs.length);
			if(choosed>indexs.length-1)
				choosed=indexs.length-1;
		}
		else {
			int y=(int)event.getY();//移动临时高度
			int h=getHeight()/indexs.length;//单个高度
			int eh=h+((indexs.length-1)*fixed)+(fixed-1);//最后一个的高度
			if(y >=(h-fixed)&&y<=eh)
				choosed = (y- h)/fixed<0?0:(y- h)/fixed;
			//choosed = ((int) event.getY() / (getHeight() / indexs.length)) * indexs.length;
		}
		Log.e("choosed",String.valueOf(choosed));
		if(oldChoosed!=choosed)
		{
			if(singleIndexclicked!=null)
				singleIndexclicked.singerNameItemClicked(indexs[choosed]);
			invalidate();
		}
		/*switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchSingle=(int) (event.getY()/getHeight()*indexs.length);
			currentSingleIndex(touchSingle);
			break;
		case MotionEvent.ACTION_MOVE:
			touchSingle=(int) (event.getY()/getHeight()*indexs.length);
			currentSingleIndex(touchSingle);
			break;
		case MotionEvent.ACTION_UP:
			touchSingle=(int) (event.getY()/getHeight()*indexs.length);
			currentSingleIndex(touchSingle);
			break;
		}*/

		return true;
	}



	OnSingerNameIndexerClicked singleIndexclicked;

	public void setOnSingerNameIndexerClicked(OnSingerNameIndexerClicked singleIndexclicked)
	{
		this.singleIndexclicked=singleIndexclicked;
	}

	public interface OnSingerNameIndexerClicked
	{
		public void singerNameItemClicked(String selectName);
	}

}
