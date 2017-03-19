package com.itheima.jedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
@RunWith(SpringJUnit4ClassRunner.class) //与Junit整合
@ContextConfiguration(locations="classpath:application-context.xml")// 加载xml配置文件

public class JedisTest {

	@Autowired
	private Jedis jedis;

	@Test
	public void test1(){
		Jedis jedis1 = new Jedis("192.168.200.128",6379);
		jedis1.set("s1", "1111");
		String s1 = jedis1.get("s1");
		System.out.println(s1);
		jedis1.close();
	}
	@Test
	public void test2(){
		jedis.set("s2", "222222");
		String s1 = jedis.get("s2");
		System.out.println(s1);
		jedis.close();
	}
	/**
	 * 创建一个商品id
	 */
	@Test
	public void test3(){
		jedis.set("pno", "1000");
		String s1 = jedis.get("pno");
		System.out.println(s1);
		jedis.close();
	}
}
