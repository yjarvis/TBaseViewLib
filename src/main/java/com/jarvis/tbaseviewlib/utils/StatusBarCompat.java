package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * 添加沉浸式效果时，对5.0以下版本做适配效果
 * <p/>
 * 添加沉浸式时，需要使用NoActionBar的主题，并且新增values-v19文件夹，并添加styles
 * <style name="AppTheme"parent="@style/AppBaseTheme">
 * <item name="android:windowTranslucentStatus">true</item>
 * </style>
 * <p/>
 * 最后，在需要实现沉浸式的布局中的根节点上添加
 * android:fitsSystemWindows="true"
 * 注:此方法一定要在setContentView之后执行,最后提一下，
 * 对于5.0由于提供了setStatusBarColor去设置状态栏颜色，但是这个方法不能在主题中设置windowTranslucentStatus属性。所以，可以编写一个value-v21文件夹，里面styles.xml写入
 * <style name="AppTheme"parent="@style/AppBaseTheme">
 * </style>
 */
public class StatusBarCompat {

    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.BASE_1_1)
    public static void compat(Activity activity, int statusColor) {

        // 5.0以上版本的适配
        if (Build.VERSION.SDK_INT >= 21) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }
        // 5.0以下4.4以上版本的适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < 21) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    /**
     * 得到状态栏的高度
     *
     * @param context
     * @return
     * @author tansheng
     */
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
