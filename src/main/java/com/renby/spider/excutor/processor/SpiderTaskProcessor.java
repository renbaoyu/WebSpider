package com.renby.spider.excutor.processor;

import java.util.ArrayList;
import java.util.List;

import com.renby.spider.entity.HtmlMatchRuleType;
import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.SpiderGroup;
import com.renby.spider.excutor.SuperSpider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class SpiderTaskProcessor implements PageProcessor {
	private SpiderGroup spiderGroup;
	private List<TaskContentRule> contentRules;

	public SpiderTaskProcessor(List<TaskContentRule> contentRules) {
		this.contentRules = contentRules;
	}

	@Override
	public void process(Page page) {
		ExtendPage exPage = (ExtendPage) page;
		for (TaskContentRule contentRule : contentRules) {
			List<String> values = excuteMatch(contentRule.getRuleType(), contentRule.getRuleExpression(),
					exPage.getHtml(), false);
			if (values != null && !values.isEmpty()) {
				exPage.putRuleResult(contentRule, values);
			}
		}
		for (SuperSpider spider : spiderGroup.getSpiders()) {
			List<String> values = excuteMatch(spider.getPageRule().getUrlRuleType(),
					spider.getPageRule().getUrlRuleExpression(), exPage.getHtml(), true);
			if (!values.isEmpty()) {
				exPage.putNewURL(spider, values);
			}
		}
		spiderGroup.getService().onPageProcessed(spiderGroup.getTask(), spiderGroup.getExplan(), exPage);
	}

	@Override
	public Site getSite() {
		return Site.me();
	}

	public void setSpiderGroup(SpiderGroup spiderGroup) {
		this.spiderGroup = spiderGroup;
	}

	private List<String> excuteMatch(HtmlMatchRuleType type, String expresion, Html html, boolean isUrl) {
		if (isUrl) {
			switch (type) {
			case Normal:
				List<String> matched = new ArrayList<String>();
				for (String line : html.links().all()) {
					if (line.indexOf(expresion) != -1) {
						matched.add(line);
					}
				}
				return matched;
			case CSS:
				return html.css(expresion).links().all();
			case JSoup:
				return html.jsonPath(expresion).links().all();
			case Regex:
				return html.regex(expresion).links().all();
			case XPath:
				return html.xpath(expresion).links().all();
			default:
				throw new RuntimeException("表达式类型不可为空");
			}
		} else {
			switch (type) {
			case Normal:
				List<String> matched = new ArrayList<String>();
				for (String line : html.get().split("\\n")) {
					if (line.indexOf(expresion) != -1) {
						matched.add(line);
					}
				}
				return matched;
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
	}
}
