package com.hhoss.proxy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cglib.proxy.Enhancer;
/*
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
*/

/*
 * 1， CGLIB实现方式是对代理的目标类进行继承

2， 生成出了的代理类可以没方法，生成出来的类可以直接转换成目标类或目标类实现接口的实现类，因JAVA向上转换

明显特点：通过输出看出，看出生成出的代理类的parent类为代理的目标类
cglib-3.0.jar依赖JAR包asm-4.0.jar，asm-util-4.0.jar

 */
public class CGLIBProxy {
	private CGLIBProxy(){}
	/**
	 * spring CGLIB proxy
	 * @param obj  the object been proxied
	 * @param clazz the output object class type
	 * @return the object instance of clazz
	 * if class is interface, it will be better to use JDKProxy directly
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object srcObj, Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		//enhancer.setSuperclass(srcObj.getClass());
		enhancer.setSuperclass(clazz);			
		enhancer.setClassLoader(clazz.getClassLoader());
		enhancer.setCallback(new MethodInterceptorImpl(srcObj));
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
	
	/**
	 * spring CGLIB proxy
	 * @param obj  the object been proxied
	 * @return the object proxied instance
	 * if class is interface, it will be better to use JDKProxy directly
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(T srcObj) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(srcObj.getClass());
		enhancer.setClassLoader(srcObj.getClass().getClassLoader());
		enhancer.setCallback(new MethodInterceptorImpl(srcObj));
		return (T)enhancer.create();
	}

	
	@SuppressWarnings("unchecked")
	public void test(){
	  	  List<String> src = new ArrayList<String>();
	      System.out.println(src.getClass());
	      List<String> obj = (List<String>) CGLIBProxy.get(src);
	      System.out.println(obj.getClass().getSuperclass());
	      System.out.println(obj.getClass());
	      src.add("first");
	      obj.add("second");
	      System.out.println("size is :"+obj.size());
	}

	public static void main(String[] args) {
		new CGLIBProxy().test();
	}

}