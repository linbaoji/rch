package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.TargetApi;
import android.view.Window;

class StatusBarLollipopImpl implements IStatusBar {
    StatusBarLollipopImpl() {
    }

    @TargetApi(21)
    public void setStatusBarColor(Window window, int color) {
        window.clearFlags(67108864);
        window.addFlags(-2147483648);
        window.setStatusBarColor(color);
    }
}
