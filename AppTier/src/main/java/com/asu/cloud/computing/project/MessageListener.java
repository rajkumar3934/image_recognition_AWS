package com.asu.cloud.computing.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.asu.cloud.computing.project.config.Config;
import com.asu.cloud.computing.project.service.MsgListenerService;

@SpringBootApplication
public class MessageListener {

	public static void main(String[] args) {
		SpringApplication.run(MessageListener.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		context.getBean(MsgListenerService.class).listenAndExecute();
		context.close();
	}
}
