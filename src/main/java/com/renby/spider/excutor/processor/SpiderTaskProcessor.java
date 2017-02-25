package com.renby.spider.excutor.processor;

import java.util.Date;
import java.util.List;

import com.renby.spider.entity.HtmlMatchRuleType;
import com.renby.spider.entity.SpiderRunLog;
import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class SpiderTaskProcessor implements PageProcessor {
	private SpiderGroup spiderGroup;
	private List<SpiderTaskContentRule> contentRules;

	public SpiderTaskProcessor(List<SpiderTaskContentRule> contentRules) {
		this.contentRules = contentRules;
	}

	@Override
	public void process(Page page) {
		ExtendPage exPage = (ExtendPage) page;
		for (SpiderTaskContentRule contentRule : contentRules) {
			List<String> values = excuteMatch(contentRule.getRuleType(), contentRule.getRuleExpression(),
					exPage.getHtml());
			if (values != null && !values.isEmpty()) {
				exPage.putField(contentRule.getName(), values);
			}
		}
		for (SuperSpider spider : spiderGroup.getSpiders()) {
			List<String> values = excuteMatch(spider.getPageRule().getUrlRuleType(),
					spider.getPageRule().getUrlRuleExpression(), exPage.getHtml());
			if (!values.isEmpty()) {
				exPage.putNewURL(spider, values);
			}
		}
		saveRunLog(exPage);
	}

	@Override
	public Site getSite() {
		return null;
	}

	public void setSpiderGroup(SpiderGroup spiderGroup) {
		this.spiderGroup = spiderGroup;
	}

	private List<String> excuteMatch(HtmlMatchRuleType type, String expresion, Html html) {
		switch (type) {
		case CSS:
			return html.css(expresion).all();
		case JSoup:
			return html.jsonPath(expresion).all();
		case Regex:
			return html.regex(expresion).all();
		case XPath:
			return html.xpath(expresion).all();
		default:
			throw new RuntimeException("表达式类型不可为空");
		}
	}

	private void saveRunLog(ExtendPage exPage) {
		SpiderRunLog log = new SpiderRunLog();
		log.setContent(exPage.getContentBytes());
		log.setContentType(exPage.getContentType());
		log.setTask(spiderGroup.getTask());
		log.setExplan(spiderGroup.getExplan());
		log.setFinishedDate(new Date());
		log.setUrl(exPage.getUrl().get());
		spiderGroup.getService().getRunLogRepository().save(log);
	}
}
