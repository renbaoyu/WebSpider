package com.renby.spider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.entity.Task;
import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.entity.TaskPageRule;
import com.renby.spider.excutor.ExtendRequest;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.repository.TaskContentRuleRepository;
import com.renby.spider.repository.TaskPageRuleRepository;
import com.renby.spider.repository.TaskRepository;
import com.renby.spider.service.IRunTimeListenService;
import com.renby.spider.service.ISpiderExcuteService;

@Component
public class SpiderExcuteServiceImpl implements ISpiderExcuteService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskPageRuleRepository pageRepository;
	@Autowired
	private TaskContentRuleRepository contentRepository;
	@Autowired
	private IRunTimeListenService runtimeListenRepository;

	public void runTask(long id) {
		Task task = taskRepository.findOne(id);
		List<TaskPageRule> pages = pageRepository.findByTask(task);
		SpiderGroup group = new SpiderGroup(task, runtimeListenRepository);
		for (TaskPageRule page : pages) {
			List<TaskContentRule> contents = contentRepository.findByPage(page);
			SuperSpider spider = SuperSpider.createSpider(task, page, contents);
			if(page.isStartPage()){
				spider.addRequest(new ExtendRequest(task.getStartUrl()));
				group.setStartSpider(spider);
			}else{
				group.addSpider(spider);
			}
		}
		group.start();
	}
}
