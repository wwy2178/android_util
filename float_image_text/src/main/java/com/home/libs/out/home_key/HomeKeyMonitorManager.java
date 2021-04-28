package com.home.libs.out.home_key;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import java.util.List;

/**
 * AnnotationApplication
 * Created by anonyper on 2019/6/10.
 */
public class HomeKeyMonitorManager {
    private static final String LOG_TAG = "体外场景";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    private static final String SYSTEM_DIALOG_REASON_FS_GESTURE = "fs_gesture";

    private boolean canWriteDownClickCount = true;
    private static HomeKeyMonitorManager ourInstance;
    private int clickHomeCount = 0;
    private Application application;
    private int spaceCount = 2;
    private List<Class<? extends Activity>> activities;
    private long clickDelayMillisForSingleClick = 6000;
    private boolean isDisplay;

    public static HomeKeyMonitorManager getInstance(Builder builder) {
        synchronized (HomeKeyMonitorManager.class) {
            if (ourInstance == null) {
                ourInstance = builder.build();
            }
        }
        return ourInstance;
    }


    private HomeKeyMonitorManager(Builder build) {
        if (build == null) {
            this.spaceCount = 2;
            this.activities = null;
            this.clickDelayMillisForSingleClick = 6000;
            this.isDisplay = true;
        } else {
            this.spaceCount = build.spaceCount;
            this.activities = build.activities;
            this.clickDelayMillisForSingleClick = build.delayMillis;
            this.isDisplay = build.isDisPlay;
        }
    }

    /**
     * 初始化 传入application
     *
     * @param application
     */
    public void init(Application application) {
        if (application == null) {
            throw new NullPointerException("application can not be null");
        }
        this.application = application;
        if (isDisplay) {
            initMonitor();
        }
    }

    /**
     * 初始化监听
     */
    private void initMonitor() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        this.application.registerReceiver(receiver, intentFilter);
    }

    /**
     * 反注册广播
     */
    public void onDestroy() {
        this.application.unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) return;
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                // 短按Home键
                writeDownAndSendHomeEvent();
            } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                // 长按Home键 或者 activity切换键
                writeDownAndSendHomeEvent();
            } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                // samsung 长按Home键
            } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                Log.i(LOG_TAG, "lock");
            } else if (SYSTEM_DIALOG_REASON_FS_GESTURE.equals(reason)) {
                //全屏手势触发Home键(已知：小米全面屏手机手势触发Home键会走这，不会走SYSTEM_DIALOG_REASON_HOME_KEY，且已经在桌面时，不会触发广播)
                writeDownAndSendHomeEvent();
            }
        }
    };

    /**
     * 记录并且发送触发Home事件
     */
    private void writeDownAndSendHomeEvent() {
        if (!canWriteDownClickCount) return;
        if (!isDisplay) return;
        ActivityManager am = (ActivityManager) application.getSystemService(application.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
        if (infos != null && infos.size() >= 1) {
            ComponentName cn = infos.get(0).topActivity;
            String currentTopActivity = cn.getClassName();
            Log.i(LOG_TAG, "home键事件触发，名称为：" + currentTopActivity);
            if (activities != null) {
                for (int i = 0; i < activities.size(); i++) {
                    if (currentTopActivity.equals(activities.get(i).getName())) {
                        Log.i(LOG_TAG, "忽略触发--->" + currentTopActivity + "--->不执行回调");
                        return;
                    }
                }
            }
            if (currentTopActivity.contains("OutSceneActivity")) {
                return;
            }
        }
        canWriteDownClickCount = false;
        clickHomeCount++;

        if (onHomeKeyEventListener != null && clickHomeCount % (spaceCount + 1) == 0) {
            onHomeKeyEventListener.onHomeKeyEventTriggered(application);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canWriteDownClickCount = true;
            }
        }, clickDelayMillisForSingleClick);
    }

    private OnHomeKeyEventListener onHomeKeyEventListener;

    public void setOnHomeKeyEventListener(OnHomeKeyEventListener onHomeKeyEventListener) {
        this.onHomeKeyEventListener = onHomeKeyEventListener;
    }


    public static class Builder {
        private int spaceCount = 2;
        private long delayMillis = 6000;
        private boolean isDisPlay = true;
        private List<Class<? extends Activity>> activities;

        /**
         * 设置间隔次数，默认是2 ,即第一次 第二次 监听到Home键不会执行逻辑，第三次才会执行相关逻辑
         *
         * @param spaceCount 间隔次数
         */
        public Builder spaceCount(int spaceCount) {
            this.spaceCount = spaceCount;
            return this;
        }

        /**
         * 在指定列表中的Activity在前台时，忽略Home按键的事件
         *
         * @param activities 间隔次数
         */
        public Builder ignoreActivities(List<Class<? extends Activity>> activities) {
            this.activities = activities;
            return this;
        }

        /**
         * 单位时间内，多次点击算一次点击
         *
         * @param delayMillis 单位时间的毫秒值
         */
        public Builder timeInterval(long delayMillis) {
            this.delayMillis = delayMillis;
            return this;
        }

        /**
         * 开关控制
         *
         * @param isDisPlay 开关控制
         */
        public Builder setDisPlay(boolean isDisPlay) {
            this.isDisPlay = isDisPlay;
            return this;
        }

        private HomeKeyMonitorManager build() {
            return new HomeKeyMonitorManager(this);
        }
    }
}