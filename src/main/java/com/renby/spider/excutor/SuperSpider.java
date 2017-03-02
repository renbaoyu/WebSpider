package com.renby.spider.excutor;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.renby.spider.excutor.processor.SpiderTaskProcessor;
import com.renby.spider.web.entity.Explan;
import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskContentRule;
import com.renby.spider.web.entity.TaskPageRule;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

public class SuperSpider extends Spider {
	private Spider spider;
	private SpiderGroup group;
	private Task task;
	private Explan explan;
	private TaskPageRule pageRule;
	public SuperSpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}

	public SuperSpider(SpiderTaskProcessor pageProcessor, Task task, TaskPageRule pageRule) {
		super(pageProcessor);
		this.task = task;
		this.pageRule = pageRule;
		super.exitWhenComplete = false;
		this.setUUID(task.getName() + (pageRule == null ? "" : ":" + pageRule.getName()));
		this.setEmptySleepTime(1000);
	}

	public static SuperSpider createSpider(Task task, TaskPageRule pageRule,
			List<TaskContentRule> contentRules) {
		SpiderTaskProcessor pageProcessor = new SpiderTaskProcessor(contentRules);
		return new SuperSpider(pageProcessor, task, pageRule);
	}

	@Override
	protected void extractAndAddRequests(Page page, boolean spawnUrl) {
		ExtendPage exPage = (ExtendPage) page;
		// 为每个页面的监测的页面添加URL
		if (spawnUrl && !exPage.getNewURLMap().isEmpty()) {
			for (Entry<SuperSpider, List<String>> urlEntry : exPage.getNewURLMap().entrySet()) {
				SuperSpider spider = urlEntry.getKey();
				List<String> urls = urlEntry.getValue();
				for (String url : urls) {
					if (StringUtils.isBlank(url) || url.equals("#") || url.startsWith("javascript:")) {
						continue;
					}
					url = UrlUtils.canonicalizeUrl(url, exPage.getUrl().toString());
					spider.getScheduler().push(new ExtendRequest(url,exPage.getUrl().get()), spider);
				}
			}
		}
	}

	@Override
    protected void processRequest(Request request) {
        Page page = downloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            onError(request);
            return;
        }
        // for cycle retry
        if (page.isNeedCycleRetry()) {
            extractAndAddRequests(page, true);
            sleep(site.getRetrySleepTime());
            return;
        }
        pageProcessor.process(page);
        extractAndAddRequests(page, spawnUrl);
        if (!page.getResultItems().isSkip()) {
        	group.getListener().onPipeLine(this, (ExtendResultItems) page.getResultItems());
            for (Pipeline pipeline : pipelines) {
                pipeline.process(page.getResultItems(), this);
            }
        }
        //for proxy status management
        request.putExtra(Request.STATUS_CODE, page.getStatusCode());
        sleep(site.getSleepTime());
    }

	@Override
	protected void initComponent() {
		super.initComponent();
		group.getListener().onSpiderStart(this);
	}

	public TaskPageRule getPageRule() {
		return pageRule;
	}

	public Explan getExplan() {
		return explan;
	}

	public Spider getSpider() {
		return spider;
	}

	public Task getTask() {
		return task;
	}

	public SpiderGroup getGroup() {
		return group;
	}

	public void setGroup(SpiderGroup group) {
		this.group = group;
		((SpiderTaskProcessor) super.pageProcessor).setSpiderGroup(group);
	}

}
