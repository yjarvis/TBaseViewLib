package com.jarvis.tbaseviewlib.view.horizontalsliding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 *  横向无限循环滑动+回弹效果
 * @author tansheng
 */
@SuppressWarnings("unused")
public class THorizontalSlidingView extends HorizontalScrollView implements OnClickListener {
	
	private Context context;
	/**
	 * Item点击的回调
	 */
	private OnItemClickListener mListener;

	private BaseAdapterHorizontalSlidingView mAdapter;
	/**
	 * 存放Item的容器
	 */
	private ViewGroup mContainer;
	/**
	 * 控件的高度
	 */
	private int mScreenHeight;
	/**
	 * 控件的宽度
	 */
	private int mScreenWidth;
	/**
	 * 每个条目的高度
	 */
	private int mItemHeight;
	/**
	 * 每个条目的宽度
	 */
	private int mItemWidth;

	/**
	 * 单屏幕显示的条目数
	 */
	private int mShowCount=0;
	/**
	 * 条目总数
	 */
	private int mItemCount;
	/**
	 * item的宽高配置LayoutParams
	 */
	private LinearLayout.LayoutParams lp = null;
	/**
	 * 是否已经初始化
	 */
	private boolean isInit=false;
	/**
	 * 滑动偏移量
	 */
	private float offsetX;
	/** 是否循环滚动(默认循环),true：循环，false:不循环 */
	private boolean isCirculation = true;
	/**横向内边距*/
	private int horizontalSpacing=0;


	public THorizontalSlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setHorizontalScrollBarEnabled(false);
	}
	
	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		super.dispatchWindowFocusChanged(hasFocus);
		if (!isInit) {
			isInit = true;
			mScreenWidth = getWidth();
			mScreenHeight = getHeight();
			//配置item
			initView();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/**
	 * 配置Item控件并添加到父布局中
	 *@author tansheng
	 */
	private void initView(){
		//没有初始化完成不进行配置
		if (!isInit) {
			return;
		}
		if (mAdapter != null) {
			// 计算每个item的宽度
			mItemWidth = (mScreenWidth- horizontalSpacing*(mShowCount))/ mShowCount;
			// 配置每个item的宽高
			lp = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
			lp.setMargins(horizontalSpacing/2,0,horizontalSpacing/2,0);
			mContainer.removeAllViews();
			for (int i = 0; i < mAdapter.getCount(); i++) {
				addChildView(i);
			}
			addChildView(0);
		}
	}
	
	/**
	 * 在容器末尾添加一个Item
	 * 
	 * @param i
	 */
	private void addChildView(int i) {
		View item = mAdapter.getView(i, null, this);
		// 设置参数
		item.setLayoutParams(lp);
		// 设置Tag
		item.setTag(i);
		// 添加事件
		item.setOnClickListener(this);
		mContainer.addView(item);
	}

	/**
	 * 在容器指定位置添加一个Item
	 * 
	 * @param i
	 */
	private void addChildView(int i, int index) {
		View item = mAdapter.getView(i, null, this);
		// 设置参数
		item.setLayoutParams(lp);
		item.setTag(i);
		item.setOnClickListener(this);
		mContainer.addView(item, index);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int scrollX = getScrollX();
		float downX = 0;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			// System.out.println("y:" + scrollY + "   x:" + scrollX);
			offsetX = downX - ev.getX();
			// 表示此时ScrollView的顶部已经达到屏幕顶部
			if (isCirculation) {
				if (scrollX == 0) {
					addChildToFirst();
				}
				// ScrollView的顶部已经到达屏幕底部
				if (Math.abs(scrollX - mItemWidth) <= mShowCount || Math.abs(scrollX) > mItemWidth) {
					addChildToLast();
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			checkForReset();
			return true;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 在底部添加一个View，并移除第一个View
	 */
	private void addChildToLast() {
//		Log.e("TAG", "addChildToLast");
		int pos = (Integer) mContainer.getChildAt(1).getTag();
		addChildView(pos);
		mContainer.removeViewAt(0);
		this.scrollTo(0, 0);
	}

	/**
	 * 在顶部添加一个View，并移除最后一个View
	 */
	protected void addChildToFirst() {
//		Log.e("TAG", "addChildToFirst");
		int pos = (Integer) mContainer.getChildAt(mItemCount - 1).getTag();
		addChildView(pos, 0);
		mContainer.removeViewAt(mContainer.getChildCount() - 1);
		// 设置参数
		this.scrollTo(mItemWidth - mShowCount - 1, 0);
		// this.scrollTo(mItemWidth, 0);
	}

	/**
	 * 检查当前getScrollY,显示完成Item，或者收缩此Item
	 */
	private void checkForReset() {
		int val = 0;
		// 设置参数
		val = (int) (getScrollX() % mItemWidth);
		// System.out.println("scollX:" + getScrollX() + "   mItemWidth:" +mItemWidth + "   val:" + val);
		if (val >= mItemWidth / 2) {
	
			if (offsetX > 0) {
				// 向左滑动
				smoothScrollBy(-1 * (mItemWidth - val+horizontalSpacing), 0);
			} else {
				// 向右滑动
				smoothScrollBy(mItemWidth - val+horizontalSpacing, 0);
			}
			// System.out.println("to->width");
		} else {
		
			if (offsetX > 0) {
				// 向左滑动
				smoothScrollBy(val, 0);
			} else {
				// 向右滑动
				smoothScrollBy(-1 * val, 0);
			}
			// System.out.println("to->0");
		}
	}

	@Override
	public void onClick(View v) {
		if (mListener != null) {
			int position = (Integer) v.getTag();
			mListener.onItemClick(position, v);
		}
	}
//------------------------------------------------以下为THorizontalSlidingView提供的一些方法------------------------------------------------------------
	
	
	/**
	 * 适配器
	 * 
	 * @author tansheng
	 * 
	 */
	public static abstract class BaseAdapterHorizontalSlidingView {
		public abstract View getView(int position, View convertView, THorizontalSlidingView parent);

		/** 屏幕中显示的item总数 */
		public abstract int getShowCount();

		/** item总数量 */
		public abstract int getCount();
	}
	/**
	 * 点击的回调
	 */
	public interface OnItemClickListener {
		void onItemClick(int position, View view);
	}

	/**设置适配器*/
	public void setAdapter(BaseAdapterHorizontalSlidingView adapter) {
		this.mAdapter = adapter;
		// 根据Adapter的方法，为容器添加Item
		if (adapter != null) {
			mItemCount = adapter.getCount();
			mContainer = (ViewGroup) getChildAt(0);
			if (mShowCount==0) {
				mShowCount = adapter.getShowCount();
			}
			// 如果屏幕中显示的item数量为0，则设置默认为1个
			if (mShowCount == 0) {
				mShowCount = 1;
			}
			//配置item
			initView();
		}
	}
	/**设置是否循环滚动*/
	public void setIsCirculation(boolean isCirculation){
		this.isCirculation=isCirculation;
	}
	/**设置当前屏幕显示个数，(如果设置了此方法，那么在Adapter中的getShowCount()就无效了)*/
	public void setShowCount(int mShowCount){
			this.mShowCount=mShowCount;
	}
	/**设置Item的点击点击监听*/
	public void setOnItemClickListener(OnItemClickListener mListener) {
		this.mListener = mListener;
	}


	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing=horizontalSpacing;
	}
	

}
