package com.home.libs.out.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.home.libs.out.activity.OutSceneRechargeActivity;
import com.home.libs.out.recharge.OnRechargeChangeListener;
import com.home.libs.out.recharge.RechargeStatus;
import com.home.libs.out.utils.util.LiveWallpaperUtils;
import com.home.libs.out.utils.util.OverlayPermissionUtils;

/**
 * Auther: Created by Admin on 2021/4/21
 * Description : 充电体外
 * @author Admin
 */
public class DefaultRechargeListener implements OnRechargeChangeListener {
    @Override
    public void onPowerChanged(Context context, RechargeStatus rechargeStatus) {
        Intent intent = new Intent(context, OutSceneRechargeActivity.class);
        intent.putExtra(OutSceneRechargeActivity.TAG, rechargeStatus);
        if (OverlayPermissionUtils.canDrawOverlay(context) || LiveWallpaperUtils.isLiveWallpaperRunning(context)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(intent);
                }
            }, 200);
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
