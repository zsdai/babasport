package com.itheima.core.service.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.core.bean.product.Color;
import com.itheima.core.bean.product.Product;
import com.itheima.core.bean.product.Sku;
import com.itheima.core.service.CmsService;
import com.itheima.core.service.StaticPageService;
/**  
**********************************************
* @ClassName: CustomMessageListener  
* @Description:
* @author 代祯山 
* @date 2017年1月2日 上午2:24:42   
**********************************************
*/ 
@Component("customMessageListener")
public class CustomMessageListener implements MessageListener{

	@Autowired
	private StaticPageService staticPageService;
	@Autowired
	private CmsService cmsService;
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage mqTextMessage=(ActiveMQTextMessage)message;
		try {
			/**获取消息队列发送过来的消息
			 * 
			 */
			String productId = mqTextMessage.getText();
			System.out.println(productId+"----------------------------------------------");
			/**
			 * 构造静态化页面需要的信息
			 */
			Map<String, Object> rootMap = new HashMap<>();
			//通过商品的id查询商品对象
			Product product = cmsService.getProductById(Long.parseLong(productId));
			//通过商品id查询sku对象 ：库存大于0  加载颜色名称
			List<Sku> skuList = cmsService.getSkuListByProductId(Long.parseLong(productId));
			rootMap.put("product", product);
			rootMap.put("skuList", skuList);
			//去掉重复颜色
			Set<Color> colors = new HashSet<>();
			for (Sku sku : skuList) {
				colors.add(sku.getColor());
			}
			rootMap.put("colors", colors);
			staticPageService.index(rootMap , productId);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
