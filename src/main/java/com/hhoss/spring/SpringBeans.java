package com.hhoss.spring;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

public abstract class SpringBeans{
	
	@SuppressWarnings("unchecked")
	public static <T> T get(String name){
		return (T)(getContext().getBean(name));
	}
	
	/**
	 * 获取类型为requiredType的对象
	 * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	 * 
	 * @param name
	 *            bean注册名
	 * @param requiredType
	 *            返回对象类型
	 * @return Object 返回requiredType类型对象
	 * @throws BeansException
	 */
	public static <T> T get(String name, Class<T> requiredType) throws BeansException {
		return getContext().getBean(name, requiredType);
	}
	
	/**
	 * @param klass the class for get instance
	 * @return object<T> 从静态变量ApplicationContext中取得Bean, 根据clazz返回bean, 有多个的时只返回第1个。
	 */
	public static <T> T get(Class<T> klass) {
		return get(klass, false);
	}
	
	/**
	 * @param klass the class for get instance
	 * @param create if not found in context, whether we will create one, we need ensure all auto wired fields are exists if set true!
	 * @return object<T> 从静态变量ApplicationContext中取得Bean, 根据clazz返回bean, 有多个的时只返回第1个,如果没有创建过，则按默认装配，并返回。
	 */
	public static <T> T get(Class<T> klass, boolean create) {
		Map<String,T> map = getContext().getBeansOfType(klass);
		if (!map.isEmpty()) {
			return map.entrySet().iterator().next().getValue();
		} else if(create) {
			return getContext().getAutowireCapableBeanFactory().createBean(klass);
		}		
		throw new IllegalStateException("can't find bean for class :" + klass);

	}
	
	public static <T> T create(Class<T> klass){
		//ConfigurableApplicationContext.getBeanFactory().createBean(klass);
		//BeanFactoryUtils.beansOfTypeIncludingAncestors(context, klass, true, false);
		return getContext().getAutowireCapableBeanFactory().createBean(klass);
	}


	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * 
	 * @param name
	 * @return boolean
	 */
	public static boolean contain(String name) {
		return getContext().containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * 
	 * @param name
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return getContext().isSingleton(name);
	}

	/**
	 * @param name
	 * @return Class 注册对象的类型
	 * @throws NoSuchBeanDefinitionException
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return getContext().getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return getContext().getAliases(name);
	}
	
	private static ApplicationContext context;
	private static ApplicationContext getContext(){
		 if( context==null ){
			 context = SpringConfig.getContext();
			// appContext = new ClassPathXmlApplicationContext("/score.xml");
		 }	
		 return context;
		
	}	
	
	/**
	 * @See {@link org.springframework.web.context.support.SpringBeanAutowiringSupport#processInjectionBasedOnCurrentContext(Object)}
	 * @param bean
	 * @return
	 */
	public static <T> T inject(T bean) {
		if(bean==null || getContext()==null){
			throw new IllegalStateException("bean or SpringContext is null.");
		}else {
			// getContext().getAutowireCapableBeanFactory().autowireBean(bean);
			AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
			bpp.setBeanFactory(getContext().getAutowireCapableBeanFactory());
			bpp.processInjection(bean);
		}
		return bean;
	}


}
