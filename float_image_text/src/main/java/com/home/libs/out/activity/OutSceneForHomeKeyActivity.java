package com.home.libs.out.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.listeners.OnAdEventListener;
import com.home.libs.R;
import com.home.libs.out.impl.OutScenePosId;
import com.home.libs.out.impl.OutSceneTag;
import com.home.libs.out.utils.statusbar.StatusBarUtil;


public class OutSceneForHomeKeyActivity extends AppCompatActivity {

    public static final String TAG = "OUTSCENEACTIVITY";
    private OutSceneTag outSceneTag = OutSceneTag.HOME_MOBILE_MANGER;
    private RelativeLayout clRoot;
    private TextView tvDesc,tvTitle,tvBtnGo;
    private ImageView ivLogo;
    private FrameLayout frameAdLayout;
    private CountDownTimer downTimer;

    public static void startInstance(Context context, OutSceneTag tag) {
        Intent intent = new Intent(context, OutSceneForHomeKeyActivity.class);
        intent.putExtra(TAG, tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_out_scene_for_homekey);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        ivLogo = findViewById(R.id.iv_logo);
        clRoot = findViewById(R.id.cl_root);
        tvBtnGo = findViewById(R.id.tvBtnGo);
        frameAdLayout = findViewById(R.id.frameAdLayout);

        StatusBarUtil.setTranslucentStatus(this);
        if (getIntent() != null) {
            outSceneTag = (OutSceneTag) getIntent().getSerializableExtra(TAG);
        }

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutSceneFinishActivity.startInstance(v.getContext(), outSceneTag);
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
            outSceneTag = (OutSceneTag) intent.getSerializableExtra(TAG);
        }
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


    @Override
    protected void onResume() {
        super.onResume();
        if (outSceneTag == OutSceneTag.HOME_CLEAR_RUBBISH) {
            tvDesc.setText("注意：手机存在大量垃圾，严重\n影响手机内存，点击立即优化");
            tvTitle.setText("垃圾清理");
            ivLogo.setImageResource(R.mipmap.icon_out_scene_clear);
        } else if (outSceneTag == OutSceneTag.HOME_WIFI) {
            tvDesc.setText("检测到附近有人蹭网，严重影响您的\n网速和网络安全，点击立即优化");
            tvTitle.setText("防蹭网");
            ivLogo.setImageResource(R.mipmap.icon_out_scene_wifi);
        } else if (outSceneTag == OutSceneTag.HOME_MOBILE_MANGER) {
            tvDesc.setText("注意：发现多个应用程序正在后台运\n行！手机卡顿，点击立即优化");
            tvTitle.setText("手机管家");
            ivLogo.setImageResource(R.mipmap.icon_out_scene_mobile_manager);
        }
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
                        tvBtnGo.setText("立即优化 "+(time==0L?"":" ( "+time+" )"));
                    }
                }

                @Override
                public void onFinish() {
                    OutSceneFinishActivity.startInstance(OutSceneForHomeKeyActivity.this, outSceneTag);
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