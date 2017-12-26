package com.jarvis.tbaseviewlib.bitmap.takephoto.utils;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by tansheng on 2017/6/30.
 */

public class TContextUtils {
    private Activity activity;
    private Fragment fragment;

    public TContextUtils(Activity activity){
        this.activity=activity;
    }
    public TContextUtils(Fragment fragment){
        this.fragment=fragment;
        this.activity=fragment.getActivity();
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
