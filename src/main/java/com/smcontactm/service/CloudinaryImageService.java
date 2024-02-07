package com.smcontactm.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryImageService {
	
	public Map<?, ?> upload(MultipartFile file);
	
	public Map<?, ?> delete(String imageToken);
	
}
