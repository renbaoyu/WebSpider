package com.renby.spider.excutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.renby.spider.entity.Explan;
import com.renby.spider.entity.RunResult;
import com.renby.spider.entity.Task;
import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.service.impl.SpiderExcuteServiceImpl;

import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class SpiderGroup {
	private static final Set<SpiderGroup> groups = new ConcurrentSkipListSet<SpiderGroup>();
	private Task task;
	private Explan explan;
	private SuperSpider startSpider;
	private final List<SuperSpider> spiders = new ArrayList<SuperSpider>();
	private SpiderExcuteServiceImpl service;
	private RunResult result;

	public SpiderGroup(Task task, SpiderExcuteServiceImpl service) {
		this.task = task;
		this.service = service;
		this.result = new RunResult();
		this.result.setName(task.getName());
	}

	public SpiderGroup(Explan explan, SpiderExcuteServiceImpl service) {
		this.explan = explan;
		this.task = explan.getTask();
		this.service = service;
		this.result = new RunResult();
		this.result.setName(task.getName());
	}

	public void addSpider(SuperSpider spider) {
		spiders.add(spider);
		spider.setGroup(this);
	}

	public static Set<SpiderGroup> getGroups() {
		return groups;
	}

	public Task getTask() {
		return task;
	}

	public Explan getExplan() {
		return explan;
	}

	public List<SuperSpider> getSpiders() {
		return spiders;
	}

	public SpiderExcuteServiceImpl getService() {
		return service;
	}

	public RunResult getResult() {
		return result;
	}

	public void stop() {
		startSpider.stop();
		for (SuperSpider spider : spiders) {
			spider.stop();
		}
	}

	public void start() {
		assert startSpider != null;
		startSpider.start();
		for (SuperSpider spider : spiders) {
			spider.setExitWhenComplete(false);
			spider.start();
		}
		service.getResultRepository().save(result);
		new RunStatusListener(this).start();
	}

	public SuperSpider getStartSpider() {
		return startSpider;
	}

	public void setStartSpider(SuperSpider startSpider) {
		this.startSpider = startSpider;
		this.startSpider.setGroup(this);
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
