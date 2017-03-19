package com.itheima.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**  
**********************************************
* @ClassName: StaticPageServiceImpl  
* @Description:静态化服务实现类
* @author 代祯山 
* @date 2017年1月3日 下午10:46:17   
**********************************************
*/ 
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

	/**  
	* @Fields configuration:交由spring实例化
	* 然而Configuration在设置模板路径时必须使用绝对路径
	* 因此可以使用FreeMarkerConfigurer 设置相对路径
	*/ 
	private Configuration conf;
	
//	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	public void index(Map<String, Object> rootMap,String productId){
		//构造出要生成的商品的html文件名称
		String path="/html/product/"+productId+".html";
		String allPath = this.getAllPath(path);
		File file = new File(allPath);
		File parentFile = file.getParentFile();
		boolean exists = parentFile.exists();
		if(!exists){//如果html下子模块不存在则创建，如product，这样也需要将product名称替为参数
			parentFile.mkdirs();
		}
		Writer out=null;
		try {
			//读UTF-8
			Template template = conf.getTemplate("item.html");
			//UTF-8写
			out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			template.process(rootMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null!=out)
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String getAllPath(String path){
		return servletContext.getRealPath(path);
	}
	private ServletContext servletContext;


	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
	}
}
