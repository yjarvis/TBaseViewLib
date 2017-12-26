package com.jarvis.tbaseviewlib.view.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.jarvis.tbaseviewlib.view.draglayout.DragLayout.Status;


/**
 * 侧滑布局
 * @author tansheng  QQ:717549357
 * @date 2015-11-30 上午9:42:54
 */
public class TSlidingViewLayout extends RelativeLayout {
    private DragLayout dl;

    public TSlidingViewLayout(Context context) {
        super(context);
    }

    public TSlidingViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TSlidingViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDragLayout(DragLayout dl) {
        this.dl = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dl.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
