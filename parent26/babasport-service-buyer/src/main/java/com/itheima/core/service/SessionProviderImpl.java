package com.itheima.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

public class SessionProviderImpl implements SessionProvider{
	
	@Autowired
	private Jedis jedis;
	
	/**  
	* @Fields exp:session过期时间
	*/ 
	private Integer exp=30;
	
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	//保存用户名到redis中
	/**  
	* @Title: addAtrributeOfUsername  
	* @Description:将用户名保存在分布式redis缓存中
	* @param key  JSESSIONID
	* @param value  
	*/  
	public void addAtrributeOfUsername(String key,String value){
		jedis.set(key+":USER_SESSION", value);
		jedis.expire(key+":USER_SESSION", 60*exp);
	}
	
	/**  
	* @Title: getAttributeOfUsername  
	* @Description:从redis中获取用户名
	* @param key
	* @return  
	*/  
	public String getAttributeOfUsername(String key){
		String username = jedis.get(key+":USER_SESSION");
		//1.session中存在，需要重置session过期时间
		if(null!=username){
			jedis.expire(key+":USER_SESSION", 60*exp);
			return username;
		}
		return null;
	}
	//保存验证码到redis中
	//从redis中获取验证码
}
