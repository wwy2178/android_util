package com.faxiang.default_scene;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.faxiang.default_scene.activity.OutSceneForPackageActivity;
import com.faxiang.default_scene.packagers.OnPackageEventListener;
import com.faxiang.default_scene.packagers.PackageStatus;
import com.faxiang.default_scene.packagers.PackageStatusInfo;
import com.faxiang.default_scene.utils.LiveWallpaperUtils;
import com.faxiang.default_scene.utils.OverlayPermissionUtils;

public class DefaultPackageEventListener implements OnPackageEventListener {


    @Override
    public void onPackageStatusChanged(Context context, PackageStatusInfo info) {
        Intent intent = new Intent(context, OutSceneForPackageActivity.class);
        intent.putExtra(OutSceneForPackageActivity.TAG, info.getState());
        intent.putExtra(OutSceneForPackageActivity.TAG1, info.getPackageName());
        if (OverlayPermissionUtils.canDrawOverlay(context) || LiveWallpaperUtils.isLiveWallpaperRunning(context)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent);
                }
            }, info.getState() == PackageStatus.PACKAGE_ADDED ? 4000 : 200);
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
