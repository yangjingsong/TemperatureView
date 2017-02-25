package com.yjs.threaddemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by yangjingsong on 17/2/22.
 */

public class PasswordEditText extends EditText {

    Paint paint;
    int gap;

    int textLength;
    int defaultLineColor;
    int filledLineColor;
    int lineHeight;
    Bitmap star;

    BitmapDrawable passwordDrawable;

    private OnPasswordFilledListener onPasswordFilledListener;



    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.PasswordEditText);
        defaultLineColor = typedArray.getColor(R.styleable.PasswordEditText_default_line_color,Color.BLACK);
        filledLineColor = typedArray.getColor(R.styleable.PasswordEditText_filled_line_color,context.getResources().getColor(R.color.colorAccent));
        lineHeight = (int) typedArray.getDimension(R.styleable.PasswordEditText_line_height,getResources().getDimension(R.dimen.line_height));
        passwordDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.PasswordEditText_password_drawable);
        if(passwordDrawable == null){
            star = BitmapFactory.decodeResource(getResources(), R.mipmap.star);
        }else {
            star = passwordDrawable.getBitmap();
        }
        init();
    }


    private void init() {

        gap = (int) getContext().getResources().getDimension(R.dimen.default_gap);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(defaultLineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineHeight);

        setBackgroundColor(Color.WHITE);
        setCursorVisible(false);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) getContext().getResources().getDimension(R.dimen.default_width);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) getContext().getResources().getDimension(R.dimen.default_height);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int allWidth = getMeasuredWidth();
        int singleWidth = (allWidth - gap * 5) / 6;
        int startX, stopX;
        int y = getMeasuredHeight() - 10;
        //画下划线
        for (int i = 0; i < 6; i++) {
            if (i < textLength) {
                paint.setColor(filledLineColor);
            } else {
                paint.setColor(defaultLineColor);
            }
            startX = (singleWidth + gap) * i;
            stopX = (singleWidth + gap) * i + singleWidth;
            canvas.drawLine(startX, y, stopX, y, paint);
        }
        //画密码


        int bitmapWidth = star.getWidth();
        int bitmapLeft = (singleWidth - bitmapWidth) / 2;
        int bitmapHeight = (getMeasuredHeight()-star.getHeight())/2;
        for (int j = 0; j < textLength; j++) {
            canvas.drawBitmap(star, bitmapLeft + j * (singleWidth + gap), bitmapHeight, null);
        }


    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        textLength = text.length();
        invalidate();
        if(textLength==6){
            onPasswordFilledListener.onPasswordFilled(text.toString());
        }
    }

    public interface OnPasswordFilledListener{
        void onPasswordFilled(String password);
    }

    public void setOnPasswordFilledListener(OnPasswordFilledListener onPasswordFilledListener) {
        this.onPasswordFilledListener = onPasswordFilledListener;
    }

    public void clearPassword(){
        setText("");
    }
}
