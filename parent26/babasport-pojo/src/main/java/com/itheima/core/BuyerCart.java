package com.itheima.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BuyerCart implements Serializable {

	/**  
	* @Fields serialVersionUID:
	*/ 
	private static final long serialVersionUID = 1L;
	
	/**  
	* @Fields items:购物项结果集
	*/ 
	private List<BuyItem> items = new ArrayList<>();

	//小计：数量  商品金额  运费  总计
	
	public void addItem(BuyItem buyItem){
		//如果是同款
		if(items.contains(buyItem)){
			for (BuyItem item : items) {
				if(item.equals(buyItem)){
					item.setAmount(item.getAmount()+buyItem.getAmount());
				}
			}
		}else{
			items.add(buyItem);
		}
	}
	public List<BuyItem> getItems() {
		return items;
	}

	public void setItems(List<BuyItem> items) {
		this.items = items;
	}
	
	/**  
	* @Title: getProdcutAmount  
	* @Description:商品总数量
	* @return  
	*/  
	@JsonIgnore//由于需要转换为json字符串，必须是要标准的javabean，（get/set）因此我们需要忽略此项
	public Integer getProdcutAmount(){
		Integer result=0;
		for (BuyItem item : items) {
			result+=item.getAmount();
		}
		return result;
	}

	/**  
	* @Title: getProductPrice  
	* @Description:商品总价
	* @return  
	*/ 
	@JsonIgnore
	public Float getProductPrice(){
		Float result=0f;
		for (BuyItem item : items) {
			result+=item.getAmount()*item.getSku().getPrice();
		}
		return result;
	}
	
	/**  
	* @Title: getFee  
	* @Description:运费
	* @return  
	*/  
	@JsonIgnore
	public Float getFee(){
		Float result=0f;
		if(getProductPrice()<75){
			result=5f;
		}
		return result;
	}
	
	/**  
	* @Title: getTotal  
	* @Description:总计（合计）
	* @return  
	*/ 
	@JsonIgnore
	public Float getTotal(){
		return getProductPrice()+getFee();
	}
}
