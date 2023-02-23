package com.asu.cloud.computing.project.config;

import org.springframework.context.annotation.Bean;

import com.asu.cloud.computing.project.repository.ComputeRepo;
import com.asu.cloud.computing.project.repository.QueueRepo;
import com.asu.cloud.computing.project.repository.SThreeRepo;
import com.asu.cloud.computing.project.service.CloudComputeResourceService;
import com.asu.cloud.computing.project.service.QueueService;
import com.asu.cloud.computing.project.service.ScalingService;
import com.asu.cloud.computing.project.service.StorageService;

public class AppConfig {
	
	@Bean
	public ScalingService loadBalanceService() {
		return new ScalingService();
	}
	
	@Bean
	public QueueService sqsService() {
		return new QueueService();
	}

	@Bean
	public QueueRepo sqsRepo() {
		return new QueueRepo();
	}
	
	@Bean
	public SThreeRepo s3Repo() {
		return new SThreeRepo();
	}

	@Bean
	public StorageService s3Service() {
		return new StorageService();
	}
	
	@Bean
	public ComputeRepo ec2Repo() {
		return new ComputeRepo();
	}

	@Bean
	public CloudComputeResourceService ec2Service() {
		return new CloudComputeResourceService();
	}
	
	@Bean
	public AwsConfiguration awsConfiguration() {
		return new AwsConfiguration();
	}
	
}
