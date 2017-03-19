package com.itheima.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.bean.product.Color;
import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.service.CmsService;
import com.itheima.core.service.SearchService;

import cn.itcast.common.page.Pagination;

@Controller
public class PortalController {

	@Autowired
	private SearchService searchService;
	
	@Autowired
	private CmsService cmsService;
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	/**  
	* @Title: search  
	* @Description: 搜索
	* @return  
	 * @throws Exception 
	*/  
	@RequestMapping("/Search")
	public String search(String keyword,Integer pageNo,Long brandId,String price,Model model) throws Exception{
		/**
		 * 搜索之前 从redis中加载品牌
		 */
		List<Brand> brandList = searchService.getBrandFromRedis();
		model.addAttribute("brandList", brandList);
		
		/**
		 * 已选条件回显
		 */
		Map<String, String> map = new HashMap<>();
		//回显品牌设置
		if(null!=brandId){
			for (Brand brand : brandList) {
				if(brandId.equals(brand.getId())){
					map.put("品牌", brand.getName());
				}
			}
		}
		//回显价格区间设置
		if(null!=price){
			if(price.contains("*")){
				map.put("价格", price.replace("-*", "及以上"));
			}else{
				map.put("价格", price);
			}
		}
		model.addAttribute("map", map);
		/**
		 * 分页
		 */
		Pagination pagination = searchService.searchPaginationByQuery(keyword, pageNo,brandId,price);
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		model.addAttribute("price", price);
		model.addAttribute("brandId", brandId);
		return "search";
	}
	
	@RequestMapping("/product/detail")
	public String toProductDetail(Long id,Model model){
		//通过商品的id查询商品对象
		Product product = cmsService.getProductById(id);
		//通过商品id查询sku对象 ：库存大于0  加载颜色名称
		List<Sku> skuList = cmsService.getSkuListByProductId(id);
		model.addAttribute("product", product);
		model.addAttribute("skuList", skuList);
		//去掉重复颜色
		Set<Color> colors = new HashSet<>();
		for (Sku sku : skuList) {
			colors.add(sku.getColor());
		}
		model.addAttribute("colors", colors);
		return "item";
	}
}
