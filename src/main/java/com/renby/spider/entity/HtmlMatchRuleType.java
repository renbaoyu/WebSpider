package com.renby.spider.entity;

public enum HtmlMatchRuleType {
	Normal("普通查找"), JSoup("JSoup"), Regex("正则表达式"), XPath("XPath"), CSS("CSS选择器");

	private String name;

	HtmlMatchRuleType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
