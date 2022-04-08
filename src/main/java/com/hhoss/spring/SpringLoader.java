package com.hhoss.spring;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * 
 * @author kejun
 */
public class SpringLoader extends ContextLoader{
	private static Class<ApplicationContextInitializer<ConfigurableApplicationContext>> clazz;

	/*
	 * setConfigLocation in SpringInitializer.class
	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext wac) {
		if(!SpringInitializer.hasConfig(servletContext)){
			wac.setConfigLocation(SpringInitializer.getConfigLocation());
			//wac.setConfigLocations(defaultLocations);
		}
		super.customizeContext(servletContext, wac);
	}
	*/
	
	/* 
	 * @see org.springframework.web.context.ContextLoader
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
	throws IllegalStateException, BeansException {
		WebApplicationContext wac = super.initWebApplicationContext(servletContext);
		//then can get wac from  WebApplicationContextUtils.getWebApplicationContext(servletContext)
		SpringContext.setContext(wac);
		return wac;
	}
	 */
	
	@Override
	protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(ServletContext sc) {
		List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes = super.determineContextInitializerClasses(sc);
		if(!classes.contains(getInitializerClass())){
			classes.add(getInitializerClass());
		}
		return classes;
	}
	
	/**
	 * @see super.loadInitializerClass(name)
	 */
	@SuppressWarnings("unchecked")
	private Class<ApplicationContextInitializer<ConfigurableApplicationContext>> getInitializerClass(){
		if( clazz==null ){
			//clazz = (Class<ApplicationContextInitializer<ConfigurableApplicationContext>>) SpringInitializer.class;
			//clazz=(Class<ApplicationContextInitializer<ConfigurableApplicationContext>>) ClassUtils.forName(SpringInitializer.class.getName(),this.getClass().getClassLoader());
			Class<?> claz= SpringInitializer.class;
			clazz = (Class<ApplicationContextInitializer<ConfigurableApplicationContext>>) claz;
		}
		return clazz;
	}

}
