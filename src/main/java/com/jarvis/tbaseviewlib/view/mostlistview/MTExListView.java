package com.jarvis.tbaseviewlib.view.mostlistview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 功能说明    解决ExpandableListView和ScrollView冲突的自定义ExpandableListView
 * 作者: ts
 * 创建日期：2014-11-6下午1:18:49
 */
public class MTExListView extends ExpandableListView {

	public MTExListView(Context context) {
		super(context);
	}

	public MTExListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MTExListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	

}
