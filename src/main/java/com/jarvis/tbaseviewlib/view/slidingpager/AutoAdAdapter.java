/**
 * 版权：<p>
 * 作者: cc<p>
 * 创建日期:2015年4月27日
 */
package com.jarvis.tbaseviewlib.view.slidingpager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * <p>
 * 作者: cc
 * <p>
 * 创建日期：2015年4月27日上午11:17:14
 * <p>
 * 示例：
 */
public class AutoAdAdapter extends PagerAdapter {
    
    List<ImageView> viewsList;
    PagerOnClick pagerOnClick;
    
    

    public void setPagerOnClick(PagerOnClick pagerOnClick) {
        this.pagerOnClick = pagerOnClick;
    }

    public AutoAdAdapter(List<ImageView> viewsList) {
        this.viewsList = viewsList;
        //this.pagerOnClick = pagerOnClick;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ImageView view = viewsList.get(position % viewsList.size());
        ((ViewPager) container).removeView(view);
        //view.setImageBitmap(null);
    }

    /**
     * 给viewpager的每一个view设置监听事件
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        ((ViewPager) container).addView(viewsList.get(position));
        View view = viewsList.get(position);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(pagerOnClick != null){
                    pagerOnClick.onClickPagerItem(position-1);
                }
            }
        });
        return viewsList.get(position);
    }

    @Override
    public int getCount() {
        return viewsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface PagerOnClick {
        public void onClickPagerItem(int position);
    }
}
