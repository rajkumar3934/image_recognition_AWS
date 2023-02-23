package com.asu.cloud.computing.project.config;

import org.springframework.context.annotation.Bean;

import com.asu.cloud.computing.project.repository.ElasticCloudComputeRepository;
import com.asu.cloud.computing.project.repository.Queuerepository;
import com.asu.cloud.computing.project.repository.SThreeStorageRepo;
import com.asu.cloud.computing.project.service.ElasticCloudComputeResource;
import com.asu.cloud.computing.project.service.MsgListenerService;
import com.asu.cloud.computing.project.service.QueueService;
import com.asu.cloud.computing.project.service.SThreeService;

public class Config {
	
	@Bean
	public CloudConfiguration awsConfiguration() {
		return new CloudConfiguration();
	}

	@Bean
	public SThreeStorageRepo s3Repo() {
		return new SThreeStorageRepo();
	}

	@Bean
	public SThreeService s3Service() {
		return new SThreeService();
	}
	
	@Bean
	public QueueService sqsService() {
		return new QueueService();
	}

	@Bean
	public Queuerepository sqsRepo() {
		return new Queuerepository();
	}
	
	@Bean
	public ElasticCloudComputeRepository ec2Repo() {
		return new ElasticCloudComputeRepository();
	}

	@Bean
	public ElasticCloudComputeResource ec2Service() {
		return new ElasticCloudComputeResource();
	}
	
	@Bean
	public MsgListenerService listenerAndDispatchingService() {
		return new MsgListenerService();
	}

}
