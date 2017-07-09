package com.itheima.common.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class BaLogger {
	

	public static final Logger CONSOLE = LoggerFactory.getLogger("console");
	
	public static final Logger PRODUCT = LoggerFactory.getLogger("product");
	
	public static final Logger COLLECTD = LoggerFactory.getLogger("collectd");
	
	private BaLogger() {
		super();
	}

	
}
