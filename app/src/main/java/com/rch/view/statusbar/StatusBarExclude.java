package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.os.Build;

public class StatusBarExclude {
    static boolean exclude = false;

    public StatusBarExclude() {
    }

    public static void excludeIncompatibleFlyMe() {
        try {
            Build.class.getMethod("hasSmartBar", new Class[0]);
        } catch (NoSuchMethodException var1) {
            exclude |= Build.BRAND.contains("Meizu");
        }

    }
}
