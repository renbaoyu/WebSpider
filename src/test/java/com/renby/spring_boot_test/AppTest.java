package com.renby.spring_boot_test;

import com.renby.spider.excutor.downloader.SeleniumDownloader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class AppTest implements PageProcessor {
	public static void main(String[] args) {
		Spider.create(new AppTest()).setDownloader(new SeleniumDownloader())
				.addUrl("http://blog.csdn.net/funi16/article/details/9036753").run();
	}

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

	}

	@Override
	public Site getSite() {
		return Site.me();
	}
}
