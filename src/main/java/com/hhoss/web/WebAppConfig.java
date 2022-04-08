package com.hhoss.web;

import java.util.Properties;

import javax.servlet.ServletContext;

import com.hhoss.Logger;
import com.hhoss.boot.Web;
import com.hhoss.util.Iterators;

/**
 * web app listener config
 * 
 * @author kejun
 * 
 */
public abstract class WebAppConfig extends Web {
	private static final Logger logger = Logger.get();

	public static void initial(final ServletContext sc) {
		logger.info("WebConfig initialze starting.");
		new Web().initial(getInitParams(sc));
		logger.info("WebConfig initialze finished.");
	}

	private static Properties getInitParams(ServletContext sc) {
		Properties props = new Properties();
		props.setProperty(WEB_REALITY_PATH, sc.getRealPath(""));
		props.setProperty(WEB_CONTEXT_PATH, sc.getContextPath());
		props.setProperty(WEB_CONTEXT_NAME, sc.getServletContextName());
		for (String name : Iterators.of(sc.getInitParameterNames())) {
			props.setProperty(name, sc.getInitParameter(name));
		}
		return props;
	}

}
