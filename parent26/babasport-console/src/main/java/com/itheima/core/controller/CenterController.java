package com.itheima.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.TestDB;
import com.itheima.core.service.TestDBService;

/**  
* @ClassName: CenterController  
* @Description: 后台管理中心 
* @author dzs  
* @date 2016年12月24日  
*    
*/  
@Controller
@RequestMapping("/control")
public class CenterController {

	@Autowired
	private TestDBService testDBService;
	
	
	
	@RequestMapping("/index.do")
	public  String index(){
		return "index";
	}
	@RequestMapping("/top.do")
	public  String top(){
		return "top";
	}
	@RequestMapping("/main.do")
	public  String main(){
		return "main";
	}
	@RequestMapping("/left.do")
	public  String left(){
		return "left";
	}
	@RequestMapping("/right.do")
	public  String right(){
		return "right";
	}
	@RequestMapping("/head.do")
	public  String head(){
		return "head";
	}
	@RequestMapping("/date.do")
	public  String date(){
		return "date";
	}
	
	
	@RequestMapping("/frame/product_main.do")
	public  String product_main(){
		return "frame/product_main";
	}
	@RequestMapping("/frame/product_left.do")
	public  String product_left(){
		return "frame/product_left";
	}
	//测试
	@RequestMapping("/test/test.do")
	public String test(){
		TestDB testDB = new TestDB();
		testDB.setId(3);
		testDB.setName("李小龙");
		testDBService.insertTest(testDB );
		return "test";
	}
}
