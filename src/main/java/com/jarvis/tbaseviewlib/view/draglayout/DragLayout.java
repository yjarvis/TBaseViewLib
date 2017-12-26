package com.jarvis.tbaseviewlib.view.draglayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jarvis.tbaseviewlib.R;
import com.nineoldandroids.view.ViewHelper;



public class DragLayout extends FrameLayout {

	
	private Context context;
	private GestureDetectorCompat gestureDetector;
	private ViewDragHelper dragHelper;
	/**滑动时的监听*/
	private DragListener dragListener;
	
	/** 是否显示背景变暗的资源图片 */
	private boolean isShowShadow = true;
	/** 是否实现侧滑的动画效果 */
	private boolean isShowAnimation = true;
	/** 不在有效的触摸滑动范围内时，是否实现滑动效果 */
	private boolean isDoScoll = true;


	/** 滑动的范围 */
	private int range;
	/** 背后组件宽 */
	private int width;
	/** 背后组件高 */
	private int height;
	/** 距离左边的宽度 */
	private int mainLeft;
	/** 背景变暗的组件 */
	private ImageView iv_shadow;
	/** 背后的布局 */
	private RelativeLayout behindLeft;
	/** 前面的布局 */
	private TSlidingViewLayout beforeMain;
	/** 滑动的状态 */
	private Status status = Status.Close;
	/** 可滑动边缘部分的范围 */
	private final int EDGELENGTH = 50;
	/** 触摸滑动的有效范围（默认为全屏） */
	private int scrollScope = FULLSCOPE;
	/** 全屏可滑动 */
	public static final int FULLSCOPE = 1;
	/** 上半部分屏幕可触摸滑动 */
	public static final int UPSCOPE = 2;
	/** 下半部分屏幕可触摸滑动 */
	public static final int DOWNSCOPE = 3;
	/** 边缘部分可触摸滑动 */
	public static final int EDGESCOPE = 4;
	

	public DragLayout(Context context) {
		this(context, null);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		gestureDetector = new GestureDetectorCompat(context, new YScrollDetector());
		dragHelper = ViewDragHelper.create(this, dragHelperCallback);
	}

	private class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
			// 判断是否是横向滑动，true表示横向滑动，false表示竖直滑动
			return Math.abs(dy) <= Math.abs(dx);
		}
	}

	private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {

		// 左右拖动时触发
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// System.out.println("mainLeft的宽度为："+mainLeft+"   dx:"+dx);
			if (isDoScoll) {

				if (mainLeft + dx < 0) {
					// 关闭时，向左拖动过多，设置为0不超过边界
					return 0;
				} else if (mainLeft + dx > range) {
					// 打开时，向右拖动过多，设置为range不超过边界
					return range;
				} else {
					// 滑动时
					return left;
				}
			} else {
				return 0;
			}
		}

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// 此方法的返回值可以决定一个parentview中哪个子view可以拖动
			return true;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return width;
		}

		// 指针释放时调用
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			// System.out.println("onViewReleased方法中：xvel:"+xvel+"    yvel"+yvel+"   releasedChild"+releasedChild);

			if (isDoScoll) {
				// 指针离开屏幕时的x坐标速度，正向右滑动，负向左滑动
				if (xvel > 300) {
					open();
				} else if (xvel < -100) {
					close();
				} else if (releasedChild == beforeMain && mainLeft > range * 0.8) {
					//关闭状态，将要打开的操作
					open();
				} else if (releasedChild == behindLeft && mainLeft > range * 0.7) {
					//打开状态，将要关闭的操作
					open();
				} else {
					close();
				}
			}
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			if (isDoScoll) {
				if (changedView == beforeMain) {
					// 向右滑动
					mainLeft = left;
					// System.out.println("mainLeft:"+mainLeft+"     left:"+left+"    changedView"+changedView);
				} else {
					// 向左滑动
					mainLeft = mainLeft + left;
					// System.out.println("mainLeft:"+mainLeft+"     left:"+left+"    changedView"+changedView);
				}

				if (mainLeft < 0) {
					mainLeft = 0;
				} else if (mainLeft > range) {
					mainLeft = range;
				}

				if (isShowShadow) {
					iv_shadow.layout(mainLeft, 0, mainLeft + width, height);
				}
				if (changedView == behindLeft) {
					// 绘制背后布局的大小
					behindLeft.layout(0, 0, width, height);
					// 绘制前面布局的大小
					beforeMain.layout(mainLeft, 0, mainLeft + width, height);
//					System.out.println("mainLeft:"+mainLeft+"     left:"+left+"    changedView"+changedView);
				}

				dispatchDragEvent(mainLeft);
			}
		}
	};

	// 导入xml布局完成后，调用此方法
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (isShowShadow) {
			iv_shadow = new ImageView(context);
			iv_shadow.setImageResource(R.drawable.back);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			addView(iv_shadow, 1, lp);
		}
		// 得到布局中的后面布局的组件
		behindLeft = (RelativeLayout) getChildAt(0);
		// 得到布局中的前面布局的组件，如果有黑色背景成，那么最前面的组件取2的位置，否则没有就取1的位置
		beforeMain = (TSlidingViewLayout) getChildAt(isShowShadow ? 2 : 1);
		beforeMain.setDragLayout(this);
		behindLeft.setClickable(true);
		beforeMain.setClickable(true);
	}

	// 当视图的大小发生改变时，将会调用
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = behindLeft.getMeasuredWidth();
		height = behindLeft.getMeasuredHeight();
		if (range == 0) {
			// 设置默认滑动范围
			range = (int) (width * 0.6f);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		behindLeft.layout(0, 0, width, height);
		beforeMain.layout(mainLeft, 0, mainLeft + width, height);
	}

	
	/**显示前面布局按下时的x坐标*/
	float xDown = 0;
	/**显示前面布局按下时的y坐标*/
	float yDown = 0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 当前面布局关闭的时候，记录下点击的x、y坐标
		if (ev.getAction() == MotionEvent.ACTION_DOWN && mainLeft == 0) {
			// 得到按下时的坐标
			xDown = ev.getX();
			yDown = ev.getY();
		}

		// 如果点击的位置不在可触摸滑动的范围内，那么就不交给父视图执行事件监听操作(即，解决侧滑与父视图中组件的手势冲突)
		switch (scrollScope) {
		case FULLSCOPE:
			break;
		case UPSCOPE:
			// 上半部分
			if (yDown > height / 2) {
				return false;
			}
			break;
		case DOWNSCOPE:
			// 下半部分
			if (yDown < height / 2) {
				return false;
			}
			break;
		case EDGESCOPE:
			// 边缘
			if (xDown > EDGELENGTH) {
				return false;
			}
			break;
		}

		return dragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// 点击的y位置
		float y = e.getY();

		switch (scrollScope) {
		case FULLSCOPE:
			// 全屏
			isDoScoll = true;
			break;
		case UPSCOPE:
			// 上半部分
			if (y > height / 2) {
				// 下半部分不做操作
				isDoScoll = false;
			} else {
				isDoScoll = true;
			}
			break;
		case DOWNSCOPE:
			// 下半部分
			if (y < height / 2) {
				// 上半部分不做操作
				isDoScoll = false;

			} else {
				isDoScoll = true;
			}
			break;
		case EDGESCOPE:
			// 边缘
			if (xDown > EDGELENGTH && mainLeft == 0) {
				// 超过边缘部分不做操作
				isDoScoll = false;
			} else {
				isDoScoll = true;
			}
			break;
		}

		try {
			dragHelper.processTouchEvent(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/** 分配滑动时的操作 */
	private void dispatchDragEvent(int mainLeft) {
		// 当前滑动总范围的百分比
		float percent = mainLeft / (float) range;
		// 得到当前滑动状态
		Status lastStatus = status;
		// 是否显示滑动动画
		if (isShowAnimation) {
			animateView(percent);
		}
		// 滑动时的回调函数
		if (dragListener != null) {
			dragListener.onDrag(percent);
			if (lastStatus != getStatus() && status == Status.Close) {
				dragListener.onClose();
			} else if (lastStatus != getStatus() && status == Status.Open) {
				dragListener.onOpen();
			}
		}

	}

	/** 实现背后的布局出现的动画和前面的布局缩小 */
	private void animateView(float percent) {
		float f1 = 1 - percent * 0.25f;
		// 前面的布局缩小
		ViewHelper.setScaleX(beforeMain, f1);
		ViewHelper.setScaleY(beforeMain, f1);
		// 背后的布局显示动画
		ViewHelper.setTranslationX(behindLeft, -behindLeft.getWidth() / 2.3f + behindLeft.getWidth() / 2.3f * percent);
		ViewHelper.setScaleX(behindLeft, 0.5f + 0.5f * percent);
		ViewHelper.setScaleY(behindLeft, 0.5f + 0.5f * percent);
		ViewHelper.setAlpha(behindLeft, percent);
		if (isShowShadow) {
			// 显示背景变暗的效果
			ViewHelper.setScaleX(iv_shadow, f1 * 1.4f * (1 - percent * 0.12f));
			ViewHelper.setScaleY(iv_shadow, f1 * 1.85f * (1 - percent * 0.12f));
		}
		getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
	}

	private Integer evaluate(float fraction, Object startValue, Integer endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;
		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;
		return (int) ((startA + (int) (fraction * (endA - startA))) << 24) | (int) ((startR + (int) (fraction * (endR - startR))) << 16) | (int) ((startG + (int) (fraction * (endG - startG))) << 8) | (int) ((startB + (int) (fraction * (endB - startB))));
	}

	@Override
	public void computeScroll() {
		// 估算后续运动，continueSettling(boolean)在每个后续帧继续运动
		if (dragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	public enum Status {
		Drag, Open, Close
	}

	public Status getStatus() {
		if (mainLeft == 0) {
			status = Status.Close;
		} else if (mainLeft == range) {
			status = Status.Open;
		} else {
			status = Status.Drag;
		}
		return status;
	}

	public void open() {
		open(true);
	}

	private void open(boolean animate) {
		if (animate) {
			// smoothSlideViewTo()动画视图(左,顶部)的位置。如果这个方法返回true,调用者应该调用continueSettling(boolean)在每个后续帧继续运动,
			// 直到它返回false。如果这个方法返回false,则停止运动。
			// 在这里的作用是：惯性滑动，即手势滑过后会自动滑动完剩下部分
			if (dragHelper.smoothSlideViewTo(beforeMain, range, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			beforeMain.layout(range, 0, range * 2, height);
			dispatchDragEvent(range);
		}
	}

	public void close() {
		close(true);
	}

	private void close(boolean animate) {
		if (animate) {
			if (dragHelper.smoothSlideViewTo(beforeMain, 0, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			beforeMain.layout(0, 0, width, height);
			dispatchDragEvent(0);
		}
	}
	

	public interface DragListener {
		public void onOpen();

		public void onClose();

		public void onDrag(float percent);
	}

	public void setDragListener(DragListener dragListener) {
		this.dragListener = dragListener;
	}
	

	/** 设置滑动范围 */
	public void setRange(int myRange) {
		if (width != 0 && myRange > width) {
			range = (int) (width * 0.6f);
		} else {
			range = myRange;
		}
	}

	/**
	 * 功能说明：设置可触摸滑动的范围 作者：ts 创建日期:2014-12-10 参数：
	 * 
	 * @param scrollScope
	 *            默认Full,DragLayout的四个范围参数（Full、UP、Down、edge(边缘)） 示例：
	 */
	public void setScrollScope(int scrollScope) {
		this.scrollScope = scrollScope;
	}

	/**
	 * 是否实现侧滑时的动画
	 * 
	 * @param isShowAnimation
	 *            true实现动画，false不实现动画，默认为true
	 */
	public void setShowAnimation(boolean isShowAnimation) {
		this.isShowAnimation = isShowAnimation;
	}

	/**
	 * 得到前面的布局View
	 * 
	 * @return
	 */
	public ViewGroup getBeforeView() {
		return beforeMain;
	}

	/**
	 * 得到后面的布局View
	 * @return
	 */
	public ViewGroup getBehindView() {
		return behindLeft;
	}

}
