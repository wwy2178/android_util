package com.home.libs.out.impl;

import android.app.Application;

import com.home.libs.out.home_key.HomeKeyMonitorManager;
import com.home.libs.out.home_key.OnHomeKeyEventListener;
import com.home.libs.out.networks.NetWorkMonitorManager;
import com.home.libs.out.networks.OnNetworkStatusChangeListener;
import com.home.libs.out.packagers.OnPackageEventListener;
import com.home.libs.out.packagers.PackageMonitorManager;
import com.home.libs.out.recharge.OnRechargeChangeListener;
import com.home.libs.out.recharge.PowerMonitorManager;

public class SceneManager {
    private static SceneManager mInstance;
    private Application application;

    private HomeKeyMonitorManager homeKeyMonitorManager;
    private NetWorkMonitorManager netWorkMonitorManager;
    private PackageMonitorManager packageMonitorManager;
    private PowerMonitorManager powerMonitorManager;

    public static SceneManager getInstance() {
        synchronized (SceneManager.class) {
            if (mInstance == null) {
                mInstance = new SceneManager();
            }
        }
        return mInstance;
    }

    private SceneManager() {
    }

    /**
     * 初始化 传入application
     */
    public void init(Application application) {
        if (application == null) {
            throw new NullPointerException("application can not be null");
        }
        this.application = application;
    }

    public void registerHomeKeyEventLister(HomeKeyMonitorManager.Builder builder, OnHomeKeyEventListener listener) {
        if (application == null) {
            throw new RuntimeException("SceneManager must be cal init(Application app) first");
        }
        homeKeyMonitorManager = HomeKeyMonitorManager.getInstance(builder);
        homeKeyMonitorManager.init(application);
        homeKeyMonitorManager.setOnHomeKeyEventListener(listener);
    }

    public void registerNetworkStateListener(NetWorkMonitorManager.Builder builder, OnNetworkStatusChangeListener listener) {
        if (application == null) {
            throw new RuntimeException("SceneManager must be cal init(Application app) first");
        }
        netWorkMonitorManager = NetWorkMonitorManager.getInstance(builder);
        netWorkMonitorManager.init(application);
        netWorkMonitorManager.setOnNetworkStatusChangeListener(listener);
    }

    public void registerPackageEventListener(PackageMonitorManager.Builder builder, OnPackageEventListener listener) {
        if (application == null) {
            throw new RuntimeException("SceneManager must be cal init(Application app) first");
        }
        packageMonitorManager = PackageMonitorManager.getInstance(builder);
        packageMonitorManager.init(application);
        packageMonitorManager.setOnPackageEventListener(listener);
    }

    public void registerPowerConnectedListener(PowerMonitorManager.Builder builder, OnRechargeChangeListener listener){
        if (application == null){
            throw new RuntimeException("SceneManager must be cal init(Application app) first");
        }
        powerMonitorManager = PowerMonitorManager.getInstance(builder);
        powerMonitorManager.init(application);
        powerMonitorManager.onPowerConnectListener(listener);
    }

    public void unRegisterAllOutScene(){
        PackageMonitorManager.getInstance(null).onDestroy();
        NetWorkMonitorManager.getInstance(null).onDestroy();
        HomeKeyMonitorManager.getInstance(null).onDestroy();
    }
}
