package com.asu.cloud.computing.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.cloud.computing.project.repository.SThreeStorageRepo;

@Service
public class SThreeService {
	@Autowired
	private SThreeStorageRepo s3Repo;

	public void saveObject(String key, String value) {
		s3Repo.saveObject(key, value);
	}
}
