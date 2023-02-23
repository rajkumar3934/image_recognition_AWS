package com.asu.cloud.computing.project.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.cloud.computing.project.repository.SThreeRepo;
@Service
public class StorageService {
	@Autowired
	private SThreeRepo sThreeRepo;
	
	
    public List<String> getResults() {
		return sThreeRepo.getModelResults();
	}
	
	public void uploadFileToSThree(String bucketName, File file)
	{
		sThreeRepo.uploadObject(bucketName, file);
	}
}
