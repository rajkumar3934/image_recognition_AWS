package com.asu.cloud.computing.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.cloud.computing.project.config.Constants;

@Service
public class ScalingService {
	@Autowired
	private QueueService sqsService;

	@Autowired
	private CloudComputeResourceService ec2Service;

	public void horizontalScale() {
		Integer nameCount = 0;
		while (true) {
			if (sqsService.getApproxNumOfMsgs(Constants.INPUT_QUEUE) > 0 && sqsService.getApproxNumOfMsgs(Constants.INPUT_QUEUE) > ec2Service.getNumInstances() - 1) {
				if (Constants.MAX_INSTANCES - ec2Service.getNumInstances() - 1 > 0) {
					nameCount += ec2Service.startResources(1, nameCount);
					continue;
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
