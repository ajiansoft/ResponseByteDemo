package com.demo.module;


public class News implements java.io.Serializable {
	private static final long serialVersionUID = 5785967012726946627L;
	private Integer newsId;
	private Integer user;
	private String title;
	private String images;
	private String content;

	// Constructors

	/** default constructor */
	public News() {
	}
	public News(Integer newsId) {
		super();
		this.newsId = newsId;
	}
 
	public Integer getNewsId() {
		return this.newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}