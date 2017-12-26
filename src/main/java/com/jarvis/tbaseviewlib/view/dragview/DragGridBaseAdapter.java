package com.jarvis.tbaseviewlib.view.dragview;

public interface DragGridBaseAdapter {
	/**
	 * 获得传入的显示数据
	 *@author tansheng  QQ:717549357
	 * @date 2015-11-30 上午10:27:33 
	 * @return  传入的显示数据
	 */
	public Object getData();
	/**
	 * 重新排列数据
	 * @param oldPosition
	 * @param newPosition
	 */
	public void reorderItems(int oldPosition, int newPosition);
	
	
	/**
	 * 设置某个item隐藏
	 * @param hidePosition
	 */
	public void setHideItem(int hidePosition);
	

}
