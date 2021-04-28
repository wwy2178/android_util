package com.faxiang.default_scene;

import android.app.Application;

import com.faxiang.default_scene.home_key.HomeKeyMonitorManager;
import com.faxiang.default_scene.home_key.OnHomeKeyEventListener;
import com.faxiang.default_scene.networks.NetWorkMonitorManager;
import com.faxiang.default_scene.networks.OnNetworkStatusChangeListener;
import com.faxiang.default_scene.packagers.OnPackageEventListener;
import com.faxiang.default_scene.packagers.PackageMonitorManager;
import com.faxiang.default_scene.recharge.OnRechargeChangeListener;
import com.faxiang.default_scene.recharge.PowerMonitorManager;

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
