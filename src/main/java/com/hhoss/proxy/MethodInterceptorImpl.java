package com.hhoss.proxy;

import java.lang.reflect.Method;
/*
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
*/

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author kejun
 * @version 1.0.0
 * @date 2015-6-8
 *
 */
public class MethodInterceptorImpl implements MethodInterceptor {
	private Object target;
	
	private MethodInterceptorImpl(){};
	public  MethodInterceptorImpl(final Object target){
		this.target=target;
	}
	
	 @Override
	 public Object intercept(Object proxy, Method method, Object[] args,MethodProxy methodProxy) throws Throwable {
	   Object result = null;
	   prepare();
	  // result = methodProxy.invokeSuper(target, args);//CastException
	  // result = methodProxy.invokeSuper(proxy, args);//java.lang.AbstractMethodError
	   result = method.invoke(target, args);//java.lang.AbstractMethodError
	   cleanup();
	   return result;
	 }

	 protected void prepare(){};
	  
	 protected void cleanup(){};
 
 
}
