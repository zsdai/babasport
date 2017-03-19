package com.itheima.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.itheima.common.web.Constans;
import com.itheima.core.service.UploadService;

@Controller
public class UploadController {
	
	@Autowired
	private UploadService uploadService;
	//上传品牌的图片
	/**
	 * String 跳转视图使用
	 * ModelAndView 无敌  没解耦  放一起  建议不使用
	 * void 不需要跳转视图时使用  异步
	 * @throws Exception 
	 */
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(MultipartFile pic,HttpServletRequest request
			,HttpServletResponse response) throws Exception{
		System.out.println(pic.getOriginalFilename());

		/**
		 * 原始使用方法：
		 */
		/*//精确到毫秒  + 随机数
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = df.format(new Date());
		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			name += r.nextInt(10);
			
		}
		//扩展名
		String ext = FilenameUtils.getExtension(pic.getOriginalFilename());
		
		String p = request.getSession().getServletContext().getRealPath("");
		String path =  "/upload/" + name + "." + ext;
		String url = p + path;
		
		//保存图片到指定位置
		pic.transferTo(new File(url));*/
		/**
		 * 使用fastFDS上传图片
		 */
		//path如:group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		//返回路径
		JSONObject jo = new JSONObject();
		jo.put("path", Constans.IMG_URL+path);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
	}
	
	/**
	 * 上传多张图片
	 * 注意：接受多张图片必须加@RequestParam 
	 * required=false 不必须有值
	 * @param pic
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload/uploadPics.do")
	public @ResponseBody List<String> uploadPics(@RequestParam(required=false)MultipartFile[] pics,HttpServletRequest request
			,HttpServletResponse response) throws Exception{
		List<String> urls= new ArrayList<>();
		String path=null;
		for (MultipartFile pic : pics) {
			path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			urls.add(Constans.IMG_URL+path);
		}
		return urls;
	}
	/**  
	* @Title: uploadFck  
	* @Description:KindEditor上传图片
	* @param request
	* @param response
	* @throws Exception  
	*/  
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//无敌版接受图片
		MultipartRequest mr=(MultipartRequest)request;
		//1或多张
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			//返回路径
			JSONObject jo = new JSONObject();
			// TODO 这里想要kindediter接受返回的json数据  key必须为url
			jo.put("url", Constans.IMG_URL+path);
			jo.put("error", 0);//表示上传无错误
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jo.toString());
		}
	}
	
}
