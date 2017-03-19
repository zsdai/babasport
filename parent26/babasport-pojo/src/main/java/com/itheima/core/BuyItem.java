package com.itheima.core;

import java.io.Serializable;

import com.itheima.core.bean.product.Sku;

/**  
**********************************************
* @ClassName: BuyItem  
* @Description:购物车中商品款项（某一款商品）
* @author 代祯山 
* @date 2017年1月8日 下午6:18:53   
**********************************************
*/ 
public class BuyItem implements Serializable{

	/**  
	* @Fields serialVersionUID:
	*/ 
	private static final long serialVersionUID = 1L;

	/**  
	* @Fields sku:库存对象，
	* 
	*  因为需要商品名称和商品 图片，所以需要在sku中扩展product
	*  
	*  因为Sku更细（更接近每一款商品），所以选择sku类
	*  商品编号、颜色、只存、数量
	*/ 
	private Sku sku;
	
	private Boolean isHave=true;//是否有货
	
	private Integer amount;//购买数量
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyItem other = (BuyItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Boolean getIsHave() {
		return isHave;
	}

	public void setIsHave(Boolean isHave) {
		this.isHave = isHave;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
