package com.jarvis.tbaseviewlib.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.BitmapHelp;
import com.jarvis.tbaseviewlib.view.scaleview.ScaleView;
import com.jarvis.tbaseviewlib.view.scaleview.ScaleViewAttacher;
/**
 * 显示大图
 * @author tansheng  QQ:717549357
 * @date 2015-11-30 下午2:30:19
 */
public class ShowImageFragment extends Fragment {

    private ScaleView scaleView;
    private String maxImgUrl;
    private BitmapHelp bitmapHelp;

    public static ShowImageFragment newInstance(String maxImgUrl) {
        ShowImageFragment fragment = new ShowImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maxImgUrl", maxImgUrl);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            maxImgUrl = bundle.getString("maxImgUrl");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_showimage, null);
        bitmapHelp=new BitmapHelp(getActivity());
        initView(view);
        setData();
        return view;
    }

    private void initView(View view) {

        scaleView = (ScaleView) view.findViewById(R.id.showimage_scaleView);
        scaleView.setOnScaleTapListener(new ScaleViewAttacher.OnScaleTapListener() {
            @Override
            public void onScaleTap(View view, float x, float y) {
                // 单击时，返回
                getActivity().finish();
            }
        });
    }

    private void setData() {
    	bitmapHelp.display(scaleView,maxImgUrl,true);
    }

}
