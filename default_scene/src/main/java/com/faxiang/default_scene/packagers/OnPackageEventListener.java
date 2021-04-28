package com.faxiang.default_scene.packagers;

import android.content.Context;

public interface OnPackageEventListener {
    void onPackageStatusChanged(Context context,PackageStatusInfo info);
}
