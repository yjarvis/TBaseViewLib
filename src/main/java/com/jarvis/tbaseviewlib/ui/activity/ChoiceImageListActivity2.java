package com.jarvis.tbaseviewlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.constrans.Constrans;
import com.jarvis.tbaseviewlib.model.image.ImageAlbum;
import com.jarvis.tbaseviewlib.ui.adapter.AdapterChoiceImageList;
import com.jarvis.tbaseviewlib.ui.common.TFragmentActivity;
import com.jarvis.tbaseviewlib.ui.common.TitleBackFragment;
import com.jarvis.tbaseviewlib.utils.AlbumHelpSingle;

import java.util.ArrayList;

/**
 * 手机图册列表界面
 * 
 * @author tansheng
 * 
 */
public class ChoiceImageListActivity2 extends TFragmentActivity {

	private ListView listview;
	private AdapterChoiceImageList adapter;
	private AlbumHelpSingle albumHelpSingle;
	/** 本机所有图册数据 */
	private ArrayList<ImageAlbum> allImageList = new ArrayList<ImageAlbum>();
	@Override
	protected void onCreate(Bundle arg0) {
		//先执行布局添加，再执行父类中的初始化
		super.onCreate(arg0);
		setContentView(R.layout.activity_choice_image_list);
		setStatusBarCompat(false);
		// 获取本机所有图册数据
		albumHelpSingle = AlbumHelpSingle.getHelper();
		allImageList = albumHelpSingle.buildAlbums(context);

		initView(true);
		setData();
	}

	@Override
	public void initView(boolean isStatusBar) {
		super.initView(isStatusBar);
		titleBackFragment = new TitleBackFragment().newInstance(resources.getString(R.string.choice_image_title), "");
		addTitleFragment(titleBackFragment);

		listview = (ListView) findViewById(R.id.choice_photolist_listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				intent = new Intent(context, ChoiceImageDetailActivity.class);
				intent.putExtra(Constrans.FLAG_TITLE, adapter.getItem(position));
				intent.putExtra("position", position);// 选择的图册位置
				startActivityForResult(intent, Constrans.CODE_CHOICE_PHOTO);
			}
		});

	}

	@Override
	public void setData() {
		adapter = new AdapterChoiceImageList(context, allImageList);
		listview.setAdapter(adapter);
	}

	@Override
	public void requestData(boolean isShow) {

	}

	@Override
	public void showFragment(Fragment fragment) {

	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		switch (result) {
		case Constrans.CODE_CHOICE_PHOTO:
			finish();
			break;

		default:
			break;
		}
		super.onActivityResult(request, result, data);
	}

}
