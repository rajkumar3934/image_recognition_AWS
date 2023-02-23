package com.asu.cloud.computing.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.asu.cloud.computing.project.repository.QueueRepo;

@Service
public class QueueService {
	@Autowired
	private QueueRepo sqsRepo;

	public void deleteMessage(List<Message> message, String queueName) {
		sqsRepo.deleteMsgs(message, queueName);
	}

	public CreateQueueResult createQueue(String queueName) {
		return sqsRepo.createNewQueue(queueName);
	}

	public List<Message> receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		return sqsRepo.receiveMsgs(queueName, waitTime, visibilityTimeout);
	}

	public void sendMessage(String messageBody, String queueName, Integer delaySeconds) {
		sqsRepo.sendMsgs(messageBody, queueName, delaySeconds);
	}

	public Integer getApproxNumOfMsgs(String queueName) {
		return sqsRepo.getApproxNumMsgs(queueName);
	}
}
