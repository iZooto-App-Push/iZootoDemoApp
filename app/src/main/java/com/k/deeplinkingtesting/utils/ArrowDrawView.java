package com.k.deeplinkingtesting.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;



public class ArrowDrawView extends View {
    private static int ARROW_SIDE_HEIGHT = 30;
    private static int ARROW_SIDE_LENGTH = 70;
    static final float MAX_OPEN_PERCENTAGE = 0.58f;
    private static final float SWITCH_FACTOR_VALUE = 0.65f;
    private static int X_OFFSET = 0;

    Paint paint = new Paint();
    private float openPercentage;

    public void setOpenPercentage(float openPercentage) {
        this.openPercentage = openPercentage;
    }

    public ArrowDrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        ARROW_SIDE_HEIGHT = MeasurementTool.dpToPx(15, context);
        ARROW_SIDE_LENGTH = MeasurementTool.dpToPx(30,context);
        X_OFFSET = MeasurementTool.dpToPx(5,context);

        paint.setColor(Color.parseColor("#999999"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MeasurementTool.dpToPx(6, context));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = X_OFFSET;
        float startY = ARROW_SIDE_HEIGHT;

        float targetX = (float) ARROW_SIDE_LENGTH;

        float adjFactor = Math.max(openPercentage, MAX_OPEN_PERCENTAGE);

        if (openPercentage > SWITCH_FACTOR_VALUE) {
            adjFactor = 1 + openPercentage - SWITCH_FACTOR_VALUE;
        }

        float targetY = ARROW_SIDE_HEIGHT * adjFactor;

        canvas.drawLine(startX, startY, targetX, targetY, paint);
        canvas.drawLine(targetX, targetY, ARROW_SIDE_LENGTH * 2, ARROW_SIDE_HEIGHT, paint);
    }
}