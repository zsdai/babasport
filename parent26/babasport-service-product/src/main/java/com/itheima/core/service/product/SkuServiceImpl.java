package com.itheima.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.core.bean.product.Sku;
import com.itheima.core.bean.product.SkuQuery;
import com.itheima.core.dao.product.ColorDao;
import com.itheima.core.dao.product.SkuDao;

/**  
**********************************************
* @ClassName: SkuServiceImpl  
* @Description: 库存业务 
* @author 代祯山 
* @date 2016年12月28日 下午10:36:59   
**********************************************
*/ 
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService{

	/**  
	* @Fields skuDao:
	*/ 
	@Autowired
	private SkuDao skuDao;
	
	/**  
	* @Fields colorDao:
	*/ 
	@Autowired
	private ColorDao colorDao;
	/**  
	* @Title: get  
	* @Description: 根据商品id查询库存信息,根据库存信息中的颜色id查询颜色名称
	* @param productId
	* @return  
	*/  
	public List<Sku> getSkuListByProductId(Long productId){
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			//mybatis一级缓存 color_id相同 直接使用前面查询的结果
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	/**  
	* @Title: updateSku  
	* @Description:   
	* @param sku  
	*/  
	public void updateSku(Sku sku){
		skuDao.updateByPrimaryKeySelective(sku);
	}
}
