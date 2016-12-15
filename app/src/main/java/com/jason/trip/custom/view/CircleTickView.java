package com.jason.trip.custom.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.jason.trip.R;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:CircleView
 * Description:
 * Created by Jason on 16/9/9.
 */
public class CircleTickView extends View {
    // 标志用于区分绘制过程
    private int status;
    private static final int STATUS_DRAW_CIRCLE = 0;
    private static final int STATUS_DRAW_TICK = 1;

    // 画圆动画
    private ValueAnimator mCircleAnimation;
    // 画圆的时间周期,默认一秒
    private long mCircleDuration = 1000;
    // 初始化圆圈路径
    private Path mCirclePath;
    private Path mCircleDst;
    private PathMeasure mCirclePathMeasure;
    private float mCircleLength;
    private float mCircleProgress = 0;

    // 打钩动画
    private ValueAnimator mTickAnimation;
    // 打勾的时间周期,默认0.5秒
    private long mTickDuration = 500;
    // 初始化打钩路径
    private Path mTickPath;
    private Path mTickDst;
    private PathMeasure mTickPathMeasure;
    private float mTickLength;
    private float mTickPercent = 0;

    // 圆圈的大小,半径
    private int circleRadius;
    private int circleColor;
    private int circleStrokeWidth;

    // 用于画圆的rec
    private RectF rec;
    // 画笔,目前不做其它处理
    private Paint tickPaint;


    public CircleTickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleTickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTickView(Context context) {
        super(context);
        init(context, null);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTickView);
        circleRadius = mTypedArray.getInteger(R.styleable.CircleTickView_circleRadius, 100);
        circleColor = mTypedArray.getColor(R.styleable.CircleTickView_circleViewColor, ContextCompat.getColor(context, R.color.colorPrimary));
        circleStrokeWidth = mTypedArray.getInteger(R.styleable.CircleTickView_circleStrokeWidth, 10);
        mTypedArray.recycle();

        initPaint();

        mTickPathMeasure = new PathMeasure();
        mTickPath = new Path();
        mTickDst = new Path();
        rec = new RectF();

        mCirclePathMeasure = new PathMeasure();
        mCirclePath = new Path();
        mCircleDst = new Path();


        mCircleAnimation = ValueAnimator.ofFloat(0f, 1f);
        mCircleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleProgress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mCircleAnimation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                status = STATUS_DRAW_CIRCLE;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = STATUS_DRAW_TICK;
                mTickAnimation.start();
            }
        });
        mCircleAnimation.setDuration(mCircleDuration);
        mCircleAnimation.start();

        mTickAnimation = ValueAnimator.ofFloat(0f, 1f);
        mTickAnimation.setDuration(mTickDuration);
        mTickAnimation.setInterpolator(new AccelerateInterpolator());
        mTickAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTickPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 根据设置该view的高度，进行对所画图进行居中处理
        int offsetHeight = (height - circleRadius * 2) / 2;

        // 设置打勾的参数
        int firStartX = width / 2 - circleRadius * 3 / 5;
        int firStartY = offsetHeight + circleRadius;

        int firEndX = (width / 2 - circleRadius / 5) - 1;
        int firEndY = offsetHeight + circleRadius + circleRadius / 2 + 1;


        int secEndX = width / 2 + circleRadius * 3 / 5;
        int secEndY = offsetHeight + circleRadius * 3 / 4;

        rec.set(width / 2 - circleRadius, offsetHeight, width / 2 + circleRadius, offsetHeight + circleRadius * 2);

        // mCirclePath添加圆的路径
        mCirclePath.addCircle(width / 2, height / 2, circleRadius, Path.Direction.CW);
        mCirclePathMeasure.setPath(mCirclePath, true);
        mCircleLength = mCirclePathMeasure.getLength();


        // mTickPath添加自定义打勾路径
        mTickPath.moveTo(firStartX, firStartY);
        mTickPath.lineTo(firEndX, firEndY);
        mTickPath.lineTo(secEndX, secEndY);
        mTickPathMeasure.setPath(mTickPath, false);
        mTickLength = mTickPathMeasure.getLength();

        // 绘制阶段分发
        switch (status) {
            case STATUS_DRAW_CIRCLE:
                drawCircle(canvas);
                break;
            case STATUS_DRAW_TICK:
                drawTick(canvas);
                break;

        }
    }

    private void drawCircle(Canvas canvas) {

        mCircleDst.reset();
        mCircleDst.lineTo(0, 0);
        float stop = mCircleLength * mCircleProgress;
        mCirclePathMeasure.getSegment(0, stop, mCircleDst, true);
        canvas.drawPath(mCircleDst, tickPaint);

    }


    private void drawTick(Canvas canvas) {
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */

        mTickPathMeasure.getSegment(0, mTickPercent * mTickLength, mTickDst, true);
        mTickDst.rLineTo(0, 0);
        canvas.drawPath(mTickDst, tickPaint);
        canvas.drawArc(rec, 0, 360, false, tickPaint);
    }

    private void initPaint() {

        tickPaint = new Paint();
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setAntiAlias(true);
        tickPaint.setColor(circleColor);
        tickPaint.setStrokeWidth(circleStrokeWidth);
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getCircleStrokeWidth() {
        return circleStrokeWidth;
    }

    public void setCircleStrokeWidth(int circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
    }

    public long getmCircleDuration() {
        return mCircleDuration;
    }

    public void setmCircleDuration(long mCircleDuration) {
        this.mCircleDuration = mCircleDuration;
    }

    public long getmTickDuration() {
        return mTickDuration;
    }

    public void setmTickDuration(long mTickDuration) {
        this.mTickDuration = mTickDuration;
    }
}

