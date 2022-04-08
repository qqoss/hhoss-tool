package com.hhoss.proxy;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.HotSwappableTargetSource;

import com.hhoss.Logger;
import com.hhoss.jour.LoggerFactory;

/**
 * @author kejun
 * @version 1.5.0
 * @date 2015-6-8
 *
 */
public class SpringProxy  {
	
	/**
	 * Spring proxy in AOP.
	 * @param obj  the object been proxy
	 * @param clazz the output object class type
	 * @return the object instance of clazz
	 * if clazz is class, it will be better to use CGLIBProxy directly
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object obj, final Class<T> clazz) {
		TargetSource ts = new HotSwappableTargetSource(obj){	
			public Class<T> getTargetClass() {return clazz;}
		};
		return (T)ProxyFactory.getProxy(ts);
	}
	
	/**
	 * JDK proxy.
	 * @param obj  the object been proxy
	 * @param clazz the output object class type
	 * @return the object instance of clazz
	 * if clazz is class, it will be better to use CGLIBProxy directly
	 */
	public static <T> T getObject(final T srcObj){
		return JDKProxy.get(srcObj);
	}	

	/**
	 * CGLIB proxy
	 * @param srcObj  the object been proxy
	 * @param clazz the output object class type
	 * @return the object instance of clazz
	 * if class is interface, it will be better to use JDKProxy directly
	 */
	public static <T> T getObject(Object srcObj, Class<T> clazz) {
		return CGLIBProxy.get(srcObj, clazz);
	}
	
	public static void main(String[] args){
		Object obj = null;
		obj = SpringProxy.get(LoggerFactory.getLogger(), Logger.class);
		System.out.println(obj.getClass());
		System.out.println(obj.getClass().getSuperclass());
		obj = SpringProxy.getObject(LoggerFactory.getLogger(), Logger.class);
		System.out.println(obj.getClass());
		System.out.println(obj.getClass().getSuperclass());
		obj = SpringProxy.getObject(LoggerFactory.getLogger());
		System.out.println(obj.getClass());
		System.out.println(obj.getClass().getSuperclass());
	}
	
}
