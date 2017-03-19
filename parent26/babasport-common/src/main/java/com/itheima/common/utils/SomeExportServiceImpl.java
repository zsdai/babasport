package com.itheima.common.utils;

import java.util.List;

import org.springframework.stereotype.Service;

/**  
**********************************************
* @ClassName: SomeExportServiceImpl  
* @Description:某个业务到处实现
* @author 代祯山 
* @date 2017年2月12日 下午4:32:33   
**********************************************
*/
@Service("someExportService")
public class SomeExportServiceImpl implements HxlsOptRowsInterface {

	/**  
	* @Title: optRows  
	* @Description:处理读取到的某一行的数据
	* @param sheetIndex sheet号
	* @param curRow 当前行
	* @param rowlist 当前行的数据
	* @return 成功返回success 失败返回失败原因
	* @throws Exception
	*/  
	@Override
	public String optRows(int sheetIndex, int curRow, List<String> rowlist) throws Exception {
		
		return null;
	}

}
