package com.jarvis.tbaseviewlib.view.mostlistview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 功能说明    解决ListView和ScrollView冲突的自定义ListView
 * 作者: jarvisT
 * 创建日期：2014-11-6下午1:18:49
 */
public class MTListView extends ListView {

	public MTListView(Context context) {
		super(context);
	}

	public MTListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MTListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	

}
