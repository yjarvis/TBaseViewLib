package com.jarvis.tbaseviewlib.view.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 日历控件
 * 
 * @author tansheng
 */
public class TCalendarView extends LinearLayout {
	
	private Context mContext;
	/**组件宽高*/
	private int mWidth;
	/**组件宽高*/
	private int mHeight;
	private ViewPager mViewPager;
	private mPagerAdapter mPagerAdapter;
	private ArrayList<GridView> viewContainter;
	private ArrayList<DataModel> dataList;
	/** 是否已经初始化界面*/
	private boolean isInit=false;
	/** 用于计算滑动的方向*/
	private int lastValue = -1;
	/** 显示跨年份总数 */
	private final int OFFSET_YEAR = 40;
	/** ViewPager显示的页码 */
	private int pagerIndex = OFFSET_YEAR * 12 / 2;
	/** 是否向右滑动，1向右滑动，-1向左滑动 ,0直接跳转*/
	private int isScrollRight = 1;
	/** 星期名称 */
	private String[] weekName = { "日", "一", "二", "三", "四", "五", "六" };
	/**星期的字体大小*/
	private int textSizeWeek=35;
	/**本月日期的字体大小*/
	private int textSizeDay=30;
	/**星期的字体颜色*/
	private int textColorWeek=Color.BLACK;
	/**本月日期的字体颜色*/
	private int textColorDay=Color.BLACK;
	/**上一个月和下一个月的字体颜色*/
	private int textColorDayNext=Color.GRAY;
	/**当前天的默认颜色*/
	private int colorDefault=Color.parseColor("#28CEF8");
	/**点击位置的默认颜色*/
	private int colorClick=Color.parseColor("#42E3CE");
	
	
	private TCalenderItemsOnClick listener;
	private OnTCalenderChangeListener listenerOnChange;
	public interface TCalenderItemsOnClick{
		/**
		 * item的点击事件
		 * @author tansheng
		 * @param v 返回点击的视图
		 * @param position 返回点击位置
		 */
		public void itemsOnClick(View v, int position, int year, int month, int day);
		
	}
	
	public interface OnTCalenderChangeListener{
		public void onPageSelected(int year, int month, int arg0) ;
		public void onPageScrolled(int arg0, float arg1, int arg2);
		public void onPageScrollStateChanged(int arg0);
	}
	
	public TCalendarView(Context context) {
		super(context);
		this.mContext=context;
//		initView(context);
	}

	public TCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		initView(context);
	}

	public TCalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext=context;
//		initView(context);
	}
	

	@Override
	public void dispatchWindowFocusChanged(boolean hasFocus) {
		super.dispatchWindowFocusChanged(hasFocus);
		if (!isInit) {
			isInit=true;
			// 获取父控件宽高
			mWidth = getWidth();
			mHeight = getHeight();
			if (mWidth==0) {
				mWidth=dipTopx(mContext, 200);
			}
			if (mHeight==0) {
				mHeight=dipTopx(mContext, 150);
			}
			
			LayoutParams lp=new LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);
			LayoutParams lpText=new LayoutParams(0, LayoutParams.WRAP_CONTENT,1);
			LinearLayout layout = new LinearLayout(mContext);
			layout.setLayoutParams(lp);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < 7; j++) {
				TextView textView = new TextView(mContext);
				textView.setText(weekName[j]);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(lpText);
				textView.setTextColor(textColorWeek);
				textView.setTextSize(pxTodip(mContext, textSizeWeek));
				layout.addView(textView);
			}
			addView(layout, 0);
	}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 初始化操作
	 * 
	 * @author tansheng
	 */
	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		mContext = context;
		viewContainter = new ArrayList<GridView>();
		dataList = new ArrayList<DataModel>();
		
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < OFFSET_YEAR * 12; i++) {
			addData(i);
			GridView mGridView = new GridView(context);
//			mGridView.setLayoutParams(params);
			mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			// mGridView.setColumnWidth(0);// 设置列的宽度
			// mGridView.setHorizontalSpacing(0);// 设置列间距
			// mGridView.setStretchMode(GridView.NO_STRETCH);
			mGridView.setNumColumns(7);// 设置列数
			mGridView.setAdapter(new GridViewAdapter(i));
			viewContainter.add(mGridView);
		}
		
		mViewPager = new ViewPager(context);
		mPagerAdapter = new mPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(pagerIndex);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (isScrollRight) {
				case 1:
					//向右滑动，则时间向以前显示
					pagerIndex -= 1;
					break;
				case -1:
					//向左滑动，则时间向以后显示
					pagerIndex += 1;
					break;
				case 0:
					break;
				}
				if (listenerOnChange!=null) {
					listenerOnChange.onPageSelected(dataList.get(pagerIndex).getYear(), dataList.get(pagerIndex).getMonth(),arg0);
				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (lastValue > arg2) {
					// 递减，向右侧滑动
					isScrollRight = 1;
				} else if (lastValue < arg2) {
					// 递增，向左侧滑动
					isScrollRight = -1;
				}
				lastValue = arg2;
				if (listenerOnChange!=null) {
					listenerOnChange.onPageScrolled(arg0, arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (listenerOnChange!=null) {
					listenerOnChange.onPageScrollStateChanged(arg0);
				}
			}
		});
	
		addView(mViewPager);
	}

	private class mPagerAdapter extends PagerAdapter {
		// viewpager中的组件数量
		@Override
		public int getCount() {
			return viewContainter.size();
		}

		/*
		 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(viewContainter.get(position));
		}

		/*
		 * 创建一个view
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(viewContainter.get(position), 0);
			return viewContainter.get(position);
		}

		/*
		 * 判断出去的view是否等于进来的view 如果为true直接复用
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	
	private class GridViewAdapter extends BaseAdapter {
		private int index;
		/**记录当月最后一天的位置*/
		private int footNumber = -1;
		/**上一次点击的位置*/
		private int lastPosition=-1;

		public GridViewAdapter(int index) {
			this.index = index;
		}

		@Override
		public int getCount() {
			// 7列6排
			return 42;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {

			TextView textView = new TextView(mContext);
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(pxTodip(mContext, textSizeDay));
			if (position <= 6) {
				if (position >= dataList.get(index).getFirstDayOfWeek()) {
					textView.setText(position - dataList.get(index).getFirstDayOfWeek() + 1 + "");
					textView.setTextColor(textColorDay);
				} else {
					// 月初多余部分
					textView.setTextColor(textColorDayNext);
					textView.setText(dataList.get(index).getLastMonthCount() - dataList.get(index).getFirstDayOfWeek() + position + 1 + "");
				}
			} else {
				if (position < dataList.get(index).getDayCount() + dataList.get(index).getFirstDayOfWeek()) {
					textView.setText(position - dataList.get(index).getFirstDayOfWeek() + 1 + "");
					textView.setTextColor(textColorDay);
				} else {
					if (footNumber == -1) {
						footNumber = position;
					}
					// 月底多余部分
					textView.setTextColor(textColorDayNext);
					textView.setText(position - footNumber + 1 + "");
				}
			}
			
			//设置当前日期的背景颜色
			if (dataList.get(index).getCurrentDay()==position- dataList.get(index).getFirstDayOfWeek()+1) {
				textView.setBackgroundColor(colorDefault);
			}
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (listener != null) {
						int day=Integer.valueOf(((TextView)v).getText().toString());
						int month=dataList.get(index).getMonth();
						int year=dataList.get(index).getYear();
						if (position <= 6) {
							// 在第一排的点击
							if (day > 7) {
								// 上一个月
								month -= 1;
								if (month <= 0) {
									// 上一年
									month = 12;
									year = year - 1;
								}
							}
						} else if (position >= dataList.get(index).getDayCount() + dataList.get(index).getFirstDayOfWeek()) {
							// 在下一个月的点击
							month += 1;
							if (month >= 13) {
								// 下一年
								month = 1;
								year = year + 1;
							}
						}
						listener.itemsOnClick(v, position,year,month,day);
					}
					
					
					if (lastPosition == -1&&dataList.get(index).getCurrentDay() != position - dataList.get(index).getFirstDayOfWeek() + 1) {
						//当第一次点击时并且点击的不是当天，则改变点击的背景颜色
						v.setBackgroundColor(colorClick);
						lastPosition = position;
					} else if (lastPosition != position) {
						//不能改变当天的背景颜色
						if (dataList.get(index).getCurrentDay() != position - dataList.get(index).getFirstDayOfWeek() + 1) {
							v.setBackgroundColor(colorClick);
							parent.getChildAt(lastPosition).setBackgroundColor(Color.parseColor("#00000000"));
							lastPosition = position;
						}
					}
				}
			});

			

			return textView;
		}

	}

	/**
	 * 加载横跨N年的所有数据
	 *@author tansheng
	 * @param i
	 */
	private void addData(int i) {
		Calendar calendar = Calendar.getInstance();
		int yearCurrent = calendar.get(Calendar.YEAR);// 得到真实的当前年
		int monthCurrent = calendar.get(Calendar.MONTH) + 1;// 得到真实的当前月
		int currentDay = calendar.get(Calendar.DAY_OF_MONTH);// 得到当前是几号
		calendar.set(Calendar.YEAR, yearCurrent + i / 12 - OFFSET_YEAR / 2);
		int year = calendar.get(Calendar.YEAR);// 得到前移后的当前年
		int month = (i % 12 + 1);// 得到当前月
		calendar.set(Calendar.MONTH, month - 1);
		int dayCount = calendar.getActualMaximum(Calendar.DATE);// 得到当前月天数
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 得到1号在当前月中属于星期几(周一至周末返回的是：2345671，则(返回值-1)即为星期几)
		calendar.set(Calendar.MONTH, month - 2 < 0 ? 11 : month - 2);// 前移到上一个月
		int lastMonthCount = calendar.getActualMaximum(Calendar.DATE);

//		System.out.println("year：" + year + "   month:" + month + "   currentDay:" + currentDay + "   dayCount" + dayCount + " FirstDayOfWeek:" + firstDayOfWeek);

		DataModel dataModel = new DataModel();
		dataModel.setYear(year);
		dataModel.setMonth(month);
		dataModel.setCurrentDay(currentDay);
		dataModel.setDayCount(dayCount);
		dataModel.setFirstDayOfWeek(firstDayOfWeek);
		dataModel.setLastMonthCount(lastMonthCount);
		if (year == yearCurrent && month == monthCurrent) {
			//得到当前日期在所有数据中的下标位置
			pagerIndex = i;
		}

		dataList.add(dataModel);
	}

	private class DataModel {
		/** 当前年 */
		private int year;
		/** 当前月 */
		private int month;
		/** 当前月天数 */
		private int dayCount;
		/** 当前是几号 */
		private int currentDay;
		/** 1号在当前月中属于星期几 */
		private int firstDayOfWeek;
		/** 上一个月的总天数 */
		private int lastMonthCount;

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getDayCount() {
			return dayCount;
		}

		public void setDayCount(int dayCount) {
			this.dayCount = dayCount;
		}

		public int getCurrentDay() {
			return currentDay;
		}

		public void setCurrentDay(int currentDay) {
			this.currentDay = currentDay;
		}

		public int getFirstDayOfWeek() {
			return firstDayOfWeek;
		}

		public void setFirstDayOfWeek(int firstDayOfWeek) {
			this.firstDayOfWeek = firstDayOfWeek;
		}

		public int getLastMonthCount() {
			return lastMonthCount;
		}

		public void setLastMonthCount(int lastMonthCount) {
			this.lastMonthCount = lastMonthCount;
		}
	}

	
	
	/** px转dip */
	private int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/** dip转px */
	private int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	
	//--------------------------------------------------以下为此控件提供的方法-------------------------------------------------------------------------
	public String[] getWeekName() {
		return weekName;
	}
	public int getTextSizeWeek() {
		return textSizeWeek;
	}
	public int getTextSizeDay() {
		return textSizeDay;
	}
	public int getTextColorWeek() {
		return textColorWeek;
	}
	public int getTextColorDay() {
		return textColorDay;
	}
	public int getTextColorDayNext() {
		return textColorDayNext;
	}
	public int getColorDefault() {
		return colorDefault;
	}
	public int getColorClick() {
		return colorClick;
	}
	/**
	 * 得到当前选择的年
	 *@author tansheng
	 * @return
	 */
	public int getYear(){
		return dataList.get(pagerIndex).getYear();
	}
	/**
	 * 得到当前选择的月
	 *@author tansheng
	 * @return
	 */
	public int getMonth(){
		return dataList.get(pagerIndex).getMonth();
	}
	/**
	 * 得到当前的日期
	 *@author tansheng
	 * @return
	 */
	public int getDay(){
		return dataList.get(pagerIndex).getCurrentDay();
	}
	
	/**
	 * 设置星期名称:例如weekName={ "日", "一", "二", "三", "四", "五", "六" }
	 *@author tansheng
	 * @param weekName
	 */
	public void setWeekName(String[] weekName) {
		this.weekName = weekName;
	}

	/**
	 * 设置星期的文字大小
	 *@author tansheng
	 * @param textSizeWeek
	 */
	public void setTextSizeWeek(int textSizeWeek) {
		this.textSizeWeek = textSizeWeek;
	}


	/**
	 * 设置当月的文字大小
	 *@author tansheng
	 * @param textSizeDay
	 */
	public void setTextSizeDay(int textSizeDay) {
		this.textSizeDay = textSizeDay;
	}

	
	/**
	 * 设置星期文字的颜色
	 *@author tansheng
	 * @param textColorWeek
	 */
	public void setTextColorWeek(int textColorWeek) {
		this.textColorWeek = textColorWeek;
	}

	/**
	 * 设置当月的文字颜色
	 *@author tansheng
	 * @param textColorDay
	 */
	public void setTextColorDay(int textColorDay) {
		this.textColorDay = textColorDay;
	}

	/**
	 * 设置当前页中的下一月和上一月的文字颜色
	 *@author tansheng
	 * @param textColorDayNext
	 */
	public void setTextColorDayNext(int textColorDayNext) {
		this.textColorDayNext = textColorDayNext;
	}

	
	/**
	 * 设置当天的背景颜色
	 *@author tansheng
	 * @param colorDefault
	 */
	public void setColorDefault(int colorDefault) {
		this.colorDefault = colorDefault;
	}
	
	
	/**
	 * 设置items的点击背景颜色
	 *@author tansheng
	 * @param colorClick
	 */
	public void setColorClick(int colorClick) {
		this.colorClick = colorClick;
	}
	/**
	 * 跳转到某年某月去
	 *@author tansheng
	 * @param year
	 * @param month
	 */
	public void setCurrentItem(int year,int month){
		if (year>=dataList.get(0).getYear()&&year<=dataList.get(dataList.size()-1).getYear()) {
			for (int i = 0; i < dataList.size(); i++) {
				if (dataList.get(i).getYear()==year&&dataList.get(i).getMonth()==month) {
					pagerIndex=i;
				}
			}
			isScrollRight=0;
			mViewPager.setCurrentItem(pagerIndex);
		}else{
			Toast.makeText(mContext, "超出年份，请重新选择", Toast.LENGTH_SHORT).show();
		}
			
	}

	/**设置点击时的监听*/
	public void setOnItemsClickListener(TCalenderItemsOnClick listener) {
		this.listener = listener;
	}
	/**设置滑动时的监听*/
	public void setOnTCalenderChangeListener(OnTCalenderChangeListener listener) {
		this.listenerOnChange = listener;
	}
	
	
	

	
	
	
	
	
	//-------------------------------------------------End--------------------------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
