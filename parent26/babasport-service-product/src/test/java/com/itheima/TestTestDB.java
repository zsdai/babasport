package com.itheima;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.core.dao.TestDao;
import com.itheima.core.pojo.TestDB;
import com.itheima.core.service.TestDBService;

@RunWith(SpringJUnit4ClassRunner.class) //与Junit整合
@ContextConfiguration(locations="classpath:application-context.xml")// 加载xml配置文件
public class TestTestDB {
	//@Autowired
	//private TestDao testDao;
	@Autowired
	private TestDBService testDBService;
	@Test
	public void testname() throws Exception {
		TestDB testDB = new TestDB();
		testDB.setId(2);
		testDB.setName("曾志伟2");
		testDB.setBirthday(new Date());
		//testDao.insertTest(testDB);
		testDBService.insertTest(testDB);
	}
}
