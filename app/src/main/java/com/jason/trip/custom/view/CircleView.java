package com.jason.trip.custom.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jason.trip.R;
/**
 * Version V1.0 <Trip客户端>
 * ClassName:CircleView
 * Description: 动态进行成功界面的打钩
 * Created by Jason on 16/9/12.
 */
public class CircleView extends View {

    // 总体绘制速度,越小速度越快
    private static final int drawSpeed = 30;
    // 画圆圈的速度
    private static final int circleSpeed = 20;
    // 第一条直线划分成几部分绘制,越小绘制速度越快
    private static final int divFirLine = 2;
    // 第二条直线划分成几部分绘制,越小绘制速度越快
    private static final int divSecLine = 4;
    // 圆圈画完后画第一条直线和第二条直线的延迟
    private static final int firDelay = 2;
    private static final int secDelay = 6;

    // 圆圈的大小,半径
    private int circleRadius;
    private int circleColor;
    private int circleStrokeWidth;

    private Paint paint;
    private RectF rec;

    private int changeCircle = 0;
    private int changeLine = 0;

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleView(Context context) {
        super(context);
        init(context, null);
    }

    public void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        rec = new RectF();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTickView);
        circleRadius = mTypedArray.getInteger(R.styleable.CircleTickView_circleRadius, 130);
        circleColor = mTypedArray.getColor(R.styleable.CircleTickView_circleViewColor, ContextCompat.getColor(context,R.color.colorPrimary));
        circleStrokeWidth=mTypedArray.getInteger(R.styleable.CircleTickView_circleStrokeWidth,10);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 根据设置该view的高度，进行对所画图进行居中处理
        int offsetHeight = (height - circleRadius * 2) / 2;

        // 设置第一条直线的相关参数
        int firStartX = width / 2 - circleRadius * 3 / 5;
        int firStartY = offsetHeight + circleRadius;
        int firEndX = (width / 2 - circleRadius / 5) - 1;
        int firEndY = offsetHeight + circleRadius + circleRadius / 2 + 1;
        float firLineX = firEndX - firStartX;
        float firLineY = firEndY - firStartY;
        float firLineOffsetX = firLineX / divFirLine;
        float firLineOffsetY = firLineY / divFirLine;

        // 设置第二条直线的相关参数
        int secStartX = firEndX - 1;
        int secStartY = firEndY - 1;
        int secEndX = width / 2 + circleRadius * 3 / 5;
        int secEndY = offsetHeight + circleRadius / 2;
        float secLineX = secEndX - secStartX;
        float secLineY = secStartY - secEndY;
        float secLineOffsetX = secLineX / divSecLine;
        float secLineOffsetY = secLineY / divSecLine;

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleStrokeWidth);
        rec.set(width / 2 - circleRadius, offsetHeight, width / 2 + circleRadius, offsetHeight + circleRadius * 2);

        if (changeCircle <= 360) {
            changeCircle = changeCircle + circleSpeed;
            canvas.drawArc(rec, 0, changeCircle, false, paint);
        } else {
//            changeLine = changeLine + 1;
//            if (changeLine >= firDelay) {
//                int tempDiv1 = changeLine - firDelay;
//                if (tempDiv1 <= divFirLine) {
//                    canvas.drawLine(firStartX, firStartY, firStartX + firLineOffsetX * tempDiv1, firStartY + firLineOffsetY * tempDiv1, paint);
//                } else {
//                    canvas.drawLine(firStartX, firStartY, firEndX, firEndY, paint);
//                }
//
//            }
//            if (changeLine >= secDelay) {
//                int tempDiv2 = changeLine - secDelay;
//                if (tempDiv2 <= divSecLine) {
//                    canvas.drawLine(secStartX, secStartY, secStartX + secLineOffsetX * tempDiv2, secStartY - secLineOffsetY * tempDiv2, paint);
//                } else {
//                    canvas.drawLine(secStartX, secStartY, secEndX, secEndY, paint);
//                }
//            }

        }
        canvas.drawArc(rec, 0, changeCircle, false, paint);
        postInvalidateDelayed(drawSpeed);
    }
}
