package com.jarvis.tbaseviewlib.view.headlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * 带头布局的listview
 * @author tansheng  QQ:717549357
 * @date 2015-11-30 上午10:37:47
 */
public class PinnedHeaderListView extends ListView {
	public interface PinnedHeaderAdapter {
		/** 隐藏时 */
		public static final int PINNED_HEADER_GONE = 0;
		/** 显示 */
		public static final int PINNED_HEADER_VISIBLE = 1;
		/** 推上去 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/** 得到当前头布局的状态 */
		int getPinnedHeaderState(int position);

		/** 配置固定下来的头布局属性 */
		public void configurePinnedHeader(View headerView, int position);

	}

	private PinnedHeaderAdapter headerAdapter;

	/** 头文件的布局 */
	private View mHeaderView;
	/** 头布局的宽 */
	private int mHeaderViewWidth;
	/** 头布局的高 */
	private int mHeaderViewHeight;
	/** 头布局是否显示 */
	private boolean mHeaderViewVisible;

	public PinnedHeaderListView(Context context) {
		super(context);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 功能说明：设置头布局界面
	 * <p>
	 * 作者：ts 创建日期:2014-11-24 参数：
	 * 
	 * @param mHeaderView
	 */
	public void setPinnedHeaderView(View mHeaderView) {
		this.mHeaderView = mHeaderView;

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		headerAdapter = (PinnedHeaderAdapter) adapter;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			// 得到头布局的宽和高
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	// 调用其children的layout函数来设置子视图相对与父视图中的位置，具体位置由函数layout的参数决定
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(getFirstVisiblePosition());
		}
	}

	public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		// 得到当前头布局的状态
		int state = headerAdapter.getPinnedHeaderState(position);
		switch (state) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE:// 隐藏
			// 设置为不显示表头
			mHeaderViewVisible = false;
			break;
		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE:// 显示

			if (mHeaderView.getTop() != 0) {
				// 将头布局绘制在顶部
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			// 设置显示头布局
			mHeaderViewVisible = true;
			// 配置固定下来的头布局属性
			headerAdapter.configurePinnedHeader(mHeaderView, position);

			break;
		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP:// 推上去

			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			// 头布局的偏移量
			int y;

			// 如果头布局的高度大于下边位置，那么就开始往上推，计算推的偏移量
			if (headerHeight > bottom) {
				y = bottom - headerHeight;
			} else {
				// 否则偏移量为0,即禁止不动
				y = 0;
			}

			mHeaderView.layout(0, y, mHeaderViewWidth, y + mHeaderViewHeight);
			mHeaderViewVisible = true;
			// 配置固定下来的头布局属性
			headerAdapter.configurePinnedHeader(mHeaderView, position);
			break;

		default:
			break;
		}

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// 如果处于显示状态
		if (mHeaderViewVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}

	}

}
