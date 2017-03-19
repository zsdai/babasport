package com.itheima.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itheima.common.utils.StringUtils;
import com.itheima.common.web.RequestUtils;
import com.itheima.core.bean.user.Buyer;
import com.itheima.core.service.BuyerService;
import com.itheima.core.service.SessionProvider;

/**  
**********************************************
* @ClassName: LoginController  
* @Description:单点登录系统
* @author 代祯山 
* @date 2017年1月5日 下午8:46:59   
**********************************************
*/ 
@Controller
public class LoginController {

	@Autowired
	private BuyerService buyerService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@RequestMapping(value="/login.aspx",method=RequestMethod.GET)
	public String toLogin(){
		return "login";
	}
	@RequestMapping(value="/login.aspx",method=RequestMethod.POST)
	public String toLogin(String username,String password,String ReturnUrl,Model model,
			HttpServletResponse response,HttpServletRequest request){
		//1.判断验证码不能为空
		//2.判断验证码必须正确
		//3.用户名不能为空，
		if(StringUtils.isNotEmpty(username)){
			//4.密码不能为空
			if(StringUtils.isNotEmpty(password)){
				//5.用户名必须存在
				Buyer buyer = buyerService.getBuyerByUsername(username);
				if(null!=buyer){
					//6.密码必须正确
					if(encodePassword(password).equals(buyer.getPassword())){
						//7.将用户放到本地session中（本次放到远程session中 这是尽量少放对象）
						sessionProvider.addAtrributeOfUsername(RequestUtils.getCSESSIONID(request, response), username);
						//8.返回之前访问的页面
						return "redirect:"+ReturnUrl;
					}else{
						model.addAttribute("error", "密码不正确");
					}
					
				}else{
					model.addAttribute("error", "该用户不存在");
				}
			}else{
				model.addAttribute("error", "密码不能为空");
			}
		}else{
			model.addAttribute("error", "用户名不能为空");
		}
		
		return "login";
	}
	
	public String encodePassword(String password){
		/**
		 * 加盐的意思
		 * String jiayan="asfgsdgerrgdf"+password+"sfgsdghehfqjh";
		 */
		char[] encodeHex=null;
		try {
			//第一次 使用MD5加密
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());
			//第二次 使用Hex加密
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return String.valueOf(encodeHex);
	}
	
	/**  
	* @Title: isLogin  
	* @Description:判断用户是否已经登陆
	* 	由jquery和spring配合完成
	* @param callback
	* @param response
	* @param request  
	*/  
	@RequestMapping("/isLogin.aspx")
	public @ResponseBody MappingJacksonValue isLogin(String callback,HttpServletResponse response,HttpServletRequest request){
		Integer result=0;
		String username = sessionProvider.getAttributeOfUsername(RequestUtils.getCSESSIONID(request, response));
		if(StringUtils.isNotEmpty(username)){
			result=1;
		}
		//使用构造函数  传入值
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);//设置回调函数
		return mjv;
	}
}
