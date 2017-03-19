package com.itheima.core.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.pojo.product.Brand;
import com.itheima.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	@RequestMapping("/brand/list.do")
	public String list(Integer pageNo,String name,Integer isDisplay,Model model){
		//List<Brand> brandList = brandService.queryBrandByCondition(name, isDisplay);
		 Pagination pagination = brandService.queryBrandByCondition(pageNo,name, isDisplay);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		return "brand/list";
	}
	
	@RequestMapping("/brand/toEdit.do")
	public String toEdit(Integer id,Model model){
		
		Brand brand=brandService.findBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	
	/**
	 * 提交修改
	 * @param brand
	 * @param model
	 * @return
	 */
	@RequestMapping("/brand/edit.do")
	public String edit(Brand brand,Model model){
		brandService.updateBrandById(brand);
		return "redirect:/brand/list.do";
	}
	/**
	 * 批量删除
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping("/brand/deletes.do")
	public String deletes(Integer[] ids,String name,Integer isDisplay,Integer pageNo,Model model){
		brandService.deleteByIds(ids); 
		/**
		 * 方式一：重定向 
		 * 2次请求  必须通过model把参数放在request域中
		 */
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		model.addAttribute("pageNo", pageNo);
		/**
		 * 方式二：转发
		 * 不需要接受任何参数，因为转发可以使用request域传递参数
		 * 直接使用：return "foward:/brand/list.do";
		 */
		return "redirect:/brand/list.do";
	}
}
