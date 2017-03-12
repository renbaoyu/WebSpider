package com.renby.spider.web.service;

import com.renby.spider.web.entity.Task;

public interface IRunResultService {
	public void deleteRunResultById(long id);
	public void deleteRunResultByTask(Task task);
}
