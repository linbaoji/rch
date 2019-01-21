package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarView extends View {
    private int mStatusBarHeight;

    public StatusBarView(Context context) {
        this(context, (AttributeSet)null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(VERSION.SDK_INT >= 19) {
            this.mStatusBarHeight = StatusBarTools.getStatusBarHeight(context);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.mStatusBarHeight);
    }
}
