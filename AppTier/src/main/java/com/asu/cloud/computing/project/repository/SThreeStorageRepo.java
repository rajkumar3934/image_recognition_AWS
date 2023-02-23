package com.asu.cloud.computing.project.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.asu.cloud.computing.project.config.CloudConfiguration;
import com.asu.cloud.computing.project.config.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class SThreeStorageRepo {
	@Autowired
	private CloudConfiguration awsConfiguration;

	public Bucket createNewBucket() {
		Bucket sThreeBucket = null;
		if (awsConfiguration.cloudStorageSThree().doesBucketExistV2(Constants.OUTPUT_STHREE)) {
			sThreeBucket = getExistingBucket();
		} else {
			sThreeBucket = awsConfiguration.cloudStorageSThree().createBucket(Constants.OUTPUT_STHREE);
		}

		return sThreeBucket;
	}

	public Bucket getExistingBucket() {
		Bucket sThreeBucket = null;
		List<Bucket> buckets = awsConfiguration.cloudStorageSThree().listBuckets();
		for(int i=0; i<buckets.size(); i++) {
			if (buckets.get(i).getName().equals(Constants.OUTPUT_STHREE))
				sThreeBucket = buckets.get(i);
		}

		return sThreeBucket;
	}

	public void saveObject(String key, String value) {
		this.createNewBucket();
		@SuppressWarnings("serial")
		Map<String, String> modelResultMap = new HashMap<String, String>() {{
				put(key, value);
		}};
		try {
			String resultString = new ObjectMapper().writeValueAsString(modelResultMap);
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setContentLength(resultString.length());
			InputStream inputStream = new ByteArrayInputStream(resultString.getBytes(StandardCharsets.UTF_8));
			final PutObjectRequest putRequest = new PutObjectRequest(Constants.OUTPUT_STHREE, resultString, inputStream,
					metaData);
			awsConfiguration.cloudStorageSThree().putObject(putRequest);

		} catch (Exception exp) {
		}
	}
}
