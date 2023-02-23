package com.asu.cloud.computing.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.asu.cloud.computing.project.repository.Queuerepository;

@Service
public class QueueService {
	@Autowired
	private Queuerepository sqsRepo;
	
	public void messageBatchDelete(List<Message> messages, String queueName) {
		sqsRepo.messageBatchDelete(messages, queueName);
	}
	
	public CreateQueueResult createNewQueue(String queueName) {
		return sqsRepo.createNewQueue(queueName);
	}
	
	public String modelOutput(String imageName) {
		return sqsRepo.imageRecognitionModel(imageName);
	}
	
	public List<Message> getMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		return sqsRepo.getMessage(queueName, waitTime, visibilityTimeout);
	}

	public void putMessage(String messageBody, String queueName, Integer delaySeconds) {
		sqsRepo.putMessage(messageBody, queueName, delaySeconds);
	}
}
