package com.faxiang.default_scene.activity;

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
import com.faxiang.default_scene.OutScenePosId;
import com.faxiang.default_scene.OutSceneTag;
import com.faxiang.default_scene.R;
import com.faxiang.default_scene.networks.NetworkState;
import com.faxiang.default_scene.utils.statusbar.StatusBarUtil;
import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.listeners.OnAdEventListener;


public class OutSceneForWifiActivity extends AppCompatActivity {

    public static final String TAG = "OutSceneForWifiActivity";

    private NetworkState networkStats = NetworkState.NONE;
    private ImageView ivLogo;
    private RelativeLayout clRoot;
    private TextView tvTitle,tvDesc,tvBtnGo;
    private FrameLayout frameAdLayout;
    private CountDownTimer downTimer;

    public static void startInstance(Context context, NetworkState stats) {
        Intent intent = new Intent(context, OutSceneForWifiActivity.class);
        intent.putExtra(TAG, stats);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_out_scene_for_wifi);
        StatusBarUtil.setTranslucentStatus(this);
        if (getIntent() != null) {
            networkStats = (NetworkState) getIntent().getSerializableExtra(TAG);
        }
        ivLogo = findViewById(R.id.iv_logo);
        clRoot = findViewById(R.id.cl_root);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        tvBtnGo = findViewById(R.id.tvBtnGo);
        frameAdLayout = findViewById(R.id.frameAdLayout);

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutSceneFinishActivity.startInstance(v.getContext(), networkStats == NetworkState.WIFI ? OutSceneTag.NETWORK_WIFI : OutSceneTag.NETWORK_GPRS);
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
            networkStats = (NetworkState) intent.getSerializableExtra(TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkStats == NetworkState.WIFI) {
            tvDesc.setText("网络已切换至WIFI");
            tvTitle.setText("网络质量：较差");
            ivLogo.setImageResource(R.mipmap.icon_out_scene_state_wifi);
        } else if (networkStats == NetworkState.GPRS) {
            tvDesc.setText("网络已切换至数据流量");
            tvTitle.setText("提示：后台应用正在偷跑流量");
            ivLogo.setImageResource(R.mipmap.icon_out_scene_state_gprs);
        } else {
            finish();
        }
    }

    /**
     * 屏蔽掉返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }if (KeyEvent.KEYCODE_BACK == keyCode){
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
                        tvBtnGo.setText("优化加速 "+(time==0L?"":" ( "+time+" )"));
                    }
                }

                @Override
                public void onFinish() {
                    OutSceneFinishActivity.startInstance(OutSceneForWifiActivity.this, networkStats == NetworkState.WIFI ? OutSceneTag.NETWORK_WIFI : OutSceneTag.NETWORK_GPRS);
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