package com.itheima.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.dao.product.BrandMapper;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.pojo.product.BrandQueryVO;
import com.itheima.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandMapper brandMapper;
	
	@Autowired
	private Jedis jedis;

	/**  
	* @Title: queryBrandByCondition  
	* @Description:根据条件查询商品品牌
	* @param name
	* @param isDisplay
	* @return  
	*/  
	@Override
	public List<Brand> queryBrandByCondition(String name, Integer isDisplay) {
		BrandQueryVO queryVO= new BrandQueryVO();
		if(name!=null){
			queryVO.setName(name);
		}
		if(isDisplay!=null){
			queryVO.setIsDisplay(isDisplay);
		}else{
			queryVO.setIsDisplay(0);
		}
		return brandMapper.queryBrandByCondition(queryVO);
	}

	/**  
	* @Title: queryBrandByCondition  
	* @Description:根据条件查询商品品牌分页信息
	* @param pageNo
	* @param name
	* @param isDisplay
	* @return  
	*/  
	@Override
	public Pagination queryBrandByCondition(Integer pageNo, String name, Integer isDisplay) {
		BrandQueryVO queryVO= new BrandQueryVO();
		StringBuffer params =new StringBuffer();
		if(name!=null){
			queryVO.setName(name);
			params.append("&name=").append(name);
		}
		if(isDisplay!=null){
			queryVO.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		}else{
			queryVO.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}
		//cpn 如果小于0 或等于0 或为null  设置为1
		queryVO.setPageNo(Pagination.cpn(pageNo));//调用setPageNo方法时  计算出startRow
		queryVO.setPageSize(3);//调用setPageSize方法时  计算出startRow
		Integer count = brandMapper.queryTotalCount(queryVO);
		Pagination pagination = new Pagination(
				queryVO.getPageNo(),
				queryVO.getPageSize(),
				count);
		//pagination 内部计算传入的页码、每页行数、总记录数 算出来的结果皮否匹配 如果不匹配计算出正确页码
		queryVO.setPageNo(pagination.getPageNo());
		pagination.setList(brandMapper.queryBrandByCondition(queryVO));
		String url="/brand/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	@Override
	public Brand findBrandById(Integer id) {
		return brandMapper.queyBrandById(id);
	}

	/**  
	* @Title: updateBrandById  
	* @Description:修改品牌时 将品牌信息同步更新到redis缓存中  注意顺序
	* @param brand  
	*/  
	@Override
	public void updateBrandById(Brand brand) {
		brandMapper.updateBrandById(brand);
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
	}

	@Override
	public void deleteByIds(Integer[] ids) {
		brandMapper.deleteByIds(ids);
	}
	

}
