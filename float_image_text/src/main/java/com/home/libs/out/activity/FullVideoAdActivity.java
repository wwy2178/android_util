package com.home.libs.out.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.home.libs.R;
import com.home.libs.out.utils.activity_result_bridge.BridgeResult;
import com.home.libs.out.utils.activity_result_bridge.CallbackResult;
import com.home.libs.out.utils.statusbar.StatusBarUtil;
import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.ad.DFullScreenVideoAd;
import com.gatherad.sdk.style.listeners.OnVideoAdListener;

public class FullVideoAdActivity extends AppCompatActivity {

    private DFullScreenVideoAd mDFullScreenVideoAd;

    public static final int AD_ERROR = 0x0000001;
    public static final int AD_SUCCESS = 0x0000002;
    public static final int REQUEST_CODE = 0x0000003;

    private static final String TAG = "GATHER_POS_ID";
    private String posId = "";

    /**
     * @param posId 聚合广告位id
     */
    public static void startInstanceWithCallback(Fragment fragment, String posId, CallbackResult callback) {
        Intent intent = new Intent(fragment.getContext(), FullVideoAdActivity.class);
        intent.putExtra(TAG, posId);
        new BridgeResult(fragment).startActivityWithResult(intent, REQUEST_CODE, callback);
    }

    /**
     * @param posId 聚合广告位id
     */
    public static void startInstanceWithCallback(FragmentActivity activity, String posId, CallbackResult callback) {
        Intent intent = new Intent(activity, FullVideoAdActivity.class);
        intent.putExtra(TAG, posId);
        new BridgeResult(activity).startActivityWithResult(intent, REQUEST_CODE, callback);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(TAG))) {
            posId = getIntent().getStringExtra(TAG);
        }
        setContentView(R.layout.activity_video_ad);
        StatusBarUtil.setTranslucentStatus(this);
        mDFullScreenVideoAd = GatherAdService.fullScreenVideoAd(posId);
        if (mDFullScreenVideoAd != null) {
            loadVideoAdWithConfig();
        } else {
            setResult(AD_SUCCESS);
            finish();
        }
    }

    private void loadVideoAdWithConfig() {
        mDFullScreenVideoAd.showAd(this, new OnVideoAdListener() {
            @Override
            public void onAdShowLoaded() {

            }

            @Override
            public void onAdShowLoadFail(int i, String s) {
                setResult(AD_ERROR);
                finish();
            }

            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdClose() {
                setResult(AD_SUCCESS);
                finish();
            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onVideoError() {

            }

            @Override
            public void onRewardVerify() {

            }

            @Override
            public void onSkippedVideo() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDFullScreenVideoAd != null) {
            mDFullScreenVideoAd.destroy();
        }
    }
}