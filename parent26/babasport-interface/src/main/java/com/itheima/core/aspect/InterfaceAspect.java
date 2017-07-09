package com.itheima.core.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.itheima.common.log.CollectedLogger;

@Aspect
public class InterfaceAspect {
	Logger collectedLogger = CollectedLogger.getCollectedLogger();
	@Around("execution(* com.itheima.core.service.*.*(..))"
			+ "|| execution(* com.itheima.core.service.*.*.*(..))")
	public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable{
		
		// 定义返回对象、得到方法需要的参数
		  Object obj = null;
		  Object[] args = joinPoint.getArgs();
		  MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		  long startTime = System.currentTimeMillis();
		  try {
		   obj = joinPoint.proceed(args);
		  } catch (Exception e) {
			  if(e instanceof Throwable){
				  throw e;
			  }else{
				  collectedLogger.error("统计"+signature.getName()+"方法执行耗时环绕通知出错："+e.getMessage());
				  throw e;
			  }
		  }
		  long endTime = System.currentTimeMillis();
		 
		  collectedLogger.info("[collectd]thirdInter"
				   + "|" +signature.getDeclaringType().getSimpleName()
				   + "|"+signature.getName()
				   + "|" + (endTime - startTime));
		 
		  return obj;
	}
}
