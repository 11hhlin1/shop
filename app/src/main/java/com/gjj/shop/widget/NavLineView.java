package com.gjj.shop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.gjj.shop.R;


/**
 * Created by Kop on 2015/6/19.
 */
public class NavLineView extends View {

    private Paint mPaint;
    private RectF mRectF;

    public NavLineView(Context context) {
        this(context, null);
    }

    public NavLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.orange));
        mRectF = new RectF();
        mRectF.top = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mRectF.bottom = h;
    }

    public void setNavLeft(float left) {
        mRectF.left = left;
    }

    public void setNavRight(float right) {
        mRectF.right = right;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mRectF, mPaint);//.drawLine(mStart, 0, mEnd, 0, mPaint);
    }
}
