package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;

class StatusBarMImpl implements IStatusBar {
    StatusBarMImpl() {
    }

    @TargetApi(23)
    public void setStatusBarColor(Window window, int color) {
        window.clearFlags(67108864);
        window.addFlags(-2147483648);
        window.setStatusBarColor(color);
        View v = window.findViewById(16908290);
        if(v != null) {
            v.setForeground((Drawable)null);
        }

    }
}

