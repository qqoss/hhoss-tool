package com.hhoss.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;

import com.hhoss.jour.LoggerFactory;

/*
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
*/


/**
 * @author kejun
 * @version 1.0.0
 * @date 2015-6-8
 *
 */
public class ProxyUtil  {
  	
	public static <T> T get(final T target){
		ClassLoader classLoader = target.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,不一定必须是Logger,也可以是其他的类加载器*/
		Class[]  interfaces = target.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		InvocationHandler handler =  new InvocationHandlerImpl(target);
		return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}	

	public static <T> T  getObject(Object obj, Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);			
		//enhancer.setClassLoader(clazz.getClassLoader()); sometimes, clazz and object is diff classLoader, will exeption
		enhancer.setCallback(new MethodInterceptorImpl(obj));
		/*
		enhancer.setCallbacks(new Callback[]{new MethodInterceptorImpl(obj), NoOp.INSTANCE});
		enhancer.setCallbackFilter(new CallbackFilter() {
			@Override
			//返回Callbacks的索引
			public int accept(Method method) {
				//对x方法不拦截
				if (method.getName().equals("x"))
					return 1;
				return 0;
			}
		});
		*/
		return (T)enhancer.create();
	}
	
	protected Class getExtendableClass(Object obj){
		Class clazz =  obj.getClass();
		if(Modifier.isFinal(clazz.getModifiers())){//can't be extended
			Class sCls = clazz.getSuperclass() ;
			if(sCls!=null &&  sCls.getSuperclass()!=null ){//has super class(not Object);
				clazz = sCls ;	
			}else if(clazz.getInterfaces().length>0){
				//has interface
				clazz = clazz.getInterfaces()[0] ;	
			}
		}
		return clazz;	
	}
	
	public static <T> T getWithTimer(final T target){	
		ClassLoader classLoader = target.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,不一定必须是Logger,也可以是其他的类加载器*/
		Class[]  interfaces = target.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		/*指派方法调用的调用处理程序,这里用了匿名内部类*/
		InvocationHandler handler = new InvocationHandler() {           
	        @Override
	        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	        	org.slf4j.Logger logger = getLogger();
	            long beginTime = System.currentTimeMillis();  //开始时间
	            //java.util.concurrent.TimeUnit.MICROSECONDS.sleep(1);
	            Object obj = method.invoke(target, args);          //实际调用的方法,并接受方法的返回值
	            long endTime = System.currentTimeMillis();   //结束时间
	            logger.debug("[{}] spent {} ms.",method.getName(),(endTime - beginTime));
	            return obj;   //返回实际调用的方法的返回值
	        }
	     };

	     return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}
	
	protected static org.slf4j.Logger getLogger(){
		org.slf4j.Logger logger = LoggerFactory.refLogger();
		ClassLoader classLoader = logger.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,不一定必须是Logger,也可以是其他的类加载器*/
		Class<?>[]  interfaces = logger.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		//Class[]  interfaces = {org.slf4j.Logger.class}; 
		InvocationHandler handler = new InvocationHandlerImpl(logger);
		return (org.slf4j.Logger)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}
	
}
