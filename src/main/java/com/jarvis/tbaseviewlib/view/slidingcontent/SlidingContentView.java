package com.jarvis.tbaseviewlib.view.slidingcontent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 横向滑动展示功能控件(支持：Listview，GridView)
 * Created by Administrator on 2016/8/1.
 */
public class SlidingContentView extends FrameLayout {
    private Context context;
    /**
     * 内容视图
     */
    private View contentView;
    /**
     * 功能视图
     */
    private View functionView;
    /**
     * 滑动模式，默认向左滑动
     */
    private TypeStatus typeStatus = TypeStatus.LEFT;
    /**
     * 滑动状态，默认关闭
     */
    private ViewStaus viewStaus = ViewStaus.CLOSE;
    /**
     * 按下的x坐标
     */
    private float downX;
    /**
     * 滑动距离
     */
    private float distanceX;

    public SlidingContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingContentView(Context context) {
        super(context);
    }

    public SlidingContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 横向滑动的模式
     */
    public enum TypeStatus {
        RIGHT, LEFT
    }

    /**
     * 当前控件的状态
     */
    public enum ViewStaus {
        CLOSE, SLIDE, OPEN
    }

    /**
     * 完成初始化后会调用此方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        contentView = getChildAt(0);
        functionView = getChildAt(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                distanceX = Math.abs(downX - event.getX()) / 1.5f;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //重绘子控件位置
        requestLayout();
        return true;
    }

    /**
     * 测量控件
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 绘制子控件位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Rect contentRect = setContentViewLayout();
        Rect functionRect = setFunctionViewLayout();
        //绘制内容视图位置
        contentView.layout(contentRect.left, contentRect.top, contentRect.right, contentRect.bottom);
        //绘制功能视图位置
        functionView.layout(functionRect.left, functionRect.top, functionRect.right, functionRect.bottom);
    }

    /**
     * 设置内容视图的位置
     */
    private Rect setContentViewLayout() {
        int left = 0;
        int top = 0;
        int right = left + getMeasuredWidth();
        int bottom = top + getMeasuredHeight();

        if (typeStatus == TypeStatus.LEFT) {
            if (viewStaus == ViewStaus.CLOSE || viewStaus == ViewStaus.SLIDE) {
                left -= distanceX;
                right -= distanceX;
            } else if(viewStaus==ViewStaus.OPEN){
                left = 0;
                right = 0;
            }
        } else {

        }

        //设置默认状态
        return new Rect(left, top, right, bottom);
    }

    /**
     * 设置功能视图的位置
     */
    private Rect setFunctionViewLayout() {
        int left = contentView.getMeasuredWidth();
        int top = 0;
        int right = contentView.getMeasuredWidth() + functionView.getMeasuredWidth();
        int bottom = top + getMeasuredHeight();
        if (typeStatus == TypeStatus.LEFT) {
            if (viewStaus == ViewStaus.CLOSE || viewStaus == ViewStaus.SLIDE) {
                left -= distanceX;
                right -= distanceX;
            } else if(viewStaus==ViewStaus.OPEN){
                left = contentView.getMeasuredWidth();
                right = contentView.getMeasuredWidth() + functionView.getMeasuredWidth();
            }
        }

        return new Rect(left, top, right, bottom);
    }
}
