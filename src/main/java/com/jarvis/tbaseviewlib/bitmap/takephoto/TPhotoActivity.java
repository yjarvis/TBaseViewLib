package com.jarvis.tbaseviewlib.bitmap.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jarvis.tbaseviewlib.bitmap.takephoto.model.TResult;
import com.jarvis.tbaseviewlib.utils.LogUtils;

/**
 * Created by tansheng on 2017/6/23.
 */
public class TPhotoActivity extends Activity implements PopWindowPhoto.PopPhotoCallBack, TPhoto.TPhotoResultCallBack {
    private TPhoto tPhoto;
    private PopWindowPhoto photoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTPhoto();
        photoDialog=new PopWindowPhoto(this);
        photoDialog.setListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tPhoto.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPhotoDialog(View view){
        photoDialog.showAsDropDown(view,0);
    }

    public TPhoto getTPhoto(){
        if (tPhoto==null){
            tPhoto=new TPhotoImp(this,this);
        }
        return tPhoto;
    }

    @Override
    public void onPhoto(int flag, int requestCode) {

    }

    @Override
    public void onCamera(int flag, int requestCode) {

    }

    @Override
    public void onSuccess(TResult result) {

    }

    @Override
    public void onFail() {
        LogUtils.e("Some exception have happaned when selected picture");
    }

    @Override
    public void onCancle() {
        LogUtils.e("User cancles the selection of picture");
    }
}
