package com.asu.cloud.computing.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.asu.cloud.computing.project.config.AppConfig;
import com.asu.cloud.computing.project.service.ScalingService;


@SpringBootApplication
public class ImageRecognitionApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ImageRecognitionApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		context.getBean(ScalingService.class).horizontalScale();
		context.close();
	}	

}
