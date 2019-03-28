package com.aiken.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.aiken.beans.Skill;
import com.aiken.beans.SkillResource;

public class ResourceBuilder 
{
	public static List<SkillResource> buildSkillResources(Skill skill, String skillType)
	{
		List<SkillResource>  resourceList = new ArrayList<>();

		resourceList.add(new SkillResource(skill, skillType + " interview questions",
					"https://www.google.com/search?q=" + skillType.toLowerCase() + "+interview+questions"));
		
		parseSite(skill,skillType, resourceList);
		return resourceList;

	}

	public static void parseSite(Skill skill, String skillType, List<SkillResource> resourceList) 
	{
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-java-programming-interview-questions-set-2/").get();
			Elements questions = doc.select("strong");
			
			boolean firstQuestion = true;
			Element firstq = null;
			
			resourceList.add(new SkillResource(skill, skillType + " interview questions",
					"https://www.google.com/search?q=" + skillType.toLowerCase() + "+interview+questions"));
			
			for (Element question : questions) 
			{
				if(firstq != null && firstq.equals(question))
				{
					break;
				}
				if(firstQuestion)
				{
					firstq = question;
					firstQuestion = false;
				}
				if(question.html().contains("?"))
				{
					resourceList.add(new SkillResource(skill, question.html(), ""));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
