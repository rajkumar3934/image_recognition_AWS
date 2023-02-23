package com.asu.cloud.computing.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;
import com.asu.cloud.computing.project.config.CloudConfiguration;

@Repository
public class ElasticCloudComputeRepository {
	@Autowired
	private CloudConfiguration awsConfiguration;

	public void endEC2() {
		String currentInstanceId = EC2MetadataUtils.getInstanceId();
		if(currentInstanceId !=null) {
			TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest().withInstanceIds(currentInstanceId);
			awsConfiguration.elasticCloudCompute().terminateInstances(terminateRequest);
		}
	}
}
