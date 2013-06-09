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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;

/**
 * 数据缓存对象 
 * @author Ajian
 * @version Jun 6, 2013 3:56:07 PM
 */
public class CacheHomePageBean implements Serializable {
	private static final long serialVersionUID = -8855849946596065544L;
	/**
	 * 二进制返回数据的边界分割线
	 */
	public static final String ResponseBoundaryString = "----sui_split_long----";
	
	private Integer newsId;
	private byte[] data;
	
	
	/**
	 * 设置一个文件映射缓存对象
	 * @param newsId
	 * @param title
	 * @param map
	 * @author Ajian
	 * @version Jun 7, 2013 6:07:31 PM
	 */
	public void setFileBean(int newsId,String title,String images,MappedByteBuffer map){
		this.newsId=newsId;
		StringBuilder describe = new StringBuilder();
		describe.append("newsId:").append(this.newsId).append(ResponseBoundaryString);
		describe.append("title:").append(title).append(ResponseBoundaryString);
		describe.append("isImageNews:").append(true).append(ResponseBoundaryString);
		describe.append("images:").append(images).append(ResponseBoundaryString);
		try {
			this.data = describe.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			this.data = describe.toString().getBytes();
		}
		byte[] b = new byte[map.limit()];
		map.get(b);
		byte[] all = new byte[b.length+this.data.length];
		System.arraycopy(this.data, 0, all, 0, this.data.length);
		System.arraycopy(b, 0, all, this.data.length, b.length);
		this.data = all;
		map=null;
	}
	
	/**
	 * 设置文本缓存对象
	 * @param newsId
	 * @param title
	 * @param text
	 * @author Ajian
	 * @version Jun 7, 2013 6:08:20 PM
	 */
	public void setTextBean(int newsId,String title,String text){
		this.newsId=newsId;
		StringBuilder describe = new StringBuilder();
		describe.append("newsId:").append(this.newsId).append(ResponseBoundaryString);
		describe.append("title:").append(title).append(ResponseBoundaryString);
		describe.append("isImageNews:").append(false).append(ResponseBoundaryString);
		describe.append("content:").append(text).append(ResponseBoundaryString);
		try {
			this.data = describe.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			this.data = describe.toString().getBytes();
		}
	}
	
	@Override
	public int hashCode() {
		return newsId;
	}
	@Override
	public boolean equals(Object obj) {
		CacheHomePageBean bean = (CacheHomePageBean) obj;
		if(bean.newsId.equals(this.newsId)){
			return true;
		}
		return false;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public byte[] getData() {
		return data;
	}


}
