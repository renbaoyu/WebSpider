package com.renby.spider.web.controller;

import java.util.Objects;

import org.springframework.data.domain.PageImpl;
import org.springframework.ui.ModelMap;

import com.renby.spider.web.vo.Pagination;

public abstract class AbstractController {
	public static final String PAGE_LIST = "/list";
	public static final String PAGE_EDIT = "/edit";
	public static final String PAGE_VIEW = "/view";

	public static final String LIST_DEFAULT_PAGE = "0";
	public static final String LIST_DEFAULT_PAGE_SIZE = "10";

	public static final String LIST_PAGINATION = "pagination";

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
	public String getViewPageUrl(long id) {
		return getBasePage() + PAGE_VIEW + "/" + id;
	}

	public void setPagination(ModelMap model, PageImpl<?> pageImpl, String baseUrl, String keywords, int pagesize,
			int page) {
		Objects.requireNonNull(model, "model不可为空");
		Objects.requireNonNull(pageImpl, "数据检索结果不可为空");

		model.addAttribute(LIST_PAGINATION,
				new Pagination(pageImpl, getPaginationedUrl(baseUrl, keywords, pagesize), page));
	}

	public String getPaginationedUrl(String baseUrl, String keywords, int pagesize) {
		return baseUrl + "?s=" + (keywords == null ? "" : keywords) + "&pagesize=" + pagesize;
	}
}
