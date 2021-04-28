package com.faxiang.default_scene;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.faxiang.default_scene.activity.OutSceneForWifiActivity;
import com.faxiang.default_scene.networks.NetworkState;
import com.faxiang.default_scene.networks.OnNetworkStatusChangeListener;
import com.faxiang.default_scene.utils.ActivityUtils;
import com.faxiang.default_scene.utils.LiveWallpaperUtils;
import com.faxiang.default_scene.utils.OverlayPermissionUtils;

public class DefaultNetworkStatusListener implements OnNetworkStatusChangeListener {
    @Override
    public void onNetworkStatusChange(Context context, NetworkState stats) {
        Intent intent = new Intent(context, OutSceneForWifiActivity.class);
        intent.putExtra(OutSceneForWifiActivity.TAG, stats);
        if (OverlayPermissionUtils.canDrawOverlay(context) || LiveWallpaperUtils.isLiveWallpaperRunning(context) || ActivityUtils.isForeground(context)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent);
                }
            }, 1000);
        } else {
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
