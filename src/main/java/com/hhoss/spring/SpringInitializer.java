package com.hhoss.spring;

import static org.springframework.web.context.ContextLoader.CONFIG_LOCATION_PARAM;
import static org.springframework.web.context.support.XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.hhoss.boot.App;

//@Configuration
//@ComponentScan(basePackages="com.hhoss")

//@EnableWebMvc
//@ImportResource({"classpath*:res/**/spring/*.xml","classpath*:res/**/spring-*.xml"}) 
//@Import({DataSourceConfig.class})  

public class SpringInitializer<C extends ConfigurableWebApplicationContext> implements ApplicationContextInitializer<C> {
	private static final String defaultLocations = "classpath*:res/**/spring-*.xml, classpath*:res/**/spring/*.xml";
	private static final String jerseyNONE = "<NONE>";//spring boot JerseyWebApplicationInitializer set as <NONE>!
	/*
	'classpath:'= uses a single classpath directory for the search
	'classpath*:'=uses a multi classpath directory for the search
	more wildcard in spring rules @see >>  Spring中使用classpath加载配置文件浅析
	XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
	**/	
	//before here, have initialized by context-param;
	//so if no configed then use properties;	

	@Override
	public void initialize(C wac) {
		//if(!validConfig(wac.getServletContext())){
		String[] locs = wac.getConfigLocations();//if null will return DEFAULT_CONFIG_LOCATION
		if( locs==null || locs.length==0 || locs[0]==null || jerseyNONE.equals(locs[0]) ){			
			wac.setConfigLocation(getConfigLocation());					
		}else if(locs.length==1&&DEFAULT_CONFIG_LOCATION.equals(locs[0])){
			if(hasConfig(wac.getServletContext())){
				wac.setConfigLocation(DEFAULT_CONFIG_LOCATION+","+getConfigLocation());	
			}else{
				wac.setConfigLocation(getConfigLocation());	
			}
		}else{
			//has been config with some paths
		}
	}
	
	protected static boolean hasConfig(ServletContext servletContext){
		String scc = getConfigProp();
		//String scc=servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if(scc==null || jerseyNONE.equals(scc)){
			File defaultConfigFile = new File(App.getRootPath(),DEFAULT_CONFIG_LOCATION);
			return defaultConfigFile.exists();
		}
		return scc.trim().length()>0;
	}
	
	/**
	 * @return spring config location path
	 */
	protected static String getConfigLocation(){
		//"contextConfigLocation"
		String configPath = getConfigProp();
		if( configPath==null||configPath.trim().isEmpty()||"<NONE>".equals(configPath)){
			configPath=defaultLocations;
		}
		return configPath;
	}
	
	private static String getConfigProp(){
		return new App(){String l=guessProperty(CONFIG_LOCATION_PARAM);}.l;
	}

}
