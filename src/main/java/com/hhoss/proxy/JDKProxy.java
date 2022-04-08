package com.hhoss.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.hhoss.Logger;
import com.hhoss.jour.LoggerFactory;
/*
1.JDKProxy实现代理的目标类必须有实现接口
2.生成出来的代理类为接口实现类，和目标类不能进行转换，只能转为接口实现类进行调用
3.明显特点：通过此方法生成出来的类名叫做 $Proxy0
 */
public class JDKProxy {
	private JDKProxy(){}
	/**
	 * JDK proxy.
	 * @param obj  the object been proxy
	 * @param clazz the output object class type
	 * @return the object instance of clazz
	 * if clazz is class, it will be better to use CGLIBProxy directly
	 */
    @SuppressWarnings("unchecked")
	public static <T> T get(final T srcObj) {
		ClassLoader classLoader = srcObj.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,也可以是其他的类加载器*/
		Class<?>[]  interfaces = srcObj.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		InvocationHandler handler =  new InvocationHandlerImpl(srcObj);
		return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
    
	
	public static <T> T getWithTimer(final T srcObj){	
		ClassLoader classLoader = srcObj.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,不一定必须是Logger,也可以是其他的类加载器*/
		Class[]  interfaces = srcObj.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		/*指派方法调用的调用处理程序,这里用了匿名内部类*/
		InvocationHandler handler = new InvocationHandler() {           
	        @Override
	        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	        	Logger logger = Logger.get();
	            long beginTime = System.currentTimeMillis();  //开始时间
	            //java.util.concurrent.TimeUnit.MICROSECONDS.sleep(1);
	            Object obj = method.invoke(srcObj, args);          //实际调用的方法,并接受方法的返回值
	            long endTime = System.currentTimeMillis();   //结束时间
	            logger.debug("[{}] spent {} ms.",method.getName(),(endTime - beginTime));
	            return obj;   //返回实际调用的方法的返回值
	        }
	     };

	     return (T)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}

	
	//example of java standard dynamic proxy, only proxy interfaces!
	protected static org.slf4j.Logger getLogger(){
		org.slf4j.Logger logger = LoggerFactory.getLogger();
		ClassLoader classLoader = logger.getClass().getClassLoader();/*定义代理类的类加载器,用于创建代理对象,不一定必须是Logger,也可以是其他的类加载器*/
		Class[]  interfaces = logger.getClass().getInterfaces(); /*代理类要实现的接口列表*/
		//Class[]  interfaces = {org.slf4j.Logger.class}; 
		InvocationHandler handler = new InvocationHandlerImpl(logger);
		return (Logger)Proxy.newProxyInstance(classLoader, interfaces, handler);
	}
	
	public void test(){
	  	  List<String> src = new ArrayList<String>();
	      System.out.println(src.getClass());
	      List<String> obj = (List<String>) JDKProxy.get(src);
	      System.out.println(obj.getClass().getSuperclass());
	      System.out.println(obj.getClass());
	      src.add("first");
	      obj.add("second");
	      System.out.println("size is :"+obj.size());
	}

	public static void main(String[] args) {
		new JDKProxy().test();
	}


}