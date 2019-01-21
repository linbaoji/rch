package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;

class StatusBarKitkatImpl implements IStatusBar {
    private static final String STATUS_BAR_VIEW_TAG = "ghStatusBarView";

    StatusBarKitkatImpl() {
    }

    @TargetApi(19)
    public void setStatusBarColor(Window window, int color) {
        window.addFlags(67108864);
        ViewGroup decorViewGroup = (ViewGroup)window.getDecorView();
        View statusBarView = decorViewGroup.findViewWithTag("ghStatusBarView");
        if(statusBarView == null) {
            statusBarView = new StatusBarView(window.getContext());
            ((View)statusBarView).setTag("ghStatusBarView");
            LayoutParams params = new LayoutParams(-1, -2);
            params.gravity = 48;
            ((View)statusBarView).setLayoutParams(params);
            decorViewGroup.addView((View)statusBarView);
        }

        ((View)statusBarView).setBackgroundColor(color);
    }
}
