package com.home.libs.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;

import com.home.libs.R;


/**
 * Auther: Created by Arvin on 2021/4/21
 * Description : 悬浮背景图片文字
 */
public class GoldImageText extends View {

    private int width, height;
    private long animDuration = 2000; // 默认动画时间

    private Paint floatTxtPaint;
//    private Drawable floatBgDrawable, maskDrawableImg;

    private int VISIBILITY_STATUS = 2; // 是否显示
    private int floatViewImage;
    private String floatTxt = ""; // 浮动文字
    private int cornerMaskImg; // 悬浮金币的右上角图标
    //    private String cornerMaskTxt = ""; // 悬浮金币的右上角图标文字
//    private int drawableTopImg; // 浮动显示设置文字上面的图标
//    private float drawablePadding; // 文字上面图标和文字之间距离
    private float floatTxtSize; // 浮动的文字大小
    private int floatTextColor; // 浮动文字颜色
    private int gravity; // 悬浮view内的文字位置
    private float paddingBottom;// 浮动View内部文字若位置底部，则文字距离View底部距离

    private int textStyle; // 字体

    private Bitmap gloatBgBitmap, maskBgBitmap;

    private int maskLeft, maskRight, maskBottom;
    private ValueAnimator valueAnimator;
    private float startPosi = -15.0f, endPosi = 15.0f;

    private CountDownTimer countDownTimer;

    public GoldImageText(Context context) {
        super(context);
    }

    public GoldImageText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public GoldImageText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoldImageText);


        cornerMaskImg = typedArray.getResourceId(R.styleable.GoldImageText_dd_cornerMaskImg, -1);
        VISIBILITY_STATUS = typedArray.getInt(R.styleable.GoldImageText_dd_visibility, 2);
        floatViewImage = typedArray.getResourceId(R.styleable.GoldImageText_dd_floatImg, -1);
//        cornerMaskTxt = typedArray.getString(R.styleable.GoldImageText_dd_cornerMaskTxt);
        floatTxt = typedArray.getString(R.styleable.GoldImageText_dd_floatTxt);
//        drawableTopImg = typedArray.getResourceId(R.styleable.GoldImageText_dd_drawableTop, -1);
//        drawablePadding = typedArray.getDimension(R.styleable.GoldImageText_dd_drawablePadding, 0);
        floatTxtSize = typedArray.getDimension(R.styleable.GoldImageText_dd_floatTxtSize, 12);
        floatTextColor = typedArray.getColor(R.styleable.GoldImageText_dd_floatTxtColor, Color.WHITE);

        textStyle = typedArray.getInt(R.styleable.GoldImageText_dd_textStyle, -1);

        gravity = typedArray.getInt(R.styleable.GoldImageText_dd_gravity, -1);
        paddingBottom = typedArray.getDimension(R.styleable.GoldImageText_dd_paddingBottom, 0);


        BitmapFactory.Options options = new BitmapFactory.Options();
        gloatBgBitmap = BitmapFactory.decodeResource(getResources(), floatViewImage, options);
        int bgWidth = options.outWidth;
        int bgHeight = options.outHeight;
        height = bgHeight;
        width = bgWidth;

        if (cornerMaskImg != -1) {
            int px = dp2px(context, 12);

            maskBgBitmap = BitmapFactory.decodeResource(getResources(), cornerMaskImg, options);
            int maskWidth = options.outWidth + bgWidth - px;
            int maskHwight = options.outHeight;
            width = maskWidth;

            maskLeft = bgWidth - px;
            maskBottom = maskHwight;

            int maskHeight = options.outHeight;
        }


        floatTxtPaint = new TextPaint();
        floatTxtPaint.setAntiAlias(true);
        floatTxtPaint.setTextSize(floatTxtSize);
        floatTxtPaint.setStrokeWidth(5);
        floatTxtPaint.setColor(floatTextColor);
        floatTxtPaint.setTextAlign(Paint.Align.CENTER);
        switch (textStyle) {
            case 1:
                floatTxtPaint.setFakeBoldText(true);
                break;
            default:
                break;
        }

        switch (VISIBILITY_STATUS) {
            case 0: // 显示
                setVisibility(VISIBLE);
                break;
            case 1: // 不显示，占位置
                setVisibility(INVISIBLE);
                break;
            default:
                setVisibility(GONE);
                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect bgRect = new Rect();
        bgRect.left = maskLeft;
        bgRect.top = 0;
        bgRect.right = width;
        bgRect.bottom = maskBottom;
        canvas.drawBitmap(gloatBgBitmap, 0, 0, null);

        if (cornerMaskImg != -1) {
            canvas.drawBitmap(maskBgBitmap, null, bgRect, null);
        }
        if (!TextUtils.isEmpty(floatTxt)) {
            //悬浮金币图片中间的X轴
            float floatImgX = maskLeft != 0?(maskLeft + dp2px(getContext(), 12)) / 2:width/2;
            if (gravity == -1 || gravity == 6) {

                //计算baseline
                Paint.FontMetrics fontMetrics = floatTxtPaint.getFontMetrics();
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                float baseline = height / 2 + distance;
                canvas.drawText(floatTxt, floatImgX, baseline, floatTxtPaint);

            } else if (gravity == 7) {
                float y = height - dp2px(getContext(), paddingBottom);
                // 底部居中
                canvas.drawText(floatTxt, floatImgX, y, floatTxtPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    // 设置悬浮View中文字内容
    public void setFloatTxt(String txt) {
        floatTxt = txt;
        invalidate();
    }

    // 设置动画浮动的浮动位置
    public void setAnimPosition(float start, float end) {
        if (start > 0) {
            startPosi = start;
        }
        if (end > 0) {
            endPosi = end;
        }
    }

    // 开始浮动
    public void startFloatAnim(long duration) {
        if (duration != 0L) {
            animDuration = duration;
        }
        startFloatAnim();
    }

    //开始动画
    public void startFloatAnim() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        setVisibility(VISIBLE);
        floatViewAnim();
        valueAnimator.start();
    }

    private void floatViewAnim() {
        if (valueAnimator == null) {
            valueAnimator = ObjectAnimator.ofFloat(this, "translationY", startPosi, endPosi);
            valueAnimator.setDuration(animDuration != 0L ? animDuration : 2000);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setInterpolator(new LinearInterpolator());
        }
    }

    // 直接设置悬浮视图是否展示，若不展示，通过参数倒计时处理隐藏时间，若显示，则直接启动动画浮动效果
    public void isShowFloatView(boolean isShow, long countDownTime) {
        setVisibility(isShow?VISIBLE:GONE);
        if (isShow) {
            if (valueAnimator != null) {
                if (!valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
            } else {
                floatViewAnim();
                valueAnimator.start();
            }
        } else {
            if (valueAnimator != null) {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
            }
            setCountDownTimer(countDownTime);
        }
    }

    private void setCountDownTimer(long countDownTime) {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(countDownTime, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    setVisibility(VISIBLE);
                    if (valueAnimator == null) {
                        floatViewAnim();
                    }
                    valueAnimator.start();
                }
            };
        }
        countDownTimer.start();
    }

    public static int dp2px(Context ctx, float dip) {
        return (int) (dip * ctx.getResources().getDisplayMetrics().density);
    }
    // 清除部分对象
    public void releseView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

}
