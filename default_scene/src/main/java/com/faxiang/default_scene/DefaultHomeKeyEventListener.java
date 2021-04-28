package com.faxiang.default_scene;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.faxiang.default_scene.activity.OutSceneForHomeKeyActivity;
import com.faxiang.default_scene.home_key.OnHomeKeyEventListener;
import com.faxiang.default_scene.utils.LiveWallpaperUtils;
import com.faxiang.default_scene.utils.OutSceneConfigUtils;
import com.faxiang.default_scene.utils.OverlayPermissionUtils;


public class DefaultHomeKeyEventListener implements OnHomeKeyEventListener {
    private int showPageCount = 0;

    @Override
    public void onHomeKeyEventTriggered(Context context) {
        showPageCount = OutSceneConfigUtils.getInt(context, "out_scene" + context.getPackageName(), "out_scene_page", 0);
        Intent intent = new Intent(context, OutSceneForHomeKeyActivity.class);
        if (showPageCount % 3 == 0) {
            intent.putExtra(OutSceneForHomeKeyActivity.TAG, OutSceneTag.HOME_CLEAR_RUBBISH);
        } else if (showPageCount % 3 == 1) {
            intent.putExtra(OutSceneForHomeKeyActivity.TAG, OutSceneTag.HOME_WIFI);
        } else {
            intent.putExtra(OutSceneForHomeKeyActivity.TAG, OutSceneTag.HOME_MOBILE_MANGER);
        }
        showPageCount++;
        OutSceneConfigUtils.putInt(context, "out_scene" + context.getPackageName(), "out_scene_page", showPageCount % 3);
        if (OverlayPermissionUtils.canDrawOverlay(context) || LiveWallpaperUtils.isLiveWallpaperRunning(context)) {
            Log.d("体外场景", "canDrawOverlay == 准备展示Home按键触发界面");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent);
                }
            }, 550);
        } else {
            Log.d("体外场景", "can·t DrawOverlay == 准备展示Home按键触发界面,有可能展示不出来");
            try {
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 550, pendingIntent);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
