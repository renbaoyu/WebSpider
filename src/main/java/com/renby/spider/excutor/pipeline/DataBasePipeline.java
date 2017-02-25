package com.renby.spider.excutor.pipeline;

import java.util.Map.Entry;

import com.renby.spider.entity.SpiderRunResultData;
import com.renby.spider.excutor.ExtendResultItems;
import com.renby.spider.excutor.SuperSpider;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 文件输出类<br>
 * 输出指定类型的文件
 * 
 * @author Administrator
 *
 */
public class DataBasePipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		ExtendResultItems items = (ExtendResultItems) resultItems;
		SuperSpider spider = (SuperSpider) task;
		for(Entry<String, Object> entry :items.getAll().entrySet()){
			SpiderRunResultData data = new SpiderRunResultData();
			data.setName(entry.getKey());
			data.setPage(spider.getPageResult());
			data.setContent(entry.getValue().toString());
		}
		
	}

}
