package com.faxiang.default_scene.utils;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.faxiang.default_scene.utils.activity_result_bridge.BridgeResult;
import com.faxiang.default_scene.utils.activity_result_bridge.CallbackResult;


public class LiveWallpaperUtils {

    public static final int REQUEST_CODE = 0x00000007;

    public static boolean isLiveWallpaperRunning(Context context) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);// 得到壁纸管理器
        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();// 如果系统使用的壁纸是动态壁纸话则返回该动态壁纸的信息,否则会返回null
        if (wallpaperInfo != null) {// 如果是动态壁纸,则得到该动态壁纸的包名,并与想知道的动态壁纸包名做比较
            String currentLiveWallpaperPackageName = wallpaperInfo.getPackageName();
            if (currentLiveWallpaperPackageName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去往某个动态壁纸的预览页面,那里可以设置壁纸
     *
     * @param activity
     * @param packageName   动态壁纸的包名
     * @param classFullName 动态壁纸service类的类全名
     */
    public static void startLiveWallpaperPreview(Activity activity, String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        activity.startActivityForResult(intent, 0);
    }


    /**
     * 去往某个动态壁纸的预览页面,那里可以设置壁纸
     *
     * @param activity
     * @param packageName   动态壁纸的包名
     * @param classFullName 动态壁纸service类的类全名
     */
    public static void startLiveWallpaperPreviewWithResult(FragmentActivity activity, String packageName, String classFullName, CallbackResult callback) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        new BridgeResult(activity).startActivityWithResult(intent, REQUEST_CODE, callback);
    }
}
