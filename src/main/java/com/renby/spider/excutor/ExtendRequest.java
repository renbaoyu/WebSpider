package com.renby.spider.excutor;

import us.codecraft.webmagic.Request;

public class ExtendRequest extends Request{
	private String parent;

	public ExtendRequest(String url){
		super(url);
	}
	
	public ExtendRequest(String url, String parent){
		super(url);
		this.parent = parent;
	}
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
}
