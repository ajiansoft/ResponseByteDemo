/*  
 * Copyright (c) 2010-2020 Chongqing Suilong Technology Co. Ltd. All Rights Reserved.  
 *  
 * This software is the confidential and proprietary information of  
 * Founder. You shall not disclose such Confidential Information  
 * and shall use it only in accordance with the terms of the agreements  
 * you entered into with Founder.  
 *  
 */  
package com.demo.init;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.demo.module.News;
import com.demo.treg.CacheHomePageBean;
import com.demo.treg.impl.HotestCacheHomePage;
import com.demo.treg.impl.NewestCacheHomePage;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 3226149031114318371L;
	private static final Logger log = Logger.getLogger(InitServlet.class);

	@Override
	public void init() throws ServletException {
		
		List<News> list = new ArrayList<News>();
		for (int i=0;i<100;i++) {
			News news = new News();
			news.setNewsId((int)Math.random()*10000+i);
			news.setUser(1);
			news.setTitle("this response is byte array!");
			news.setImages(news.getNewsId()%2==0?"abc.jpg,cdb,jpg,dddd.jpg":null);
			news.setContent("try it! the id is "+i);
			list.add(news);
		}
		
		List<CacheHomePageBean> cache = new ArrayList<CacheHomePageBean>(18*5);
		CacheHomePageBean bean = null;
		File file = null;
		MappedByteBuffer fileMap = null;
		String imgs = null;
		News news = null;
		for (int i=list.size()-1;i>=0;i--) {
			bean = new CacheHomePageBean();
			news = list.get(i);
			imgs = news.getImages();
			if(imgs==null){
				bean.setTextBean(news.getNewsId(), news.getTitle(), news.getContent());
			}else{
				file = new File("E:\\picture\\"+imgs.split(",")[0]);
				try {
					fileMap = new RandomAccessFile(file,"r").getChannel().map(MapMode.READ_ONLY, 0, file.length());
					bean.setFileBean(news.getNewsId(), news.getTitle(), imgs, fileMap);
				} catch (FileNotFoundException e) {
					log.warn("id为"+news.getNewsId()+"的news对象的image路径下没有发现文件.",e);
					continue;
				} catch (IOException e) {
					log.warn("id为"+news.getNewsId()+"的news对象内存映射文件时异常.",e);
					continue;
				} finally{
					fileMap = null;
				}
			}
			cache.add(bean);
		}
		NewestCacheHomePage.getInstance().addCache(cache.toArray(new CacheHomePageBean[]{}));
		HotestCacheHomePage.getInstance().addCache(cache.toArray(new CacheHomePageBean[]{}));
	}

}
