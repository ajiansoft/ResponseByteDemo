/*  
 * Copyright (c) 2010-2020 Chongqing Suilong Technology Co. Ltd. All Rights Reserved.  
 *  
 * This software is the confidential and proprietary information of  
 * Founder. You shall not disclose such Confidential Information  
 * and shall use it only in accordance with the terms of the agreements  
 * you entered into with Founder.  
 *  
 */  
package com.demo.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Test {
	
	@org.junit.Test
	public void homepageTest(){
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = null;
		try {
			uri = new URL("http://localhost:8080/ResponseByteDemo/servlet/MainServlet?number=1");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) uri.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", CHARSET);
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 得到响应码
		int responseCode = 0;
		try {
			responseCode = conn.getResponseCode();
			System.out.println("响应码："+responseCode);
			
			//读取分割标志下标
			long s = System.nanoTime();
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			byte[] b = new byte[1024*5];
			int index = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while((index = in.read(b))!=-1){
				out.write(b);
			}
			byte[] stream = out.toByteArray();
			
			byte[] spiltByte = "----sui_split_long----".getBytes();
			BoundaryFinder bf = new BoundaryFinder(spiltByte);
			int pos=0;index = 0;
			byte[] str = null;
			String v = null;
			Map<String, Object> responseValue = new HashMap<String, Object>();
			while((index=bf.indexOf(stream,pos>0?pos+spiltByte.length:0))>0){
				str = new byte[pos>0?index-spiltByte.length-pos:index];
				System.arraycopy(stream, pos>0?pos+spiltByte.length:0, str, 0, str.length);
				pos=index;
				v = new String(str,"UTF-8");
				responseValue.put(v.substring(0,v.indexOf(":")), v.substring(v.indexOf(":")+1));
			}
			for (Entry<String, Object> en : responseValue.entrySet()) {
				System.out.println(en.getKey()+"->"+en.getValue());
			}
			if(responseValue.get("isImageNews").equals("true")){
				if(pos+spiltByte.length<stream.length){
					str = new byte[stream.length-spiltByte.length-pos];
					System.arraycopy(stream, pos+spiltByte.length, str, 0, str.length);
					//将图片输出到本地设备
					BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream("F:\\"+responseValue.get("images").toString().split(",")[0]));
			    	bo.write(str);
			    	bo.flush();bo.close();
				}
			}else{
				System.out.println("content:"+responseValue.get("content"));
			}
			System.out.println("解析响应流用时："+(System.nanoTime()-s)+"纳秒.");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		conn.disconnect();
	}
}

/**
 * 本class来源于fastupload开源组件
 * @author Ajian
 * @version Jun 8, 2013 5:50:11 PM
 */
class BoundaryFinder {

	private byte[] boundary;

	private int[] charTable;

	private int[] offsetTable;

	public BoundaryFinder(byte[] boundary) {
		super();
		this.boundary = boundary;
		charTable = makeCharTable(boundary);
		offsetTable = makeOffsetTable(boundary);
	}
	
	public int getBoundaryLength() {
		return boundary.length;
	}
 
	/**
	 * Returns the index within this string of the first occurrence of the
	 * specified substring. If it is not a substring, return -1.
	 * 
	 * @param buffer
	 *            The byte buffer to be scanned
	 
	 * @return The start index of the boundary
	 */
	public int indexOf(byte[] buffer) {
		return indexOf(buffer, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of the
	 * specified substring. If it is not a substring, return -1.
	 * 
	 * @param text
	 *            The string to be scanned
	 
	 * @return The start index of the boundary
	 */
	public int indexOf(byte[] buffer, int start) {
		return indexOf(buffer, start, buffer.length);
	}

	/**
	 * Returns the index within this string of the first occurrence of the
	 * specified substring. If it is not a substring, return -1.
	 * 
	 * @param buffer
	 *            The byte buffer to be scanned
 
	 * @return The start index of the boundary
	 */
	public int indexOf(byte[] buffer, int start, int end) {
		if (boundary.length == 0) {
			return 0;
		}
		for (int i = boundary.length - 1 + start, j; i < end;) {
			for (j = boundary.length - 1; boundary[j] == buffer[i]; --i, --j) {
				if (j == 0) {
					return i;
				}
			}
			// i += needle.length - j; // For naive method
			int offset = Math.max(offsetTable[boundary.length - 1 - j], charTable[buffer[i] < 0 ? buffer[i] + 256 : buffer[i]]);
			i += offset;
		}
		return -1;
	}

	/**
	 * Makes the jump table based on the mismatched character information.
	 */
	private int[] makeCharTable(byte[] needle) {
		final int ALPHABET_SIZE = 256;
		int[] table = new int[ALPHABET_SIZE];
		for (int i = 0; i < table.length; ++i) {
			table[i] = needle.length;
		}
		for (int i = 0; i < needle.length - 1; ++i) {
			table[needle[i]] = needle.length - 1 - i;
		}
		return table;
	}

	/**
	 * Makes the jump table based on the scan offset which mismatch occurs.
	 */
	private int[] makeOffsetTable(byte[] needle) {
		int[] table = new int[needle.length];
		int lastPrefixPosition = needle.length;
		for (int i = needle.length - 1; i >= 0; --i) {
			if (isPrefix(needle, i + 1)) {
				lastPrefixPosition = i + 1;
			}
			table[needle.length - 1 - i] = lastPrefixPosition - i + needle.length - 1;
		}
		for (int i = 0; i < needle.length - 1; ++i) {
			int slen = suffixLength(needle, i);
			table[slen] = needle.length - 1 - i + slen;
		}
		return table;
	}

	/**
	 * Is needle[p:end] a prefix of needle?
	 */
	private boolean isPrefix(byte[] needle, int p) {
		for (int i = p, j = 0; i < needle.length; ++i, ++j) {
			if (needle[i] != needle[j]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the maximum length of the substring ends at p and is a suffix.
	 */
	private int suffixLength(byte[] needle, int p) {
		int len = 0;
		for (int i = p, j = needle.length - 1; i >= 0 && needle[i] == needle[j]; --i, --j) {
			len += 1;
		}
		return len;
	}
	
	public static void main(String[] args) {
		byte[] boundary = "---------------------------194760164713951335551783200939".getBytes();
		byte[] boundary2 = new byte[boundary.length];
		BoundaryFinder bf = new BoundaryFinder(boundary);
		byte[] text= ("-----------------------------194760164713951335551783200939\r\n" +
				"Content-Disposition: form-data; name=\"_text1.text1\"\r\n" +
				"123123123123dfdfsdfsdafsdaffdafasfa0939ttttttttttttttttttttertertqweqweqwrrrrr9391112235551783200939\r\n" +
				"-----------------------------194760164713951335551783200939--").getBytes();
		int i = bf.indexOf(text, 20);
		System.arraycopy(text, i, boundary2, 0, boundary.length);
		System.out.println("index: " + i  + " " + new String(boundary2) );
	}
}

