package com.home.libs.out.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gatherad.sdk.GatherAdService;
import com.gatherad.sdk.style.listeners.OnAdEventListener;
import com.home.libs.R;
import com.home.libs.out.impl.OutScenePosId;
import com.home.libs.out.impl.OutSceneTag;
import com.home.libs.out.recharge.RechargeStatus;

/**
 * Auther: Created by Admin on 2021/4/21
 * Description : 充电或断开电源体外显示
 */
public class OutSceneRechargeActivity extends Activity {
    public static final String TAG = "OutSceneRechargeActivity";
    private ImageView imgClose;
    private TextView powerStatus,tvBtnGo;
    private RelativeLayout rlAdlayout;
    private RechargeStatus rechargeStatus = RechargeStatus.ONPOWER_NO_STATUS;
    private CountDownTimer countDownTimer;
    private Intent intent;
    private String powerStatusCont = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_out_scene_recharge);

        imgClose = findViewById(R.id.imgClose);
        powerStatus = findViewById(R.id.powerStatus);
        rlAdlayout = findViewById(R.id.rlAdlayout);
        tvBtnGo = findViewById(R.id.tvBtnGo);
        imgClose.setOnClickListener(v -> finish());
        if (OutScenePosId.getInstance().isPopNativeShow()) {
            showNativeAd();
        }
        intent = getIntent();
        rechargeStatus();
        setViewClick();
    }

    private void rechargeStatus(){
        if (intent != null) {
            rechargeStatus = (RechargeStatus) intent.getSerializableExtra(TAG);
        }
        if (rechargeStatus == RechargeStatus.ONPOWER_CONNECTED){
            powerStatus.setText("电源已连接");
            powerStatusCont = "充电加速";
            tvBtnGo.setText(powerStatusCont);
            Drawable disConnectDraw = getApplication().getResources().getDrawable(R.drawable.svg_power_connected_icon);
            disConnectDraw.setBounds(0,0,disConnectDraw.getMinimumWidth(),disConnectDraw.getMinimumHeight());
            powerStatus.setCompoundDrawables(null,disConnectDraw,null,null);
        }else {
            powerStatus.setText("电源已断开");
            powerStatusCont = "延缓放电";
            tvBtnGo.setText(powerStatusCont);
            Drawable disConnectDraw = getApplication().getResources().getDrawable(R.drawable.power_disconnected_icon);
            disConnectDraw.setBounds(0,0,disConnectDraw.getMinimumWidth(),disConnectDraw.getMinimumHeight());
            powerStatus.setCompoundDrawables(null,disConnectDraw,null,null);
        }
    }

    private void setViewClick(){
        tvBtnGo.setOnClickListener(v -> {
            OutSceneFinishActivity.startInstance(OutSceneRechargeActivity.this, rechargeStatus == RechargeStatus.ONPOWER_CONNECTED?  OutSceneTag.POWER_SPEED_CONNECTED:OutSceneTag.POWER_SPEED_DISCONNECTED);
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
        rechargeStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OutScenePosId.getInstance().isAutoExecute()) {
            setTimerCounter();
        }
    }

    private void setTimerCounter(){
        if (countDownTimer == null){
            countDownTimer = new CountDownTimer(6000,1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millis) {
                    long time = millis/1000L;
                    if (tvBtnGo != null){
                        tvBtnGo.setText(powerStatusCont+(time==0L?"":" ( "+time+" )"));
                    }
                }

                @Override
                public void onFinish() {
                    OutSceneFinishActivity.startInstance(OutSceneRechargeActivity.this, rechargeStatus == RechargeStatus.ONPOWER_CONNECTED?  OutSceneTag.POWER_SPEED_CONNECTED:OutSceneTag.POWER_SPEED_DISCONNECTED);
                    finish();
                }
            };
        }
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    private void showNativeAd(){
        GatherAdService.nativeAd(OutScenePosId.getInstance().getOutPopNativeId())
                .setAdSize(280,0)
                .setBindDislike(true)
                .showAd(this, rlAdlayout, new OnAdEventListener() {
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

                    }

                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdClose() {
                        if (rlAdlayout != null){
                            rlAdlayout.removeAllViews();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
