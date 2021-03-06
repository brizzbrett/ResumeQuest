package com.aiken.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig
{
	@Bean
	public Docket fileApi()
	{
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.select().apis(RequestHandlerSelectors.basePackage("com.aiken"))
				.build();
		Collection<VendorExtension> extensions = new ArrayList<>();
		Contact contact = new Contact("Brett Aiken", "http://linkedin.com/in/brettaiken", "brizzbrett@yahoo.com");
		ApiInfo apiInfo = new ApiInfo("ResumeQuest API", "Resume interview question builder.","1.0.0", "TOS", contact, "Apache license", "url", extensions);

		docket.apiInfo(apiInfo);
		return docket;
	}
}
