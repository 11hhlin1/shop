package com.gjj.shop.person;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;


public class ListViewCompat extends ListView {

    private static final String TAG = "ListViewCompat";
    private int mTouchSlop;

    private SlideView mFocusedItemView;
    
    private int mLastX;
    
    private int mLastY;
    boolean isDispatch = false;
    boolean isReset = false;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup vg = (ViewGroup) getParent();
        boolean isUp = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                float diffX = Math.abs(x - mLastX);
                float diffY = Math.abs(y - mLastY);
                if(diffX > diffY  && diffX > mTouchSlop && x < mLastX) {
                    int position = pointToPosition(mLastX, mLastY);
                    Log.e(TAG, "postion=" + position);
                    if (position != INVALID_POSITION) {
                        CollectInfo data = (CollectInfo) getItemAtPosition(position);
                        mFocusedItemView = data.slideView;
                        Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
                    }
                    isDispatch = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                if(mFocusedItemView != null) {
                    if(mFocusedItemView.getmState() != SlideView.OnSlideListener.SLIDE_STATUS_OFF && isDispatch) {
                        mFocusedItemView.shrink();
                        isDispatch = false;
                        isReset = true;
                        Log.d(TAG, "onTouchEvent isReset true");
                    } else {
                        isReset = false;
                        Log.d(TAG, "onTouchEvent isReset false");
                    }
                }
                break;
            default:
                break;
        }

        if(mFocusedItemView != null && isDispatch) {
            isUp = mFocusedItemView.onRequireTouchEvent(event);
            if(isUp && mFocusedItemView.getmState() == SlideView.OnSlideListener.SLIDE_STATUS_OFF) {
                isDispatch = false;
                Log.d(TAG, "onTouchEvent isDispatch false");
            }
        }
        if(isDispatch) {
            vg.requestDisallowInterceptTouchEvent(true);
            Log.d(TAG, "onTouchEvent requestDisallowInterceptTouchEvent true");
        } else {
            vg.requestDisallowInterceptTouchEvent(false);
            Log.d(TAG, "onTouchEvent requestDisallowInterceptTouchEvent false");
        }

        boolean result = isReset || isDispatch;
        if(result) {
            setEnabled(false);
            if(isReset) {
                isReset = false;
            }
        } else {
               if(mFocusedItemView != null && mFocusedItemView.getmState() == SlideView.OnSlideListener.SLIDE_STATUS_OFF && isUp) {
                    return true;
                }
                Log.d(TAG, "onTouchEvent  event>>" + event.getAction());
                setEnabled(true);
                return super.onTouchEvent(event);
        }
        return true;
    }



}
