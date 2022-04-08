package com.hhoss.spring;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DynamicBeanTest {
	 public static void main(String[] args) throws BeanDefinitionStoreException, ClassNotFoundException {
		  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "applicationContext.xml");
		        //ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		  
		  DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
		        //TestService testService = new TestServiceImpl();
		        //这里是动态注册单例，不过实例要事先自己new,这种做法不推荐
		        //beanFactory.registerSingleton("testService", testService); 
		       
		       
		        //这里是动态注册非单例bean,把setScope("prototype") 改为setScope("singleton")就是单例注册了，推荐使用这种方式动态注册
		        //有的同学可能会问，采用动态注册Bean有上面好处，好处就是：
		        //你的bean再也不需要在Spring的applicationContext.xml中注册了，也不需要在你的bean上加什么@Component、@Resource、@Service等各种bean相关的注解了
		        //从bean注册中彻底解放了，你甚至可以自己写一个监听器（比如实现一个ServletContextListener），在项目启动时，搜索项目中需要注册的bean通过现在这种方法动态注册，后来新增的bean可以额外再动态注册，是不是感觉世界又美好了很多
		  beanFactory.registerBeanDefinition("testDao", BeanDefinitionBuilder.genericBeanDefinition(Class.forName("com.nicolas.dao.impl.TestDaoImpl")).setScope("prototype").getRawBeanDefinition());
		  beanFactory.registerBeanDefinition("testService", BeanDefinitionBuilder.genericBeanDefinition(Class.forName("com.nicolas.service.impl.TestServiceImpl")).setScope("prototype").addPropertyReference("testDao", "testDao").getRawBeanDefinition());
	  /*
	        TestService testService2 = (TestService)beanFactory.getBean("testService");
	        TestService testService3 = (TestService)beanFactory.getBean("testService");
	       
	        //判断是否为单例，false就表明不是单例
	        //com.nicolas.service.impl.TestServiceImpl这里的包路径你其实可以通过项目某个包下的所有以某某字符串结尾的类就可以获取得到
	        //比如你搜索项目中com.yida.service.impl包下的所有以Impl结尾的类，你得到了类，那通过类就可以知道类的名称，再拼接上包路径，那么
	        //那上面写死的包路径你就可以动态获取了，这里仅仅只是为了测试
	        System.out.println(testService2.equals(testService3));
	       
	        testService2.sayHello();
	        */
	}
}
