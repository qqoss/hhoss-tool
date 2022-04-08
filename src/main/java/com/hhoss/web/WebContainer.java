package com.hhoss.web;

import java.util.Date;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.springframework.context.ApplicationContextAware;

import com.hhoss.Logger;
import com.hhoss.boot.App;
import com.hhoss.conf.ResHolder;
import com.hhoss.spring.SpringConfig;
import com.hhoss.util.convert.DateConverter;

//@HandlesTypes({ StatViewServlet.class }) 
public class WebContainer implements ServletContainerInitializer{
	private static final Logger logger = Logger.get();
	private static final ResHolder conf = (ResHolder)App.getProperties("res.app.hhoss.tool");

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
	 * 
	 * 需要在jar包含META-INF/services/javax.servlet.ServletContainerInitializer文件，
	 * 文件内容为已经实现ServletContainerInitializer接口的类：com.hhoss.web.WebDynamicConfig
	 */
	@Override
	public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
		logger.info("CustomServletContainerInitializer is loading ..."); 
		for(Class<?> cls: set){
			addHandle(cls,ctx);
		}
	}
	
	public static void initial(ServletContext ctx){
		regConvert();
		regHandler(ctx);
	}
	
	protected static void regConvert(){
		ConvertUtils.register(new StringConverter(""), String.class);
		ConvertUtils.register(new IntegerConverter(), Integer.class);
		ConvertUtils.register(new LongConverter(), Long.class);
		ConvertUtils.register(new DoubleConverter(), Double.class);
		ConvertUtils.register(new FloatConverter(), Float.class);
		ConvertUtils.register(new DateConverter(), Date.class);
	}
	
	protected static void regHandler(ServletContext sc){ 
		//TODO should by config
		addStruts2(sc);
		addSpringMVC(sc);
		List<String> handles = conf.getList("web.container.service.handle");
		for(String handle : handles){
			if(handle.length()>1)addHandle(handle,sc);
		}
		/*
		addHandle("com.hhoss.web.filters.EncodingFilter",sc);
		addHandle("com.alibaba.druid.support.http.StatViewServlet",sc);
		addHandle("com.alibaba.dubbo.remoting.http.servlet.BootstrapListener",sc);
		*/
	}
	
	private static void addHandle(String clsName, ServletContext sc){
		try {
			addHandle(Class.forName(clsName),sc);
		} catch (ClassNotFoundException e) {
			logger.info("ClassNotFoundException:{}", clsName); 
		} 
	}
	
	@SuppressWarnings("unchecked")
	private static void addHandle(Class<?> cls, ServletContext ctx){
		if(cls==null){return;}
		if(ServletContextListener.class.isAssignableFrom(cls)){
			addListener(ctx,(Class<EventListener>)cls);
		}
		if(Servlet.class.isAssignableFrom(cls)){
			addServlet(ctx,(Class<Servlet>)cls);
		}
		if(Filter.class.isAssignableFrom(cls)){
			addFilter(ctx,(Class<Filter>)cls);
		}				
	}
	
	private static void addServlet(ServletContext ctx, Class<Servlet> cls){
		logger.info("adding {} {}","servlet", cls.getName()); 
		String name = cls.getSimpleName();
	    String mapping = conf.getProperty("web.servlet."+name+".mapping","/"+name);
	    //ServletRegistration.Dynamic servlet = ctx.addServlet(name, cls); 
	    ctx.addServlet(name, cls).addMapping(mapping); 
		// servlet.setInitParameter("servletInitName", "servletInitValue"); 
	}
	
	private static void addListener(ServletContext ctx, Class<EventListener> cls){
		logger.info("adding {} {}","listener", cls.getName()); 
	    ctx.addListener((Class<EventListener>)cls); 
	}
	
	private static void addFilter(ServletContext ctx, Class<Filter> cls){
		logger.info("adding {} {}","filter",  cls.getName()); 
	    EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class); 
	    dispatcherTypes.add(DispatcherType.REQUEST); 
	    dispatcherTypes.add(DispatcherType.FORWARD); 
	    dispatcherTypes.add(DispatcherType.ASYNC); 
	    String mapping = conf.getProperty("web.filter."+cls.getSimpleName()+".mapping","/*");
	    FilterRegistration.Dynamic register = ctx.addFilter(cls.getSimpleName(), cls); 
	    register.addMappingForUrlPatterns(dispatcherTypes, true, mapping.split(";")); 
	   //register.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, cls.getSimpleName()); 		
	}
	
	private static void addSpringMVC(ServletContext sc){
		//addHandle(Class.forName("org.springframework.web.filter.CharacterEncodingFilter"),sc);
		try{
			//AnnotationConfigWebApplicationContext webApplicationContext =new AnnotationConfigWebApplicationContext(); 
	        //webApplicationContext.register(SpringInitializer.class);>>SpringConfig.getContext()
	        Object servlet = Class.forName("org.springframework.web.servlet.DispatcherServlet").newInstance();
        	((ApplicationContextAware)servlet).setApplicationContext(SpringConfig.getContext());
        	ServletRegistration.Dynamic register=sc.addServlet("springDispatcherServlet", (Servlet)servlet);
        	//register.setInitParameter("contextConfigLocation", "classpath*:res/**/*-mvc.xml,");  
        	register.setLoadOnStartup(1);  
        	register.addMapping("/");  
		}catch(Exception e){
			logger.warn("register Spring-MVC Servlet fail: {}.",e.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addStruts2(ServletContext sc){
		try{
	        Class filter = Class.forName("org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter");
 			FilterRegistration.Dynamic register=sc.addFilter("struts2Filter", filter);
        	register.setInitParameter("config", "struts-default.xml,struts-plugin.xml,struts.xml,res/config/struts/app-start.xml");  
        	register.addMappingForUrlPatterns(null, true, "/*");//null means DispatcherType.REQUEST
		}catch(Exception e){
			logger.warn("register Struts2 Filter fail: {}.",e.getMessage());
		}
	}


}
