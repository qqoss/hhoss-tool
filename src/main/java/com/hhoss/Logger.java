package com.hhoss;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.spi.LocationAwareLogger;

import ch.qos.logback.core.CoreConstants;

import com.hhoss.jour.LoggerFactory;
import com.hhoss.proxy.ProxyUtil;
public abstract class Logger implements LocationAwareLogger {	
	public static Logger get() {
		return ProxyUtil.getObject(LoggerFactory.refLogger(), Logger.class);
	}
	public static Logger get(String name){
		return ProxyUtil.getObject(LoggerFactory.getLogger(name), Logger.class);
	}
	public static Logger get(Class<?> clazz){
		return get(clazz.getName());
	}
	   
	public void setLevel(String level) {
		LoggerFactory.setLevel(getName(),level);
	}
	
    public void log(String strLevel, String message, Object... args){
    	int level = LoggerFactory.level(strLevel);
    	if( level>-1 ){
    		log(getMarker(), getName(), level, message, args, null);
    	}
    }
    
    private Marker getMarker(){
    	String fqcn =  getName();
    	int i = fqcn.lastIndexOf(CoreConstants.DOT);
    	if( i>-1 && i<fqcn.length()-1){
    		return MarkerFactory.getMarker(fqcn.substring(i+1));
    	}
    	return MarkerFactory.getMarker(fqcn);
    }

}
