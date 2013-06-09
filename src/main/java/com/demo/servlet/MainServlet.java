/*  
 * Copyright (c) 2010-2020 Chongqing Suilong Technology Co. Ltd. All Rights Reserved.  
 *  
 * This software is the confidential and proprietary information of  
 * Founder. You shall not disclose such Confidential Information  
 * and shall use it only in accordance with the terms of the agreements  
 * you entered into with Founder.  
 *  
 */  
package com.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.treg.CacheHomePageBean;
import com.demo.treg.impl.NewestCacheHomePage;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = -5324975369293945553L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 请求URL：
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ns = request.getParameter("number");
		int number = Integer.parseInt(ns);
		//从缓存中获取数据
		CacheHomePageBean bean = NewestCacheHomePage.getInstance().getCacheByNumber(number);
		response.getOutputStream().write(bean.getData());
	}

}
