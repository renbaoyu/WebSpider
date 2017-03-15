package com.renby.spider.web.service;

import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.web.entity.Explan;
import com.renby.spider.web.entity.Task;

public interface IRunTimeListenService {
	public void onSpiderGroupStart(SpiderGroup group);
	public void onSpiderGroupStop(SpiderGroup group);
	public void onTaskFinished(SpiderGroup group);
	public void onSpiderStart(SuperSpider spider);
	public void onPageProcessed(Task task, Explan explan, ExtendPage page);
	public void onPipeLine(SuperSpider spider, ExtendPage page);
}
