package com.faxiang.default_scene.networks;

import android.content.Context;

public interface OnNetworkStatusChangeListener {
    void onNetworkStatusChange(Context context, NetworkState stats);
}
