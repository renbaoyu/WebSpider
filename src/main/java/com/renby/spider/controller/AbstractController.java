package com.renby.spider.controller;

public abstract class AbstractController {
	public static final String LIST_PAGE = "/list";
	public static final String EDIT_PAGE = "/edit";
	public static final String VIEW_PAGE = "/view";

	public static final String LIST_DEFAULT_PAGE = "0";
	public static final String LIST_DEFAULT_PAGE_SIZE = "20";

	public abstract String getBasePage();

	public String getListPage() {
		return getBasePage() + LIST_PAGE;
	}

	public String getEditPage() {
		return getBasePage() + EDIT_PAGE;
	}

	public String getViewPage() {
		return getBasePage() + VIEW_PAGE;
	}
}
