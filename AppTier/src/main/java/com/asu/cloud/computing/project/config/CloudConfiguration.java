package com.asu.cloud.computing.project.config;

import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class CloudConfiguration {

	public BasicAWSCredentials credentials() {
		return new BasicAWSCredentials(Constants.AWS_CLOUD_ACCESS_KEY, Constants.AWS_CLOUD_SECRET_KEY);
	}

	public AmazonS3 cloudStorageSThree() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials())).withRegion(Constants.AWS_REGION).build();
	}

	public AmazonSQS queue() {
		return AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials())).withRegion(Constants.AWS_REGION).build();
	}

	public AmazonEC2 elasticCloudCompute() {
		return AmazonEC2ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials())).withRegion(Constants.AWS_REGION).build();
	}

}
