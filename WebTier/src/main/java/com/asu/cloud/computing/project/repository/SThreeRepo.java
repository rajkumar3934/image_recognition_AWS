package com.asu.cloud.computing.project.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.asu.cloud.computing.project.config.AwsConfiguration;
import com.asu.cloud.computing.project.config.Constants;

@Repository
public class SThreeRepo {
	@Autowired
	private AwsConfiguration awsConfig;
	
	public List<String> getModelResults() {
		List<String> keys = new ArrayList<>();
		for (S3ObjectSummary obj : awsConfig.awsS3().listObjects(new ListObjectsRequest().withBucketName(Constants.OUTPUT_STHREE)).getObjectSummaries()) {
			keys.add(obj.getKey());
		}
		return keys;
	}

	public void uploadObject(final String bucketName, final File file) {
		createBucket(bucketName);
		awsConfig.awsS3().putObject(new PutObjectRequest(bucketName, file.getName(), file));
	}

	public Bucket getBucket(String bucketName) {
		Bucket s3Bucket = null;
		for (Bucket bukt : awsConfig.awsS3().listBuckets()) {
			if (bukt.getName().equals(bucketName))
				s3Bucket = bukt;
		}
		return s3Bucket;
	}
	
	public Bucket createBucket(String bucketName) {
		return awsConfig.awsS3().doesBucketExistV2(bucketName) ? getBucket(bucketName) : awsConfig.awsS3().createBucket(bucketName);
	}
}
