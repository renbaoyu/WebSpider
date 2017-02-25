package com.renby.spider.excutor.downloader;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.renby.spider.excutor.ExtendPage;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 基于HttpClientDownloader，保存原始响应字节数组及响应类型
 * 
 * @author renby
 *
 */
public class HttpClientFileDownloader extends HttpClientDownloader {
	protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task)
			throws IOException {
		byte[] contentBytes = getContentBytes(httpResponse);
		String content = getContent(charset, httpResponse, contentBytes);
		ExtendPage page = new ExtendPage();
		page.setContentBytes(contentBytes);
		page.setContentType(getContentType(httpResponse));
		page.setContentCharset(getHtmlCharset(httpResponse, contentBytes));
		page.setRawText(content);
		page.setUrl(new PlainText(request.getUrl()));
		page.setRequest(request);
		page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		return page;
	}

	private byte[] getContentBytes(HttpResponse httpResponse) throws IOException {
		return IOUtils.toByteArray(httpResponse.getEntity().getContent());
	}

	protected String getContent(String charset, HttpResponse httpResponse, byte[] contentBytes) throws IOException {
		if (charset == null) {
			String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
			if (htmlCharset != null) {
				return new String(contentBytes, htmlCharset);
			} else {
				return new String(contentBytes);
			}
		} else {
			return new String(contentBytes, charset);
		}
	}

	protected String getContentType(HttpResponse httpResponse) throws IOException {
		String value = httpResponse.getEntity().getContentType().getValue().toLowerCase();
		return value.indexOf(";") > 0 ? value.substring(0, value.indexOf(";")) : value;
	}
}
