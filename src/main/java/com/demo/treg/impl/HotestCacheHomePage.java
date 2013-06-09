/*  
 * Copyright (c) 2010-2020 Chongqing Suilong Technology Co. Ltd. All Rights Reserved.  
 *  
 * This software is the confidential and proprietary information of  
 * Founder. You shall not disclose such Confidential Information  
 * and shall use it only in accordance with the terms of the agreements  
 * you entered into with Founder.  
 *  
 */  
package com.demo.treg.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Element;

import com.demo.treg.AbstractCacheHomePage;
import com.demo.treg.CacheHomePageBean;

/**
 * ”最热“首页缓存数据
 * @author Ajian
 * @version Jun 6, 2013 5:12:04 PM
 */
public class HotestCacheHomePage extends AbstractCacheHomePage implements Serializable {
	private static final long serialVersionUID = 4602224672699177549L;

	private HotestCacheHomePage(){super();}
	private static class HotestCacheHomePageHolder{
		private static HotestCacheHomePage impl = new HotestCacheHomePage();
	}
	public static HotestCacheHomePage getInstance(){
		return HotestCacheHomePageHolder.impl;
	}
	
	@Override
	protected String getCacheName() {
		return "TalkEverybody_HomePage_Cache_Hotest";
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void removeCache(int newsId) {
		super.removeCache(newsId);
		CacheHomePageBean bean = null;
		Element ele = null;
		int oldKey=0;
		List<Integer> keys = cache.getKeys();
		Collections.sort(keys);
		//System.out.println("old keys:"+Arrays.toString(keys.toArray()));
		for (int i = 1; i <= keys.size(); i++) {
			if(keys.get(i-1)>i){
				oldKey = keys.get(i-1);
				bean = (CacheHomePageBean) cache.get(oldKey).getObjectValue();
				cache.remove(oldKey);
				ele = new Element(i,bean);
				cache.put(ele);
			}
		}
		/*keys = cache.getKeys();
		Collections.sort(keys);
		System.out.println("new keys:"+Arrays.toString(keys.toArray()));*/
	}
}
