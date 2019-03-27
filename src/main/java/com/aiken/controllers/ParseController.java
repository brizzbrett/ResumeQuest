package com.aiken.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aiken.beans.FileInformation;
import com.aiken.beans.SkillResource;
import com.aiken.services.ParseService;

@RestController
public class ParseController {

	private static final Logger logger = LoggerFactory.getLogger(ParseController.class);

	@Autowired
	private ParseService helper;

	@GetMapping(path="/skills/{skill}")
	public ResponseEntity<Set<SkillResource>> getSkills(@PathVariable String skill, HttpServletRequest request) 
	{
	
		Set<SkillResource> resource = helper.getSkillResources(skill);
		if(resource != null)
		{
			return ResponseEntity.ok(resource);
		}
		
		return ResponseEntity.noContent().build();
	}

	@PostMapping(path="/upload",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, 
			consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public FileInformation uploadFile(@RequestParam("file") MultipartFile file) 
	{
		FileInformation fileInfo = helper.saveSkills(file);

		return fileInfo;
	}

	@GetMapping(path="/download/{fileName:.+}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) 
	{
		// Load file as Resource
		Resource resource = helper.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try 
		{
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} 
		catch (IOException ex) 
		{
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) 
		{
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}