package com.itheima.core.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.common.utils.StringUtils;
import com.itheima.core.BuyItem;
import com.itheima.core.BuyerCart;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.bean.user.Buyer;
import com.itheima.core.dao.product.ColorDao;
import com.itheima.core.dao.product.ProductDao;
import com.itheima.core.dao.product.SkuDao;
import com.itheima.core.dao.user.BuyerDao;

import redis.clients.jedis.Jedis;

/**  
**********************************************
* @ClassName: BuyerServiceImpl  
* @author 代祯山 
* @date 2017年1月5日 下午10:18:08   
* @Description:
* 	查询用户信息
* 	订单
*	 购物车
**********************************************
*/ 
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{

	@Autowired
	private BuyerDao buyerDao;
	
	@Autowired
	private Jedis jedis;
	
	public Buyer getBuyerByUsername(String username){
		
		/**
		 * 使用用户名查询用户信息太慢
		 * 
		 * Mysql 3个库  300万数据  每个库100万  
		 * ID%3 取模=  012  决定存储在哪个库
		 */
		/**
		 * 注册的时候 redis生成一个id，
		 * 数据库保存：id username....
		 * 保存同时redis中存一个key：value的值  key为username  value为生成的id
		 * 
		 */
		String id = jedis.get(username);
		if(StringUtils.isEmpty(id)){
			return null;
		}else{
			
			return buyerDao.selectByPrimaryKey(Long.parseLong(id));
		}
		
	}
	
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ColorDao colorDao;
	/**  
	* @Title: getSkuById  
	* @Description:根据skuId填充购物车中的Item信息
	* @param skuId
	* @return  
	*/  
	public Sku getSkuById(Long skuId){
		//填充购物车：通过skuid查询出sku对象，在通过sku对象中的productId和colorId查询出product和color对象，
		Sku sku = skuDao.selectByPrimaryKey(skuId);
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		return sku;
	}
	
	/**  
	* @Title: addCartProductToRedis  
	* @Description:  将购物车添加到redis中
	*/  
	//5.将购物车保存到redis中
	public void addBuyerCartToRedis(BuyerCart buyerCart,String username){
		//购物车在redis中的id
		String cartIdInRedis = "buyerCart:"+username;
		List<BuyItem> items = buyerCart.getItems();
		if(items.size()>0){
			for (BuyItem item : items) {
				//TODO 具体保存格式 buyerCart:fbb2016 skuId amount
				//判断是否存在同款商品合并
				if(jedis.hexists(cartIdInRedis, String.valueOf(item.getSku().getId()))){
					//存在
					jedis.hincrBy(cartIdInRedis, String.valueOf(item.getSku().getId()), item.getAmount());
				}else{
					//不存在
					jedis.hset(cartIdInRedis,
							String.valueOf(item.getSku().getId()), //
							String.valueOf(item.getAmount()));
				}
			}
		}
	}
	/**  
	* @Title: getBuyerCartFromRedis  
	* @Description:从redis中获取购物车
	* @param username
	* @return  
	*/  
	public BuyerCart getBuyerCartFromRedis(String username){
		BuyerCart buyerCart = new BuyerCart();
		BuyItem buyItem=null;
		Sku sku=null;
		Map<String, String> map = jedis.hgetAll("buyerCart:"+username);
		if(null!=map){
			Set<Entry<String, String>> entrySet = map.entrySet();
			if(null!=entrySet&&entrySet.size()>0){
				for (Entry<String, String> entry : entrySet) {
					sku = new Sku();
					sku.setId(Long.parseLong(entry.getKey()));;//设置skuId
					buyItem = new BuyItem();
					buyItem.setSku(sku);
					buyItem.setAmount(Integer.parseInt(entry.getValue()));//设置购买数量
					//加入购物车
					buyerCart.addItem(buyItem);
				}
			}
		}
		return buyerCart;
	}
}
