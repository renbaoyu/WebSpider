package com.renby.spider.web.service;

import java.util.List;

import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskPageRule;

public interface ITaskService {
	public void deleteTask(long id);
	public void deleteTaskPage(long id);
	public void deleteTaskPage(Task task);
	public void deleteTaskPage(List<TaskPageRule> pages);
	public void deleteTaskContent(long id);
	public void deleteTaskContent(TaskPageRule page);
	public void deleteTaskContent(List<TaskPageRule> pages);
}
