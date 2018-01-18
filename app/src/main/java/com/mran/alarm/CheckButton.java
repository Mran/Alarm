package com.mran.alarm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by mran on 18-1-16.
 */

public class CheckButton extends View {
    public CheckButton(Context context) {
        super(context);
        init(context, null);

    }

    public CheckButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CheckButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public CheckButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    Context mContext;
    AttributeSet mAttributeSet;
    Paint mBgPaint;
    Paint mTextPaint;
    Paint mFrPaint;

    void init(Context context, AttributeSet attributeSet) {

        mContext = context;
        mAttributeSet = attributeSet;
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(0xff2a3486);
        mValueAnimator = new ValueAnimator();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xffffffff);
        mFrPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrPaint.setColor(0xffffffff);
        mFrPaint.setShadowLayer(5, 1, 2, 0xff000000);

    }

    int textW;
int alpha;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cirX, cirY, width / 2, mBgPaint);
        canvas.save();

        canvas.scale(radius, radius, width / 2, height / 2);
        mFrPaint.setAlpha(alpha);
        if (isChecked) {
            mTextPaint.setColor(0xff2a3486);
            canvas.drawCircle(cirX, cirY, width / 2, mFrPaint);
        } else {
            mTextPaint.setColor(0xffffffff);
        }
        canvas.drawText("SUN", cirX - textW / 2, cirX+textH/2, mTextPaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float width;
    float height;
    int cirX = 0;
    int cirY = 0;
    float radius = 1;
    ValueAnimator mValueAnimator;
    boolean isChecked = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isChecked = !isChecked;
                mValueAnimator.start();
                break;
        }
        return super.onTouchEvent(event);
    }

    int textH;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w * 0.7f;
        height = w * 0.7f;
        cirX = w / 2;
        cirY = w / 2;
        mTextPaint.setTextSize(width * 0.35f);
        Rect rect = new Rect();

        mTextPaint.getTextBounds("SUN", 0, 3, rect);
        textW = rect.width();
        textH = rect.height();
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(100);
        mValueAnimator.setFloatValues(1.3f, 1.0f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (float) animation.getAnimatedValue();
                float a=radius*(-0.375f)+1.375f;
                alpha= (int) (255*a);
                Log.d("CheckButton", "onAnimationUpdate: alpha"+alpha);
                invalidate();
            }
        });

    }
}
