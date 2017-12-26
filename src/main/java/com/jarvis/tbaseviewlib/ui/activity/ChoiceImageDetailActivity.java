package com.jarvis.tbaseviewlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.takephoto.Constant;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.OptionConfig;
import com.jarvis.tbaseviewlib.bitmap.takephoto.utils.TAlbumUtils;
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.model.image.ImageItem;
import com.jarvis.tbaseviewlib.ui.adapter.AdapterChoiceImageDetail;
import com.jarvis.tbaseviewlib.ui.adapter.AdapterChoiceImageDetail.TextSelectNumber;
import com.jarvis.tbaseviewlib.ui.common.TFragmentActivity;
import com.jarvis.tbaseviewlib.ui.common.TitleBackFragment;
import com.jarvis.tbaseviewlib.utils.TUtils;

import java.util.ArrayList;

/**
 * 图册详情列表界面
 *
 * @author tansheng
 */
public class ChoiceImageDetailActivity extends TFragmentActivity {

    private GridView gridview;
    private TextView scanText;// 浏览
    private TextView okText;// 确定
    private AdapterChoiceImageDetail adapter;
    /**
     * 选择的图册的位置，通过此位置，获取图册中每个图片的对象
     */
    private int positionItem = 0;
    private ArrayList<ImageItem> allList;
    private ArrayList<ImageItem> lastSelectList;
    private OptionConfig config;
    @Override
    protected void onCreate(Bundle arg0) {
        //先执行布局添加，再执行父类中的初始化
        super.onCreate(arg0);
        setContentView(R.layout.activity_choice_image_detail);
        setStatusBarCompat(false);
        Intent intent = getIntent();
        positionItem = intent.getIntExtra("position", 0);
        config= (OptionConfig) getIntent().getSerializableExtra(Constant.PHOTO_OPTION_CONFIG);
        allList=new ArrayList<>();
        initView(true);
        setData();
    }

    @Override
    public void initView(boolean isStatusBar) {
        super.initView(isStatusBar);
        titleBackFragment = new TitleBackFragment().newInstance(titleName, "");
        addTitleFragment(titleBackFragment);

        scanText = (TextView) findViewById(R.id.choice_photodetail_scan);
        okText = (TextView) findViewById(R.id.choice_photodetail_ok);
        gridview = (GridView) findViewById(R.id.choice_photodetail_gridview);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });

        scanText.setOnClickListener(this);
        okText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.choice_photodetail_scan) {
            if (getSelectImages() == null || getSelectImages().size()== 0) {
                TUtils.showToast(context,resources.getString(R.string.error_scaner));
                return;
            }

            //点击预览
            intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra(Constrans.FLAG_IMAGE_SHOW, getSelectImages());
            startActivity(intent);
            overridePendingTransition(R.anim.activity_anim_in, 0);
        } else if (id == R.id.choice_photodetail_ok) {
            Intent intent=new Intent();
            intent.putExtra(Constant.CODE_RESULT_PHOTO,getSelectImages());
            setResult(Constant.CODE_CHOICE_PHOTO,intent);
            finish();
        }
    }

    @Override
    public void setData() {
        allList.addAll(TAlbumUtils.of(this).build().getAllData().get(positionItem).getImageList());
        if (config==null||config.isHaveCache()){
            //每次进来都是上一次选择的状态(默认使用这种方式)
            if (Constant.getLastSelectList()==null){
                lastSelectList=new ArrayList<>();
                Constant.setLastSelectList(lastSelectList);
            }else {
                lastSelectList=Constant.getLastSelectList();
            }
        }else {
            Constant.setLastSelectList(null);
            lastSelectList=new ArrayList<>();
            //每次进来都是未选择状态
            for (int i = 0; i < allList.size(); i++) {
                allList.get(i).setSelect(false);
            }
        }

        adapter = new AdapterChoiceImageDetail(context,allList,lastSelectList, 3, new TextSelectNumber() {
            @Override
            public void onSelect(int count) {
                okText.setText(resources.getString(R.string.choice_image_ok) + "(" + count + ")");
            }
        });
        adapter.setMaxNumber(getIntent().getIntExtra(Constant.MAX_SELECTE,1));
        gridview.setAdapter(adapter);
        okText.setText(resources.getString(R.string.choice_image_ok) + "(" + lastSelectList.size() + ")");
    }

    @Override
    public void requestData(boolean isShow) {

    }

    @Override
    public void showFragment(Fragment fragment) {

    }
    private ArrayList<ImageItem> getSelectImages(){
        Constant.setLastSelectList(adapter.getSelectImages());
        return adapter.getSelectImages();
    }

}
