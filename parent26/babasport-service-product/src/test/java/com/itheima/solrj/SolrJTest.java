package com.itheima.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/*@RunWith(SpringJUnit4ClassRunner.class) //与Junit整合
@ContextConfiguration(locations="classpath:application-context.xml")// 加载xml配置文件
*/public class SolrJTest {

	@Test
	public void test1() throws SolrServerException, IOException{
		SolrServer solrServer = new HttpSolrServer("http://192.168.200.128:8080/solr/collection1");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "2");
		doc.addField("name", "没有就要争取有，有了再说");
		solrServer.add(doc);
		solrServer.commit();
	}
	
	//@Autowired
	private SolrServer solrServer;
	@Test
	public void test2() throws SolrServerException, IOException{
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "3");
		doc.addField("name", "坚持自我");
		solrServer.add(doc);
		solrServer.commit();
	}
	@Test
	public void deleteIndex() throws Exception {
		// 创建HttpSolrServer
		HttpSolrServer server = new HttpSolrServer("http://192.168.200.128:8080/solr/collection1");

		// 根据指定的ID删除索引
		// server.deleteById("c001");

		// 根据条件删除
		//server.deleteByQuery("id:c001");

		// 删除全部（慎用）
		server.deleteByQuery("*:*");

		// 提交
		server.commit();
	}
}
