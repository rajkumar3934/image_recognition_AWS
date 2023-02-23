package com.asu.cloud.computing.project.repository;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.asu.cloud.computing.project.config.CloudConfiguration;
import com.asu.cloud.computing.project.config.Constants;

@Repository
public class Queuerepository {
	@Autowired
	private CloudConfiguration awsConfiguration;
	
	public void putMessage(String messageBody, String queueName, Integer delaySeconds) {
		String queueUrl = null;
		try {
			queueUrl = awsConfiguration.queue().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
			CreateQueueResult createQueueResult = this.createNewQueue(queueName);
		}
		queueUrl = awsConfiguration.queue().getQueueUrl(queueName).getQueueUrl();
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(messageBody).withDelaySeconds(delaySeconds);
		awsConfiguration.queue().sendMessage(sendMessageRequest);

	}
	
	public String parseURL(String urlInput) {
		String name = null;
		String[] tokens = urlInput.split("/");
		for(int i=0; i<tokens.length; i++) {
			name = tokens[i];
		}
		return name;
	}

	public void messageBatchDelete(List<Message> messages, String queueName) {
		String url = awsConfiguration.queue().getQueueUrl(queueName).getQueueUrl();
		List<DeleteMessageBatchRequestEntry> entries = new ArrayList<>();
		for(int i=0; i<messages.size(); i++) {
			entries.add(new DeleteMessageBatchRequestEntry(messages.get(i).getMessageId(), messages.get(i).getReceiptHandle()));
		}
		DeleteMessageBatchRequest batch = new DeleteMessageBatchRequest(url, entries);
		awsConfiguration.queue().deleteMessageBatch(batch);
	}

	public CreateQueueResult createNewQueue(String queueName) {
		return awsConfiguration.queue().createQueue(queueName);
	}

	
	public List<Message> getMessage(String sqsQueue, Integer waitTime, Integer visibilityTimeout) {
		
		String queueUrl = awsConfiguration.queue().getQueueUrl(sqsQueue).getQueueUrl();
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		ReceiveMessageResult receiveMessageResult = awsConfiguration.queue().receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		if(messageList.isEmpty()) {
			return null;
		}else {
			return messageList;
		}
	}
	
	public String imageRecognitionModel(String name) {

		GetObjectRequest req = new GetObjectRequest(Constants.INPUT_STHREE, name);
		S3Object object = awsConfiguration.cloudStorageSThree().getObject(req);
		S3ObjectInputStream objectContent = object.getObjectContent();
		try {
			IOUtils.copy(objectContent, new FileOutputStream("/home/ubuntu/classifier/" + name));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String output = null;
		Process pro;
		try {
			pro = new ProcessBuilder("/bin/bash", "-c",
					"cd  /home/ubuntu/classifier/ && " + "python3 image_classification.py " + name).start();

			pro.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			output = reader.readLine();
			pro.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.trim();
	}
}
