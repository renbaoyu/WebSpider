package com.renby.spider.web.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;

public class Pagination {
	private int pageCount;
	private int size;
	private long dataSize;
	private int page;
	private String listUrl;
	private List<Integer> pages = new ArrayList<Integer>();
	public static int showPageNum = 10;

	public Pagination(int dataSize, int pageSize, int currentPage) {
		this.dataSize = dataSize;
		this.size = pageSize;
		this.page = currentPage;
		this.pageCount = (dataSize + pageSize - 1) / pageSize;
	}

	public Pagination(PageImpl<?> pageImpl, String listUrl, int page) {
		this.dataSize = pageImpl.getTotalElements();
		this.size = pageImpl.getTotalPages();
		this.page = page;
		this.pageCount = pageImpl.getTotalPages();
		this.listUrl = listUrl;
		int start = 0;
		int end = 0;
		if (pageCount > showPageNum) {
			if (page > showPageNum / 2) {
				if (page < pageCount - showPageNum / 2) {
					start = page - showPageNum / 2;
					end = page + showPageNum / 2;
				} else {
					start = pageCount - showPageNum;
					end = pageCount;
				}
			} else {
				start = 0;
				end = showPageNum;
			}
		} else {
			start = 0;
			end = pageCount;
		}
		pages.clear();
		for (int i = start; i < end; i++) {
			pages.add(i);
		}
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getDataSize() {
		return dataSize;
	}

	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

}
