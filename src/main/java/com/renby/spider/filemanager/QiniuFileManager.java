package com.renby.spider.filemanager;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renby.spider.runtime.SystemConfig;

public class QiniuFileManager {
	private static Logger logger = Logger.getLogger(QiniuFileManager.class);
	/**
	 * 
	 * @param file 文件
	 * @param name 文件名，默认不指定key的情况下，以文件内容的hash值作为文件名
	 * @return
	 */
	public static String uploadFile(File file, String name){
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone1());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = SystemConfig.getString("qiniu.accesskey");
		String secretKey = SystemConfig.getString("qiniu.secretkey");
		String host = SystemConfig.getString("qiniu.host");
		String bucket = SystemConfig.getString("qiniu.bucket");
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(file, name, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return host + putRet.key;
		} catch (QiniuException e) {
			logger.error("七牛云存储出错",e);
		    return null;
		}
	}
}
