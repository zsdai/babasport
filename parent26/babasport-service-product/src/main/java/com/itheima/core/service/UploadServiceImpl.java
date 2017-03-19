package com.itheima.core.service;

import org.springframework.stereotype.Service;

import com.itheima.common.fsfds.FastFDSUtils;

@Service("uploadService")
public class UploadServiceImpl implements UploadService{

	@Override
	public String uploadPic(byte[] pic,String name,long size) throws Exception{
		return FastFDSUtils.uploadPic(pic, name, size);
	}
}
