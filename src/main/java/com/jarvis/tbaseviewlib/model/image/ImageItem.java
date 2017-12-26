package com.jarvis.tbaseviewlib.model.image;

import java.io.Serializable;

/**
 * 一个相册内，每个图片的详细数据
 * @author tansheng
 */
@SuppressWarnings("serial")
public class ImageItem implements Serializable{
	/**图片id*/
	public String imageId;
	/**缩略图的路径*/
	public String thumbnailPath;
	/**原始图片路径*/
	public String imagePath;
	/**是否被选中*/
	public boolean isSelect=false;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}
}
