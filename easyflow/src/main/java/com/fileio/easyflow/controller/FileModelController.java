package com.fileio.easyflow.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fileio.easyflow.model.FileModel;
import com.fileio.easyflow.service.FileModelService;

@RestController
@RequestMapping("/api/v1/files")
public class FileModelController {

	private FileModelService fileModelService;
	
	public FileModelController(FileModelService fileModelService) {
		this.fileModelService = fileModelService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		FileModel fileModel = new FileModel(file);
		fileModelService.saveFile(fileModel);
		return ResponseEntity
				.ok()
				.body("File uploaded successfully");
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
		FileModel fileModel = fileModelService.findFileById(id);
		return ResponseEntity
				.ok()
				.contentType(MediaType.parseMediaType(fileModel.getContentType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
				.body(fileModel.getData());
	}
}
