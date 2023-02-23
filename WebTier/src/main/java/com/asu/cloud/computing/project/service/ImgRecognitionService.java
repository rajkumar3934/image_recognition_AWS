package com.asu.cloud.computing.project.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.Message;
import com.asu.cloud.computing.project.config.Constants;

@Service
public class ImgRecognitionService {
	private static final String DOES_NOT_EXISTS = "The image has no name attached";

	private static Hashtable<String, String> hashTable = new Hashtable<String, String>();

	@Autowired
	private QueueService sqsService;

	@Autowired
	private StorageService s3Service;

	@Async
	public String uploadImageFile(final MultipartFile multipartFile) throws IOException {
		String imageName = "";
		try {
			File file = convertMultiPartFileToFile(multipartFile);
			s3Service.uploadFileToSThree(Constants.INPUT_STHREE, file);
			imageName = file.getName();
		} catch (final AmazonServiceException ex) {
			ex.printStackTrace();
		}
		return imageName;
	}

	public void sendImageFileToQueue(String imageName, String fileName) {
		sqsService.sendMessage(imageName, Constants.INPUT_QUEUE, 0);
	}

	private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
		if (Objects.isNull(multipartFile.getOriginalFilename())) {
			throw new RuntimeException(DOES_NOT_EXISTS);
		}
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			outputStream.write(multipartFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			outputStream.close();
		}
		return file;
	}

	public String getFromHashorSQS(String imageName) {
		while (true) {
			String predictedName = hashTable.get(imageName);
			if (predictedName == null) {
				List<Message> outputMessageFromQueue = sqsService.receiveMessage(Constants.OUTPUT_QUEUE, 15, 15);
				if (outputMessageFromQueue == null) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				for (Message outputMsg : outputMessageFromQueue) {

					String outputMessageBodyFromQueue = outputMsg.getBody();
					String[] tokens = outputMessageBodyFromQueue.split(":");
					Integer count = 0;
					String imageNameInQueue = null;
					String prediction = null;
					for (String string : tokens) {
						if (count == 0)
							imageNameInQueue = string;
						else
							prediction = string;
						count++;
					}
					hashTable.put(imageNameInQueue, prediction);
				}
				sqsService.deleteMessage(outputMessageFromQueue, Constants.OUTPUT_QUEUE);
				predictedName = hashTable.get(imageName);
				if (predictedName != null)
					return predictedName;

			} else {
				return predictedName;
			}
		}
	}
}
