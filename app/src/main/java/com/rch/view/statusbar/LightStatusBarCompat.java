package com.rch.view.statusbar;

/**
 * Created by Administrator on 2018/11/29.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

class LightStatusBarCompat {
    private static final LightStatusBarCompat.ILightStatusBar IMPL;

    LightStatusBarCompat() {
    }

    static void setLightStatusBar(Window window, boolean lightStatusBar) {
        IMPL.setLightStatusBar(window, lightStatusBar);
    }

    static {
        if(LightStatusBarCompat.MIUILightStatusBarImpl.isMe()) {
            if(VERSION.SDK_INT >= 23) {
                IMPL = new LightStatusBarCompat.MLightStatusBarImpl() {
                    private final LightStatusBarCompat.ILightStatusBar DELEGATE = new LightStatusBarCompat.MIUILightStatusBarImpl();

                    public void setLightStatusBar(Window window, boolean lightStatusBar) {
                        super.setLightStatusBar(window, lightStatusBar);
                        this.DELEGATE.setLightStatusBar(window, lightStatusBar);
                    }
                };
            } else {
                IMPL = new LightStatusBarCompat.MIUILightStatusBarImpl();
            }
        } else if(LightStatusBarCompat.MeizuLightStatusBarImpl.isMe()) {
            IMPL = new LightStatusBarCompat.MeizuLightStatusBarImpl();
        }else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            IMPL = new LightStatusBarCompat.OppoLightStatusBarImpl();
        } else if(VERSION.SDK_INT >= 23) {
            IMPL = new LightStatusBarCompat.MLightStatusBarImpl();
        }  else {
            IMPL = new LightStatusBarCompat.ILightStatusBar() {
                public void setLightStatusBar(Window window, boolean lightStatusBar) {
                }
            };
        }

    }

    private static class MeizuLightStatusBarImpl implements LightStatusBarCompat.ILightStatusBar {
        private MeizuLightStatusBarImpl() {
        }

        static boolean isMe() {
            return Build.DISPLAY.startsWith("Flyme");
        }

        public void setLightStatusBar(Window window, boolean lightStatusBar) {
            LayoutParams params = window.getAttributes();

            try {
                Field darkFlag = LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt((Object)null);
                int value = meizuFlags.getInt(params);
                if(lightStatusBar) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(params, value);
                window.setAttributes(params);
                darkFlag.setAccessible(false);
                meizuFlags.setAccessible(false);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }

    private static class MIUILightStatusBarImpl implements LightStatusBarCompat.ILightStatusBar {
        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        private MIUILightStatusBarImpl() {
        }

        static boolean isMe() {
            FileInputStream is = null;

            boolean var2;
            try {
                is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                Properties prop = new Properties();
                prop.load(is);
                var2 = prop.getProperty("ro.miui.ui.version.code") != null || prop.getProperty("ro.miui.ui.version.name") != null || prop.getProperty("ro.miui.internal.storage") != null;
                return var2;
            } catch (IOException var12) {
                var2 = false;
            } finally {
                if(is != null) {
                    try {
                        is.close();
                    } catch (IOException var11) {
                        ;
                    }
                }

            }

            return var2;
        }

        public void setLightStatusBar(Window window, boolean lightStatusBar) {
            Class clazz = window.getClass();

            try {
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                extraFlagField.invoke(window, new Object[]{Integer.valueOf(lightStatusBar?darkModeFlag:0), Integer.valueOf(darkModeFlag)});
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }

    private static class MLightStatusBarImpl implements LightStatusBarCompat.ILightStatusBar {
        private MLightStatusBarImpl() {
        }

        @TargetApi(11)
        public void setLightStatusBar(Window window, boolean lightStatusBar) {
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if(lightStatusBar) {
                ui |= 8192;
            } else {
                ui &= -8193;
            }

            decor.setSystemUiVisibility(ui);
        }
    }

    private static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;
    //设置OPPO手机状态栏字体为黑色(colorOS3.0,6.0以下部分手机)
    private static class OppoLightStatusBarImpl implements LightStatusBarCompat.ILightStatusBar{

        @Override
        public void setLightStatusBar(Window window, boolean lightStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int vis = window.getDecorView().getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (lightStatusBar) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (lightStatusBar) {
                    vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
                } else {
                    vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
                }
            }
            window.getDecorView().setSystemUiVisibility(vis);
        }
    }

    interface ILightStatusBar {
        void setLightStatusBar(Window var1, boolean var2);
    }
}
