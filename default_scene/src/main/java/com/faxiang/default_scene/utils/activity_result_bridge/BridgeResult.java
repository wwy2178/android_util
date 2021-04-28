package com.faxiang.default_scene.utils.activity_result_bridge;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class BridgeResult {

    private BridgeFragment mBridgeFragment;
    private String mTag = "tag";

    public BridgeResult(FragmentActivity activity) {
        mBridgeFragment = getBridgeFragment(activity);
    }

    public BridgeResult(@NonNull Fragment fragment) {
        if (fragment.getActivity() != null) {
            mBridgeFragment = getBridgeFragment(Objects.requireNonNull(fragment.getActivity()));
        }
    }

    //初始化Fragment对象 进行生命周期绑定
    private BridgeFragment getBridgeFragment(FragmentActivity activity) {
        //初始化Fragment时，将它与Activity生命周期进行绑定
        BridgeFragment bridgeFragment = new BridgeFragment();
        FragmentManager manager = activity.getSupportFragmentManager();
        try {
            if (!manager.isDestroyed() && !activity.isDestroyed() && !activity.isFinishing()) {
                manager.beginTransaction().add(bridgeFragment, mTag).commitNowAllowingStateLoss();
                return bridgeFragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //提供跳转方法
    public void startActivityWithResult(Intent intent, int requestCode, CallbackResult callback) {
        if (mBridgeFragment != null) {
            mBridgeFragment.startResult(intent, requestCode, callback);
        }
    }
}