package com.hhoss.spring;
/*
 * @see org.springframework.web.context.ContextLoaderListener
 */

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hhoss.Logger;

/**
 * Bootstrap listener to start up Spring's root {@link WebApplicationContext}.
 * Simply delegates to {@link ContextLoader}.
 *
 * <p>This listener should be registered after
 * {@link org.springframework.web.util.Log4jConfigListener}
 * in <code>web.xml</code>, if the latter is used.
 *
 * @author kejun
 * @see ContextLoaderServlet
 * @see org.springframework.web.util.Log4jConfigListener
 */
@Component @Lazy(false)
public class SpringConfig implements ApplicationContextAware{
	private static final Logger logger = Logger.get();
	private static ContextLoader contextLoader;
	private static ApplicationContext context; 
	/**
	 * initialize the root web application context for standalone/below servlet 3.0.
	 */
	public static void initial(ServletContext servletContext) {
		if( contextLoader == null ){
			contextLoader = new SpringLoader();
			contextLoader.initWebApplicationContext(servletContext);
		}
		//reset(WebApplicationContextUtils.getWebApplicationContext(servletContext));
	}

	/**
	 * Close the root web application context.
	 */
	public static void destroy(ServletContext ctx) {
		if (contextLoader != null) {
			contextLoader.closeWebApplicationContext(ctx);
		}
	}
	
	public static void initLocations(String[] configLocations ){
		setContext(new ClassPathXmlApplicationContext(configLocations));
	}

	/**
	 * use user home files
	 */
	public static void initFiles(String[] files) {
		for (int i = 0; i < files.length; i++) {
			String temppath = System.getProperty("user.dir") + File.separator + "conf" + File.separator + files[i];
			File f = new File(temppath);
			if (!f.exists()) {
				temppath = "classpath:" + files[i];
			}

			logger.info("配置文件{}路径：{}", files[i], temppath);
			files[i] = temppath;
		}		
		setContext(new FileSystemXmlApplicationContext(files));
	}

	
	
	
	/*
	 * ******************************************************************
	 * <bean class="com.hhoss.spring.SpringConfig" lazy-init="false" />
	 * * 
	 * * Description :Using this file to get the entity bean generated by spring.  
	 * * 
	 * * Author : kejun.zheng 
	 * *   
	 * *                         Revision  History
	 * *  Name            Date       Ver                  Description
	 * * ----------------------------------------------------------------
	 * * kejun.zheng      Apr 29, 2008     1.0                 New
	 * *   
	 * ******************************************************************
	 */
	

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext context){
		setContext(context);
	}

	/**
	 * @return ApplicationContext
	 */
	public static ApplicationContext getContext() {
		if (context == null) {
			//context = SpringLoader.getCurrentWebApplicationContext();
			logger.warn("applicationContext is null, please ensure spring***.xml is config and initialized");
			//throw new IllegalStateException("applicationContext is null, please config SpringContext in spring***.xml");
		}
		return context;
	}
	
	/**
	 * @param context
	 * @throws BeansException
	 */
	static void setContext(ApplicationContext ctx) {
		if (ctx!= null) {
			context = ctx;
		}
	}
	
	public static boolean isReady(){
		return context!=null;
	}


}