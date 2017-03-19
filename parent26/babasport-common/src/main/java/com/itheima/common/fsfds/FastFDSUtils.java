package com.itheima.common.fsfds;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

public class FastFDSUtils {
	
	/**
	 * 上传图片 返回图片路径
	 * @return
	 * @throws Exception 
	 */
	public static String uploadPic(byte[] picBytes,String name,long size) throws Exception{
		
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		String realPath = resource.getClassLoader().getResource("fdfs_client.conf").getPath();
		ClientGlobal.init(realPath);
		/**
		 * 创建Tracker客户端
		 */
		TrackerClient trackerClient = new TrackerClient();
		//TrackerServer服务器返回来给你的storage的地址
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
		String ext = FilenameUtils.getExtension(name);
		//自定义meta信息
		NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("filename",name);
			meta_list[1] = new NameValuePair("fileext",ext);
			meta_list[2] = new NameValuePair("filesize",String.valueOf(size));
		//执行上传，返回路径
		//http://192.168.200.128/group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		//返回的路径如：group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = storageClient1.upload_file1(picBytes, ext, meta_list);
		return path;
	}
}
