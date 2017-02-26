package com.renby.spider.excutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.renby.spider.entity.TaskContentRule;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;

/**
 * 扩展页面上下文，增加原始字节流、返回类型、返回字符集信息
 * @author Administrator
 *
 */
public class ExtendPage extends Page{
	private byte[] contentBytes;

	private String contentType;
	
	private String contentCharset;
	
	private Map<SuperSpider, List<String>> newURLMap = new HashMap<SuperSpider, List<String>>();
	private ExtendResultItems resultItems = new ExtendResultItems();
	
	public byte[] getContentBytes() {
		return contentBytes;
	}

	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
		resultItems.setContentBytes(contentBytes);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String responseType) {
		this.contentType = responseType;
		resultItems.setContentType(responseType);
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}
	
    public Page setSkip(boolean skip) {
        resultItems.setSkip(skip);
        return this;

    }

    public void putField(String key, Object field) {
        resultItems.put(key, field);
    }

    public void putRuleResult(TaskContentRule key, Object value) {
        resultItems.putRuleResult(key, value);
    }

    public void setRequest(Request request) {
        super.setRequest(request);
        resultItems.setRequest(request);
    }

    public ResultItems getResultItems() {
        return resultItems;
    }

	public Map<SuperSpider, List<String>> getNewURLMap() {
		return newURLMap;
	}

	public void putNewURL(SuperSpider spider,  List<String> newURLMap) {
		this.newURLMap.put(spider, newURLMap);
	}
}
