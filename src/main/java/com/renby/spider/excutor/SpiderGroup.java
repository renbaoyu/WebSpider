package com.renby.spider.excutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.renby.spider.excutor.downloader.HttpClientFileDownloader;
import com.renby.spider.excutor.downloader.SeleniumDownloader;
import com.renby.spider.runtime.SystemConfig;
import com.renby.spider.web.entity.Explan;
import com.renby.spider.web.entity.RunResult;
import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.Task.SiteType;
import com.renby.spider.web.service.IRunTimeListenService;

import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class SpiderGroup {
	private static final Set<SpiderGroup> groups = new ConcurrentSkipListSet<SpiderGroup>();
	private Task task;
	private Explan explan;
	private SuperSpider startSpider;
	private final List<SuperSpider> spiders = new ArrayList<SuperSpider>();
	private IRunTimeListenService service;
	private RunResult result;

	public SpiderGroup(Task task, IRunTimeListenService service) {
		this.task = task;
		this.service = service;
		this.result = new RunResult();
		this.result.setName(task.getName());
		this.result.setTask(task);
	}

	public SpiderGroup(Explan explan, IRunTimeListenService service) {
		this.explan = explan;
		this.task = explan.getTask();
		this.service = service;
		this.result = new RunResult();
		this.result.setName(task.getName());
		this.result.setTask(task);
	}

	public void addSpider(SuperSpider spider) {
		spiders.add(spider);
		spider.setGroup(this);
		setDownloader(spider);

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

	public IRunTimeListenService getListener() {
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
		service.onSpiderGroupStop(this);
	}

	public void start() {
		assert startSpider != null;
		startSpider.start();
		for (SuperSpider spider : spiders) {
			spider.setExitWhenComplete(false);
			spider.start();
		}
		service.onSpiderGroupStart(this);
		new RunStatusListener(this).start();
	}

	public SuperSpider getStartSpider() {
		return startSpider;
	}

	public void setStartSpider(SuperSpider startSpider) {
		this.startSpider = startSpider;
		this.startSpider.setGroup(this);
		setDownloader(startSpider);
	}

	private void setDownloader(SuperSpider spider){
		Downloader downloader = null;
		if(task.getSiteType() == SiteType.STATIC_HTML){
			downloader = new HttpClientFileDownloader();
		}else{
			downloader = new SeleniumDownloader();
			boolean needScreenShot = SystemConfig.getBoolean("spider.screenshot");
			((SeleniumDownloader)downloader).setScreenshot(needScreenShot);
		}
		int thread = SystemConfig.getIntValue("spider.download.thread");
		downloader.setThread(thread);
		spider.setDownloader(downloader);
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
			group.getListener().onTaskFinished(group);
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
