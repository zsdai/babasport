package com.itheima.core.service.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.core.service.SearchService;
/**  
**********************************************
* @ClassName: CustomMessageListener  
* @Description:自定义消息处理器类
* @author 代祯山 
* @date 2017年1月2日 上午2:24:42   
**********************************************
*/ 
@Component("customMessageListener")
public class CustomMessageListener implements MessageListener{

	@Autowired
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage mqTextMessage=(ActiveMQTextMessage)message;
		try {
			String productId = mqTextMessage.getText();
			System.out.println(productId+"----------------------------------------------");
			searchService.saveProductToSolr(Long.parseLong(productId));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
