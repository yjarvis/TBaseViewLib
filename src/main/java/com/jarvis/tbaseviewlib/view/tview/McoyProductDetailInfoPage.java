package com.jarvis.tbaseviewlib.view.tview;


import android.content.Context;
import android.view.View;

public class McoyProductDetailInfoPage implements McoySnapPageLayout.McoySnapPage{
	
	private Context context;
	
	private View rootView = null;
	private McoyScrollView mcoyScrollView = null;
	
	public McoyProductDetailInfoPage (Context context, View rootView,int scorllViewId) {
		this.context = context;
		this.rootView = rootView;
		mcoyScrollView = (McoyScrollView) this.rootView.findViewById(scorllViewId);
	}
	
	@Override
	public View getRootView() {
		return rootView;
	}

	@Override
	public boolean isAtTop() {
		return true;
	}

	@Override
	public boolean isAtBottom() {
        int scrollY = mcoyScrollView.getScrollY();
        int height = mcoyScrollView.getHeight();
        int scrollViewMeasuredHeight = mcoyScrollView.getChildAt(0).getMeasuredHeight();

        if ((scrollY + height) >= scrollViewMeasuredHeight) {
            return true;
        }
        return false;
	}

}
