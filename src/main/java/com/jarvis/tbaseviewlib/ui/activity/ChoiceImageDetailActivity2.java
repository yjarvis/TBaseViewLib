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
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.ui.adapter.AdapterChoiceImageDetail;
import com.jarvis.tbaseviewlib.ui.common.TFragmentActivity;
import com.jarvis.tbaseviewlib.ui.common.TitleBackFragment;
import com.jarvis.tbaseviewlib.utils.AlbumHelpSingle;
import com.jarvis.tbaseviewlib.utils.TUtils;

/**
 * 图册详情列表界面
 *
 * @author tansheng
 */
public class ChoiceImageDetailActivity2 extends TFragmentActivity {

    private GridView gridview;
    private TextView scanText;// 浏览
    private TextView okText;// 确定
    private AdapterChoiceImageDetail adapter;
    /**
     * 选择的图册的位置，通过此位置，获取图册中每个图片的对象
     */
    private int positionItem = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        //先执行布局添加，再执行父类中的初始化
        super.onCreate(arg0);
        setContentView(R.layout.activity_choice_image_detail);
        setStatusBarCompat(false);
        Intent intent = getIntent();
        positionItem = intent.getIntExtra("position", 0);
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
            if (AlbumHelpSingle.getSelectData() == null || AlbumHelpSingle.getSelectData().size() == 0) {
                TUtils.showToast(context,resources.getString(R.string.error_scaner));
                return;
            }

            //点击预览
            intent = new Intent(context, ShowImageActivity.class);
            intent.putStringArrayListExtra(Constrans.FLAG_IMAGE_SHOW, AlbumHelpSingle.getSelectData());
            startActivity(intent);
            overridePendingTransition(R.anim.activity_anim_in, 0);
        } else if (id == R.id.choice_photodetail_ok) {
            /*
			 *这里只需要关闭此界面即可，操作后的所有数据都在AlbumHelper中保存有
			 */
            setResult(Constrans.CODE_CHOICE_PHOTO);
            finish();
        }
    }

    @Override
    public void setData() {
//        adapter = new AdapterChoiceImageDetail(context, positionItem, 3, new TextSelectNumber() {
//            @Override
//            public void onSelect(int count) {
//                okText.setText(resources.getString(R.string.choice_image_ok) + "(" + count + ")");
//            }
//        });
//        gridview.setAdapter(adapter);
        okText.setText(resources.getString(R.string.choice_image_ok) + "(" + AlbumHelpSingle.getSelectData().size() + ")");
    }

    @Override
    public void requestData(boolean isShow) {

    }

    @Override
    public void showFragment(Fragment fragment) {

    }

}
