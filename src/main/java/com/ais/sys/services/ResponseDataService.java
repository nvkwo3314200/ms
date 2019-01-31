package com.ais.sys.services;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.ais.sys.models.ResponseData;
import com.ais.sys.models.UserInfoModel;

@Service("responseDataService")
public class ResponseDataService {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseDataService.class);

	@Resource
	private MessageSource messageSource;

	@Resource
	private SessionService sessionService;

	private final static String SEPARATOR = "_";
	private final static String IETF_SEPARATOR = "-";
	private final static String EMPTY_STRING = "";
	private final static Locale DEFAULT_LOCALE = Locale.ENGLISH;

	public <T> ResponseData<T> getResponseData(){
		
		ResponseData<T> responseData = new ResponseData<>();
		
		String language = EMPTY_STRING;
		
		UserInfoModel user = sessionService.getCurrentUser();
		
		if (user != null) {
			language = user.getSessionLang();
		}
		
		try {
			
			responseData.setMessageSource(messageSource);
			Locale locale = langToLocale(language, SEPARATOR);
			responseData.setLocale(locale);
			
		} catch (Exception e) {
			
			LOG.error(e.getMessage(), e);
			
		}
		
		return responseData;
		
	}

	public Locale langToLocale(String lang, String separator) {
		if (StringUtils.isEmpty(lang)) {
			return DEFAULT_LOCALE;
		}

		String language = EMPTY_STRING;
		String country = EMPTY_STRING;
		String variant = EMPTY_STRING;
		
		int i1 = lang.indexOf(separator);
		if (i1 < 0) {
			language = lang;
		} else {
			language = lang.substring(0, i1);
			++i1;
			int i2 = lang.indexOf(separator, i1);
			if (i2 < 0) {
				country = lang.substring(i1);
			} else {
				country = lang.substring(i1, i2);
				variant = lang.substring(i2 + 1);
			}
		}

		if (language.length() == 2) {
			language = language.toLowerCase();
		} else {
			language = EMPTY_STRING;
		}

		if (country.length() == 2) {
			country = country.toUpperCase();
		} else {
			country = EMPTY_STRING;
		}

		if ((variant.length() > 0)
				&& ((language.length() == 2) || (country.length() == 2))) {
			variant = variant.toUpperCase();
		} else {
			variant = EMPTY_STRING;
		}
		
		return new Locale(language, country, variant);
		
	}
}