package com.asu.cloud.computing.project.config;

import com.amazonaws.regions.Regions;

public class Constants {

	public static final String AWS_CLOUD_ACCESS_KEY = "";

	public static final String AWS_CLOUD_SECRET_KEY = "";

	public static final Regions AWS_REGION = Regions.US_EAST_1;

	public static final String INPUT_QUEUE = "Bandits-SQS-Input-Queue";

	public static final String OUTPUT_QUEUE = "Bandits-SQS-Output-Queue";

	public static final String INPUT_STHREE = "bandits-s3-input";

	public static final String OUTPUT_STHREE = "bandits-s3-output";

	public static final String SECGRP_ID = "sg-02f16e50ec6bbd0b5";
	
	public static final Integer MAX_INSTANCES = 19;
	
	public static final Integer  MAX_REQUESTS_INSTANCE = 5;

	public static final Integer EXISTING_INSTANCES = 1;

	public static final String IMAGE_ID = "ami-0fc32cfa1b75cb4f4";


}
