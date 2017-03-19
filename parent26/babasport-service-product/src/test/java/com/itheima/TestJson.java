package com.itheima;

import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.core.bean.user.Buyer;

public class TestJson {

	/**  
	* @Title: testJson  
	* @Description:requestBody和responseBody底层转json串类
	* jkson
	* @throws Exception  
	*/  
	@Test
	public void testJson() throws Exception{
		Buyer buyer = new Buyer();
		buyer.setUsername("创造奇迹");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		StringWriter w = new StringWriter();
		objectMapper.writeValue(w, buyer);
		//有对象转化为String
		System.out.println(w.toString());
		
		//由String转化为对象
		Buyer buyer1 = objectMapper.readValue(w.toString(), Buyer.class);
		System.out.println(buyer1.toString());
		w.close();
	}
}
