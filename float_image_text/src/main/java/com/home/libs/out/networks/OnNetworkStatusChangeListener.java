package com.home.libs.out.networks;

import android.content.Context;

public interface OnNetworkStatusChangeListener {
    void onNetworkStatusChange(Context context, NetworkState stats);
}
