/**
 * 版权：<p>
 * 作者: cc<p>
 * 创建日期:2015年4月27日
 */
package com.jarvis.tbaseviewlib.view.slidingpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.BitmapHelp;
import com.jarvis.tbaseviewlib.view.slidingpager.AutoAdAdapter.PagerOnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 作者: cc
 * <p>
 * 创建日期：2015年4月27日上午11:10:51
 * <p>
 * 示例：
 */
public class AutoAdView extends FrameLayout {

    Context context;
    ViewPager mViewPager;
    LinearLayout dotsLayout;
    AutoAdAdapter adAdapter;
    PagerOnClick pagerOnClick;

    /**
     * 自定义属性
     */
    private boolean isEnableDots;
    private boolean isEnableAutoPlay;
    private int dotsPoistion;
    private Drawable background;
    private int intervalTime;

    List<ImageView> images;// viewpager显示的view集合
    List<ImageView> dots;// 动态添加的imageview点的list

    List<String> datas;
    List<Integer> datasResources;
    //	ImageLoader imageLoader;
//	DisplayImageOptions options;
    BitmapHelp bitmapHelp;

    private final int DEFAULT_TIME = 1000 * 10;

    public boolean isStart = false;// 纪录轮播是否开启

    int finalpositoin = 1;// 切换用到的num

    public List<String> getDatas() {
        return datas;
    }

    @SuppressWarnings("deprecation")
    public void setDatas(List<String> datas) {
        this.datas = datas;

        if (background != null && (datas == null || datas.size() == 0)) {
            setBackgroundDrawable(background);
        } else {
            setBackgroundDrawable(null);
        }

        if (datas != null && datas.size() > 0) {
            init();
        }
    }

    @SuppressWarnings("deprecation")
    public void setDatasResources(List<Integer> datasResources) {
        this.datasResources = datasResources;
        if (background != null && (datasResources == null || datasResources.size() == 0)) {
            setBackgroundDrawable(background);
        } else {
            setBackgroundDrawable(null);
        }
        if (datasResources != null && datasResources.size() > 0) {
            init();
        }
    }

    public void clearDatas() {
        if (datas != null) {
            datas.clear();
        }
    }

    public PagerOnClick getPagerOnClick() {
        return pagerOnClick;
    }

    public void setPagerOnClick(PagerOnClick pagerOnClick) {
        this.pagerOnClick = pagerOnClick;
    }

    @SuppressWarnings("deprecation")
    public AutoAdView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_ad_mod, this);

        bitmapHelp = new BitmapHelp(context);
//		imageLoader = CqtImageLoader.getInstance().getImageLoader(context);
//		options = new DisplayImageOptions.Builder().cacheInMemory(true)
//				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.md_default)
//				.showImageOnFail(R.drawable.md_default)
//				.considerExifParams(true).delayBeforeLoading(0)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//				.bitmapConfig(Bitmap.Config.RGB_565).build();

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.AutoAdView);
        isEnableDots = ta.getBoolean(R.styleable.AutoAdView_isEnableDots, true);
        isEnableAutoPlay = ta.getBoolean(
                R.styleable.AutoAdView_isEnableAutoPlay, true);
        dotsPoistion = ta.getInt(R.styleable.AutoAdView_dotsPoistion, 0);

        background = ta.getDrawable(R.styleable.AutoAdView_ad_background);

        if (background != null) {
            setBackgroundDrawable(background);
        }

        intervalTime = ta.getInt(R.styleable.AutoAdView_intervalTime,
                DEFAULT_TIME);

        ta.recycle();
    }

    public void init() {
        mViewPager = (ViewPager) findViewById(R.id.vai_viewpager);
        dotsLayout = (LinearLayout) findViewById(R.id.va_dots);

        images = new ArrayList<ImageView>();
        int length = datas!=null&&datas.size()!=0?datas.size() + 2:datasResources.size()+2;
        ImageView mViewPagerImageView = null;
        ViewGroup.LayoutParams viewPagerImageViewParams = null;
        for (int i = 0; i < length; i++) {
            mViewPagerImageView = new ImageView(getContext());
            viewPagerImageViewParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mViewPagerImageView.setLayoutParams(viewPagerImageViewParams);
            mViewPagerImageView.setScaleType(ScaleType.FIT_XY);
            images.add(mViewPagerImageView);
        }

        dots = new ArrayList<ImageView>();
        ImageView dotView;
        dotsLayout.removeAllViews();

        for (int i = 0; i < (datas!=null&&datas.size()!=0?datas.size():datasResources.size()); i++) {
            dotView = new ImageView(getContext());
            dotView.setTag(String.valueOf(i));
            dotView.setBackgroundResource(R.drawable.feature_point);
            dotsLayout.addView(dotView);
            dots.add(dotView);
        }

        adAdapter = new AutoAdAdapter(images);
        adAdapter.setPagerOnClick(pagerOnClick);

        mViewPager.setAdapter(adAdapter);
        mViewPager.setOnPageChangeListener(changeListener);
        mViewPager.setCurrentItem(1);

        if (isEnableDots) {
            dotsLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = null;
            params = (RelativeLayout.LayoutParams) dotsLayout.getLayoutParams();
            switch (dotsPoistion) {
                case 0:
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    break;
                case 1:
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case 2:
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    break;
                default:
                    break;
            }
            dotsLayout.setLayoutParams(params);
        } else {
            dotsLayout.setVisibility(View.INVISIBLE);
        }

        if (isEnableAutoPlay) {
            if (!isStart) {
                startPlay();
            }
        } else {
            stopPlay();
        }
    }

    public void startPlay() {
        handler.postDelayed(runnable, intervalTime);
        isStart = true;
    }

    public void stopPlay() {
        handler.removeCallbacks(runnable);
        isStart = false;
    }

    /**
     * viewpager切换监听
     */
    OnPageChangeListener changeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (position == 0) {
//				imageLoader.displayImage(datas.get(datas.size() - 1),images.get(position), options);
                if (datas == null || datas.size() == 0) {
                    images.get(position).setImageResource(datasResources.get(datasResources.size() - 1));
                    finalpositoin = datasResources.size() + 1;
                } else {
                    bitmapHelp.display(images.get(position), datas.get(datas.size() - 1), true);
                    finalpositoin = datas.size() + 1;
                }
            } else if (position == images.size() - 1) {
//				ImageLoader.getInstance().displayImage(datas.get(0),images.get(position), options);
                if (datas == null || datas.size() == 0) {
                    images.get(position).setImageResource(datasResources.get(0));
                    finalpositoin = datasResources.size() + 1;
                } else {
                    bitmapHelp.display(images.get(position), datas.get(0), true);
                    finalpositoin = 0;
                }
            } else {
//				ImageLoader.getInstance().displayImage(datas.get(position - 1),images.get(position), options);
                if (datas == null || datas.size() == 0) {
                    images.get(position).setImageResource(datasResources.get(position - 1));
                    finalpositoin = position + 1;
                } else {
                    bitmapHelp.display(images.get(position), datas.get(position - 1), true);
                    finalpositoin = position + 1;
                }
            }

            int pageIndex = position;
            if (datas == null || datas.size() == 0) {
                if (position == 0) {
                    pageIndex = datasResources.size();
                } else if (position == datasResources.size() + 1) {
                    pageIndex = 1;
                }
            } else {
                if (position == 0) {
                    pageIndex = datas.size();
                } else if (position == datas.size() + 1) {
                    pageIndex = 1;
                }
            }
            if (position != pageIndex) {
                mViewPager.setCurrentItem(pageIndex, false);
                return;
            }
            int count = images.size() - 2;

            if (images != null && count > 0) {
                // int index = (position);
                // String text = index + "/" + count;
                // tv_num.setText(text);
            } else {
                // String text = 0 + " / " + 0;
                // tv_num.setText(text);
            }

            setDots(position - 1);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 0) {

            } else if (arg0 == 1) {
                handler.removeCallbacks(runnable);// 处理鼠标点击item的时候停止轮播
                isStart = false;
            } else if (arg0 == 2) {
                if (!isStart && isEnableAutoPlay) {
                    handler.postDelayed(runnable, intervalTime);
                    isStart = true;
                }

            }
        }
    };

    /**
     * 控制轮播的runnable
     */
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager != null && handler != null) {
                mViewPager.setCurrentItem(finalpositoin);
                handler.postDelayed(runnable, intervalTime);
            }
        }
    };

    /**
     * 控制点的背景
     *
     * @param position
     */
    public void setDots(int position) {
        for (int i = 0; i < (datas!=null&&datas.size()!=0?datas.size():datasResources.size()); i++) {
            if (position == i) {
                dots.get(i).setBackgroundResource(R.drawable.feature_point_cur);
            } else {
                dots.get(i).setBackgroundResource(R.drawable.feature_point);
            }
        }
    }


    /**
     * 拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          