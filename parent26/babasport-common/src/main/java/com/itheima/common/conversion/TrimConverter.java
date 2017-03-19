package com.itheima.common.conversion;

import org.springframework.core.convert.converter.Converter;

/**  
**********************************************
* @ClassName: TrimConverter  
* @Description:全局参数去空格转换器
* @author 代祯山 
* @date 2017年1月2日 下午4:20:16   
**********************************************
*/ 
public class TrimConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		try {
			if(source!=null){
				source=source.trim();
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
