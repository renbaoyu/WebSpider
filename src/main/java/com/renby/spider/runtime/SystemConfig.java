package com.renby.spider.runtime;

import java.io.IOException;
import java.util.Properties;

public class SystemConfig {
	private static final Properties prop = new Properties();
	static{
		try {
			prop.load(SystemConfig.class.getClassLoader().getResourceAsStream("sysconfig.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static String getString(String name){
		return prop.getProperty(name);
	}
	
	public static int getIntValue(String name){
		return Integer.valueOf(prop.getProperty(name));
	}
	
	public static boolean getBoolean(String name){
		return "true".equalsIgnoreCase(prop.getProperty(name));
	}
}
