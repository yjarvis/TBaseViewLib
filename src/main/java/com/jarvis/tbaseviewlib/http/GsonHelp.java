package com.jarvis.tbaseviewlib.http;

import com.google.gson.Gson;

/**
 * 功能说明: Gson操作类(单列模式)
 * 
 * @author 作者：jarvisT
 */
public class GsonHelp {

	public static Gson gson;

	public static Gson newInstance() {
		if (null == gson) {
			synchronized (GsonHelp.class) {
				if (null == gson) {
					gson = new Gson();
				}
			}
		}
		return gson;
	}

	private GsonHelp() {

	}

}
