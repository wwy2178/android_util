package com.faxiang.default_scene.packagers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotationApplication
 * Created by anonyper on 2019/6/10.
 */
public class PackageMonitorManager {
    public static final String TAG = "PackageMonitor >>> : ";
    private static PackageMonitorManager ourInstance;
    private Application application;
    private Map<String, String> mapLabels;
    private Map<String, Drawable> mapLogos;
    private boolean isDisplay = false;

    public static PackageMonitorManager getInstance(Builder builder) {
        synchronized (PackageMonitorManager.class) {
            if (ourInstance == null) {
                ourInstance = new PackageMonitorManager(builder);
            }
        }
        return ourInstance;
    }

    private PackageMonitorManager(Builder builder) {
        if (builder == null) {
            this.isDisplay = true;
        } else {
            this.isDisplay = builder.isDisPlay;
        }
    }

    /**
     * 初始化 传入application
     */
    public void init(Application application) {
        if (application == null) {
            throw new NullPointerException("application can not be null");
        }
        if (!isDisplay) {
            return;
        }
        this.application = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initAllAppInfo();
        } else {
            initAllAppInfo2();
        }
        initMonitor();
    }

    /**
     * 获取已安装应用信息
     *
     * @return
     */
    private void initAllAppInfo() {
        PackageManager pm = application.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = null;
        list = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (list != null) {
            Collections.sort(list, new ResolveInfo.DisplayNameComparator(pm));
            mapLabels = new HashMap<>();
            mapLogos = new HashMap<>();
            for (ResolveInfo info : list) {
                String packageName = info.activityInfo.packageName;
                Log.i("label", info.loadLabel(pm).toString());
                Log.i("package", packageName);
                mapLogos.put(packageName, info.loadIcon(pm));
                mapLabels.put(packageName, info.loadLabel(pm).toString());
            }
        }
    }

    /**
     * 获取已安装应用信息
     *
     * @return
     */
    private void initAllAppInfo2() {
        PackageManager pm = application.getPackageManager();
        List<PackageInfo> packageList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        if (packageList != null && packageList.size() > 0) {
            mapLabels = new HashMap<>();
            mapLogos = new HashMap<>();
            for (PackageInfo info : packageList) {
                String packageName = info.packageName;
                mapLogos.put(packageName, info.applicationInfo.loadIcon(pm));
                mapLabels.put(packageName, info.applicationInfo.loadLabel(pm).toString());
            }
        }
    }

    public String getAppLabelByPackageName(String packageName) {
        if (mapLabels != null) {
            return mapLabels.get(packageName);
        }
        return null;
    }

    public Drawable getAppLogoByPackageName(String packageName) {
        if (mapLabels != null) {
            return mapLogos.get(packageName);
        }
        return null;
    }

    /**
     * 初始化安装卸载监听
     */
    private void initMonitor() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
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
            if (!isDisplay) {
                return;
            }
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_PACKAGE_ADDED)) {
                PackageStatusInfo info = new PackageStatusInfo();
                info.setState(PackageStatus.PACKAGE_ADDED);
                String packageName = intent.getData().getSchemeSpecificPart();
                info.setPackageName(packageName);
                info.setLauncherLogo(getAppLogoByPackageName(packageName));
                info.setAppName(getAppLabelByPackageName(packageName));
                if (onPackageEventListener != null) {
                    onPackageEventListener.onPackageStatusChanged(application, info);
                }
            } else if (intent.getAction().equalsIgnoreCase(Intent.ACTION_PACKAGE_REMOVED)) {
                PackageStatusInfo info = new PackageStatusInfo();
                info.setState(PackageStatus.PACKAGE_REMOVED);
                String packageName = intent.getData().getSchemeSpecificPart();
                info.setPackageName(packageName);
                info.setLauncherLogo(getAppLogoByPackageName(packageName));
                info.setAppName(getAppLabelByPackageName(packageName));
                if (onPackageEventListener != null) {
                    onPackageEventListener.onPackageStatusChanged(application, info);
                }
            }
        }
    };

    private OnPackageEventListener onPackageEventListener;

    public void setOnPackageEventListener(OnPackageEventListener onPackageEventListener) {
        this.onPackageEventListener = onPackageEventListener;
    }

    public static class Builder {
        private boolean isDisPlay;

        /**
         * 开关控制
         *
         * @param isDisPlay 开关控制
         */
        public Builder setDisPlay(boolean isDisPlay) {
            this.isDisPlay = isDisPlay;
            return this;
        }

        private PackageMonitorManager build() {
            return new PackageMonitorManager(this);
        }
    }
}