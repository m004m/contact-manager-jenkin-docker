package com.smcontactm.controller;

import com.smcontactm.service.CloudinaryImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageUploadCloudinaryController {

	@Autowired
	private CloudinaryImageService cloudinaryImageService;

	@PostMapping("/upload-Image-cloud")
	public ResponseEntity<Map<?, ?>> uploadImageToCloudinary(@RequestParam("Image") MultipartFile file) {

		Map<?, ?> map = this.cloudinaryImageService.upload(file);
		return new ResponseEntity<Map<?, ?>>(map,HttpStatus.OK);
	}

	@GetMapping("/delete-Image-cloud")
	public ResponseEntity<Map<?, ?>> deleteImageCloudinary(@RequestParam("token") String token) {
		Map<?, ?> deleteImgMap = this.cloudinaryImageService.delete(token);
		return new ResponseEntity<Map<?, ?>>(deleteImgMap,HttpStatus.OK);
	}

}
