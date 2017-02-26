package com.renby.spider.excutor;

import java.util.HashMap;
import java.util.Map;

import com.renby.spider.entity.SpiderTaskContentRule;

import us.codecraft.webmagic.ResultItems;

public class ExtendResultItems extends ResultItems {
	private static final String CONTENT_TYPE = "contentType";
	private static final String CONTENT_BYTES = "contentBytes";
	private static final String FILE_PARENT = "fileParent";
	private static final String FILE_NAME = "fileName";
	private Map<SpiderTaskContentRule, Object> ruleResult = new HashMap<SpiderTaskContentRule, Object>();
	public String getContentType(){
		return super.get(CONTENT_TYPE);
	}
	
	public void setContentType(String contentType){
		super.put(CONTENT_TYPE, contentType);
	}
	
	public byte[] getContentBytes(){
		return super.get(CONTENT_BYTES);
	}
	
	public void setContentBytes(byte[] contentBytes){
		super.put(CONTENT_BYTES, contentBytes);
	}
	
	public String getFileParent(){
		return super.get(FILE_PARENT);
	}
	
	public void setFileParent(String fileParent){
		super.put(FILE_PARENT, fileParent);
	}
	
	public String getFileName(){
		return super.get(FILE_NAME);
	}
	
	public void setFileName(String fileName){
		super.put(FILE_NAME, fileName);
	}
	
	public void putRuleResult(SpiderTaskContentRule rule, Object data){
		ruleResult.put(rule, data);
	}
	
	public Map<SpiderTaskContentRule, Object> getRuleResult(){
		return ruleResult;
	}
}
