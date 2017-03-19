package com.itheima.core.dao.product;

import java.util.List;

import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.BrandQueryVO;

public interface BrandMapper {

	public List<Brand> queryBrandByCondition(BrandQueryVO queryVO);
	
	public Integer queryTotalCount(BrandQueryVO queryVO);
	
	public Brand queyBrandById(Integer id);
	
	public void updateBrandById(Brand brand);

	public void deleteByIds(Integer[] ids);
}
