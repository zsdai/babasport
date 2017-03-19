package com.itheima.core.service;

import java.util.List;

import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.Sku;

public interface CmsService {
	public Product getProductById(Long id);
	public List<Sku> getSkuListByProductId(Long id);
}
