package com.renby.spider.excutor;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.renby.spider.entity.SpiderExplan;
import com.renby.spider.entity.SpiderRunResultPage;
import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.entity.SpiderTaskPageRule;
import com.renby.spider.excutor.processor.SpiderTaskProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.UrlUtils;

public class SuperSpider extends Spider {
	private Spider spider;
	private SpiderGroup group;
	private SpiderTask task;
	private SpiderExplan explan;
	private SpiderTaskPageRule pageRule;
	private SpiderRunResultPage pageResult;

	public SuperSpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}

	public SuperSpider(SpiderTaskProcessor pageProcessor, SpiderTask task, SpiderTaskPageRule pageRule) {
		super(pageProcessor);
		this.task = task;
		this.pageRule = pageRule;
		super.exitWhenComplete = false;
	}

	public static SuperSpider createSpider(SpiderTask task, SpiderTaskPageRule pageRule,
			List<SpiderTaskContentRule> contentRules) {
		SpiderTaskProcessor pageProcessor = new SpiderTaskProcessor(contentRules);
		return new SuperSpider(pageProcessor, task, pageRule);
	}

	@Override
	protected void extractAndAddRequests(Page page, boolean spawnUrl) {
		ExtendPage exPage = (ExtendPage) page;
		// 为每个页面的抓取器添加URL
		if (spawnUrl && !exPage.getNewURLMap().isEmpty()) {
			for (Entry<SuperSpider, List<String>> urlEntry : exPage.getNewURLMap().entrySet()) {
				SuperSpider spider = urlEntry.getKey();
				List<String> urls = urlEntry.getValue();
				for (String url : urls) {
					if (StringUtils.isBlank(url) || url.equals("#") || url.startsWith("javascript:")) {
						continue;
					}
					url = UrlUtils.canonicalizeUrl(url, exPage.getUrl().toString());
					spider.getScheduler().push(new Request(url), spider);
				}
			}
		}
	}

	@Override
	protected void initComponent() {
		super.initComponent();
		pageResult = new SpiderRunResultPage();
		pageResult.setResult(group.getResult());
		if(pageRule == null){
			pageResult.setName(task.getName() + "任务首页面");
		}else{
			pageResult.setName(pageRule.getName());
		}
		group.getService().getResultPageRepository().save(pageResult);
	}

	public SpiderTaskPageRule getPageRule() {
		return pageRule;
	}

	public SpiderExplan getExplan() {
		return explan;
	}

	public Spider getSpider() {
		return spider;
	}

	public SpiderTask getTask() {
		return task;
	}

	public SpiderRunResultPage getPageResult() {
		return pageResult;
	}

	public SpiderGroup getGroup() {
		return group;
	}

	public void setGroup(SpiderGroup group) {
		this.group = group;
		((SpiderTaskProcessor) super.pageProcessor).setSpiderGroup(group);
	}

}
