package com.jarvis.tbaseviewlib.bitmap.takephoto;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.takephoto.model.TResult;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.CompressConfig;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.OptionConfig;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.TContextUtils;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.TIntentUtils;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.model.image.ImageItem;
import com.jarvis.tbaseviewlib.utils.TUtils;

import java.io.File;
import java.util.ArrayList;

import static com.jarvis.tbaseviewlib.utils.TUtils.getFormatTimeByCustom;

/**
 * Created by tansheng on 2017/6/23.
 */

public class TPhotoImp implements TPhoto {
    private TContextUtils contextUtils;
    private TPhotoResultCallBack listener;
    private CompressConfig compress;
//    private static TPhotoImp instance;
    /**
     * 配置
     */
    private OptionConfig config;
    /**
     * 拍照输出的图片
     */
    private File outFile;
    /**
     * 拍照时生成的图片路径
     */
    private String fPath;

//    public static TPhotoImp of(Context context,TPhotoResultCallBack listener){
//        if (instance==null){
//            synchronized (TPhotoImp.class){
//                if (instance==null){
//                    instance=new TPhotoImp(context,listener);
//                }
//            }
//        }
//        return instance;
//    }

    public TPhotoImp(Activity activity,TPhotoResultCallBack listener){
        contextUtils =new TContextUtils(activity);
        this.listener=listener;
    }
    public TPhotoImp(Fragment fragment, TPhotoResultCallBack listener){
        contextUtils =new TContextUtils(fragment);
        this.listener=listener;
    }

    /**
     * 设置配置
     * @param config
     */
    @Override
    public void setOptionConfig(OptionConfig config){
        this.config=config;
    }

    @Override
    public void setCompressConfig(CompressConfig compress) {
        this.compress=compress;
    }

    @Override
    public void onPickMuliple(int limt) {
        startActivityForResult(contextUtils,TIntentUtils.intentToPhoto(contextUtils,limt,config),Constant.CODE_CHOICE_PHOTO);
    }

    @Override
    public void onPickGallery() {
        onPickMuliple(1);
//        startActivityForResult(contextUtils,TIntentUtils.intentToPhoto(contextUtils,1,config),Constant.CODE_CHOICE_PHOTO);
    }

    @Override
    public void onPickCamera(File file) {
        outFile=file;
        if (outFile==null){
            // 图片文件名称
            String photoFileName = getFormatTimeByCustom(System.currentTimeMillis(), "yyyyMMdd_HHmmss")+".jpg";
            // 判断SD卡是否可用
            if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
                // 创建一个文件
                outFile = new File(CacheData.getImagesCache() + photoFileName);
            }else {
                TUtils.showToast(contextUtils.getActivity(),contextUtils.getActivity().getResources().getString(R.string.toast_sdcar_err));
                return;
            }
        }
        // 得到创建的文件的uri地址
        Uri outUri = Uri.fromFile(outFile);
        // 得到图片的路径
        fPath = outFile.getAbsolutePath();
        startActivityForResult(contextUtils,TIntentUtils.intentToCamera(outUri),Constant.CODE_CHOICE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.CODE_CHOICE_PHOTO://从相册选择不裁剪
                if (resultCode==Constant.CODE_CHOICE_PHOTO&&data!=null){
                    TResult tResult=new TResult();
                    tResult.setImages((ArrayList<ImageItem>) data.getSerializableExtra(Constant.CODE_RESULT_PHOTO));
                    takeResult(tResult);
                }else {
                    listener.onCancle();
                }
                break;
            case Constant.CODE_CHOICE_CAMERA://从相机选择不裁剪
                if (resultCode==Activity.RESULT_OK){
                    ImageItem imageItem=new ImageItem();
                    imageItem.setSelect(true);
                    imageItem.setImagePath(fPath);
                    TResult tResult=new TResult();
                    tResult.addImage(imageItem );
                    takeResult(tResult);
                }else {
                    listener.onCancle();
                }
                break;
        }

    }

    /**
     * 组合出选择图片的结果
     */       
    private void takeResult(TResult tResult){
        if (compress==null){
            if (tResult.getImages()==null||tResult.getImages().size()<=0){
                listener.onFail();
            }else {
                listener.onSuccess(tResult);
            }
        }else {
            compress.startCompress(tResult, new CompressConfig.CompressCallBackListener() {
                @Override
                public void cancleCompress(String e) {
                    listener.onCancle();
                }

                @Override
                public void errorCompress(String e) {
                    listener.onFail();
                }

                @Override
                public void successCompress(TResult result) {
                    listener.onSuccess(result);
                }
            });
        }
    }


    private void startActivityForResult(TContextUtils contextUtils,Intent intent,int requstCode){
        if (contextUtils.getFragment()!=null){
            contextUtils.getFragment().startActivityForResult(intent,requstCode);
        }else {
            contextUtils.getActivity().startActivityForResult(intent,requstCode);
        }

    }
}
