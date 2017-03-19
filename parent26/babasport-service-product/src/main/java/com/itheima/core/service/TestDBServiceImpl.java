package com.itheima.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.dao.TestDao;
import com.itheima.core.pojo.TestDB;

@Service("testDBService")
@Transactional
public class TestDBServiceImpl implements TestDBService {

	@Autowired
	private TestDao dao; 
	@Override
	public void insertTest(TestDB testDB) {
		dao.insertTest(testDB);
	}

}
