package com.yjs.threaddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yangjingsong on 17/2/23.
 */

public class TemperatureView extends View {
    Paint circlePaint;
    Paint barPaint;
    TextPaint centerTextPaint;
    TextPaint sideTextPaint;
    int perDegree;
    float currentDegree;
    int currentTemp;
    Paint tempCirclePaint;
    Paint centerCirclePaint;

    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setStrokeWidth(dip2px(4));
        circlePaint.setColor(Color.parseColor("#A4B5E3"));
        centerTextPaint = new TextPaint();
        centerTextPaint.setStyle(Paint.Style.STROKE);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setDither(true);
        centerTextPaint.setTextSize(50);
        centerTextPaint.setColor(Color.parseColor("#A4B5E3"));

        perDegree = 180 / 12;

        tempCirclePaint = new Paint();
        tempCirclePaint.setColor(Color.parseColor("#F58B35"));
        tempCirclePaint.setAntiAlias(true);
        tempCirclePaint.setStyle(Paint.Style.STROKE);
        tempCirclePaint.setStrokeWidth(dip2px(2));

        centerCirclePaint = new Paint();
        LinearGradient lg = new LinearGradient(
                -dip2px(90), -dip2px(90), dip2px(90), dip2px(90), Color.parseColor("#F58B35"), Color.parseColor("#F8A969"), Shader.TileMode.MIRROR);

        centerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        centerCirclePaint.setAntiAlias(true);
        centerCirclePaint.setShader(lg);
        centerCirclePaint.setStrokeWidth(dip2px(4));

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setColor(Color.parseColor("#F58B35"));
        barPaint.setAntiAlias(true);

        sideTextPaint = new TextPaint();
        sideTextPaint.setAntiAlias(true);
        sideTextPaint.setTextSize(dip2px(14));
        sideTextPaint.setColor(Color.parseColor("#F58B35"));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) getResources().getDimension(R.dimen.temp_width);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) getResources().getDimension(R.dimen.temp_height);
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth() / 2;
        int y = getMeasuredHeight() / 2;
        canvas.translate(x, y);
        //画中心圆
        canvas.drawCircle(0, 0, dip2px(90), centerCirclePaint);
        //画上边的竖线
        canvas.drawLine(0, -dip2px(92), 0, -dip2px(102), tempCirclePaint);
        String maxTemp = "29℃";
        Rect rect = new Rect();

        sideTextPaint.getTextBounds(maxTemp, 0, maxTemp.length(), rect);


        canvas.drawText(maxTemp, -rect.width() / 2, -dip2px(107), sideTextPaint);

        //画下边竖线
        circlePaint.setColor(Color.parseColor("#5B77C5"));
        canvas.drawLine(0, dip2px(92), 0, dip2px(103), tempCirclePaint);

        String minTemp = "17℃";
        sideTextPaint.getTextBounds(minTemp, 0, minTemp.length(), rect);
        canvas.drawText(minTemp, -rect.width() / 2, dip2px(107) + rect.height(), sideTextPaint);

        RectF rectF = new RectF(-dip2px(102), -dip2px(102), dip2px(102), dip2px(102));
        canvas.save();
        canvas.rotate(90, 0, 0);
        //画温度线
        canvas.drawArc(rectF, 0, currentDegree, false, tempCirclePaint);

        canvas.restore();

        canvas.save();

        canvas.rotate((float) (180 + currentDegree), 0, 0);

        //画把手
        canvas.drawLine(0, -dip2px(101), 0, -dip2px(120), tempCirclePaint);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, -dip2px(120), dip2px(4), barPaint);
        canvas.restore();

        String currentTempStr = currentTemp + "℃";
        centerTextPaint.setTextSize(dip2px(30));
        centerTextPaint.getTextBounds(currentTempStr, 0, currentTempStr.length(), rect);
        //写中心温度
        centerTextPaint.setColor(Color.WHITE);
        canvas.drawText(currentTempStr, -rect.width() / 2, rect.height() / 2, centerTextPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("touchXY", event.getX() - getMeasuredWidth() / 2 + "");
        Log.d("touchXY", event.getY() - getMeasuredHeight() / 2 + "");
        float realX = event.getX() - getMeasuredWidth() / 2;
        float realY = event.getY() - getMeasuredHeight() / 2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d("isTouchOnThumb", isTouchOnThumb(realX, realY) + "");
                seekTo(realX, realY, false);
                break;
            case MotionEvent.ACTION_MOVE:
                seekTo(realX, realY, false);
                break;
        }
        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp) {
        if (isTouchOnThumb(eventX, eventY)) {


            double radian = Math.atan2(eventY, eventX);

            Log.d("radian", Math.toDegrees(radian) + "");
            double cDegree = Math.toDegrees(radian);
            double degree = 0;
            if (cDegree > 0) {
                degree = cDegree - 90;
            } else {
                degree = 360 + cDegree - 90;
            }

            int temp = (int) Math.round(degree / (180 / 12));
            setCurrentTemp(temp + 17);
            Log.d("temp", temp + "");
        }
    }


    private boolean isTouchOnThumb(float eventX, float eventY) {
        boolean result = true;
//        if (eventX > 0) {
//            result = false;
//        } else {
//            result = true;
//        }
//        else {
//            double distance = Math.sqrt(Math.pow(eventX, 2) + Math.pow(eventY, 2));
//            Log.d("distance", distance + "");
//            if (distance >= dip2px(0) && distance <= dip2px(130)) {
//                result = true;
//            } else {
//                result = false;
//            }
//        }
        return result;
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setCurrentTemp(int temp) {
        if (temp > 29 || temp < 17) {
            new RuntimeException("温度不合法");
            return;
        }
        currentTemp = temp;
        currentDegree = (temp - 17) * perDegree;
        invalidate();

    }

    public int getCurrentTemp(){
        return currentTemp;
    }

}
