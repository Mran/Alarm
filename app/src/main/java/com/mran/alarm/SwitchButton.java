package com.mran.alarm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

/**
 * Created by mran on 18-1-12.
 */

public class SwitchButton extends View {
    private int cirX;
    private int cirRadius;
    private Paint mCirPaint;

    public SwitchButton(Context context) {
        super(context);
        init(context, null);

    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    Paint mPaint;
    Paint mBgPaint;

    Context mContext;
    AttributeSet mAttributeSet;

    void init(Context context, AttributeSet attributeSet) {
//        isHardwareAccelerated();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mContext = context;
        mAttributeSet = attributeSet;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffaa1223);
        mPaint.setTextSize(50);
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(0xffddeeaa);
        mBgPaint.setTextSize(50);
        mCirPaint = new Paint();
        mCirPaint.setAntiAlias(true);
        mCirPaint.setColor(0xffffffff);

        mCirPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        mL2RAnimation = new ValueAnimator();
        mR2LAnimation = new ValueAnimator();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(5, 5, width, height, height / 2, height / 2, mBgPaint);
        mCirPaint.setShadowLayer(10, 1, 2, 0xff000000);
        mCirPaint.setStrokeWidth(0.2f);
        mCirPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cirX, height / 2, cirRadius-1, mCirPaint);

        canvas.drawCircle(cirX, height / 2, cirRadius, mCirPaint);
        canvas.saveLayer(0, 0, width, height, null);
        mPaint.setColor(0xffffffff);
        canvas.drawText("AM", cirRadius / 4.5f, height / 3 * 2, mPaint);
        canvas.drawText("FM", width - rightW - cirRadius / 2.5f, height / 3 * 2, mPaint);
        mCirPaint.clearShadowLayer();
        mCirPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(cirX, height / 2, cirRadius, mCirPaint);
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int width;
    int height;
    ValueAnimator mL2RAnimation;
    ValueAnimator mR2LAnimation;
    int rightW;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w - 10;
        height = h - 10;
        cirX = height / 2;
        mPaint.setTextSize(height / 2);
        Rect rectF = new Rect();

        mPaint.getTextBounds("AM", 0, 2, rectF);
        int leftX = rectF.centerX();
        mPaint.getTextBounds("FM", 0, 2, rectF);
        rightW = rectF.width();
        cirRadius = height / 2;
        mL2RAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mL2RAnimation.setIntValues(cirRadius, width - cirRadius);
        mL2RAnimation.setDuration(300);
        mL2RAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cirX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mR2LAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mR2LAnimation.setIntValues(width - cirRadius, cirRadius);
        mR2LAnimation.setDuration(300);
        mR2LAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cirX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    public static final boolean AM = true;
    public static final boolean PM = false;
    Boolean AMORPM = AM;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                AMORPM = !AMORPM;
                if (AMORPM == AM) {
                    mR2LAnimation.start();
                } else
                    mL2RAnimation.start();
                break;
        }
        return super.onTouchEvent(event);
    }
}
