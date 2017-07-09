package com.itheima.core.aspect;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.itheima.common.log.BaLogger;
import com.itheima.common.log.CollectedLogger;

@Aspect
public class SQLAspect {
	
	Logger collectedLogger = CollectedLogger.getCollectedLogger();
	
	@Around("execution(* com.itheima.core.dao.*.*(..))"
			+ "|| execution(* com.itheima.core.dao.*.*.*(..))")
	public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable{
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		StopWatch clock = new StopWatch();
		clock.start(); // 计时开始
		Object[] args = joinPoint.getArgs();
		Object obj = null;
		try {
			obj = joinPoint.proceed(args);
		} catch (Exception e) {
			if(e instanceof Throwable){
				  throw e;
			  }else{
				  collectedLogger.error("统计"+signature.getName()+"-SQL耗时时间错误："+e.getMessage());
			  }
		}
		clock.stop();
		Long time = clock.getTime();
		BaLogger.COLLECTD.info("[collectd]sqltime"
				+"|"+signature.getDeclaringType().getSimpleName()
				+"|"+signature.getName()
				+"|"+time);
		return obj;
	}
}
