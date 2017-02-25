package com.renby.spider.excutor.schedule;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

/**
 * 栈任务调度器实现<p>
 * 后加入的任务先执行
 * @author Administrator
 *
 */
public class StackScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {

    private Deque<Request> queue = new ConcurrentLinkedDeque<Request>();

    @Override
    public void pushWhenNoDuplicate(Request request, Task task) {
        queue.add(request);
    }

    public synchronized Request poll(Task task) {
        return queue.pollLast();
    }

    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    public int getTotalRequestsCount(Task task) {
        return getDuplicateRemover().getTotalRequestsCount(task);
    }
}
