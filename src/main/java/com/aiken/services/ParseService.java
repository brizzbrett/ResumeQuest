package com.aiken.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private static String filename;
    private static String fileDownloadUri;
    private static String contentType;
    private static FileInformation fi;
    
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
	
	public List<SkillResource> getSkillResources(String s)
	{
		Set<Skill> skillList = new HashSet<Skill>(skillRepo.findAll());
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
    	filename = StringUtils.cleanPath(file.getOriginalFilename());
    	contentType = file.getContentType();
		
		if(filename.contains(".."))
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
	    	
	    	for(Skill skill : SkillBuilder.buildSkills(docxExtract, "docx"))
	    	{
//	    		skill.setResourceList(ResourceBuilder.buildSkillResources(skill, skill.getSkillType()));
	    		skillRepo.save(skill);
	    	}
	    	
    	}
    	else if(file.getContentType().equals("application/pdf"))
    	{
    		String pdfExtract = Converter.pdfConverter(file, filename);
	    	for(Skill skill : SkillBuilder.buildSkills(pdfExtract, "pdf"))
	    	{
//	    		ResourceBuilder.buildSkillResources(skill, skill.getSkillType());
	    		skillRepo.save(skill);
	    	}
    	}
    	else
    	{
    		throw new WrongFileTypeException("File must be of type docx or pdf - " + file.getOriginalFilename() + " - " + file.getContentType());
    	}
		
		fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
				.path("resumequest_" + filename).toUriString();
		fi = new FileInformation(filename, fileDownloadUri, contentType, new TreeSet<Skill>(skillRepo.findAll()));
	    return fi;		
    }

    public Resource loadFileAsResource(String filename) 
    {
        try 
        {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } 
            else 
            {
                throw new DLFileNotFoundException("File not found " + filename);
            }
        } 
        catch (MalformedURLException ex) 
        {
            throw new DLFileNotFoundException("File not found " + filename, ex);
        }
    }

	public Converter getConverter()
	{
		return converter;
	}
	
	public void deletePersistedFile()
	{
		skillRepo.deleteAll();
		resourceRepo.deleteAll();
	}

	public FileInformation getPersistedFile()
	{
		return fi;
	}
}
