package com.jarvis.tbaseviewlib.view.mullayout;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;

/**
 * 瀑布流BaseAdapter
 * @author tansheng
 */
public abstract class TWaterFallBaseAdapter {
	private final DataSetObservable mDataSetObservable = new DataSetObservable();

	/** 得到加载总数 */
	public abstract int getCount();

	/** 加载Item */
	public abstract View getView(View converView, int position);

	/** 得到列数 */
	public abstract int getColumnNum();

	public abstract Object getItem(int position);

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}

	/**
	 * 通知附加的观察者，底层数据已经改变，任何与数据集相关联的View都应该重绘。
	 */
	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}

	/**
	 * 通知附件的观察者，底层数据已经无效或不可用；当适配器无效并不应该再报数据集变化时触发。
	 */
	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}

}
