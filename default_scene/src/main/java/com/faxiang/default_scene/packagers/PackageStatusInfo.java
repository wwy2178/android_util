package com.faxiang.default_scene.packagers;

import android.graphics.drawable.Drawable;

public class PackageStatusInfo {

    private String packageName;
    private PackageStatus state;
    private String appName;
    private Drawable launcherLogo;


    public PackageStatus getState() {
        return state;
    }

    public void setState(PackageStatus state) {
        this.state = state;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getLauncherLogo() {
        return launcherLogo;
    }

    public void setLauncherLogo(Drawable launcherLogo) {
        this.launcherLogo = launcherLogo;
    }
}
