package com.smcontactm.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
	
	@Autowired
	private Cloudinary cloudinary;
	

	@Override
	public Map<?, ?> upload(MultipartFile file) {
		try {
			Map<?, ?> data = this.cloudinary.uploader().upload(file.getBytes(), new HashMap<Object, Object>());
			System.out.println("Upload image called"+data);
			return data;
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Image Upload fails");
		}
	
	}


	@Override
	public Map<?, ?> delete(String imageToken) {
		try {
			Map<?,?> deleteImageMap = this.cloudinary.uploader().destroy(imageToken, new HashMap<>());
			System.out.println("Delete image called"+deleteImageMap);
			return deleteImageMap;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error while deleting the file");
		}
		
	}

}
