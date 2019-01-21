package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;


import com.rch.base.MyApplication;

import java.lang.reflect.Method;

public final class BarUtils {
    private static final int DEFAULT_ALPHA = 112;
    private static final String TAG_COLOR = "TAG_COLOR";
    private static final String TAG_ALPHA = "TAG_ALPHA";
    private static final int TAG_OFFSET = -123;

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getStatusBarHeight() {
        Resources resources = MyApplication.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void setStatusBarVisibility(@NonNull Activity activity, boolean isVisible) {
        setStatusBarVisibility(activity.getWindow(), isVisible);
    }

    public static void setStatusBarVisibility(@NonNull Window window, boolean isVisible) {
        if(isVisible) {
            window.clearFlags(1024);
        } else {
            window.addFlags(1024);
        }

    }

    public static boolean isStatusBarVisible(@NonNull Activity activity) {
        int flags = activity.getWindow().getAttributes().flags;
        return (flags & 1024) == 0;
    }

    public static void setStatusBarLightMode(@NonNull Activity activity, boolean isLightMode) {
        setStatusBarLightMode(activity.getWindow(), isLightMode);
    }

    public static void setStatusBarLightMode(@NonNull Window window, boolean isLightMode) {
        if(VERSION.SDK_INT >= 23) {
            View decorView = window.getDecorView();
            if(decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if(isLightMode) {
                    window.addFlags(-2147483648);
                    vis |= 8192;
                } else {
                    vis &= -8193;
                }

                decorView.setSystemUiVisibility(vis);
            }
        }

    }

    public static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        if(VERSION.SDK_INT >= 19) {
            Object haveSetOffset = view.getTag(-123);
            if(haveSetOffset == null || !((Boolean)haveSetOffset).booleanValue()) {
                MarginLayoutParams layoutParams = (MarginLayoutParams)view.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(), layoutParams.rightMargin, layoutParams.bottomMargin);
                view.setTag(-123, Boolean.valueOf(true));
            }
        }
    }

    public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        if(VERSION.SDK_INT >= 19) {
            Object haveSetOffset = view.getTag(-123);
            if(haveSetOffset != null && ((Boolean)haveSetOffset).booleanValue()) {
                MarginLayoutParams layoutParams = (MarginLayoutParams)view.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin - getStatusBarHeight(), layoutParams.rightMargin, layoutParams.bottomMargin);
                view.setTag(-123, Boolean.valueOf(false));
            }
        }
    }

    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        setStatusBarColor(activity, color, 112, false);
    }

    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha) {
        setStatusBarColor(activity, color, alpha, false);
    }

    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha, boolean isDecor) {
        if(VERSION.SDK_INT >= 19) {
            hideAlphaView(activity);
            transparentStatusBar(activity);
            addStatusBarColor(activity, color, alpha, isDecor);
        }
    }

    public static void setStatusBarColor(@NonNull View fakeStatusBar, @ColorInt int color) {
        setStatusBarColor((View)fakeStatusBar, color, 112);
    }

    public static void setStatusBarColor(@NonNull View fakeStatusBar, @ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha) {
        if(VERSION.SDK_INT >= 19) {
            fakeStatusBar.setVisibility(View.VISIBLE);
            transparentStatusBar((Activity)fakeStatusBar.getContext());
            LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = getStatusBarHeight();
            fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
        }
    }

    public static void setStatusBarAlpha(@NonNull Activity activity) {
        setStatusBarAlpha(activity, 112, false);
    }

    public static void setStatusBarAlpha(@NonNull Activity activity, @IntRange(from = 0L,to = 255L) int alpha) {
        setStatusBarAlpha(activity, alpha, false);
    }

    public static void setStatusBarAlpha(@NonNull Activity activity, @IntRange(from = 0L,to = 255L) int alpha, boolean isDecor) {
        if(VERSION.SDK_INT >= 19) {
            hideColorView(activity);
            transparentStatusBar(activity);
            addStatusBarAlpha(activity, alpha, isDecor);
        }
    }

    public static void setStatusBarAlpha(@NonNull View fakeStatusBar) {
        setStatusBarAlpha((View)fakeStatusBar, 112);
    }

    public static void setStatusBarAlpha(@NonNull View fakeStatusBar, @IntRange(from = 0L,to = 255L) int alpha) {
        if(VERSION.SDK_INT >= 19) {
            fakeStatusBar.setVisibility(View.VISIBLE);
            transparentStatusBar((Activity)fakeStatusBar.getContext());
            LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = getStatusBarHeight();
            fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        }
    }

    public static void setStatusBarColor4Drawer(@NonNull Activity activity, @NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, @ColorInt int color, boolean isTop) {
        setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, 112, isTop);
    }

    public static void setStatusBarColor4Drawer(@NonNull Activity activity, @NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, @ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha, boolean isTop) {
        if(VERSION.SDK_INT >= 19) {
            drawer.setFitsSystemWindows(false);
            transparentStatusBar(activity);
            setStatusBarColor(fakeStatusBar, color, isTop?alpha:0);
            int i = 0;

            for(int len = drawer.getChildCount(); i < len; ++i) {
                drawer.getChildAt(i).setFitsSystemWindows(false);
            }

            if(isTop) {
                hideAlphaView(activity);
            } else {
                addStatusBarAlpha(activity, alpha, false);
            }

        }
    }

    public static void setStatusBarAlpha4Drawer(@NonNull Activity activity, @NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, boolean isTop) {
        setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, 112, isTop);
    }

    public static void setStatusBarAlpha4Drawer(@NonNull Activity activity, @NonNull DrawerLayout drawer, @NonNull View fakeStatusBar, @IntRange(from = 0L,to = 255L) int alpha, boolean isTop) {
        if(VERSION.SDK_INT >= 19) {
            drawer.setFitsSystemWindows(false);
            transparentStatusBar(activity);
            setStatusBarAlpha(fakeStatusBar, isTop?alpha:0);
            int i = 0;

            for(int len = drawer.getChildCount(); i < len; ++i) {
                drawer.getChildAt(i).setFitsSystemWindows(false);
            }

            if(isTop) {
                hideAlphaView(activity);
            } else {
                addStatusBarAlpha(activity, alpha, false);
            }

        }
    }

    private static void addStatusBarColor(Activity activity, int color, int alpha, boolean isDecor) {
        ViewGroup parent = isDecor?(ViewGroup)activity.getWindow().getDecorView():(ViewGroup)activity.findViewById(16908290);
        View fakeStatusBarView = parent.findViewWithTag("TAG_COLOR");
        if(fakeStatusBarView != null) {
            if(fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }

            fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        } else {
            parent.addView(createColorStatusBarView(parent.getContext(), color, alpha));
        }

    }

    private static void addStatusBarAlpha(Activity activity, int alpha, boolean isDecor) {
        ViewGroup parent = isDecor?(ViewGroup)activity.getWindow().getDecorView():(ViewGroup)activity.findViewById(16908290);
        View fakeStatusBarView = parent.findViewWithTag("TAG_ALPHA");
        if(fakeStatusBarView != null) {
            if(fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }

            fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        } else {
            parent.addView(createAlphaStatusBarView(parent.getContext(), alpha));
        }

    }

    private static void hideColorView(Activity activity) {
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag("TAG_COLOR");
        if(fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
    }

    private static void hideAlphaView(Activity activity) {
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag("TAG_ALPHA");
        if(fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
    }

    private static int getStatusBarColor(int color, int alpha) {
        if(alpha == 0) {
            return color;
        } else {
            float a = 1.0F - (float)alpha / 255.0F;
            int red = color >> 16 & 255;
            int green = color >> 8 & 255;
            int blue = color & 255;
            red = (int)((double)((float)red * a) + 0.5D);
            green = (int)((double)((float)green * a) + 0.5D);
            blue = (int)((double)((float)blue * a) + 0.5D);
            return Color.argb(255, red, green, blue);
        }
    }

    private static View createColorStatusBarView(Context context, int color, int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, getStatusBarHeight()));
        statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        statusBarView.setTag("TAG_COLOR");
        return statusBarView;
    }

    private static View createAlphaStatusBarView(Context context, int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, getStatusBarHeight()));
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setTag("TAG_ALPHA");
        return statusBarView;
    }

    private static void transparentStatusBar(Activity activity) {
        if(VERSION.SDK_INT >= 19) {
            Window window = activity.getWindow();
            if(VERSION.SDK_INT >= 21) {
                window.addFlags(-2147483648);
                int option = 1280;
                window.getDecorView().setSystemUiVisibility(option);
                window.setStatusBarColor(0);
            } else {
                window.addFlags(67108864);
            }

        }
    }

    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        return MyApplication.getInstance().getTheme().resolveAttribute(16843499, tv, true)? TypedValue.complexToDimensionPixelSize(tv.data, MyApplication.getInstance().getResources().getDisplayMetrics()):0;
    }

    @RequiresPermission("android.permission.EXPAND_STATUS_BAR")
    public static void setNotificationBarVisibility(boolean isVisible) {
        String methodName;
        if(isVisible) {
            methodName = VERSION.SDK_INT <= 16?"expand":"expandNotificationsPanel";
        } else {
            methodName = VERSION.SDK_INT <= 16?"collapse":"collapsePanels";
        }

        invokePanels(methodName);
    }

    private static void invokePanels(String methodName) {
        try {
            @SuppressLint("WrongConstant") Object service = MyApplication.getInstance().getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName, new Class[0]);
            expand.invoke(service, new Object[0]);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static int getNavBarHeight() {
        Resources res = MyApplication.getInstance().getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId != 0?res.getDimensionPixelSize(resourceId):0;
    }

    public static void setNavBarVisibility(@NonNull Activity activity, boolean isVisible) {
        setNavBarVisibility(activity.getWindow(), isVisible);
    }

    public static void setNavBarVisibility(@NonNull Window window, boolean isVisible) {
        if(isVisible) {
            window.clearFlags(512);
        } else {
            window.addFlags(512);
            View decorView = window.getDecorView();
            if(VERSION.SDK_INT >= 19) {
                int visibility = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(visibility & -4097);
            }
        }

    }

    @TargetApi(19)
    public static void setNavBarImmersive(@NonNull Activity activity) {
        setNavBarImmersive(activity.getWindow());
    }

    @TargetApi(19)
    public static void setNavBarImmersive(@NonNull Window window) {
        View decorView = window.getDecorView();
        window.clearFlags(512);
        int uiOptions = 4610;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static boolean isNavBarVisible(@NonNull Activity activity) {
        return isNavBarVisible(activity.getWindow());
    }

    public static boolean isNavBarVisible(@NonNull Window window) {
        boolean isNoLimits = (window.getAttributes().flags & 512) != 0;
        if(isNoLimits) {
            return false;
        } else {
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            return (visibility & 2) == 0;
        }
    }
}
