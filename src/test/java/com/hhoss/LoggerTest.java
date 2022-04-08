package com.hhoss;

import org.slf4j.LoggerFactory;

public class LoggerTest {
	private static final Logger proxy1 = Logger.get("com.hhoss.Logger$proxy1");
	private static final Logger proxy2 = Logger.get("com.hhoss.Logger$proxy2");
	private static final org.slf4j.Logger slf4j1 = LoggerFactory.getLogger("com.hhoss.Logger$slf4j1");
	private static final org.slf4j.Logger slf4j2 = LoggerFactory.getLogger("com.hhoss.Logger$slf4j2");
	private static final int loops = 50_000;
	
	private static void test1(){
		for(int i=0;i<loops;i++){
			proxy1.trace("proxy loop {}",i);
			proxy2.trace("proxy loop {}",i);
		}
	}
	
	private static void test2(){
		for(int i=0;i<loops;i++){
			slf4j1.trace("slf4j loop {}",i);
			slf4j2.trace("slf4j loop {}",i);
		}
	}
	
	private static void test3(){
		for(int i=0;i<loops;i++){
			proxy1.debug("proxy loop {}",i);
			proxy2.debug("proxy loop {}",i);
		}
	}
	
	private static void test4(){
		for(int i=0;i<loops;i++){
			slf4j1.debug("slf4j loop {}",i);
			slf4j2.debug("slf4j loop {}",i);
		}
	}
	
	public static void main(String[] args) {
		long ts0 = System.currentTimeMillis();
		test1();long ts1 = System.currentTimeMillis();
		test2();long ts2 = System.currentTimeMillis();
		test3();long ts3 = System.currentTimeMillis();
		test4();long ts4 = System.currentTimeMillis();
		slf4j1.debug("slf4j-trace: loop {}, time {} ms, average:{} us",loops*2,ts1-ts0,(ts1-ts0)*1000.0/loops);
		proxy1.debug("proxy-trace: loop {}, time {} ms, average:{} us",loops*2,ts2-ts1,(ts2-ts1)*1000.0/loops);
		slf4j2.debug("slf4j-debug: loop {}, time {} ms, average:{} us",loops*2,ts3-ts2,(ts3-ts2)*1000.0/loops);
		proxy2.debug("proxy-debug: loop {}, time {} ms, average:{} us",loops*2,ts4-ts3,(ts4-ts3)*1000.0/loops);
	}

}
