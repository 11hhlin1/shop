package com.gjj.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by user on 16/9/4.
 */
public class UnScrollableListView extends ListView{
    public UnScrollableListView(Context context) {
        super(context);
    }

    public UnScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnScrollableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
