package com.hhoss.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author kejun
 * @version 1.0.0
 * @date 2015-6-8
 *
 */
public class InvocationHandlerImpl implements InvocationHandler  {
	private Object target;
	
	private InvocationHandlerImpl(){};
	public  InvocationHandlerImpl(Object target){
		this.target=target;
	}

    /**
     * 在代理实例上处理方法调用并返回结果
     * @param proxy   代理对象(注意不是目标对象)
     * @param method  被代理的方法
     * @param args    被代理的方法的参数集
     * @return 返回方法调用结果
     */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	   	return method.invoke(target, args); //实际调用的方法
	}

	public Object invoke2(Object proxy, Method method, Object[] args) throws Throwable {
       long beginTime = System.currentTimeMillis();  //开始时间
       //TimeUnit.MICROSECONDS.sleep(1);
       Object obj = method.invoke(target, args);          //实际调用的方法,并接受方法的返回值
       long endTime = System.currentTimeMillis();   //结束时间
       System.out.println("[" + method.getName() + "] spend " + (endTime - beginTime) + " ms");
       return obj;   //返回实际调用的方法的返回值
	}
		
}
