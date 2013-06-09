/*  
 * Copyright (c) 2010-2020 Chongqing Suilong Technology Co. Ltd. All Rights Reserved.  
 *  
 * This software is the confidential and proprietary information of  
 * Founder. You shall not disclose such Confidential Information  
 * and shall use it only in accordance with the terms of the agreements  
 * you entered into with Founder.  
 *  
 */  
package com.demo.treg;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;




/**
 * 首页数据缓存对象
 * @author Ajian
 * @version Jun 5, 2013 3:05:31 PM
 */
public abstract class AbstractCacheHomePage {
	protected static final Logger log = Logger.getLogger(AbstractCacheHomePage.class);
	protected Cache cache;
	
	public AbstractCacheHomePage(){
		try {
			CacheManager m = CacheManager.create(this.getClass().getClassLoader().getResource("").toURI().getPath()+"ehcache.xml");
			if((cache = m.getCache(getCacheName()))==null){
				log.error("ehcache.xml中没有检测到首页对应的"+getCacheName()+"缓存配置.系统将退出，请检查ehcache.xml文件!");
				System.exit(1);
			}
		} catch (Exception e) {
			log.error("初始化首页缓存异常.",e);
		}
	}

	/**
	 * 获取缓存名称
	 * @return
	 * @author Ajian
	 * @version Jun 6, 2013 5:21:03 PM
	 */
	protected abstract String getCacheName();
	
	/**
	 * 添加首页缓存对象到缓存中
	 * @param beans
	 * @author Ajian
	 * @version Jun 6, 2013 4:27:34 PM
	 */
	public synchronized void addCache(CacheHomePageBean... beans) {
		Element ele = null;
		int size = cache.getSize(),i=0;
		for (CacheHomePageBean bean : beans) {
			i++;
			ele = new Element(size+i,bean);
			cache.put(ele);
		}
	}
	
	/**
	 * 根据新闻编号获取对象
	 * @param newsId
	 * @return
	 * @author Ajian
	 * @version Jun 6, 2013 5:24:27 PM
	 */
	@SuppressWarnings("unchecked")
	public CacheHomePageBean getCacheByNewsId(int newsId){
		for (int key : (List<Integer>)cache.getKeys()) {
			if(((CacheHomePageBean)cache.get(key).getObjectValue()).hashCode()==newsId){
				return (CacheHomePageBean)cache.get(key).getObjectValue();
			}
		}
		return null;
	}

	/**
	 * 根据编号获取对象
	 * @param number
	 * @return
	 * @author Ajian
	 * @version Jun 6, 2013 5:25:58 PM
	 */
	public CacheHomePageBean getCacheByNumber(int number) {
		int key = cache.getSize()-(number-1);
		Element ele = null;
		if((ele=cache.get(key))!=null){
			return (CacheHomePageBean) ele.getObjectValue();
		}else{
			return getCacheByNumber(number-1);
		}
	}
	
	/**
	 * 删除缓存bean，如果存在的话
	 * @param newsId
	 * @author Ajian
	 * @version Jun 7, 2013 10:39:21 AM
	 */
	@SuppressWarnings("unchecked")
	public void removeCache(int newsId){
		int k = 0;
		for (int key : (List<Integer>)cache.getKeys()) {
			if(((CacheHomePageBean)cache.get(key).getObjectValue()).hashCode()==newsId){
				k = key;
				break;
			}
		}
		if(k>0){
			cache.remove(k);
		}
	}
	
}
