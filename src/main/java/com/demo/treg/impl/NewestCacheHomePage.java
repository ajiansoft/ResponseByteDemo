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

import com.demo.treg.AbstractCacheHomePage;

/**
 * ”最新“首页缓存数据
 * @author Ajian
 * @version Jun 6, 2013 5:12:04 PM
 */
public class NewestCacheHomePage extends AbstractCacheHomePage implements Serializable {
	private static final long serialVersionUID = 4602224672699177549L;

	private NewestCacheHomePage(){super();}
	private static class NewestCacheHomePageHolder{
		private static NewestCacheHomePage impl = new NewestCacheHomePage();
	}
	public static NewestCacheHomePage getInstance(){
		return NewestCacheHomePageHolder.impl;
	}
	
	@Override
	protected String getCacheName() {
		return "TalkEverybody_HomePage_Cache_Newest";
	}
}
