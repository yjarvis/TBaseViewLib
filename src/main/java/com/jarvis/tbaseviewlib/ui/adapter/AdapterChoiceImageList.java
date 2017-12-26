package com.jarvis.tbaseviewlib.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.bitmap.BitmapHelp;
import com.jarvis.tbaseviewlib.model.image.ImageAlbum;
import com.jarvis.tbaseviewlib.utils.AlbumShow;

import java.util.ArrayList;

/**
 * QQ发表说说界面——>从手机相册中选择界面的adapter
 * 
 * @author tansheng
 * 
 */
public class AdapterChoiceImageList extends BaseAdapter {

	private Context context;
	private ArrayList<ImageAlbum> list;
	private AlbumShow albumRes;
	private BitmapHelp bitmapHelp;

	public AdapterChoiceImageList(Context context,ArrayList<ImageAlbum> list) {
		this.context = context;
		this.list=list;
		bitmapHelp=new BitmapHelp(context);
		albumRes=new AlbumShow();
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position).albumName;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		HolderView holderView = null;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_choice_photolist_list, null);
			holderView.icon = (ImageView) convertView.findViewById(R.id.adapter_choice_photolist_icon);
			holderView.name = (TextView) convertView.findViewById(R.id.adapter_choice_photolist_name);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		
		if (list.get(position).imageList!=null) {
			String thumbPath=list.get(position).imageList.get(0).thumbnailPath;
			String sourcePath=list.get(position).imageList.get(0).imagePath;
			//通过判断是否有缩略图来显示
//			albumRes.disPlayBitmap(context, holderView.icon, thumbPath, sourcePath);
			bitmapHelp.display(holderView.icon,sourcePath,true);
		}else {
			holderView.icon.setImageBitmap(null);
		}
		//图册中图片总数
		holderView.name.setText(list.get(position).albumName+"("+list.get(position).count+")");
		
		
		return convertView;
	}

	private class HolderView {
		private ImageView icon;
		private TextView name;
	}

	

}
