package com.renby.spider.runtime;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.web.entity.TaskProgress;

public class ProgressManager {
	private static ReentrantLock lock = new ReentrantLock();
	private static Map<SpiderGroup, TaskProgress> progressMap = new ConcurrentHashMap<SpiderGroup, TaskProgress>();

	public static void addProgress(SpiderGroup group, TaskProgress progress) {
		progressMap.put(group, progress);
	}

	public static TaskProgress getProgress(SpiderGroup group) {
		return progressMap.get(group);
	}

	public static void increaseProcessedPage(SpiderGroup group) {
		lock.lock();
		try {
			TaskProgress progress = progressMap.get(group);
			progress.setProcessedPageCount(progress.getProcessedPageCount() + 1);
			progress.setFinishedTime(new Date());
			progress.setCust((int) ((progress.getFinishedTime().getTime() - progress.getStartTime().getTime())/1000));
		} finally {
			lock.unlock();
		}
	}
}
