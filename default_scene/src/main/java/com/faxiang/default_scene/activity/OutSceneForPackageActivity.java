package com.faxiang.default_scene.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.faxiang.default_scene.OutScenePosId;
import com.faxiang.default_scene.OutSceneTag;
import com.faxiang.default_scene.R;
import com.faxiang.default_scene.packagers.PackageMonitorManager;
import com.faxiang.default_scene.packagers.PackageStatus;
import com.faxiang.default_scene.utils.statusbar.StatusBarUtil;
import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.listeners.OnAdEventListener;

public class OutSceneForPackageActivity extends AppCompatActivity {

    public static final String TAG = "OutSceneForPackageActivity";
    public static final String TAG1 = "OutSceneForPackageActivity1";
    private PackageStatus action;
    private String packageName = "";

    private ImageView  ivLogo;
    private RelativeLayout clRoot;
    private TextView tvTitle,tvBtnGo;
    private FrameLayout frameAdLayout;
    private CountDownTimer downTimer;

    public static void startInstance(Context context, String action, String packageName) {
        Intent intent = new Intent(context, OutSceneForPackageActivity.class);
        intent.putExtra(TAG, action);
        intent.putExtra(TAG1, packageName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_out_scene_for_package);
        StatusBarUtil.setTranslucentStatus(this);
        if (getIntent() != null) {
            action = (PackageStatus) getIntent().getSerializableExtra(TAG);
            packageName = getIntent().getStringExtra(TAG1);
        }
        ivLogo = findViewById(R.id.iv_logo);
        clRoot = findViewById(R.id.cl_root);
        tvTitle = findViewById(R.id.tv_title);
        tvBtnGo = findViewById(R.id.tvBtnGo);
        frameAdLayout = findViewById(R.id.frameAdLayout);

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutSceneFinishActivity.startInstance(v.getContext(), OutSceneTag.HOME_CLEAR_RUBBISH);
                finish();
            }
        });
        if (OutScenePosId.getInstance().isPopNativeShow()){
            showNativeAd();
        }
        if (OutScenePosId.getInstance().isAutoExecute()) {
            setDownTimer();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            action = (PackageStatus) getIntent().getSerializableExtra(TAG);
            packageName = getIntent().getStringExtra(TAG1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String appName = PackageMonitorManager.getInstance(null).getAppLabelByPackageName(packageName);
        Drawable appIcon = PackageMonitorManager.getInstance(null).getAppLogoByPackageName(packageName);
        if (appName != null) {
            if (action == PackageStatus.PACKAGE_ADDED) {
                SpannableString spannableString = new SpannableString("[" + appName + "]已安装，产生大量垃圾文件，建立现在清理");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009CFF"));
                spannableString.setSpan(colorSpan, 0, spannableString.length() - 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tvTitle.setText(spannableString);
            } else if (action == PackageStatus.PACKAGE_REMOVED) {
                SpannableString spannableString = new SpannableString("[" + appName + "]已卸载，产生大量垃圾文件，建立现在清理");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF4B4B"));
                spannableString.setSpan(colorSpan, 0, spannableString.length() - 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tvTitle.setText(spannableString);
            } else {
                finish();
            }
        } else {
            if (action == PackageStatus.PACKAGE_ADDED) {
                tvTitle.setText("应用已安装，产生大量垃圾文件，建立现在清理");
            } else if (action == PackageStatus.PACKAGE_REMOVED) {
                tvTitle.setText("应用已卸载，产生大量垃圾文件，建立现在清理");
            } else {
                finish();
            }
        }
        if (appIcon != null) {
            ivLogo.setImageDrawable(appIcon);
        } else {
            ivLogo.setImageResource(R.mipmap.icon_out_scene_mobile_manager);
        }
    }

    /**
     * 屏蔽掉返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downTimer != null){
            downTimer.cancel();
            downTimer = null;
        }
    }

    private void setDownTimer(){
        if (downTimer == null){
            downTimer = new CountDownTimer(6000,1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millis) {
                    long time = millis/1000;
                    if (tvBtnGo != null){
                        tvBtnGo.setText("立即清理 "+(time==0L?"":" ( "+time+" )"));
                    }
                }

                @Override
                public void onFinish() {
                    OutSceneFinishActivity.startInstance(OutSceneForPackageActivity.this, OutSceneTag.HOME_CLEAR_RUBBISH);
                    finish();
                }
            };
        }
        downTimer.start();
    }

    private void showNativeAd(){
        GatherAdService.nativeAd(OutScenePosId.getInstance().getOutPopNativeId())
                .setAdSize(300,0)
                .setBindDislike(true)
                .showAd(this, frameAdLayout, new OnAdEventListener() {
                    @Override
                    public void onAdShowLoaded() {
                        if (frameAdLayout != null){
                            frameAdLayout.setVisibility(View.VISIBLE);
                        }
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

                    }

                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdClose() {
                        if (frameAdLayout != null){
                            frameAdLayout.removeAllViews();
                            frameAdLayout.setVisibility(View.GONE);
                        }
                    }
                });
    }
}