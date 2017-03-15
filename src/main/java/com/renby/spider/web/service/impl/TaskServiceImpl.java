package com.renby.spider.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskPageRule;
import com.renby.spider.web.repository.ExplanRepository;
import com.renby.spider.web.repository.TaskContentRuleRepository;
import com.renby.spider.web.repository.TaskPageRuleRepository;
import com.renby.spider.web.repository.TaskProgressRepository;
import com.renby.spider.web.repository.TaskRepository;
import com.renby.spider.web.service.IRunResultService;
import com.renby.spider.web.service.ITaskService;

@Component
public class TaskServiceImpl implements ITaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskPageRuleRepository pageRepository;
	@Autowired
	private TaskContentRuleRepository contentRepository;
	@Autowired
	private TaskProgressRepository progressRepository;
	@Autowired
	private ExplanRepository explanRepository;
	@Autowired
	private IRunResultService runResultService;

	@Override
	public void deleteTask(long id) {
		Task task = taskRepository.findOne(id);
		explanRepository.deleteByTask(task);
		progressRepository.deleteByTask(task);
		runResultService.deleteRunResultByTask(task);
		deleteTaskPage(task);
		taskRepository.delete(task);
	}

	@Override
	public void deleteTaskPage(long id) {
		TaskPageRule page = pageRepository.findOne(id);
		deleteTaskContent(page);
		pageRepository.delete(page);
	}

	@Override
	public void deleteTaskPage(Task task) {
		List<TaskPageRule> page = pageRepository.findByTask(task);
		deleteTaskContent(page);
		pageRepository.delete(page);
	}

	@Override
	public void deleteTaskPage(List<TaskPageRule> pages) {
		deleteTaskContent(pages);
		pageRepository.delete(pages);

	}

	@Override
	public void deleteTaskContent(long id) {
		contentRepository.delete(id);
	}

	@Override
	public void deleteTaskContent(List<TaskPageRule> pages) {
		contentRepository.deleteByPageIn(pages);
	}

	@Override
	public void deleteTaskContent(TaskPageRule page) {
		contentRepository.deleteByPage(page);
	}

}
