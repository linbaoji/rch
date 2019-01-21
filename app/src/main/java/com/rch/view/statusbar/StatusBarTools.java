package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */
import android.content.Context;
import android.content.res.Resources;

public class StatusBarTools {
    public StatusBarTools() {
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }
}

