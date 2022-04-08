package com.hhoss.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import com.hhoss.Logger;
import com.hhoss.jour.LoggerConfig;
import com.hhoss.spring.SpringConfig;


/**
 * web app listener config
 * @author kejun
 * @see javax.servlet.ServletContainerInitializer   sevlet 3.0 
 * @see org.springframework.web.SpringServletContainerInitializer implements ServletContainerInitializer
 */
@Order(0)
public class WebListener implements WebApplicationInitializer,ServletContextListener{
	Logger logger = Logger.get();
	private static boolean initialized;

	/* 
	 * @see javax.servlet.ServletContextListener
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("WebContextListener initial from ServletContextListener");
		doInitial(event.getServletContext());
	}

	/* 
	 * @see javax.servlet.ServletContextListener
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		doDestroy(event.getServletContext());
	}

	/* 
	 * @see org.springframework.web.WebApplicationInitializer
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//logger.info("WebContextListener initial from WebApplicationInitializer");
		doInitial(servletContext);
	}
	
	private synchronized void doInitial(ServletContext sc){
		if(!initialized){
			initialized=true;
			WebAppConfig.initial(sc);
			LoggerConfig.initial();
			SpringConfig.initial(sc);
			WebContainer.initial(sc);
//			quartzConfig.initial(sc);
			WebAppConfig.setStatus(1);
		}
	}
	
	private synchronized void doDestroy(ServletContext sc){
		if(initialized){
			SpringConfig.destroy(sc); //ContextLoaderListener.contextDestroyed 
			//LoggerConfig.destroy();
			WebAppConfig.destroy();
			initialized=false;
		}
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}

}
