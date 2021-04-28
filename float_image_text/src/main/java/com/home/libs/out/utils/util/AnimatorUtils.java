package com.home.libs.out.utils.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;

public class AnimatorUtils {

    /**
     * 金币+1的动画
     */
    public static void startAddOneGoldAnimator(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", 20, -50);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "alpha", 1.0F, 0.0F);
        animator2.setDuration(1000);
        animator3.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator2, animator3);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 扫描旋转动画
     */
    public static void startScanningAnimator(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "rotation", 0f, 720f);
        animator2.setDuration(3000);
        animator2.setRepeatCount(-1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 红包晃动效果动画
     */
    public static void startRedPacketAnimal(View view) {
        Animation rotateAnimation = new RotateAnimation(0, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new CycleInterpolator(3));
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(900);
        rotateAnimation.setStartOffset(800);
        view.startAnimation(rotateAnimation);
    }
}
