package com.itheima.core.service.product;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.ProductQuery;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private Jedis jedis;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public Pagination getPaginationByCondition(Integer pageNo,String name,Long brandId,Boolean isShow){
		ProductQuery productQuery = new ProductQuery();
		StringBuilder params = new StringBuilder();
		if(name!=null){
			productQuery.createCriteria().andNameLike("%"+name+"%");
			params.append("&name=").append(name);
		}
		if(brandId!=null){
			productQuery.createCriteria().andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if(isShow!=null){
			productQuery.createCriteria().andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else{
			productQuery.createCriteria().andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(20);
		productQuery.setOrderByClause("id desc");
		
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				productDao.countByExample(productQuery)
				);
		productQuery.setPageNo(pagination.getPageNo());
		pagination.setList(productDao.selectByExample(productQuery));
		pagination.pageView("/product/list.do", params.toString());
		return pagination;
	}
	/**  
	* @Title: addProduct  
	* @Description:   
	* @param product  
	*/  
	
	public void addProduct(Product product){
		/**
		 * 通过Jedis设置商品id
		 */
		Long id = jedis.incr("pno");
		product.setId(id);
		/**
		 * 设置默认值
		 */
		product.setIsShow(false);//是否上架
		product.setIsDel(true);//是否删除
		product.setCreateTime(new Date());
		
		productDao.insertSelective(product);
		Sku sku=null;
		for(String color:product.getColors().split(",")){
			for(String size:product.getSizes().split(",")){
				sku = new Sku();
				//商品
				sku.setProductId(product.getId());
				//颜色
				sku.setColorId(Long.parseLong(color));
				//尺码
				sku.setSize(size);
				//市场价
				sku.setMarketPrice(0f);
				//售价
				sku.setPrice(0f);
				//运费
				sku.setDeliveFee(0f);
				//库存
				sku.setStock(0);
				//购买限制
				sku.setUpperLimit(200);
				//时间
				sku.setCreateTime(new Date());
				skuDao.insertSelective(sku);
			}
		}
	}
	
	/**  
	* @Title: isSale  
	* @Description:   上架 
	* 	1.更改商品状态 
	* 	2.保存索引到solr服务器 
	* 	3.静态化
	* @param ids  
	*/  
	public void isSale(Long[] ids){
		//使用同一个对象 循环更改id的值 保存
		Product product = new Product();
		product.setIsShow(true);
		for (final Long id : ids) {
			product.setId(id);
			/**
			 * 1.更改商品状态
			 */
			productDao.updateByPrimaryKeySelective(product );
			/**
			 * 2.保存索引到solr服务器 
			 */
			//发送消息
			jmsTemplate.send(new MessageCreator() {
				
				//session相当于管道
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(String.valueOf(id));
				}
			});
			/**
			 * 3.静态化  同样使用以上的发送方法  只需要模式即可（改为发布订阅模式）
			 */
		}
	}
}
