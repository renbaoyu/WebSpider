package com.renby.spider.excutor.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.renby.spider.entity.RunResultData;
import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.excutor.ExtendResultItems;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.repository.RunResultDataRepository;

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
		List<RunResultData> datas = new ArrayList<RunResultData>();
		for (Entry<TaskContentRule, Object> entry : items.getRuleResult().entrySet()) {
			RunResultData data = new RunResultData();
			data.setName(entry.getKey().getName());
			data.setPage(spider.getPageResult());
			data.setContent(entry.getValue().toString());
			datas.add(data);
		}
		if (!datas.isEmpty()) {
			RunResultDataRepository resultDataRepository = spider.getGroup().getService()
					.getResultDataRepository();
			resultDataRepository.save(datas);
		}

	}

}
