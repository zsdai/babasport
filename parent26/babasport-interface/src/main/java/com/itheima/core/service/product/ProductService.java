package com.itheima.core.service.product;

import com.itheima.core.bean.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {
	public Pagination getPaginationByCondition(Integer pageNo,String name,Long brandId,Boolean isShow);
	
	public void addProduct(Product product);
	public void isSale(Long[] ids);
}
