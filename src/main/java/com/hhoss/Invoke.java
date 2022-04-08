package com.hhoss;

import java.lang.reflect.Method;

import com.hhoss.spring.SpringBeans;

public class Invoke {
	private static final Logger logger = Logger.get();

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			logger.warn("args is empty! please check!");
		}
		int alen = args.length;
		String arg0 = args[0];
		if (alen == 1) {
			if (arg0.indexOf('.') > 0) {
				invokeByQualifyName(arg0);
			} else {
				invokeBySimpleName(arg0);
			}
		}
	}

	private static Object invokeBySimpleName(String name) throws Exception {
		String[] a = new String[0];
		Object obj = SpringBeans.get(name);
		Method mth = obj.getClass().getMethod("main", a.getClass());
		return mth.invoke(obj, a);
	}

	private static Object invokeByQualifyName(String name) throws Exception {
		String[] a = new String[0];
		Class<?> clazz = Class.forName(name);
		Method method = clazz.getMethod("main", a.getClass());
		return method.invoke(null, a);
	}

	public Object invokeInstanceMethod(String className, String methodName, Object[] args) throws Exception {
		Class clazz = Class.forName(className);
		Object owner = clazz.newInstance();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = clazz.getMethod(methodName, argsClass);
		return method.invoke(owner, args);
	}

	public Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
		Class clazz = Class.forName(className);
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = clazz.getMethod(methodName, argsClass);
		return method.invoke(null, args);
	}
}
