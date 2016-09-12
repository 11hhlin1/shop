package com.gjj.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import cn.finalteam.loadingviewfinal.GridViewFinal;

public class UnScrollableGridView extends GridView {

    public UnScrollableGridView(Context context) {
        super(context);
    }

    public UnScrollableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnScrollableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            return true;//forbid its child(gridview) to scroll
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
