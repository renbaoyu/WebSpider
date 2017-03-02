package com.renby.spider.web.controller;

public abstract class AbstractController {
	public static final String PAGE_LIST = "/list";
	public static final String PAGE_EDIT = "/edit";
	public static final String PAGE_VIEW = "/view";

	public static final String LIST_DEFAULT_PAGE = "0";
	public static final String LIST_DEFAULT_PAGE_SIZE = "40";

	public abstract String getBasePage();

	public String getListPage() {
		return getBasePage() + PAGE_LIST;
	}

	public String getEditPage() {
		return getBasePage() + PAGE_EDIT;
	}

	public String getViewPage() {
		return getBasePage() + PAGE_VIEW;
	}
}
