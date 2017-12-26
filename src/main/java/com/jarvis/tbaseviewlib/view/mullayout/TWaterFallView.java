package com.jarvis.tbaseviewlib.view.mullayout;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 瀑布流
 * @author tansheng
 */
public class TWaterFallView extends LinearLayout {
	private TWaterFallBaseAdapter mAdapter;
	private Context context;
	/** 界面是否初始完成，true:已经初始化完成，false:未初始化完成 */
	private boolean isInit = false;
	/** 父容器宽度 */
	private int mWidth;
	/** 每个item的宽度 */
	private int itemWidth;
	/** 记录每列的Item布局 */
	private ArrayList<LinearLayout> columnLayout;
	/** 上一次的显示总数 */
	private int lastCount = 0;

	public TWaterFallView(Context context) {
		super(context);
		init(context);
	}

	public TWaterFallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TWaterFallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	private OnItemClickListener listener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;

	}

	/**
	 * 创建列容器布局
	 * 
	 * @author tansheng
	 * @param columnNum
	 *            列数
	 * @param itemWidth
	 *            每列的宽度
	 */
	private void creatColLayout(int columnNum) {
		// 未初始化完成时，不调用
		if (!isInit) {
			return;
		}
		// 单个Item的宽度
		try {
			itemWidth = mWidth / columnNum;
		} catch (Exception e) {
		}
		// 加载显示列数
		for (int i = 0; i < columnNum; i++) {
			LinearLayout layout = new LinearLayout(context);
			LayoutParams itemParam = new LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setLayoutParams(itemParam);
			addView(layout);
			columnLayout.add(layout);
		}
	}

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		super.dispatchWindowFocusChanged(hasFocus);
		if (!isInit) {
			isInit = true;
			// 获取父容器宽度
			mWidth = getWidth();
			if (mAdapter != null) {
				// 先生成列的容器布局LinearLayout
				creatColLayout(mAdapter.getColumnNum());
				// 然后生成Item布局
				creatItemLayout();
			}
		}
	}

	/**
	 * 创建每个Item
	 * 
	 * @author tansheng
	 * @param layoutId
	 *            布局Id
	 */
	private void creatItemLayout() {
		// 未初始化完成时，不调用
		if (!isInit) {
			return;
		}
		if (mAdapter != null) {
			for (int i = lastCount; i < mAdapter.getCount(); i++) {
				// 创建每个item
				 View itemView = mAdapter.getView(null, i);
				if (itemView != null) {
					LayoutParams params = new LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
					itemView.setLayoutParams(params);
					itemView.setTag(i);
					itemView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							int position= (Integer) view.getTag();
							if (listener != null) {
								listener.onItemClick(view,position);
							}
						}
					});
					// 将得到的item添加到容器中
					columnLayout.get(i % mAdapter.getColumnNum()).addView(itemView);
				}
			}
			// 保存上一次显示的总数
			lastCount = mAdapter.getCount();
		}
	}

	private TMulAdapterDataSetObserver mDataSetObserver;

	public void setAdapter(TWaterFallBaseAdapter adapter) {
		// 解除绑定
		if (mAdapter != null && mDataSetObserver != null) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
			this.removeAllViews();
		}

		columnLayout = new ArrayList<LinearLayout>();
		lastCount = 0;

		mAdapter = adapter;
		mDataSetObserver = new TMulAdapterDataSetObserver();
		// 绑定adapter
		mAdapter.registerDataSetObserver(mDataSetObserver);

		// 先生成列的容器布局LinearLayout
		creatColLayout(mAdapter.getColumnNum());
		// 然后生成Item布局
		creatItemLayout();
	}

	// 实现观察者监听，当数据变化时调用
	class TMulAdapterDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			super.onChanged();
			// 加载下一页数据
			creatItemLayout();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			// 重新绘制界面，并从第1条数据开始加载显示
			for (int i = 0; i < columnLayout.size(); i++) {
				columnLayout.get(i).removeAllViews();
			}
			lastCount = 0;
			creatItemLayout();
		}

	}

}
