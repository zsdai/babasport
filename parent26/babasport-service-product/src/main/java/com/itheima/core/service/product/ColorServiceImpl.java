package com.itheima.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itheima.core.bean.product.Color;
import com.itheima.core.bean.product.ColorQuery;
import com.itheima.core.dao.product.ColorDao;

@Service("colorService")
public class ColorServiceImpl  implements ColorService{

	@Autowired
	private ColorDao colorDao;
	
	public List<Color> getColors(){
		ColorQuery colorQuery =  new  ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0l);
		return colorDao.selectByExample(colorQuery);
	}
}
