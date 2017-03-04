package com.renby.spider.excutor.downloader;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.renby.spider.excutor.ExtendPage;
import com.renby.spider.excutor.SuperSpider;
import com.renby.spider.filemanager.QiniuFileManager;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 使用Selenium调用浏览器进行渲染。<br>
 * 需要下载Selenium driver支持。<br>
 *
 */
public class SeleniumDownloader implements Downloader, Closeable {
	public static String SCREENSHOT_URL = "ScreenshotUrl";
	private Logger logger = Logger.getLogger(getClass());

	private String PHANTOMJS = getClass().getClassLoader().getResource("phantomjs.exe").getPath();
	private WebDriverManager manager = new WebDriverManager();
	private boolean screenshot = true;

	public void setScreenshot(boolean screenshot) {
		this.screenshot = screenshot;
	}

	@Override
	public Page download(Request request, Task task) {
		logger.info("downloading page " + request.getUrl());
		WebDriver webDriver = manager.getWebDriver();
		WebDriver.Options manage = webDriver.manage();
		Site site = task.getSite();
		if (site.getCookies() != null) {
			for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
				Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
				manage.addCookie(cookie);
			}
		}
		webDriver.get(request.getUrl());
		try {
			SuperSpider spider = (SuperSpider) task;
			Thread.sleep(spider.getPageRule().getLoadedDelay() * 1000);
		} catch (InterruptedException e) {
			logger.error("下载延时时被中断", e);
		}
		if (site.getCookies() != null) {
			for (Cookie cookie : manage.getCookies()) {
				site.addCookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
				manage.addCookie(cookie);
			}
		}
		WebElement webElement = webDriver.findElement(By.xpath("/html"));
		screenshot((TakesScreenshot) webDriver, request);
		manager.returnDriver(webDriver);
		String content = webElement.getAttribute("outerHTML");
		ExtendPage page = new ExtendPage();
		page.setRawText(content);
		page.setUrl(new PlainText(request.getUrl()));
		page.setRequest(request);
		page.setContentBytes(webElement.getText().getBytes());
		page.setRawText(content);
		page.setUrl(new PlainText(request.getUrl()));
		page.setRequest(request);
		return page;
	}

	@Override
	public void setThread(int thread) {
		manager.setPoolSize(thread);
	}

	@Override
	public void close() throws IOException {
		manager.close();
	}

	private void screenshot(TakesScreenshot downloader, Request request) {
		if (screenshot) {
			File scrFile = downloader.getScreenshotAs(OutputType.FILE);
			String screenshotUrl = QiniuFileManager.uploadFile(scrFile, null);
			request.putExtra(SCREENSHOT_URL, screenshotUrl);
		}
	}

	class WebDriverManager {
		private int poolSize = 1;

		private List<WebDriver> drivers = Collections.synchronizedList(new ArrayList<WebDriver>());
		private BlockingQueue<WebDriver> driverQueue = new LinkedBlockingQueue<WebDriver>();

		void setPoolSize(int poolSize) {
			this.poolSize = poolSize;
		}

		WebDriver getWebDriver() {
			try {
				if (drivers.size() >= poolSize) {
					return driverQueue.take();
				} else {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setJavascriptEnabled(true);
					caps.setCapability("takesScreenshot", true);
					caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOMJS);
					WebDriver webDriver = new PhantomJSDriver(caps);
					driverQueue.put(webDriver);
					drivers.add(webDriver);
					return webDriver;
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		void returnDriver(WebDriver driver) {
			try {
				driverQueue.put(driver);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		void close() {
			for (WebDriver driver : drivers) {
				driver.quit();
			}
		}

	}
}
