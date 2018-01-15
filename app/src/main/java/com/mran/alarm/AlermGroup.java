package com.mran.alarm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mran on 18-1-7.
 */

public class AlermGroup extends ConstraintLayout {
    private Paint mPaintBigCirle;
    private float smallCircle;
    private float bigCircle;
    private Path mBigPath;
    private Path mSmallPath;
    private Paint mTextPaint;
    private Paint mbgPaint;

    private float hourDegress;
    private float minuteDegress;
    private float lastHourDegrss;
    private float lastMinuteDegrss;
    ValueAnimator mBgValueAnimator;
    ValueAnimator mRatateValueAnimator;
    private int bgRadius;

    public AlermGroup(Context context) {
        super(context);
        init(context, null);

    }

    public AlermGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public AlermGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

//    public AlermGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, attrs);
//    }

    Context mContext;
    AttributeSet mAttributeSet;
    Paint mPaintSamllCircle;
    Region mBigRegion;
    Region mSmallRegion;

    void init(Context context, AttributeSet attributeSet) {

        mContext = context;
        mAttributeSet = attributeSet;
        setWillNotDraw(false);
        mPaintSamllCircle = new Paint();
        mPaintSamllCircle.setAntiAlias(true);
        mPaintSamllCircle.setColor(0xffd57458);
        mPaintBigCirle = new Paint();
        mPaintBigCirle.setAntiAlias(true);
        mPaintBigCirle.setColor(0xffb5614c);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(50);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mbgPaint = new Paint();
        mbgPaint.setColor(0x662f6df5);
        mbgPaint.setAntiAlias(true);
        mBigPath = new Path();
        mSmallPath = new Path();
        mBigRegion = new Region();
        mSmallRegion = new Region();
        mBgValueAnimator = new ValueAnimator();
        mRatateValueAnimator=new ValueAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Log.d("AlermGroup", "onDraw: " + hourDegress);


        //画出底层大圆
        canvas.save();
        canvas.drawCircle(cirX, cirY, bigCircle, mPaintBigCirle);
        canvas.restore();
        canvas.save();
        //画出上层小圆
        canvas.drawCircle(cirX, cirY, smallCircle, mPaintSamllCircle);
        canvas.restore();
//        for (KikItem k : mHourKikItems) {
//            canvas.drawPath(k.mRegion.getBoundaryPath(), mPaintBigCirle);
//        }
//        for (KikItem k : mMinuteKikItems) {
//            canvas.drawPath(k.mRegion.getBoundaryPath(), mPaintSamllCircle);
//        }
        canvas.save();
        canvas.drawCircle(width / 2, height / 2, bgRadius, mbgPaint);
        canvas.restore();
        //画出小时数字
        canvas.save();
        canvas.rotate(hourDegress, cirX, cirY);
        for (int i = 0; i < 12; i++) {
            canvas.drawText(mHourKikItems.get(i).show, cirX, cirY + smallCircle - 30, mTextPaint);
            canvas.rotate(30, cirX, cirY);
        }
        canvas.restore();

        //画出分钟数字
        canvas.save();
        canvas.rotate(minuteDegress, cirX, cirY);
        for (int i = 0; i < 12; i++) {
            canvas.drawText(mMinuteKikItems.get(i).show, cirX, cirY + bigCircle - 20, mTextPaint);
            canvas.rotate(30, cirX, cirY);
        }
        canvas.restore();
        canvas.save();


    }

    String[] hours = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] minuts = new String[]{"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "0"};
    List<KikItem> mHourKikItems = new ArrayList<>();
    List<KikItem> mMinuteKikItems = new ArrayList<>();


    private class KikItem {
        String show;
        int value;
        Region mRegion;
    }

    int width;
    int height;
    float lastX;
    float lastY;
    int mRoateWho;//判断对哪个进行旋转
    final static int SMALL = 1;
    final static int BIG = 2;
    float lastDegress;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                lastDegress = (float) (getRadian(x, y) * 57.34);
                if (mSmallRegion.contains((int) x, (int) y)) {
                    for (KikItem k : mHourKikItems) {
                        if (k.mRegion.contains((int) x, (int) y)) {
                            Log.d("AlermGroup", "onTouchEvent:mHourKikItems " + k.value);
                        }
                    }
                    mRoateWho = SMALL;
                } else if (mBigRegion.contains((int) x, (int) y)) {
                    mRoateWho = BIG;
                    for (KikItem k : mMinuteKikItems) {
                        if (k.mRegion.contains((int) x, (int) y)) {
                            Log.d("AlermGroup", "onTouchEvent: mMinuteKikItems" + k.value);
                        }
                    }
                }
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d("AlermGroup", "onTouchEvent: radius" + getRadian(x, y));
                if (mRoateWho == SMALL) {
                    hourDegress = lastHourDegrss + (float) (getRadian(x, y) * 57.34) - lastDegress;
                } else if (mRoateWho == BIG) {
                    minuteDegress = lastMinuteDegrss + (float) (getRadian(x, y) * 57.34) - lastDegress;
                }
                invalidate();
                return true;
//                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mRoateWho == SMALL) {
                    lastHourDegrss = hourDegress;
                    for (int i = 0; i < 12; i++) {
                        KikItem k = mHourKikItems.get(i);
                        Matrix matrix = new Matrix();
                        matrix.preRotate(30 * i + lastHourDegrss, cirX, cirY);
                        Path path = new Path();
                        path.addCircle(cirX, cirY + smallCircle - 60, 60, Path.Direction.CCW);
                        path.transform(matrix);
                        k.mRegion.setPath(path, mSmallRegion);
                    }
                } else if (mRoateWho == BIG) {
                    lastMinuteDegrss = minuteDegress;
                    for (int i = 0; i < 12; i++) {
                        KikItem k = mMinuteKikItems.get(i);
                        Matrix matrix = new Matrix();
                        matrix.preRotate(30 * i + lastMinuteDegrss, cirX, cirY);
                        Path path = new Path();
                        path.addCircle(cirX, cirY + bigCircle - 60, 60, Path.Direction.CCW);
                        path.transform(matrix);
                        k.mRegion.setPath(path, mBigRegion);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    int cirX;//圆的x坐标
    int cirY;//圆的y坐标


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        smallCircle = (width * 0.9f) / 2;
        bigCircle = (width * 1.15f) / 2;
        mBigPath.addCircle(cirX, cirY, bigCircle, Path.Direction.CW);
        mSmallPath.addCircle(cirX, cirY, smallCircle, Path.Direction.CCW);
        cirY = 300;
        cirX = width / 2;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Region region = new Region();
        region.set(-w, -h, w, (int) (bigCircle * 2));
        Path smallPath = new Path();
        smallPath.addCircle(cirX, cirY, smallCircle, Path.Direction.CW);
        mSmallRegion.setPath(smallPath, region);
        Path bigPath = new Path();
        bigPath.addCircle(cirX, cirY, bigCircle, Path.Direction.CW);
        mBigRegion.setPath(bigPath, region);

        //给小圆设置触摸范围
        for (int i = 0; i < 12; i++) {
            KikItem kikItem = new KikItem();
            kikItem.value = i;
            kikItem.show = String.valueOf(i);
            kikItem.mRegion = new Region();
            Path path = new Path();
            path.addCircle(cirX, cirY + smallCircle - 60, 60, Path.Direction.CCW);
            Matrix matrix = new Matrix();
            matrix.postRotate(30 * i, cirX, cirY);
            path.transform(matrix);
            kikItem.mRegion.setPath(path, mSmallRegion);
            mHourKikItems.add(kikItem);
        }
        //给小圆设置触摸范围
        for (int i = 0; i < 12; i++) {
            KikItem kikItem = new KikItem();
            kikItem.value = i * 5;
            kikItem.show = String.valueOf(i * 5);
            kikItem.mRegion = new Region();
            Path path = new Path();
            path.addCircle(cirX, cirY + bigCircle - 60, 60, Path.Direction.CCW);
            Matrix matrix = new Matrix();
            matrix.postRotate(30 * i, cirX, cirY);
            path.transform(matrix);
            kikItem.mRegion.setPath(path, mSmallRegion);
            mMinuteKikItems.add(kikItem);
        }
        mBgValueAnimator.setIntValues(50, height);
        mBgValueAnimator.setDuration(500);
        mBgValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mBgValueAnimator.setStartDelay(0);
        mBgValueAnimator.start();

        mRatateValueAnimator.setIntValues(90, 0);
        mRatateValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mRatateValueAnimator.setDuration(500);
        mRatateValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int startDegress = (int) animation.getAnimatedValue();
                hourDegress = startDegress;
                minuteDegress = startDegress;
                invalidate();
            }
        });
//        mRatateValueAnimator.setStartDelay(100);
        mRatateValueAnimator.start();
    }

    private double getRadian(float x, float y) {
        float disX = x - cirX;
        float disY = cirY - y;
        double radians = 0;
        if (disX > 0 && disY > 0) {
            // 第一象限
            radians = Math.atan(disX / disY);
        } else if (disX > 0 && disY < 0) {
            // 第二象限
            radians = Math.atan(disY / -disX);
            radians += Math.PI / 2;
        } else if (disX < 0 && disY <= 0) {
            // 第三象限
            radians = Math.atan(-disX / -disY);
            radians += Math.PI;
        } else if (disX < 0 && disY >= 0) {
            // 第四象限
            radians = Math.atan(disY / -disX);
            radians += Math.PI * 3 / 2;
            // 以下是点击的坐标点在以圆心为坐标原点的坐标轴上的情况
        } else if (disX == 0 && disY > 0) {
            // 在Y正轴上
            radians = 0;
        } else if (disX == 0 && disY < 0) {
            // 在Y负轴上
            radians = Math.PI;
        } else if (disX > 0 && disY == 0) {
            // 在X正轴上
            radians = Math.PI / 2;
        } else if (disX < 0 && disY == 0) {
            // 在X负轴上
            radians = Math.PI * 2 / 3;
        }
        return radians;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, r, t, b);
    }
}
