/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jarvis.tbaseviewlib.view.wheelview;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The simple Array wheel adapter.
 */
public class AbStringWheelAdapter implements AbWheelAdapter {
	
	/** The default items length. */
	public static final int DEFAULT_LENGTH = -1;
	// items
	/** The items. */
	private List<String> items;
	// length
	/** The length. */
	private int length = -1;
	
	/**
	 * Constructor.
	 *
	 * @param items the items
	 * @param length the max items length
	 */
	public AbStringWheelAdapter(List<String> items, int length) {
		this.items = items;
		this.length = length;
	}
	
	/**
	 * Constructor.
	 *
	 * @param items the items
	 */
	public AbStringWheelAdapter(List<String> items) {
		this(items, DEFAULT_LENGTH);
	}
	

	/**
	 * 描述：TODO.
	 *
	 * @param index the index
	 * @return the item
	 * @see com.yuexh.support.wheelview.ab.view.wheel.AbWheelAdapter#getItem(int)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:49
	 * @version v1.0
	 */
	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return null;
	}

	/**
	 * 描述：TODO.
	 *
	 * @return the items count
	 * @see com.yuexh.support.wheelview.ab.view.wheel.AbWheelAdapter#getItemsCount()
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:49
	 * @version v1.0
	 */
	@Override
	public int getItemsCount() {
		return items.size();
	}

	/**
	 * 描述：TODO.
	 *
	 * @return the maximum length
	 * @see com.yuexh.support.wheelview.ab.view.wheel.AbWheelAdapter#getMaximumLength()
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:49
	 * @version v1.0
	 */
	@Override
	public int getMaximumLength() {
		if(length!=-1){
			return length;
		}
		int maxLength = 0;
		for(int i=0;i<items.size();i++){
			String cur = items.get(i);
			int l = strLength(cur);
			if(maxLength<l){
				maxLength = l;
			}
		}
		return maxLength;
	}
	
	
	
	 /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    private  boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    
	 /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return  字符串的长度（中文字符计2个）
     */
    private  int strLength(String str) {
         int valueLength = 0;
         String chinese = "[\u0391-\uFFE5]";
         if(!isEmpty(str)){
             //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
             for (int i = 0; i < str.length(); i++) {
                 //获取一个字符
                 String temp = str.substring(i, i + 1);
                 //判断是否为中文字符
                 if (temp.matches(chinese)) {
                     //中文字符长度为2
                     valueLength += 2;
                 } else {
                     //其他字符长度为1
                     valueLength += 1;
                 }
             }
         }
         return valueLength;
     }

}
