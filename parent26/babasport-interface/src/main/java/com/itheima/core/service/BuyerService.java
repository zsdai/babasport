package com.itheima.core.service;

import com.itheima.core.BuyerCart;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.bean.user.Buyer;

public interface BuyerService {
	public Buyer getBuyerByUsername(String username);
	public Sku getSkuById(Long skuId);
	public void addBuyerCartToRedis(BuyerCart buyerCart,String username);
	public BuyerCart getBuyerCartFromRedis(String username);
}
