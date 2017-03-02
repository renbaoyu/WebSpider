package com.renby.spider.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.excutor.ExtendRequest;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskContentRule;
import com.renby.spider.web.entity.TaskPageRule;
import com.renby.spider.web.repository.TaskContentRuleRepository;
import com.renby.spider.web.repository.TaskPageRuleRepository;
import com.renby.spider.web.repository.TaskRepository;
import com.renby.spider.web.service.IRunTimeListenService;
import com.renby.spider.web.service.ISpiderExcuteService;

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
