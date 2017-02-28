package com.renby.spider.service;

import com.renby.spider.entity.Explan;
import com.renby.spider.entity.Task;
import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.ExtendResultItems;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;

public interface IRunTimeListenService {
	public void onSpiderGroupStart(SpiderGroup group);
	public void onSpiderGroupStop(SpiderGroup group);
	public void onTaskFinished(SpiderGroup group);
	public void onSpiderStart(SuperSpider spider);
	public void onPageProcessed(Task task, Explan explan, ExtendPage page);
	public void onPipeLine(SuperSpider spider, ExtendResultItems resultItems);
}
