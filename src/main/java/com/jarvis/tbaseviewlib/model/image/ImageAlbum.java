package com.jarvis.tbaseviewlib.model.image;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *本机中一个图册的数据
 */
@SuppressWarnings("serial")
public class ImageAlbum implements Serializable{
	/**
	 * 图册id
	 */
	private String id;
	/**图册内的图片数量*/
	public int count;
	/**图册名*/
	public String albumName;
	/**图册中的每个图片对象*/
	public ArrayList<ImageItem> imageList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public ArrayList<ImageItem> getImageList() {
		return imageList;
	}

	public void setImageList(ArrayList<ImageItem> imageList) {
		this.imageList = imageList;
	}
}
