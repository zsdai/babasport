package com.itheima.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.bean.product.SkuQuery;
import com.itheima.core.dao.product.ColorDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;

/**  
**********************************************
* @ClassName: CmsServiceImpl  
* @Description:加载商品详情页面的信息
* @author 代祯山 
* @date 2017年1月2日 下午4:41:01   
**********************************************
*/ 
@Service("cmsService")
public class CmsServiceImpl implements CmsService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	/**  
	* @Title: getProductById  
	* @Description:通过商品的id查询商品对象
	* @param id
	* @return  
	*/  
	public Product getProductById(Long id){
		return productDao.selectByPrimaryKey(id);
	}
	
	/**  
	* @Title: getSkuListByProductId  
	* @Description:通过商品id查询sku对象 ：库存大于0  每一个sku对象加载颜色名称
	* @param id
	* @return  
	*/  
	public List<Sku> getSkuListByProductId(Long id){
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria()
		.andProductIdEqualTo(id)
		.andStockGreaterThan(0);
		List<Sku> skuList = skuDao.selectByExample(skuQuery);
		for (Sku sku : skuList) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skuList;
	}
}
