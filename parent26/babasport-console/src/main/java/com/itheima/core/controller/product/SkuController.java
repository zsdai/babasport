package com.itheima.core.controller.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itheima.core.bean.product.Sku;
import com.itheima.core.service.product.SkuService;

@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	/**  
	* @Title: list  
	* @Description: 根据商品id查询商品库存信息  
	* @param productId
	* @return  
	*/  
	@RequestMapping("/sku/list.do")
	public String list(Long productId,Model model){
		List<Sku> skuList = skuService.getSkuListByProductId(productId);
		model.addAttribute("skuList", skuList);
		return "sku/list";
	}
	/**  
	* @Title: update  
	* @Description:修改   
	* @param sku  
	 * @throws IOException 
	*/  
	@RequestMapping("/sku/update.do")
	public void update(Sku sku,HttpServletResponse response) throws IOException{
		JSONObject jo = new JSONObject();
		try {
			skuService.updateSku(sku);
			jo.put("message", "保存成功！");
		} catch (Exception e) {
			jo.put("message", "保存失败！");
			e.printStackTrace();
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
	}
	
}
