package com.hhoss.spring;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import com.hhoss.conf.ResHolder;

public class PlaceHolder extends PropertyPlaceholderConfigurer {
	private Resource[] locations;
	
	public PlaceHolder(){		
		setIgnoreUnresolvablePlaceholders(true);//set default as true;
		setOrder(getOrder()-1);
	}
	
	public @Override void setLocation(Resource location) {
		setLocations( new Resource[] {location});
	}
	public @Override void setLocations(Resource... locations) {
		this.locations = locations;
		super.setLocations(locations);
	}
	
	private Properties loadBucket(Properties propsBucket) throws IOException {
		Properties pnode = propsBucket; 
		if(this.locations != null) for(Resource location : this.locations) {
			PropertiesLoaderUtils.fillProperties(pnode=new ResHolder(location.getFilename(),pnode), location);
		}
		return pnode;
	}

	/**
	 * Return a merged Properties instance containing both the
	 * loaded properties and properties set on this FactoryBean.
	 */
	@Override protected Properties mergeProperties() throws IOException {
		Properties holder = new ResHolder("res.holder.spring.rooter");
		if (this.localOverride){ holder = loadBucket(holder);	}		
		if (this.localProperties != null) for (Properties localProp : this.localProperties) {
			//localProperties defined in bean <props>. inside xml
			holder=new ResHolder("res.holder.spring.inside",holder).lookup("hasher,parent");
			CollectionUtils.mergePropertiesIntoMap(localProp, holder);
		}		
		if(!this.localOverride){ holder = loadBucket(holder); }
		//parser=TokenParser.get(holder,true);
		holder.setProperty("spi.spring.holder.order", String.valueOf(getOrder()));
		SpringProvider.append(holder);
		return holder;
	}
	
	@Override public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			processProperties(beanFactory,  mergeProperties());
		} catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}
	/**
	 * Property placeholder to process the @Scheduled annotation.
	 * 
	 * @return the {@link PropertySourcesPlaceholderConfigurer} to use
	@Bean 
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	 */


}
