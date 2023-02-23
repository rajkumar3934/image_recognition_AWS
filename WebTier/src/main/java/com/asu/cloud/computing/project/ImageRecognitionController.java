package com.asu.cloud.computing.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.asu.cloud.computing.project.service.ImgRecognitionService;

@Controller
public class ImageRecognitionController {

	@Autowired
	private ImgRecognitionService imageService;
	
	@GetMapping(value = "/")
	String index() {
		return "index";
	}

	@GetMapping(value = "/upload")
	String uploadForm() {
		return "imageRecog";
	}

	@PostMapping(value = "/fileUpload")
	@ResponseBody
	public String uploadFiles(Model model, @RequestPart(value = "myfile") MultipartFile[] partFiles)throws Exception {
		String res = null;
		try {
			for (MultipartFile multipartFile : partFiles) {
				String imageName = imageService.uploadImageFile(multipartFile);
				imageService.sendImageFileToQueue(imageName, multipartFile.getName());
				res =  getProcessedResults(imageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getProcessedResults(String imageName) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(imageName.substring(0, imageName.length() - 5));
		sb.append(":");
		sb.append(imageService.getFromHashorSQS(imageName));
		sb.append(")");
		return sb.toString();
	}
}
