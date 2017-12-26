package com.jarvis.tbaseviewlib.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.ui.common.TFragmentActivity;
import com.jarvis.tbaseviewlib.ui.fragment.ShowImageFragment;
import com.jarvis.tbaseviewlib.view.scaleview.HackyViewPager;

import java.util.ArrayList;

/***
 * 控制显示图片浏览的界面
 *
 * @author tansheng
 *
 */
public class ShowImageActivity2 extends TFragmentActivity {

	private HackyViewPager viewPager;
	/** 图片源数据 */
	private ArrayList<String> imgList;
	/** 初始化显示第几个图片 */
	private int defaulPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		Intent intent = getIntent();
		imgList = intent.getStringArrayListExtra(Constrans.FLAG_IMAGE_SHOW);
		defaulPosition = intent.getIntExtra("defaulPosition", 0);
		initView(true);
	}

	@Override
	public void setData() {

	}

	@Override
	public void requestData(boolean isShow) {

	}

	@Override
	public void showFragment(Fragment fragment) {

	}

    @Override
    public void initView(boolean isStatusBar) {
        super.initView(isStatusBar);
		viewPager = (HackyViewPager) findViewById(R.id.showimage_viewpager);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(defaulPosition);
	}

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@SuppressWarnings("static-access")
		@Override
		public Fragment getItem(int position) {
			return new ShowImageFragment().newInstance(imgList.get(position));
		}

		@Override
		public int getCount() {
			return imgList.size();
		}

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_anim_out);
	}

}
