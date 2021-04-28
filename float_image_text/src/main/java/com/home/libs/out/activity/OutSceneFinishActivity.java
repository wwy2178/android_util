package com.home.libs.out.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.home.libs.R;
import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.ad.DNativeAd;
import com.gatherad.sdk.style.listeners.OnAdEventListener;
import com.google.android.material.appbar.AppBarLayout;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsFeedPage;
import com.kwad.sdk.api.KsScene;
import com.home.libs.out.impl.OutScenePosId;
import com.home.libs.out.impl.OutSceneTag;
import com.home.libs.out.impl.VideoAdType;
import com.home.libs.out.utils.activity_result_bridge.CallbackResult;
import com.home.libs.out.utils.statusbar.StatusBarUtil;
import com.home.libs.out.utils.util.AnimatorUtils;
import com.home.libs.out.utils.util.SimulateClickUtils;

public class OutSceneFinishActivity extends AppCompatActivity {

    public static final String TAG = "OUTSCENEFINISHACTIVITY";
    private OutSceneTag outSceneTag = OutSceneTag.HOME_MOBILE_MANGER;
    private KsFeedPage mKsFeedPage;
    private DNativeAd mDNativeAd;
    private LottieAnimationView lottieAnimationView;
    private TextView tvTitle, tvDesc, tvLoading, tvIKnow;
    private ImageView ivClose, ivAnimationView;
    private FrameLayout fmAnimationView, fmAdContainer, container;
    private View viewTop;

    public static void startInstance(Context context, OutSceneTag tag) {
        Intent intent = new Intent(context, OutSceneFinishActivity.class);
        intent.putExtra(TAG, tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_out_scene_finish);
        StatusBarUtil.setTranslucentStatus(this);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        tvLoading = findViewById(R.id.tv_loading);
        tvIKnow = findViewById(R.id.tv_i_know);
        lottieAnimationView = findViewById(R.id.lottie_animation_view);
        ivClose = findViewById(R.id.iv_close);
        ivAnimationView = findViewById(R.id.iv_animation_view);
        fmAnimationView = findViewById(R.id.fm_animation_view);
        fmAdContainer = findViewById(R.id.fm_ad_container);
        container = findViewById(R.id.container);
        viewTop = findViewById(R.id.view_top);

        mDNativeAd = GatherAdService.nativeAd(OutScenePosId.getInstance().getNativeId());
        if (getIntent() != null) {
            outSceneTag = (OutSceneTag) getIntent().getSerializableExtra(TAG);
        }
        initContentPage();
        showContentPage();
        initLottieAnimationView();
        if (mDNativeAd != null) {
            showAd();
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            outSceneTag = (OutSceneTag) intent.getSerializableExtra(TAG);
        }
        initLottieAnimationView();
    }

    private void initLottieAnimationView() {
        if (outSceneTag == OutSceneTag.HOME_WIFI || outSceneTag == OutSceneTag.NETWORK_WIFI) {
            lottieAnimationView.setVisibility(View.GONE);
            fmAnimationView.setVisibility(View.VISIBLE);
            AnimatorUtils.startScanningAnimator(ivAnimationView);
        } else {
            fmAnimationView.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.VISIBLE);
        }
        tvLoading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lottieAnimationView == null) return;
                lottieAnimationView.pauseAnimation();
                lottieAnimationView.setVisibility(View.GONE);
                if (fmAnimationView == null) return;
                fmAnimationView.setVisibility(View.GONE);
                if (tvLoading == null) return;
                tvLoading.setVisibility(View.GONE);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadVideoAd();
            }
        }, 3000);
    }

    private void loadVideoAd() {
        if (OutScenePosId.getInstance().getVideoIdType() == VideoAdType.REWARD_VIDEO) {
            String posId = OutScenePosId.getInstance().getRewardVideoIdForHomeKeyClear();
            if (outSceneTag == OutSceneTag.HOME_CLEAR_RUBBISH) {
                posId = OutScenePosId.getInstance().getRewardVideoIdForHomeKeyClear();
            } else if (outSceneTag == OutSceneTag.HOME_MOBILE_MANGER) {
                posId = OutScenePosId.getInstance().getRewardVideoIdForHomeKeyMobileManager();
            } else if (outSceneTag == OutSceneTag.HOME_WIFI) { //home按键场景的
                posId = OutScenePosId.getInstance().getRewardVideoIdForHomeKeyWIFI();
            } else if (outSceneTag == OutSceneTag.NETWORK_GPRS) {
                posId = OutScenePosId.getInstance().getRewardVideoIdForNetwork();
            } else if (outSceneTag == OutSceneTag.NETWORK_WIFI) { //网络弹窗场景
                posId = OutScenePosId.getInstance().getRewardVideoIdForNetwork();
            } else if (outSceneTag == OutSceneTag.PACKAGE_CHANGE) {
                posId = OutScenePosId.getInstance().getRewardVideoIdForPackage();
            } else if (outSceneTag == OutSceneTag.POWER_SPEED_CONNECTED||outSceneTag == OutSceneTag.POWER_SPEED_DISCONNECTED){
                posId = OutScenePosId.getInstance().getRewardVideoIdForRecharge();
            }

            RewardVideoAdActivity.startInstanceWithCallback(OutSceneFinishActivity.this, posId, new CallbackResult() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (lottieAnimationView == null) return;
                    lottieAnimationView.pauseAnimation();
                    lottieAnimationView.setVisibility(View.GONE);
                    if (fmAnimationView == null) return;
                    fmAnimationView.setVisibility(View.GONE);
                }
            });
        } else {
            String posId = OutScenePosId.getInstance().getFullVideoIdForHomeKeyClear();
            if (outSceneTag == OutSceneTag.HOME_CLEAR_RUBBISH) {
                posId = OutScenePosId.getInstance().getFullVideoIdForHomeKeyClear();
            } else if (outSceneTag == OutSceneTag.HOME_MOBILE_MANGER) {
                posId = OutScenePosId.getInstance().getFullVideoIdForHomeKeyMobileManager();
            } else if (outSceneTag == OutSceneTag.HOME_WIFI) { //home按键场景的
                posId = OutScenePosId.getInstance().getFullVideoIdForHomeKeyWIFI();
            } else if (outSceneTag == OutSceneTag.NETWORK_GPRS) {
                posId = OutScenePosId.getInstance().getFullVideoIdForNetwork();
            } else if (outSceneTag == OutSceneTag.NETWORK_WIFI) { //网络弹窗场景
                posId = OutScenePosId.getInstance().getFullVideoIdForNetwork();
            } else if (outSceneTag == OutSceneTag.PACKAGE_CHANGE) {
                posId = OutScenePosId.getInstance().getFullVideoIdForPackage();
            }else if (outSceneTag == OutSceneTag.POWER_SPEED_CONNECTED||outSceneTag == OutSceneTag.POWER_SPEED_DISCONNECTED){
                posId = OutScenePosId.getInstance().getFullVideoIdForRecharge();
            }
            FullVideoAdActivity.startInstanceWithCallback(OutSceneFinishActivity.this, posId, new CallbackResult() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (lottieAnimationView == null) return;
                    lottieAnimationView.pauseAnimation();
                    lottieAnimationView.setVisibility(View.GONE);
                    if (fmAnimationView == null) return;
                    fmAnimationView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (outSceneTag == OutSceneTag.HOME_CLEAR_RUBBISH || outSceneTag == OutSceneTag.PACKAGE_CHANGE) {
            tvTitle.setText("已清除手机中的垃圾文件");
            tvDesc.setText("手机内存得到了释放");
            lottieAnimationView.setAnimation("clear_rubbish/data.json");
            lottieAnimationView.playAnimation();
        } else if (outSceneTag == OutSceneTag.NETWORK_WIFI) {
            tvTitle.setText("WIFI网络速度已加速");
            tvDesc.setText("网络得到了明显提升");
        } else if (outSceneTag == OutSceneTag.HOME_MOBILE_MANGER) {
            tvTitle.setText("已清理后台运行应用");
            tvDesc.setText("手机运行速度明显提升");
            lottieAnimationView.setAnimation("mobile_manager/data.json");
            lottieAnimationView.playAnimation();
        } else if (outSceneTag == OutSceneTag.HOME_WIFI) {
            tvTitle.setText("已检测并清除了蹭网设备");
            tvDesc.setText("网络得到了明显提升");
        } else if (outSceneTag == OutSceneTag.NETWORK_GPRS) {
            tvTitle.setText("已清理后台应用");
            tvDesc.setText("为您节省了大量4G流量");
            lottieAnimationView.setAnimation("clear_rubbish/data.json");
            lottieAnimationView.playAnimation();
        } else if (outSceneTag == OutSceneTag.POWER_SPEED_CONNECTED){
            tvTitle.setText("充电加速已完成");
            tvDesc.setText("为您节省了大量充电时间");
            lottieAnimationView.setAnimation("power_speed/data.json");
            lottieAnimationView.playAnimation();
        } else if (outSceneTag == OutSceneTag.POWER_SPEED_DISCONNECTED){
            tvTitle.setText("电池守护已完成");
            tvDesc.setText("手机延缓放电完成");
            lottieAnimationView.setAnimation("power_speed/data.json");
            lottieAnimationView.playAnimation();
        }
    }

    private void showAd() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        float width = dm.widthPixels / 360 / dm.density * 340;
        mDNativeAd.setAdSize(width, 0).showAd(this, fmAdContainer, new OnAdEventListener() {
            @Override
            public void onAdShowLoaded() {

            }

            @Override
            public void onAdShowLoadFail(int i, String s) {

            }

            @Override
            public void onRenderSuccess(View view) {

            }

            @Override
            public void onRenderFail(int i, String s) {

            }

            @Override
            public void onAdClick() {
                finish();
            }

            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClose() {

            }
        });
        tvIKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimulateClickUtils.clickView(fmAdContainer, true, null);
            }
        });
    }

    /**
     * 1.获取接口对象 KsFeedPage
     */
    private void initContentPage() {
        KsScene adScene = new KsScene.Builder(OutScenePosId.getInstance().getKsSceneId()).build();
        if (adScene == null || KsAdSDK.getLoadManager() == null) {
            setAppBarNoScroll();
            return;
        }
        mKsFeedPage = KsAdSDK.getLoadManager().loadFeedPage(adScene);
    }

    /**
     * 2.获取的Fragment进行展示
     */
    private void showContentPage() {
        if (mKsFeedPage != null && mKsFeedPage.getFragment() != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mKsFeedPage.getFragment())
                    .commitAllowingStateLoss();
        } else {
            container.setVisibility(View.GONE);
            setAppBarNoScroll();
        }
    }

    private void setAppBarNoScroll() {
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) viewTop.getLayoutParams();
        mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
        viewTop.setLayoutParams(mParams);
    }

    /**
     * 屏蔽掉返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}