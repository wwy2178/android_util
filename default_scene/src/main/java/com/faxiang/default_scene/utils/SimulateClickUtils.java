package com.faxiang.default_scene.utils;

import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.common.theone.interfaces.common.admodel.AdConfigs;

import java.util.Random;

public class SimulateClickUtils {

    private static final String TAG = "SimulateClickUtils";

    static Handler mMainHandler = new Handler();

    /**
     * 根据是否透传 进行模拟点击
     *
     * @param view       广告容器
     * @param isTouChuan 是否透传
     * @param listener 模拟点击完成的回调
     */
    public static void clickView(View view, boolean isTouChuan, OnClickFinishListener listener) {
        if (!isTouChuan) {
            // 获取透传开关，如果不透传就直接结束
            if (listener != null) {
                listener.onClickFinish(false);
            }
            return;
        }

        if (view == null) {
            if (listener != null) {
                listener.onClickFinish(false);
            }
            return;
        }

        int width = view.getWidth();
        int height = view.getHeight();
        if (width == 0 || height == 0) {
            if (listener != null) {
                listener.onClickFinish(false);
            }
            return;
        }

        int[] coord = randomCoord(width, height);
        clickView(view, coord[0], coord[1]);
//        clickView(view, width / 2, height / 2);
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onClickFinish(true);
                }
            }
        }, 500);
    }

    private static void clickView(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        long eventTime = downTime + randomDelayTime();
        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        // 分发 按下事件
        view.dispatchTouchEvent(downEvent);
        // 分发 抬起事件
        view.dispatchTouchEvent(upEvent);
    }

    /**
     * 随机的坐标点
     *
     * @param width
     * @param height
     * @return
     */
    private static int[] randomCoord(int width, int height) {
        Random random = new Random();//默认构造方法
        int coordX = random.nextInt(width / 2) + width / 4;
        int coordY = random.nextInt(height / 2) + height / 4;
        int[] coord = new int[]{coordX, coordY};
        return coord;
    }

    /**
     * 随机的按下时间
     *
     * @return
     */
    private static int randomDelayTime() {
        Random random = new Random();
        int delayTime = random.nextInt(100) + 100;
//        LogTool.LogE(TAG, "randomDelayTime---> " + delayTime);
        return delayTime;
    }

    public interface OnClickFinishListener {
        public void onClickFinish(boolean isTouChuan);
    }

    /**
     * 概率透过 true-点击广告，false-不点广告
     */
    public static boolean isShowADProbability(String adSwitch) {
        boolean isShowProbability = false;
        Random r = new Random();
        int number = r.nextInt(100) + 1;
        if (AdConfigs.getInstance().isAdConfigsDisplay(adSwitch, false)) {
            isShowProbability = number < AdConfigs.getInstance().getAdConfigsType(adSwitch,0);
        }
        return isShowProbability;
    }
}