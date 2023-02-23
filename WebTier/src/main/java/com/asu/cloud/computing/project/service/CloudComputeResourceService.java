package com.asu.cloud.computing.project.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.asu.cloud.computing.project.config.Constants;
import com.asu.cloud.computing.project.repository.ComputeRepo;

@Service
public class CloudComputeResourceService {
	@Autowired
	private ComputeRepo computeResourceRepo;
	
	public Integer startResources(Integer count, Integer nameCount) {
		return computeResourceRepo.createNewInstance(Constants.IMAGE_ID, count, nameCount);
	}

	public Integer getNumInstances() {
		
		DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
		Filter runningInstancesFilter = new Filter();
		runningInstancesFilter.setName("instance-state-name");
		runningInstancesFilter.setValues(Arrays.asList(new String[] {"running", "pending"}));
		describeInstancesRequest.setFilters( Arrays.asList(new Filter[] {runningInstancesFilter}));
		describeInstancesRequest.setMaxResults(1000);
		
		DescribeInstancesResult result = computeResourceRepo.descInstances(describeInstancesRequest);
		int countOfRunningInstances = 0;
		for(int i=0; i<result.getReservations().size(); i++) {
			countOfRunningInstances += result.getReservations().get(i).getInstances().size();
		}
		return countOfRunningInstances;
	}
}
