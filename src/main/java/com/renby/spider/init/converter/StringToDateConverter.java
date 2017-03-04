package com.renby.spider.init.converter;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {
	private final static String[] dateFormats = { "EEE, d MMM yyyy HH:mm:ss z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ssZ", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss" };

	@Override
	public Date convert(String source) {
		try {
			return DateUtils.parseDate(source, dateFormats);
		} catch (ParseException e) {
			return null;
		}
	}

}
