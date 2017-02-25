package com.renby.spider.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.entity.SpiderTaskPageRule;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.repository.SpiderRunLogRepository;
import com.renby.spider.repository.SpiderRunResultDataRepository;
import com.renby.spider.repository.SpiderRunResultPageRepository;
import com.renby.spider.repository.SpiderRunResultRepository;
import com.renby.spider.repository.SpiderTaskContentRuleRepository;
import com.renby.spider.repository.SpiderTaskPageRuleRepository;
import com.renby.spider.repository.SpiderTaskRepository;
import com.renby.spider.service.ISpiderExcuteService;

@Component
public class SpiderExcuteServiceImpl implements ISpiderExcuteService {
	@Autowired
	private SpiderTaskRepository taskRepository;
	@Autowired
	private SpiderTaskPageRuleRepository pageRepository;
	@Autowired
	private SpiderTaskContentRuleRepository contentRepository;
	@Autowired
	private SpiderRunLogRepository runLogRepository;
	@Autowired
	private SpiderRunResultRepository resultRepository;
	@Autowired
	private SpiderRunResultPageRepository resultPageRepository;
	@Autowired
	private SpiderRunResultDataRepository resultDataRepository;

	public void runTask(long id) {
		SpiderTask task = taskRepository.getOne(id);
		List<SpiderTaskPageRule> pages = pageRepository.findByTask(task);
		SpiderGroup group = new SpiderGroup(task, this);
		for (SpiderTaskPageRule page : pages) {
			List<SpiderTaskContentRule> contents = contentRepository.findByPage(page);
			SuperSpider spider = SuperSpider.createSpider(task, page, contents);
			group.addSpider(spider);
		}
		group.start();
	}

	public SpiderRunLogRepository getRunLogRepository() {
		return runLogRepository;
	}

	public SpiderRunResultRepository getResultRepository() {
		return resultRepository;
	}

	public SpiderRunResultPageRepository getResultPageRepository() {
		return resultPageRepository;
	}

	public SpiderRunResultDataRepository getResultDataRepository() {
		return resultDataRepository;
	}
}
