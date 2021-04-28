package com.faxiang.default_scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.common.theone.interfaces.common.admodel.AdConfigs;
import com.faxiang.default_scene.R;
import com.faxiang.default_scene.utils.activity_result_bridge.BridgeResult;
import com.faxiang.default_scene.utils.activity_result_bridge.CallbackResult;
import com.faxiang.default_scene.utils.statusbar.StatusBarUtil;
import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.ad.DRewardVideoAd;
import com.gatherad.sdk.style.listeners.OnVideoAdListener;

public class RewardVideoAdActivity extends AppCompatActivity {

    private DRewardVideoAd mRewardVideoAd;

    public static final int AD_ERROR = 0x0000001;
    public static final int AD_SUCCESS = 0x0000002;
    public static final int REQUEST_CODE = 0x0000003;

    private static final String TAG = "GATHER_POS_ID";
    private String posId = "";

    /**
     * @param posId 聚合广告位id
     */
    public static void startInstanceWithCallback(Fragment fragment, String posId, CallbackResult callback) {
        Intent intent = new Intent(fragment.getContext(), RewardVideoAdActivity.class);
        intent.putExtra(TAG, posId);
        new BridgeResult(fragment).startActivityWithResult(intent, REQUEST_CODE, callback);
    }

    /**
     * @param posId 聚合广告位id
     */
    public static void startInstanceWithCallback(FragmentActivity activity, String posId, CallbackResult callback) {
        Intent intent = new Intent(activity, RewardVideoAdActivity.class);
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
        mRewardVideoAd = GatherAdService.rewardVideoAd(posId);
        if (mRewardVideoAd != null) {
            loadVideoAdWithConfig();
        }else {
            setResult(AD_SUCCESS);
            finish();
        }
    }

    private void loadVideoAdWithConfig() {
        mRewardVideoAd.showAd(this, new OnVideoAdListener() {
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
        if (mRewardVideoAd != null) {
            mRewardVideoAd.destroy();
        }
    }
}