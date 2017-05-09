package com.itheima.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.itheima.common.web.RequestUtils;
import com.itheima.core.service.SessionProvider;

/**
 * 判断用户是否登录拦截器
 * @author Administrator
 *
 */
public class BuyerInterceptor implements HandlerInterceptor{
	
	
	@Autowired
	private SessionProvider sessionProvider;

	//进入handler方法之前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//此请求必须登录
		String username = sessionProvider.getAttributeOfUsername(RequestUtils.getCSESSIONID(request, response));
		if(null==username){
			response.sendRedirect("http://localhost:8082/login.aspx?ReturnUrl=http://localhost:8081/");
			return false;
		}
		//已登录
		return true;
	}

	//进入handler方法之后
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	//页面渲染之后
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
