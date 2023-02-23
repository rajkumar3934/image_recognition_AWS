package com.asu.cloud.computing.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.cloud.computing.project.repository.ElasticCloudComputeRepository;

@Service
public class ElasticCloudComputeResource {
	
	@Autowired
	private ElasticCloudComputeRepository ec2Repo;

	public void endElasticComputeCloudInstance() {
		ec2Repo.endEC2();
	}
}
