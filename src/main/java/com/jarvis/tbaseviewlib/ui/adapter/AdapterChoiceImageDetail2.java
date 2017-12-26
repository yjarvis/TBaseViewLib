package com.jarvis.tbaseviewlib.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.model.image.ImageItem;
import com.jarvis.tbaseviewlib.utils.AlbumHelpSingle;
import com.jarvis.tbaseviewlib.utils.AlbumShow;
import com.jarvis.tbaseviewlib.utils.TUtils;

import java.util.ArrayList;

/**
 * QQ发表说说界面——>选择某个相册后，里面的全部图片数据界面
 * 
 * @author tansheng
 * 
 */
public class AdapterChoiceImageDetail2 extends BaseAdapter {

	private Context context;
	private TextSelectNumber listener;
	private AlbumShow albumRes;
	private AlbumHelpSingle albumHelpSingle;
	/** 图册中所有图片对象 */
	private ArrayList<ImageItem> allList;
	/** 已经选中的图片数量 */
	private int selectNum = 0;
	/** 动态改变图片的宽高，使图片大小适配屏幕 */
	private LayoutParams params;
	/** 每个item的宽度 */
	int widthItem;

	public AdapterChoiceImageDetail2(Context context, int positionItem, int numColumns, TextSelectNumber listener) {
		this.context = context;
		this.listener = listener;
		albumRes = new AlbumShow();
		//获取选中的图层所有图片对象
		albumHelpSingle = AlbumHelpSingle.getHelper();
		this.allList = albumHelpSingle.buildAlbums(context).get(positionItem).imageList;
		// 计算每列的宽度(屏幕宽-列间距*列数-父组件左右边距)
		widthItem = (TUtils.getScreenWidth((Activity) context) - TUtils.dipTopx(context, 5) * numColumns - TUtils.dipTopx(context, 20)) / numColumns;
		params = new LayoutParams(widthItem, widthItem);
		if (listener != null) {
			listener.onSelect(selectNum);
		}
		this.selectNum= AlbumHelpSingle.getSelectData().size();

	}

	@Override
	public int getCount() {
		return allList != null ? allList.size() : 0;
	}

	@Override
	public String getItem(int position) {
		return "";
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_choice_photolist_detail, null);
			holderView.layout = (FrameLayout) convertView.findViewById(R.id.adapter_choice_photodetail_layout);
			holderView.icon = (ImageView) convertView.findViewById(R.id.adapter_choice_photodetail_icon);
			holderView.select = (RelativeLayout) convertView.findViewById(R.id.adapter_choice_photodetail_select);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.layout.setLayoutParams(params);

		final ImageItem data = allList.get(position);

		if (allList.get(position) != null) {
			// 通过判断是否有缩略图来显示
			albumRes.disPlayBitmap(context, holderView.icon, data.thumbnailPath, data.imagePath);
		} else {
			holderView.icon.setImageBitmap(null);
		}

		// 是否显示选中效果
		if (data.isSelect) {
			holderView.select.setVisibility(View.VISIBLE);
		} else {
			holderView.select.setVisibility(View.GONE);
		}

		holderView.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holderView.select.isShown()) {
					// 不选择
					data.isSelect = false;
					holderView.select.setVisibility(View.GONE);
					AlbumHelpSingle.removeSelectData(data.imagePath);
					selectNum--;
					if (listener != null) {
						listener.onSelect(selectNum);
					}
				} else {
					// 选择
					if (selectNum < AlbumHelpSingle.getMaxSelectNumber()) {
						data.isSelect = true;
						holderView.select.setVisibility(View.VISIBLE);
						AlbumHelpSingle.putSelectData(data.imagePath, data);
						selectNum++;
						if (listener != null) {
							listener.onSelect(selectNum);
						}
					} else {
						TUtils.showToast(context, TUtils.getFormatString(context.getResources().getString(R.string.toast_choice_image_max_number),String.valueOf(AlbumHelpSingle.getMaxSelectNumber())));
					}
				}

			}
		});

		return convertView;
	}

	private class HolderView {
		private ImageView icon;
		private RelativeLayout select;
		private FrameLayout layout;// 控制每个item的大小
	}

	public interface TextSelectNumber {
		public void onSelect(int count);
	}


	
	
}
