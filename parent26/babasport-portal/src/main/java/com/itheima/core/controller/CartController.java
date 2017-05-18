package com.itheima.core.controller;

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.common.web.Constans;
import com.itheima.common.web.RequestUtils;
import com.itheima.core.BuyItem;
import com.itheima.core.BuyerCart;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.service.BuyerService;
import com.itheima.core.service.SessionProvider;

@Controller("cartController")
public class CartController {

	@Autowired
	private BuyerService buyerService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	/**  
	* @Title: addItemToCart  
	* @Description:添加商品到购物车
	* @param skuId
	* @param amount
	* @param request
	* @param response
	* @return  
	* @throws Exception 
	*/  
	@RequestMapping("/shopping/buyerCart")
	public String addItemToCart(Long skuId,Integer amount,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		BuyerCart buyerCart=null;
		//1.获取cookie
		Cookie[] cookies = request.getCookies();
		if(null!=cookies&&cookies.length>0){
			for (Cookie cookie : cookies) {
				//2.判断cookie中 是否有购物车
				if(Constans.BUYER_CART.equals(cookie.getName())){
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
				}
			}
		}
		//3.没有 创建购物车
		if(null==buyerCart){
			buyerCart = new BuyerCart();
		}
		
		if(null!=skuId&&null!=amount){
			//4.追加当前商品
			BuyItem buyItem = new BuyItem();
			Sku sku = new Sku();
			sku.setId(skuId);
			buyItem.setSku(sku);
			buyItem.setAmount(amount);
			buyerCart.addItem(buyItem);
		}
		
		String username = sessionProvider.getAttributeOfUsername(RequestUtils.getCSESSIONID(request, response));
		if(null!=username){
			// TODO 已经登陆
			//5.将购物车保存到redis中
			buyerService.addBuyerCartToRedis(buyerCart, username);
			//6.清空cookie，避免下次登陆时，又将已买商品放到购物车中
			Cookie cookie = new Cookie(Constans.BUYER_CART, null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
		}else{
			// TODO 没有登陆
			if(null!=skuId&&null!=amount){
				/*
				 * 自己的方法:合并同款
				 * 往购物车中添加某款商品时，判断该款商品是否已经在购物车中存在了
				 * 1.如果存在了，那么只需要变更amount的值
				 */
				/*boolean flag=true;
				List<BuyItem> items = buyerCart.getItems();
				if(items.size()>0){
					for (BuyItem item : items) {
						if(skuId.equals(item.getSku().getId())){
							item.setAmount(item.getAmount()+amount);
							flag=false;
							break;
						}
					}
				}
				if(flag){
					BuyItem buyItem = new BuyItem();
					Sku sku = new Sku();
					sku.setId(skuId);
					buyItem.setSku(sku);
					buyItem.setAmount(amount);
					buyerCart.addItem(buyItem);
				}*/
				
				StringWriter w = new StringWriter();
				om.writeValue(w, buyerCart);
			//5.更新cookie
				Cookie cookie = new Cookie(Constans.BUYER_CART, w.toString());
				cookie.setPath("/");
				cookie.setMaxAge(60*60*24);
			//6.将cookie写回浏览器
				response.addCookie(cookie);
			}
		}
		
		/*
		 * 7.提示用户添加成功，并显示去购物车结算: 
		 * TODO 不能使用forward，因为是同一个request，所以我们在addItemToCart方法网cookie中添加的新商品
		 * 信息不能够在toCartList中用request取到
		 */
		return "redirect:/shopping/toCartList";
	}
	
	/**  
	* @Title: toCartList  
	* @Description:去购物车页面
	* @param skuId
	* @param amountd
	* @param request
	* @param response
	* @return  
	* @throws Exception 
	*/  
	@RequestMapping("/shopping/toCartList")
	public String toCartList(HttpServletRequest request,HttpServletResponse response
			,Model model) throws Exception{
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		BuyerCart buyerCart=null;
		//1.获取cookie
		Cookie[] cookies = request.getCookies();
		if(null!=cookies&&cookies.length>0){
			for (Cookie cookie : cookies) {
				//2.判断cookie中 是否有购物车
				if(Constans.BUYER_CART.equals(cookie.getName())){
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
				}
			}
		}
		String username = sessionProvider.getAttributeOfUsername(RequestUtils.getCSESSIONID(request, response));
		if(null!=username){
			//TODO 已经登陆
			if(null!=buyerCart){
				//3.存在，放入到redis中，
				buyerService.addBuyerCartToRedis(buyerCart, username);
				//4.清空cookie
				Cookie cookie = new Cookie(Constans.BUYER_CART, null);
				cookie.setPath("/");
				cookie.setMaxAge(0);
			}
			//不存在，什么也不做
			//5.从redis中获取购物车
			buyerCart = buyerService.getBuyerCartFromRedis(username);
			//6.判断有没有
		}else{
			//TODO 未登陆
			//3不存在：创建购物车（空车）可以不创建吗？
			if(null==buyerCart){
				buyerCart = new BuyerCart();
			}
		}
		//4.存在：装备购物车回显的数据
		List<BuyItem> items = buyerCart.getItems();
		if(items.size()>0){//购物车中有商品
			//填充购物车：通过skuid查询出sku对象，在通过sku对象中的productId和colorId查询出product和color对象，
			for (BuyItem buyItem : items) {
				buyItem.setSku(buyerService.getSkuById(buyItem.getSku().getId()));
			}
		}
		//5.跳转到购物车页面
		model.addAttribute("buyerCart", buyerCart);
		return "cart";
	}
	
	/**
	 * 结算
	 * @return
	 */
	@RequestMapping(value="/buyer/trueBuy")
	public String trueBuy(Long[] ids){
		//1、判断用户是否登陆   1）未登陆 跳转到登陆页面 跳转到首页  2）登陆 放行
		//2、判断购物车中是否有商品 1）无商品 刷新购物车页面进行提示 2）有商品  继续判断
		//3、判断购物车中商品是否有货 1）无货 刷新购物车页面进行无货提示 2）有货  真过了进入订单提交页面
		return "order-cart";
	}
}
