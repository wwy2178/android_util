package com.faxiang.default_scene.recharge;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

/**
 * Auther: Created by Admin on 2021/4/21
 * Description : 充电状态管理
 */
public class PowerMonitorManager {

    private static PowerMonitorManager ourInstance;
    private boolean isDisplay = false;
    private Application application;
    private OnRechargeChangeListener changeListener;

    public static PowerMonitorManager getInstance(Builder builder){
        synchronized (PowerMonitorManager.class) {
            if (ourInstance == null) {
                ourInstance = new PowerMonitorManager(builder);
            }
        }
        return ourInstance;
    }

    private PowerMonitorManager(PowerMonitorManager.Builder builder) {
        if (builder == null) {
            this.isDisplay = true;
        } else {
            this.isDisplay = builder.isDisPlay;
        }
    }

    public void init(Application application){
        if (application == null) {
            throw new NullPointerException("application can not be null");
        }
        this.application = application;
        if (!isDisplay) {
            return;
        }

        initPowerMonitor();
    }

    private void initPowerMonitor() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            this.application.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.contains(Intent.ACTION_POWER_CONNECTED)){// 连接电源
                if (changeListener != null){
                    changeListener.onPowerChanged(context, RechargeStatus.ONPOWER_CONNECTED);
                }
            }else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)){// 断开电源
                if (changeListener != null){
                    changeListener.onPowerChanged(context, RechargeStatus.ONPOWER_DISCONNECT);
                }
            }
        }
    };

    public void onPowerConnectListener(OnRechargeChangeListener listener){
        changeListener = listener;
    }

    public void onDestory(){
        this.application.unregisterReceiver(broadcastReceiver);
    }

    public static class Builder {
        private boolean isDisPlay = true;

        /**
         * 开关控制
         *
         * @param isDisPlay 开关控制
         */
        public Builder setDisPlay(boolean isDisPlay) {
            this.isDisPlay = isDisPlay;
            return this;
        }

        private PowerMonitorManager build() {
            return new PowerMonitorManager(this);
        }
    }
}
