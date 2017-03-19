package com.itheima.core.service.product;

import java.util.List;

import com.itheima.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {
	public List<Brand> queryBrandByCondition(String name, Integer isDisplay);

	public Pagination queryBrandByCondition(Integer pageNo, String name, Integer isDisplay);

	public Brand findBrandById(Integer id);

	public void updateBrandById(Brand brand);

	public void deleteByIds(Integer[] ids);
}
