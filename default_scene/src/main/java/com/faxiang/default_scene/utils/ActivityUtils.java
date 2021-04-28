package com.faxiang.default_scene.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

public class ActivityUtils {


    /**
     * 判断某个界面是否在前台
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
        if (infos != null && infos.size() >= 1) {
            ComponentName cn = infos.get(0).topActivity;
            String currentTopPackage = cn.getPackageName();

            if (currentTopPackage.contains(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
