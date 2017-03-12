package com.renby.spider.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.web.entity.RunResult;
import com.renby.spider.web.entity.RunResultPage;
import com.renby.spider.web.entity.Task;
import com.renby.spider.web.repository.RunResultDataRepository;
import com.renby.spider.web.repository.RunResultPageRepository;
import com.renby.spider.web.repository.RunResultRepository;
import com.renby.spider.web.service.IRunResultService;

@Component
public class RunResultServiceImpl implements IRunResultService {

	@Autowired
	private RunResultRepository resultRepository;
	@Autowired
	private RunResultPageRepository resultPageRepository;
	@Autowired
	private RunResultDataRepository resultDataRepository;
	@Override
	public void deleteRunResultById(long id) {
		RunResult result = resultRepository.findOne(id);
		List<RunResultPage> pages = resultPageRepository.findByResult(result);
		resultDataRepository.deleteByPageIn(pages);
		resultPageRepository.deleteInBatch(pages);
		resultRepository.delete(result);
	}
	@Override
	public void deleteRunResultByTask(Task task) {
		List<RunResult> results = resultRepository.findByTask(task);
		List<RunResultPage> pages = resultPageRepository.findByResultIn(results);
		resultDataRepository.deleteByPageIn(pages);
		resultPageRepository.deleteInBatch(pages);
		resultRepository.delete(results);
	}
}
