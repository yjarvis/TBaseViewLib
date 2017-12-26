package com.jarvis.tbaseviewlib.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.model.image.ImageItem;
import com.jarvis.tbaseviewlib.ui.activity.ChoiceImageListActivity;
import com.jarvis.tbaseviewlib.utils.AlbumHelpSingle;

import java.util.ArrayList;

/**
 * 功能说明：相册、拍照选择弹出窗
 *
 * @author 作者：jarvisT
 * @date 2015-1-26 下午2:32:28
 */
public class PopWindowPhoto implements OnDismissListener, OnClickListener {
    private Context context;
    private PopupWindow popupWindow;
    private View view;

    private TextView photo;
    private TextView camera;
    private TextView cancle;
    private TextView viewBg;
    private LinearLayout layout;
    private PopPhotoCallBack listener;

    /**
     * 选择相册后返回标记
     */
    public static final int CODE_CHOICE_PHOTO = Constrans.CODE_CHOICE_PHOTO;
    /**
     * 选择拍照后返回标记
     */
    public static final int CODE_CHOICE_CAMERA = Constrans.CODE_CHOICE_CAMERA;

    /**
     * 默认设置图册选择图片的最大数为6
     */
    private final int MAXSELECTNUMBER = 6;
    /**
     * 是否使用自定义的调用相册的方法
     */
    private boolean isPhoto = true;
    /**
     * 是否使用自定义的调用相册的方法
     */
    private boolean isCamera = true;
    /**
     * 弹窗位置的入口标记
     */
    private int flag = 1;

    public interface PopPhotoCallBack {

        /**
         * 点击相册
         *
         * @param flag        弹窗点击的入口标记
         * @param requestCode 返回跳转时的参数
         * @author tansheng  QQ:717549357
         * @date 2015-11-27 下午3:29:45
         */
        public void onPhoto(int flag, int requestCode);

        /**
         * 点击拍照
         *
         * @param flag        弹窗点击的入口标记
         * @param requestCode 返回跳转时的参数
         * @author tansheng  QQ:717549357
         * @date 2015-11-27 下午3:29:45
         */
        public void onCamera(int flag, int requestCode);
    }

    public PopWindowPhoto(Context context) {
        this.init(context, MAXSELECTNUMBER, null);
    }

    public PopWindowPhoto(Context context, int maxSelectNumber) {
        this.init(context, maxSelectNumber, null);
    }

    public PopWindowPhoto(Context context, PopPhotoCallBack listener) {
        this.init(context, MAXSELECTNUMBER, listener);
    }

    public PopWindowPhoto(Context context, int maxSelectNumber, PopPhotoCallBack listener) {
        this.init(context, maxSelectNumber, listener);
    }

    private void init(Context context, int maxSelectNumber, PopPhotoCallBack listener) {
        this.context = context;
        this.listener = listener;
        AlbumHelpSingle.setMaxSelectNumber(maxSelectNumber);
        view = LayoutInflater.from(context).inflate(R.layout.pop_choice_pic, null);

        photo = (TextView) view.findViewById(R.id.pop_chocie_pic_photo);
        camera = (TextView) view.findViewById(R.id.pop_chocie_pic_camera);
        cancle = (TextView) view.findViewById(R.id.pop_chocie_pic_cancel);
        viewBg = (TextView) view.findViewById(R.id.pop_bg);
        layout = (LinearLayout) view.findViewById(R.id.pop_layout);

        photo.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancle.setOnClickListener(this);
        viewBg.setOnClickListener(this);

        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 设置popwindow的动画效果
        // popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }

    // 当popWindow消失时响应
    @Override
    public void onDismiss() {
    }

    /**
     * 弹窗显示的位置
     */
    public void showAsDropDown(View view, int flag) {
        this.flag = flag;
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        setBackgroundBlack(null, 0);

        Animation animationTran = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animationTran.setDuration(250);
        Animation animationAlpha = new AlphaAnimation(0, 1);
        animationAlpha.setDuration(250);
        viewBg.startAnimation(animationAlpha);
        layout.startAnimation(animationTran);
    }

    /**
     * 控制背景变暗 0变暗 1变亮
     */
    private void setBackgroundBlack(View view, int what) {
        if (view == null) {
            return;
        }

        switch (what) {
            case 0:
                view.setVisibility(View.VISIBLE);
                break;
            case 1:
                view.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 消除弹窗
     */
    public void dissmiss() {
        Animation animationTran = new TranslateAnimation(0, 0, 0, layout.getHeight());
        animationTran.setDuration(200);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                popupWindow.dismiss();

            }
        }, 200);
        layout.startAnimation(animationTran);
        Animation animationAlpha = new AlphaAnimation(1, 0);
        animationAlpha.setDuration(200);
        viewBg.startAnimation(animationAlpha);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pop_chocie_pic_photo) {
            //设置对应的入口flag
            AlbumHelpSingle.setFlag(flag);
            // 打开相册
            if (isPhoto) {
                Intent intent = new Intent(context, ChoiceImageListActivity.class);
                ((Activity) context).startActivityForResult(intent, CODE_CHOICE_PHOTO);
            } else {
                if (listener != null) {
                    listener.onPhoto(CODE_CHOICE_PHOTO, flag);
                }
            }
        } else if (id == R.id.pop_chocie_pic_camera) {
            // 打开相机
            if (isCamera) {
                AlbumHelpSingle.choiceCarmera((Activity) context, CacheData.getImagesCache(), CODE_CHOICE_CAMERA, false, flag);
            } else {
                if (listener != null) {
                    listener.onCamera(CODE_CHOICE_CAMERA, flag);
                }
            }
        }
        dissmiss();
    }

    /**
     * 设置是否打开自定义的图册
     *
     * @param isPhoto
     * @author tansheng QQ:717549357
     * @date 2015-11-27 下午2:07:54
     */
    public void setTPhoto(boolean isPhoto) {
        this.isPhoto = isPhoto;
    }

    /**
     * 设置是否打开自定义的图册
     *
     * @param isCamera
     * @author tansheng QQ:717549357
     * @date 2015-11-27 下午2:07:54
     */
    public void setTCamera(boolean isCamera) {
        this.isCamera = isCamera;
    }

    /**
     * 是否显示手机相册选项
     * @param isShow
     */
    public void setShowPhoto(boolean isShow){
        if (isShow){
            photo.setVisibility(View.VISIBLE);
        }else {
            photo.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示拍照选项
     * @param isShow
     */
    public void setShowCamera(boolean isShow){
        if (isShow){
            camera.setVisibility(View.VISIBLE);
        }else {
            camera.setVisibility(View.GONE);
        }
    }

    /**
     * 放入选择的图片数据
     *
     * @param key
     * @param data 选中的图片对象，拍照时传null
     * @author tansheng QQ:717549357
     * @date 2015-11-27 下午2:47:09
     */
    public static void putSelectData(String key, ImageItem data) {
        AlbumHelpSingle.putSelectData(key, data);
    }

    /**
     * 获得选择的图片数据
     *
     * @return
     */
    public static ArrayList<String> getSelectData() {
        return AlbumHelpSingle.getSelectData();
    }

    /**
     * 获得照相的图片数据
     *
     * @return
     */
    public static String getPath() {
        return AlbumHelpSingle.getPath();
    }

    /**
     * 是否存在拍照文件
     * @return
     */
    public static boolean isHaveFile(String path) {
        return AlbumHelpSingle.isHaveFile(path);
    }

    /**
     * 得到当前选择显示入口的标记
     * @return
     */
    public int getFlag() {
        return flag;
    }
}
