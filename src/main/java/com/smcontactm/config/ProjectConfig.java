package com.smcontactm.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ProjectConfig {
	
	@Bean
	public Cloudinary getCloudinaryConfig() {
		
		Map<Object, Object> config = new HashMap<>();
		
		config.put("cloud_name", "dhdicqnlp");
		config.put("api_key", "162793958734258");
		config.put("api_secret", "Ui2mBNFLpjp5lPrN1x1nYieaF9o");
		config.put("secure", true);
		
		
		return new Cloudinary(config);
	}

}
