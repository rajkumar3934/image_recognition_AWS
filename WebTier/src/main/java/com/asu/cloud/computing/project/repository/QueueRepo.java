package com.asu.cloud.computing.project.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.asu.cloud.computing.project.config.AwsConfiguration;
@Repository
public class QueueRepo {
	@Autowired
	private AwsConfiguration awsConfig;


	public void deleteMsgs(List<Message> messages, String queueName) {
		List<DeleteMessageBatchRequestEntry> batchEntries = new ArrayList<>();
		for (Message msg : messages) {batchEntries.add(new DeleteMessageBatchRequestEntry(msg.getMessageId(),msg.getReceiptHandle()));}
		awsConfig.awsSQS().deleteMessageBatch(new DeleteMessageBatchRequest(awsConfig.awsSQS().getQueueUrl(queueName).getQueueUrl(), batchEntries));
	}

	public CreateQueueResult createNewQueue(String queueName) {
		return awsConfig.awsSQS().createQueue(queueName);
	}

	public List<Message> receiveMsgs(String queueName, Integer waitTime, Integer visibilityTimeout) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(getQueueUrl(queueName));
		receiveMessageRequest.setMaxNumberOfMessages(10);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		ReceiveMessageResult receiveMessageResult = awsConfig.awsSQS().receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		return messageList.isEmpty() ? null : messageList;
	}

	public void sendMsgs(String messageBody, String queueName, Integer delayinSec) {
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(getQueueUrl(queueName)).withMessageBody(messageBody).withDelaySeconds(delayinSec);
		awsConfig.awsSQS().sendMessage(sendMessageRequest);

	}

	public Integer getApproxNumMsgs(String queueName) {
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("ApproximateNumberOfMessages");
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(getQueueUrl(queueName), attributeNames);
		Map<String, String> map = awsConfig.awsSQS().getQueueAttributes(getQueueAttributesRequest)
				.getAttributes();
		String numberOfMessagesString = (String) map.get("ApproximateNumberOfMessages");
		Integer numberOfMessages = Integer.valueOf(numberOfMessagesString);
		return numberOfMessages;
	}
	
	public String getQueueUrl(String queueName) {
		String queueUrl = null;
		try {
			queueUrl = awsConfig.awsSQS().getQueueUrl(queueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
			CreateQueueResult createQueueResult = this.createNewQueue(queueName);
			queueUrl = awsConfig.awsSQS().getQueueUrl(queueName).getQueueUrl();
		}
		return queueUrl;
	}
}
