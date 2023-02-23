package com.asu.cloud.computing.project.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.asu.cloud.computing.project.config.AwsConfiguration;
import com.asu.cloud.computing.project.config.Constants;
import com.asu.cloud.computing.project.config.SequenceGenerator;

@Repository
public class ComputeRepo {
	private static final String NAME = "Name for Resource";
	private static final String APP_INSTANCE = "ImageRecognitionApp-Instance";
	private static final String INSTANCE = "instance";
	private static final String b64UDString = "IyEvYmluL2Jhc2gNCnN1ZG8gLXUgdWJ1bnR1IC1IIHNoIC1jICJjZCB+OyBqYXZhIC1qYXIgQXBwVGllci5qYXIiIA0KLS0vLw==";
	

	@Autowired
	private AwsConfiguration awsConfig;

	public Integer createNewInstance(String imageId, Integer maxNumberOfInstances, Integer nameCount) {
		List<String> securityGroupIds = new ArrayList<String>();
		securityGroupIds.add(Constants.SECGRP_ID);
		
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		TagSpecification tagSpecification = new TagSpecification();
		Collection<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();
		int nextVal = SequenceGenerator.generate();
		tag.setValue(APP_INSTANCE + "-" + nextVal);
		tag.setKey(NAME);
		tags.add(new Tag("Name",APP_INSTANCE + "-" + nextVal));
		tags.add(tag);
		tagSpecification.setResourceType(INSTANCE);
		tagSpecification.setTags(tags);
		tagSpecifications.add(tagSpecification);
		RunInstancesRequest runRequest = new RunInstancesRequest(imageId, 1, 1);
		runRequest.setInstanceType(InstanceType.T2Micro);
		runRequest.setSecurityGroupIds(securityGroupIds);
		runRequest.setTagSpecifications(tagSpecifications);
		runRequest.setUserData(b64UDString);
		try {
			awsConfig.awsEC2().runInstances(runRequest);
		} catch (AmazonEC2Exception amzEc2Exp) {
			amzEc2Exp.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nameCount;
	}

	public DescribeInstanceStatusResult descInstance(DescribeInstanceStatusRequest describeRequest) {
		return awsConfig.awsEC2().describeInstanceStatus(describeRequest);
	}

	public DescribeInstancesResult descInstances(DescribeInstancesRequest describeInstancesRequest) {
		return awsConfig.awsEC2().describeInstances(describeInstancesRequest);
	}
}
