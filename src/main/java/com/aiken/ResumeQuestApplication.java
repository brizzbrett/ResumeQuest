package com.aiken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.aiken.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class ResumeQuestApplication 
{

	public static void main(String[] args) 
	{
		SpringApplication.run(ResumeQuestApplication.class, args);
	}

}
