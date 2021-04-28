package com.home.libs.out.utils.util;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;


import com.home.libs.out.utils.statusbar.OSUtils;

import java.lang.reflect.Method;

public class OverlayPermissionUtils {

    public static final String VIVOTAG = "ro.vivo.os.build.display.id";


    /**
     * 检查悬浮窗权限
     */
    public static boolean canDrawOverlay(Context context) {
        try {
            if (OSUtils.isVivo()) {
                return checkVivoCanDrawOverlay(context);
            }
            return checkCanDrawOverlay(context);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * VivoRom获取悬浮窗权限状态
     */
    public static boolean checkVivoCanDrawOverlay(Context context) {
        int result2 = getvivoFloatPermissionStatus2(context);
        int result = getvivoFloatPermissionStatus(context);
        return result == 0 || result2 == 0;
    }


    /**
     * VivoRom获取悬浮窗权限状态
     *
     * @param context
     * @return 1或其他是没有打开，0是打开，该状态的定义和{android.app.AppOpsManager#MODE_ALLOWED}，MODE_IGNORED等值差不多，自行查阅源码
     */
    public static int getvivoFloatPermissionStatus(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("content://com.iqoo.secure.provider.secureprovider/allowfloatwindowapp");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.getColumnNames();
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentlmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 1;
            }
        } else {
            return 1;
        }
    }

    /**
     * VivoRom获取悬浮窗权限状态
     *
     * @param context
     * @return
     */
    public static int getvivoFloatPermissionStatus2(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/float_window_apps");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 1;
            }
        }
        return 1;
    }


    public static boolean checkCanDrawOverlay(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                return Settings.canDrawOverlays(context);
            }
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (manager != null) {
                try {
                    int result = manager.checkOp(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Binder.getCallingUid(), context.getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignore) {
                    return false;
                }
            }
            try {
                WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (mgr == null) return false; //getSystemService might return null
                View viewToAdd = new View(context);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
                viewToAdd.setLayoutParams(params);
                mgr.addView(viewToAdd, params);
                mgr.removeView(viewToAdd);
                return true;
            } catch (Exception ignore) {
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4-5.1
            if (OSUtils.isOppo() && (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1)) {
                return true;
            }
            return getAppOps(context);
        } else {//4.4以下
            return true;
        }
    }

    public static boolean getAppOps(Context context) {
        try {
            @SuppressLint("WrongConstant")
            Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
        }
        return false;
    }
}
