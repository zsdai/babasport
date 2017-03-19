package com.itheima.core.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.bean.product.Color;
import com.itheima.core.bean.product.Product;
import com.itheima.core.pojo.product.Brand;
import com.itheima.core.service.product.BrandService;
import com.itheima.core.service.product.ColorService;
import com.itheima.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private ColorService colorService;
	
	@RequestMapping("/product/list.do")
	public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model){
		Pagination pagination = productService.getPaginationByCondition(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		List<Brand> brandList = brandService.queryBrandByCondition("", 1);
		model.addAttribute("brandList", brandList);
		return "product/list";
	}
	@RequestMapping("/product/toAdd.do")
	public String toAdd(Model model){
		List<Brand> brandList = brandService.queryBrandByCondition("", 1);
		model.addAttribute("brandList", brandList);
		List<Color> colors = colorService.getColors();
		model.addAttribute("colorList", colors);
		return "product/add";
	}
	/**  
	* @Title: add  
	* @Description:   添加商品
	* @param product
	* @return  
	*/  
	@RequestMapping("/product/add.do")
	public String add(Product product){
		//保存商品信息
		//库存
		productService.addProduct(product);
		return "redirect:/product/list.do";
	}
	/**  
	* @Title: isSale  
	* @Description: 上架
	* @param ids
	* @param name
	* @param brandId
	* @param isShow
	* @param pageNo  
	*/  
	@RequestMapping("/product/isSale.do")
	public String isSale(Long[] ids,String name,Long brandId,Boolean isShow,Long pageNo){
		productService.isSale(ids);
		return "forward:/product/list.do";
	}
	
}
