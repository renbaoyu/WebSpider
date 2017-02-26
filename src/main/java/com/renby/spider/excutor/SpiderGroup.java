package com.renby.spider.excutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.renby.spider.entity.SpiderExplan;
import com.renby.spider.entity.SpiderRunResult;
import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.service.impl.SpiderExcuteServiceImpl;

import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class SpiderGroup {
	private static final Set<SpiderGroup> groups = new ConcurrentSkipListSet<SpiderGroup>();
	private SpiderTask task;
	private SpiderExplan explan;
	private SuperSpider startSpider;
	private final List<SuperSpider> spiders = new ArrayList<SuperSpider>();
	private SpiderExcuteServiceImpl service;
	private SpiderRunResult result;

	public SpiderGroup(SpiderTask task, SpiderExcuteServiceImpl service) {
		this.task = task;
		this.service = service;
		this.startSpider = SuperSpider.createSpider(task, null, new ArrayList<SpiderTaskContentRule>());
		this.startSpider.addUrl(task.getStartUrl());
		this.startSpider.setGroup(this);
		this.result = new SpiderRunResult();
		this.result.setName(task.getName());
	}

	public SpiderGroup(SpiderExplan explan, SpiderExcuteServiceImpl service) {
		this.explan = explan;
		this.task = explan.getTask();
		this.service = service;
		this.startSpider = SuperSpider.createSpider(task, null, new ArrayList<SpiderTaskContentRule>());
		this.startSpider.setGroup(this);
	}

	public void addSpider(SuperSpider spider) {
		spiders.add(spider);
		spider.setGroup(this);
	}

	public static Set<SpiderGroup> getGroups() {
		return groups;
	}

	public SpiderTask getTask() {
		return task;
	}

	public SpiderExplan getExplan() {
		return explan;
	}

	public List<SuperSpider> getSpiders() {
		return spiders;
	}

	public SpiderExcuteServiceImpl getService() {
		return service;
	}

	public SpiderRunResult getResult() {
		return result;
	}

	public void stop() {
		for (SuperSpider spider : spiders) {
			spider.stop();
		}
	}

	public void start() {
		startSpider.start();
		for (SuperSpider spider : spiders) {
			spider.setExitWhenComplete(false);
			spider.start();
		}
		service.getResultRepository().save(result);
//		service.getResultRepository().flush();
		new RunStatusListener(this).start();
	}

	public SuperSpider getStartSpider() {
		return startSpider;
	}

	class RunStatusListener extends Thread {
		SpiderGroup group;

		RunStatusListener(SpiderGroup group) {
			this.group = group;
		}

		@Override
		public void run() {
			while (true) {
				if (isFinished()) {
					break;
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException("任务被中断", e);
				}
			}
			group.getStartSpider().stop();
			group.stop();

		}

		private boolean isFinished() {
			boolean isFinished = true;
			SuperSpider startSpider = group.getStartSpider();
			MonitorableScheduler schedule = (MonitorableScheduler) startSpider.getScheduler();
			if (startSpider.getThreadAlive() > 0 || schedule.getLeftRequestsCount(startSpider) > 0) {
				isFinished = false;
			}
			for (SuperSpider spider : spiders) {
				schedule = (MonitorableScheduler) spider.getScheduler();
				if (spider.getThreadAlive() > 0 || schedule.getLeftRequestsCount(spider) > 0) {
					isFinished = false;
				}
			}
			return isFinished;
		}
	}

}
