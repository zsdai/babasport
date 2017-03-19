package com.itheima.core.service.product;

import java.util.List;

import com.itheima.core.bean.product.Sku;

public interface SkuService {
	public List<Sku> getSkuListByProductId(Long productId);
	public void updateSku(Sku sku);
}
