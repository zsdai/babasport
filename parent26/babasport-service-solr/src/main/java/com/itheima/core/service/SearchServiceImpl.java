package com.itheima.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.ProductQuery;
import com.itheima.core.bean.product.SkuQuery;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**  
**********************************************
* @ClassName: SearchServiceImpl  
* @Description: 首页商品搜索业务方法
* @author 代祯山 
* @date 2016年12月31日 下午12:28:21   
**********************************************
*/ 
@Service("searchService")
public class SearchServiceImpl  implements SearchService{

	/**  
	* @Fields solrServer:
	*/ 
	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private Jedis jedis;
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private SkuDao skuDao;
	public Pagination searchPaginationByQuery(String keyword,Integer pageNo,Long brandId,String price) throws Exception{
		
		SolrQuery solrQuery = new SolrQuery();
		StringBuilder params = new StringBuilder();
		/**
		 * 设置默认查询字段
		 */
			solrQuery.set("df", "name_ik");
		/**
		 *  输入关键字
		 */
			if (StringUtils.isNotEmpty(keyword)) {
				solrQuery.setQuery(keyword);
				params.append("keyword=").append(keyword);
			} else {
				solrQuery.setQuery("*:*");
			}
		/**
		 * 过滤
		 */
			// 1.输入商品分类过滤条件
			if (brandId!=null) {
				solrQuery.addFilterQuery("brandId:" + brandId);
				params.append("&brandId=").append(brandId);
			}

			// 2.输入价格区间过滤条件
			// price的值：0-9 10-19
			if (StringUtils.isNotEmpty(price)) {
				String[] ss = price.split("-");
				if (ss.length == 2) {
					solrQuery.addFilterQuery("price:[" + ss[0] + " TO " + ss[1]+ "]");
				}
				params.append("&price=").append(price);
			}
		
		/**
		 * 分页
		 */
			ProductQuery productQuery = new ProductQuery();
					productQuery.setPageNo(Pagination.cpn(pageNo));
					productQuery.setPageSize(4);
			solrQuery.setStart(productQuery.getStartRow());
			solrQuery.setRows(productQuery.getPageSize());
			/**
			 * 排序
			 */
			if ("1".equals("1")) {
				solrQuery.setSort("price", ORDER.desc);
			} else {
				solrQuery.setSort("price", ORDER.asc);
			}
			/**
			 * 高亮
			 */
			// 设置高亮信息
			solrQuery.setHighlight(true);
			solrQuery.addHighlightField("name_ik");
			solrQuery.setHighlightSimplePre("<span style=\"color:red;font-weight: bold;font-size: 14px;\" >");
			solrQuery.setHighlightSimplePost("</span>");
			QueryResponse queryResponse = solrServer.query(solrQuery);
			SolrDocumentList docs = queryResponse.getResults();
			//去处总条数
			long totalCount = docs.getNumFound();
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			//创建商品集合:将solr中查询的信息填充的product对象中
			List<Product> products = new ArrayList<>();
			Product product;
			for (SolrDocument doc : docs) {
				product= new Product();
				product.setId(Long.parseLong(String.valueOf(doc.get("id"))));
				List<String> list = highlighting.get(doc.get("id")).get("name_ik");
				if(list!=null){
					product.setName(list.get(0));
				}else{
					product.setName(String.valueOf(doc.get("name_ik")));
				}
				product.setPrice((Float)doc.get("price"));
				product.setImgUrl(String.valueOf(doc.get("url")));
				products.add(product);
			}
			//构造分页对象
			Pagination pagination = new Pagination(productQuery.getPageNo(),
					productQuery.getPageSize(),
					(int)totalCount, 
					products);
			
			String url="/Search";
			//分页在页面上展示url参数
			pagination.pageView(url, params.toString());
		return pagination;
	}
	
	/**  
	* @Title: getBrandFromRedis  
	* @Description:查询商品时  从jedis中获取品牌信息
	* @return  
	*/  
	public List<Brand> getBrandFromRedis(){
		List<Brand> brandList = new ArrayList<>();
		Map<String, String> brandMap = jedis.hgetAll("brand");
		if(brandMap!=null){
			Set<Entry<String, String>> entrySet = brandMap.entrySet();
			Brand brand = null;
			for (Entry<String, String> entry : entrySet) {
				brand = new Brand();
				brand.setId(Long.parseLong(entry.getKey()));
				brand.setName(entry.getValue());
				brandList.add(brand);
			}
		}
		return brandList ;
	}
	/**  
	* @Title: saveProductToSolr  
	* @Description: 保存商品信息到solr服务器
	* @param id  
	*/  
	public void saveProductToSolr(Long id){
		SolrInputDocument doc = new SolrInputDocument();
		//商品id
		doc.addField("id", id);
		//商品名称
		Product dbProduct = productDao.selectByPrimaryKey(id);
		doc.addField("name_ik", dbProduct.getName());
		//图片url
		doc.addField("url", dbProduct.getImages()[0]);
		//品牌id
		doc.addField("brandId", dbProduct.getBrandId());
		//商品价格 选择最低价 select price from bbs_sku where product_id = 1 ordery price asc limit 0,1 TODO 值得回味
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		skuQuery.setFields("price");
		doc.addField("price", skuDao.selectByExample(skuQuery).get(0).getPrice());
		
	try {
		solrServer.add(doc);
		solrServer.commit();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
