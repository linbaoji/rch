package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StatusBarCompat {
    static final IStatusBar IMPL;

    public StatusBarCompat() {
    }

    private static boolean isEMUI() {
        File file = new File(Environment.getRootDirectory(), "build.prop");
        if(file.exists()) {
            Properties properties = new Properties();
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (Exception var12) {
                var12.printStackTrace();
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

            }

            return properties.containsKey("ro.build.hw_emui_api_level");
        } else {
            return false;
        }
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        boolean isLightColor = toGrey(color) > 225;
        setStatusBarColor(activity, color, isLightColor);
    }

    public static int toGrey(@ColorInt int color) {
        int blue = Color.blue(color);
        int green = Color.green(color);
        int red = Color.red(color);
        return red * 38 + green * 75 + blue * 15 >> 7;
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color, boolean lightStatusBar) {
        setStatusBarColor(activity.getWindow(), color, lightStatusBar);
    }

    public static void setStatusBarColor(Window window, @ColorInt int color, boolean lightStatusBar) {
        if((window.getAttributes().flags & 1024) <= 0 && !StatusBarExclude.exclude) {
            IMPL.setStatusBarColor(window, color);
            LightStatusBarCompat.setLightStatusBar(window, lightStatusBar);
        }
    }

    public static void setFitsSystemWindows(Window window, boolean fitSystemWindows) {
        if(VERSION.SDK_INT >= 14) {
            internalSetFitsSystemWindows(window, fitSystemWindows);
        }

    }

    @TargetApi(14)
    static void internalSetFitsSystemWindows(Window window, boolean fitSystemWindows) {
        ViewGroup contentView = (ViewGroup)window.findViewById(16908290);
        View childView = contentView.getChildAt(0);
        if(childView != null) {
            childView.setFitsSystemWindows(fitSystemWindows);
        }

    }

    public static void resetActionBarContainerTopMargin(Window window) {
        View contentView = window.findViewById(16908290);
        ViewGroup group = (ViewGroup)contentView.getParent();
        if(group.getChildCount() > 1) {
            View view = group.getChildAt(1);
            internalResetActionBarContainer(view);
        }

    }

    public static void resetActionBarContainerTopMargin(Window window, @IdRes int actionBarContainerId) {
        View view = window.findViewById(actionBarContainerId);
        internalResetActionBarContainer(view);
    }

    private static void internalResetActionBarContainer(View actionBarContainer) {
        if(actionBarContainer != null) {
            LayoutParams params = actionBarContainer.getLayoutParams();
            if(params instanceof MarginLayoutParams) {
                ((MarginLayoutParams)params).topMargin = 0;
                actionBarContainer.setLayoutParams(params);
            }
        }

    }

    public static void setLightStatusBar(Window window, boolean isLightStatusBar) {
        LightStatusBarCompat.setLightStatusBar(window, isLightStatusBar);
    }

    public static void setTranslucent(Window window, boolean translucent) {
        if(VERSION.SDK_INT >= 19) {
            if(translucent) {
                window.addFlags(67108864);
                internalSetFitsSystemWindows(window, false);
            } else {
                window.clearFlags(67108864);
            }
        }

    }

    static {
        if(VERSION.SDK_INT >= 23) {
            IMPL = new StatusBarMImpl();
        } else if(VERSION.SDK_INT >= 21 && !isEMUI()) {
            IMPL = new StatusBarLollipopImpl();
        } else if(VERSION.SDK_INT >= 19) {
            IMPL = new StatusBarKitkatImpl();
        } else {
            IMPL = new IStatusBar() {
                public void setStatusBarColor(Window window, @ColorInt int color) {
                }
            };
        }

    }
}

