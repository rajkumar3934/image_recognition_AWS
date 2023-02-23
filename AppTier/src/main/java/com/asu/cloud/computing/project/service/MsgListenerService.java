package com.asu.cloud.computing.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;
import com.asu.cloud.computing.project.config.Constants;

@Service
public class MsgListenerService {
	@Autowired
	private QueueService sqsService;

	@Autowired
	private SThreeService s3Service;

	@Autowired
	private ElasticCloudComputeResource ec2Service;

	public void listenAndExecute() {
		while (true) {
			List<Message> inboundMessages = sqsService.getMessage(Constants.INPUT_QUEUE, 0, 10);
			if (inboundMessages == null)
				break;
			for(int i=0; i<inboundMessages.size(); i++) {
				String nameOfImage = inboundMessages.get(i).getBody();
				String predictedValue = sqsService.modelOutput(nameOfImage);
				if (predictedValue == null) {
					predictedValue = "Image Predicted Value:";
				}
				s3Service.saveObject(nameOfImage.substring(0, nameOfImage.length() - 5), predictedValue);
				sqsService.putMessage(nameOfImage + ":" + predictedValue, Constants.OUTPUT_QUEUE, 0);
			}
			
			sqsService.messageBatchDelete(inboundMessages, Constants.INPUT_QUEUE);
		}
		ec2Service.endElasticComputeCloudInstance();
	}
}
