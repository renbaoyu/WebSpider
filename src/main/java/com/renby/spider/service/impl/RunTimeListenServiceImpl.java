package com.renby.spider.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.renby.spider.entity.Explan;
import com.renby.spider.entity.RunLog;
import com.renby.spider.entity.RunResultData;
import com.renby.spider.entity.RunResultPage;
import com.renby.spider.entity.Task;
import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.entity.TaskProgress;
import com.renby.spider.entity.TaskProgress.TaskStatus;
import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.ExtendRequest;
import com.renby.spider.excutor.ExtendResultItems;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.repository.RunLogRepository;
import com.renby.spider.repository.RunResultDataRepository;
import com.renby.spider.repository.RunResultPageRepository;
import com.renby.spider.repository.RunResultRepository;
import com.renby.spider.repository.TaskProgressRepository;
import com.renby.spider.runtime.ProgressManager;
import com.renby.spider.service.IRunTimeListenService;

@Component
public class RunTimeListenServiceImpl implements IRunTimeListenService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskProgressRepository taskProgressRepository;
	@Autowired
	private RunLogRepository runLogRepository;
	@Autowired
	private RunResultRepository resultRepository;
	@Autowired
	private RunResultPageRepository resultPageRepository;
	@Autowired
	private RunResultDataRepository resultDataRepository;

	@Override
	public void onSpiderGroupStart(SpiderGroup group) {
		resultRepository.save(group.getResult());

		TaskProgress progress = new TaskProgress();
		progress.setTask(group.getTask());
		progress.setExplan(group.getExplan());
		progress.setStartTime(new Date());
		progress.setStatus(TaskStatus.RUNNING);
		progress.setProcessedPageCount(0);
		taskProgressRepository.save(progress);
		ProgressManager.addProgress(group, progress);

		logger.info("任务[{}]已启动", group.getTask().getName());
	}

	@Override
	public void onSpiderStart(SuperSpider spider) {
		RunResultPage pageResult = new RunResultPage();
		pageResult.setResult(spider.getGroup().getResult());
		if (spider.getPageRule() == null) {
			pageResult.setName(spider.getTask().getName() + "任务首页面");
		} else {
			pageResult.setName(spider.getPageRule().getName());
		}
		resultPageRepository.save(pageResult);
		logger.info("已开始监测[{}]页面", spider.getPageRule().getName());
	}

	@Override
	public void onPageProcessed(Task task, Explan explan, ExtendPage page) {
		RunLog log = new RunLog();
		log.setContent(page.getContentBytes());
		log.setContentType(page.getContentType());
		log.setContentCharset(page.getContentCharset());
		log.setTask(task);
		log.setExplan(explan);
		log.setFinishedTime(new Date());
		log.setUrl(page.getUrl().get());
		log.setParentUrl(((ExtendRequest) page.getRequest()).getParent());
		log.setStateCode(page.getStatusCode());
		runLogRepository.save(log);
		logger.info("已处理页面[{}]", page.getUrl().get());
	}

	@Override
	public void onPipeLine(SuperSpider spider, ExtendResultItems resultItems) {
		List<RunResultData> datas = new ArrayList<RunResultData>();
		for (Entry<TaskContentRule, Object> entry : resultItems.getRuleResult().entrySet()) {
			RunResultData data = new RunResultData();
			data.setName(entry.getKey().getName());
			data.setPage(spider.getPageResult());
			data.setContent(entry.getValue().toString());
			datas.add(data);
		}
		if (!datas.isEmpty()) {
			resultDataRepository.save(datas);
		}
		ProgressManager.increaseProcessedPage(spider.getGroup());
		taskProgressRepository.save(ProgressManager.getProgress(spider.getGroup()));
	}

	@Override
	public void onSpiderGroupStop(SpiderGroup group) {
		logger.info("监测任务[{}]已停止", group.getTask().getName());
	}

	@Override
	public void onTaskFinished(SpiderGroup group) {
		TaskProgress progress = ProgressManager.getProgress(group);
		progress.setFinishedTime(new Date());
		progress.setStatus(TaskStatus.FINISHED);
		int seconds = (int) ((progress.getFinishedTime().getTime() - progress.getStartTime().getTime())/1000);
		progress.setCust(seconds);
		taskProgressRepository.save(progress);
		logger.info("监测任务[{}]已结束", group.getTask().getName());
	}
}
