package com.itheima.common.web;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**  
**********************************************
* @ClassName: RequestUtils  
* @Description:获取csessionid工具类
* @author 代祯山 
* @date 2017年1月6日 下午10:24:58   
**********************************************
*/ 
public class RequestUtils {
	
	public static String getCSESSIONID(HttpServletRequest request,HttpServletResponse response){
		//1.获取cookis
		Cookie[] cookies = request.getCookies();
		if(null!=cookies&&cookies.length>0){
			//2.遍历cookies 判断是否含有csessionid
			for (Cookie cookie : cookies) {
				if("CSESSIONID".equals(cookie.getName())){
					//3.有  直接返回
					return cookie.getValue();
				}
			}
		}
		//4.没有创建csessionid
		String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
		//5. 创建cookie 把csessionid添加到cookie中
		Cookie cookie = new Cookie("CSESSIONID", csessionid);
			//设置cookie路径 来确保每次访问不同的url都会携带上cookie
			cookie.setPath("/");
			//设置cookie存活时间 -1 浏览器关闭时    0立马销毁  >0 以秒为单位
			cookie.setMaxAge(-1);
		//6.写回浏览器
			response.addCookie(cookie);
		//7.返回创建的csessionid
		return csessionid;
	}

}
