package com.home.libs.out.networks;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * AnnotationApplication
 * Created by anonyper on 2019/6/10.
 */
public class NetWorkMonitorManager {
    public static final String TAG = "NetWorkMonitor >>> : ";
    private static NetWorkMonitorManager ourInstance;
    private Application application;
    private ConnectivityManager.NetworkCallback networkCallback = null;
    private NetworkState state;
    private boolean isDisplay = false;

    public static NetWorkMonitorManager getInstance(Builder builder) {
        synchronized (NetWorkMonitorManager.class) {
            if (ourInstance == null) {
                ourInstance = new NetWorkMonitorManager(builder);
            }
        }
        return ourInstance;
    }


    private NetWorkMonitorManager(Builder builder) {
        if (builder == null) {
            this.isDisplay = true;
        } else {
            this.isDisplay = builder.isDisPlay;
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
        if (!isDisplay) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initNetworkCallback();
        }
        initMonitor();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            /**
             * 网络可用的回调连接成功
             */
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                int netType = NetStateUtils.getAPNType(NetWorkMonitorManager.this.application);
                NetworkState netWorkState = NetworkState.NONE;
                switch (netType) {
                    case 0://None
                        netWorkState = NetworkState.NONE;
                        break;
                    case 1://Wifi
                        netWorkState = NetworkState.WIFI;
                        break;
                    default://GPRS
                        netWorkState = NetworkState.GPRS;
                        break;
                }
                //首次初始化 或者 两次状态一样的时候，只记录状态，不走回调
                if (NetWorkMonitorManager.this.state == null
                        || NetWorkMonitorManager.this.state == netWorkState
                        || netWorkState == NetworkState.NONE) {
                    NetWorkMonitorManager.this.state = netWorkState;
                    return;
                }
                NetWorkMonitorManager.this.state = netWorkState;
                if (onNetworkStatusChangeListener != null) {
                    onNetworkStatusChangeListener.onNetworkStatusChange(application, netWorkState);
                }
            }

            /**
             * 网络不可用时调用和onAvailable成对出现
             */
            @Override
            public void onLost(Network network) {
                super.onLost(network);
                //首次初始化 或者 两次状态一样的时候，只记录状态，不走回调
            }

            /**
             * 在网络连接正常的情况下，丢失数据会有回调 即将断开时
             */
            @Override
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
            }

            /**
             * 网络功能更改 满足需求时调用
             * @param network
             * @param networkCapabilities
             */
            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            /**
             * 网络连接属性修改时调用
             * @param network
             * @param linkProperties
             */
            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
            }

            /**
             * 网络缺失network时调用
             */
            @Override
            public void onUnavailable() {
                super.onUnavailable();
            }
        };
    }

    /**
     * 初始化网络监听 根据不同版本做不同的处理
     */
    private void initMonitor() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.application.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//API 大于26时
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API 大于21时
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        } else {//低版本
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ANDROID_NET_CHANGE_ACTION);
            this.application.registerReceiver(receiver, intentFilter);
        }
    }

    /**
     * 反注册广播
     */
    public void onDestroy() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            this.application.unregisterReceiver(receiver);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager connectivityManager = (ConnectivityManager) this.application.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.application.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
                //网络发生变化 没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
                int netType = NetStateUtils.getAPNType(context);
                NetworkState netWorkState = NetworkState.NONE;
                switch (netType) {
                    case 0://None
                        netWorkState = NetworkState.NONE;
                        break;
                    case 1://Wifi
                        netWorkState = NetworkState.WIFI;
                        break;
                    default://GPRS
                        netWorkState = NetworkState.GPRS;
                        break;
                }

                //首次初始化 或者 两次状态一样的时候，只记录状态，不走回调
                if (NetWorkMonitorManager.this.state == null || NetWorkMonitorManager.this.state == netWorkState) {
                    NetWorkMonitorManager.this.state = netWorkState;
                    return;
                }
                NetWorkMonitorManager.this.state = netWorkState;
                if (onNetworkStatusChangeListener != null) {
                    onNetworkStatusChangeListener.onNetworkStatusChange(application, netWorkState);
                }
            }
        }
    };

    private OnNetworkStatusChangeListener onNetworkStatusChangeListener;

    public void setOnNetworkStatusChangeListener(OnNetworkStatusChangeListener onNetworkStatusChangeListener) {
        this.onNetworkStatusChangeListener = onNetworkStatusChangeListener;
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

        private NetWorkMonitorManager build() {
            return new NetWorkMonitorManager(this);
        }
    }
}