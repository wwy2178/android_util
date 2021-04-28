package com.home.libs.out.packagers;

import android.content.Context;

public interface OnPackageEventListener {
    void onPackageStatusChanged(Context context,PackageStatusInfo info);
}
