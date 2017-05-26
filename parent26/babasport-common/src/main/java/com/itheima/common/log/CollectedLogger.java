package com.itheima.common.log;

import org.apache.log4j.Logger;

public class CollectedLogger {
	
	/** 默认logger名称*/
	public static final String DEFAULT_COLLECTED_NAME = "collectdlog";
	/** 日志写出器*/
	private static Logger collectedLogger;
	
	/** 屏蔽构造函数*/
	private CollectedLogger(){
		
	}

	/**
	 * 获取监控日志写出器logger
	 * @return Logger
	 */
	public static Logger getCollectedLogger(){
		
		if(null == collectedLogger){
			collectedLogger = Logger.getLogger(DEFAULT_COLLECTED_NAME);
		}
		return collectedLogger;
	}
}
