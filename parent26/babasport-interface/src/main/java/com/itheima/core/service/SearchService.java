package com.itheima.core.service;

import java.util.List;

import com.itheima.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface SearchService {
	public Pagination searchPaginationByQuery(String keyword,Integer pageNo,Long brandId,String price) throws Exception;
	public List<Brand> getBrandFromRedis();
	public void saveProductToSolr(Long id);
}
