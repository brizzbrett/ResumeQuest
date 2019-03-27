package com.aiken.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aiken.beans.FileInformation;
import com.aiken.beans.Skill;
import com.aiken.beans.SkillResource;
import com.aiken.config.FileStorageProperties;
import com.aiken.exceptions.DLFileNotFoundException;
import com.aiken.exceptions.FileStorageException;
import com.aiken.exceptions.WrongFileTypeException;
import com.aiken.files.Converter;
import com.aiken.files.SkillBuilder;
import com.aiken.repos.ResourceRepository;
import com.aiken.repos.SkillRepository;

@Service
public class ParseService 
{
	
	private final Path fileStorageLocation;
    private Converter converter;
    private static Set<Skill> skillList;
    private static String filename;
    private static String fileDownloadUri;
    private static String contentType;
    
    @Autowired
    private SkillRepository skillRepo;
    
    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    public ParseService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
	public Set<SkillResource> getSkillResources(String s)
	{
		if(skillList != null)
		{
			for(Skill skill : skillList)
			{
				if(skill.getSkillType().equalsIgnoreCase(s))
				{
					return resourceRepo.findBySkill(skill);
				}
			}
		}
		return null;
	}
    public FileInformation saveSkills(MultipartFile file) 
    {  	
    	if(skillList == null)
    	{
	    	filename = StringUtils.cleanPath(file.getOriginalFilename());
	    	contentType = file.getContentType();
			
			if (filename.contains(".."))
			{
				throw new FileStorageException("Sorry! Filename contains invalid path sequence - " + filename);
			}
			
			Path targetLocation = fileStorageLocation.resolve("resumequest_" + filename);
			try
			{
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
	    			file.getContentType().equals("application/octet-stream"))
	    	{
		    	String docxExtract = Converter.docXConverter(file, filename);
		    	skillList = SkillBuilder.buildSkills(docxExtract, "docx");
		    	for(Skill skill : skillList)
		    	{
		    		skillRepo.save(skill);
		    	}
		    	
	    	}
	    	else if(file.getContentType().equals("application/pdf"))
	    	{
	    		String pdfExtract = Converter.pdfConverter(file, filename);
	    		skillList = SkillBuilder.buildSkills(pdfExtract, "docx");
		    	for(Skill skill : skillList)
		    	{
		    		skillRepo.save(skill);
		    	}
	    	}
	    	else
	    	{
	    		throw new WrongFileTypeException("File must be of type docx or pdf - " + file.getOriginalFilename() + " - " + file.getContentType());
	    	}
			
			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
					.path("resumequest_" + filename).toUriString();
    	}
	    	
	    return new FileInformation(filename, fileDownloadUri, contentType, skillList);   		
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new DLFileNotFoundException("File not found " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new DLFileNotFoundException("File not found " + filename, ex);
        }
    }

	public Converter getConverter()
	{
		return converter;
	}

	public static Set<Skill> getSkillsList()
	{
		return skillList;
	}
}
