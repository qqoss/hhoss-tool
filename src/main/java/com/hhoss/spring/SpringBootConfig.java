package com.hhoss.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations={"classpath*:res/**/spring-*.xml","classpath*:res/**/spring/*.xml"})
public class SpringBootConfig {

}
