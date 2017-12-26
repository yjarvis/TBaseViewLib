/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.jarvis.tbaseviewlib.view.xlistview;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;


public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private TextView mLastUpdateTextView;
	private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long mLastUpdateTime = -1;
	private int mState = STATE_NORMAL;
	private final static String KEY_SharedPreferences = "xlist_classic_last_update";

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	/**箭头动画持续时间*/
	private final int ROTATE_ANIM_DURATION = 180;
	/**正常状态*/
	public final static int STATE_NORMAL = 0;
	/**准备状态*/
	public final static int STATE_READY = 1;
	/**刷新中状态*/
	public final static int STATE_REFRESHING = 2;
	/**刷新完成*/
	public final static int STATE_COMPLETE = 3;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
		mLastUpdateTextView = (TextView) findViewById(R.id.xlistview_header_time);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		setLastUpdateTimeRelateObject(context);
	}

	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			
			mProgressBar.setVisibility(View.VISIBLE);
		} else if(state == STATE_COMPLETE) {
			 // update last update time
	        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
	        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
	            mLastUpdateTime = new Date().getTime();
	            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
	        }
	        mProgressBar.setVisibility(View.GONE);
	        mHintTextView.setText(R.string.xlistview_header_hint_succeed);
		} else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			mShouldShowLastUpdate = true;
	        tryUpdateLastUpdateTime();
	        mLastUpdateTimeUpdater.start();
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
				mShouldShowLastUpdate = true;
		        tryUpdateLastUpdateTime();
		        mLastUpdateTimeUpdater.start();
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			mShouldShowLastUpdate = false;
			tryUpdateLastUpdateTime();
	        mLastUpdateTimeUpdater.stop();	    
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
	
	
	 private String mLastUpdateTimeKey;
	 private boolean mShouldShowLastUpdate = true;
	/**
     * Specify the last update time by this key string
     *
     * @param key
     */
   public void setLastUpdateTimeKey(String key) {
       if (TextUtils.isEmpty(key)) {
           return;
       }
       mLastUpdateTimeKey = key;
   }

   /**
    * Using an object to specify the last update time.
    *
    * @param object
    */
   public void setLastUpdateTimeRelateObject(Object object) {
       setLastUpdateTimeKey(object.getClass().getName());
   }
   
   private void tryUpdateLastUpdateTime() {
       if (TextUtils.isEmpty(mLastUpdateTimeKey) || !mShouldShowLastUpdate) {
           mLastUpdateTextView.setVisibility(GONE);
       } else {
           String time = getLastUpdateTime();
           if (TextUtils.isEmpty(time)) {
               mLastUpdateTextView.setVisibility(GONE);
           } else {
               mLastUpdateTextView.setVisibility(VISIBLE);
               mLastUpdateTextView.setText(time);
           }
       }
   }

   private String getLastUpdateTime() {

       if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
           mLastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(mLastUpdateTimeKey, -1);
       }
       if (mLastUpdateTime == -1) {
           return null;
       }
       long diffTime = new Date().getTime() - mLastUpdateTime;
       int seconds = (int) (diffTime / 1000);
       if (diffTime < 0) {
           return null;
       }
       if (seconds <= 0) {
           return null;
       }
       StringBuilder sb = new StringBuilder();
       sb.append(getContext().getString(R.string.xlistview_last_update));

       if (seconds < 60) {
           sb.append(seconds + getContext().getString(R.string.xlistview_seconds_ago));
       } else {
           int minutes = (seconds / 60);
           if (minutes > 60) {
               int hours = minutes / 60;
               if (hours > 24) {
                   Date date = new Date(mLastUpdateTime);
                   sb.append(sDataFormat.format(date));
               } else {
                   sb.append(hours + getContext().getString(R.string.xlistview_hours_ago));
               }

           } else {
               sb.append(minutes + getContext().getString(R.string.xlistview_minutes_ago));
           }
       }
       return sb.toString();
   }
   
   private class LastUpdateTimeUpdater implements Runnable {

       private boolean mRunning = false;

       private void start() {
           if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
               return;
           }
           mRunning = true;
           run();
       }

       private void stop() {
           mRunning = false;
           removeCallbacks(this);
       }

       @Override
       public void run() {
           tryUpdateLastUpdateTime();
           if (mRunning) {
               postDelayed(this, 1000);
           }
       }
   }

}
