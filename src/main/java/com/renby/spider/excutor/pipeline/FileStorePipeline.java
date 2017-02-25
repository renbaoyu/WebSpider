package com.renby.spider.excutor.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.renby.spider.excutor.ExtendResultItems;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 文件输出类<br>
 * 输出指定类型的文件
 * 
 * @author Administrator
 *
 */
public class FileStorePipeline implements Pipeline {
	private Set<String> fileTypes = new HashSet<String>();

	private File parentPath;

	/**
	 * 实例化文件输出类
	 * 
	 * @param path
	 *            文件存放根文件夹
	 */
	public FileStorePipeline(String path) {
		parentPath = new File(path);
		assert path != null;
		if (!parentPath.exists()) {
			parentPath.mkdirs();
		}
		initFileType();
	}

	public void process(ResultItems resultItems, Task task) {
		if (!(resultItems instanceof ExtendResultItems)) {
			return;
		}
		ExtendResultItems result = (ExtendResultItems) resultItems;
		if (!fileTypes.contains(result.getContentType())) {
			return;
		}

		FileOutputStream output = null;
		try {
			File parent = getParent(result, task);
			String fileName = getFileName(result, task);
			output = new FileOutputStream(new File(parent, fileName));
			output.write(result.getContentBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取父文件夹
	 * @param resultItems
	 * @param task
	 * @return
	 */
	protected File getParent(ExtendResultItems resultItems, Task task) {
		File parent = resultItems.getFileParent() == null ? parentPath
				: new File(parentPath, resultItems.getFileParent());
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return parent;
	}

	/**
	 * 获取文件名
	 * @param resultItems
	 * @param task
	 * @return
	 */
	protected String getFileName(ExtendResultItems resultItems, Task task) {
		String fileName = resultItems.getFileName();
		if (fileName == null) {
			String url = resultItems.getRequest().getUrl();
			if (url.indexOf("?") > 0) {
				fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
			} else {
				fileName = url.substring(url.lastIndexOf("/") + 1);
			}
		}
		return fileName;
	}

	/**
	 * 获取文件类型集合
	 * 
	 * @param fileType
	 */
	public Set<String> getFileTypes() {
		return fileTypes;
	}

	/**
	 * 默认添加常用文件类型
	 */
	private void initFileType() {
		fileTypes.add("image/png");
		fileTypes.add("image/gif");
		fileTypes.add("image/jpeg");
		fileTypes.add("image/bmp");
		fileTypes.add("audio/flac");
		fileTypes.add("audio/mpeg");
		fileTypes.add("audio/x-mpeg");
		fileTypes.add("audio/mp4");
		fileTypes.add("audio/midi");
		fileTypes.add("audio/ogg");
		fileTypes.add("audio/x-wav");
		fileTypes.add("audio/x-ms-wma");
		fileTypes.add("audio/x-aac");
	}
}
