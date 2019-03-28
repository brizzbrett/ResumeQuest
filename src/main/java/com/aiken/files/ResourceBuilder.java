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
	public static List<SkillResource> buildSkillResources(Skill skill)
	{
		List<SkillResource>  resourceList = new ArrayList<>();
		
		parseStrongSites(skill, resourceList);
		
		return resourceList;

	}

	public static void parseStrongSites(Skill skill, List<SkillResource> resourceList) 
	{
		List<Document> docs = new ArrayList<>();
		try {
			String selector = "";
			String skillType = skill.getSkillType().toUpperCase();
			switch(skillType)
			{
			case "JAVA":			
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-java-programming-interview-questions-set-1/").get());
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-java-programming-interview-questions-set-2/").get());
				selector = "strong";
				break;
			case "C":			
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-c-programming-interview-questions-set-1/").get());
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-c-programming-interview-questions-set-2/").get());
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-c-programming-interview-questions-set-3/").get());
				selector = "strong";
				break;
			case "C++":
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-c-interview-questions-set-1/").get());
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-c-interview-questions-set-2/").get());
				selector = "strong";
				break;
			case "JAVASCRIPT":
				docs.add(Jsoup.connect("https://www.geeksforgeeks.org/commonly-asked-javascript-interview-questions-set-1/").get());
				selector = "strong";
				break;
			case "AWS":
				docs.add(Jsoup.connect("https://www.dezyre.com/article/-top-50-aws-interview-questions-and-answers-for-2018/399").get());
				selector = "ol > li";
				break;
			case "CSS":
				docs.add(Jsoup.connect("https://career.guru99.com/top-50-csscascading-style-sheet-interview-questions/").get());
				selector = "strong";
			default:
			}
			
			resourceList.add(new SkillResource(skill, skill.getSkillType() + " interview questions - Google Search",
					"https://www.google.com/search?q=" + skill.getSkillType().toLowerCase() + "+interview+questions"));
			
			if(!docs.isEmpty())
			{
				System.out.println("HERE!");
				List<Elements> questionsList = new ArrayList<>();
				for(Document doc : docs)
				{
					questionsList.add(doc.select(selector));
				}
				
				boolean firstQuestion = true;
				Element firstq = null;
				
				for (Elements questions : questionsList) 
				{
					for(Element question : questions)
					{
						if(firstq != null && firstq.equals(question))
						{
							firstq = null;
							break;
						}
						if(firstQuestion)
						{
							firstq = question;
							firstQuestion = false;
						}
						if(question.html().contains("Who") ||
								question.html().contains("Which") ||
								question.html().contains("What") ||
								question.html().contains("When") ||
								question.html().contains("Where") ||
								question.html().contains("Why") ||
								question.html().contains("Difference") ||
								question.html().contains("Is") ||
								question.html().contains("How"))
						{
							if(question.html().contains("a href"))
							{
								resourceList.add(new SkillResource(skill, question.html(), ""));
							}
							else
							{
								String url = "https://www.google.com/search?q=";
								for(String q : question.html().replaceFirst("Q.?:[0-9]+", "").split(" "))
								{
									url += q + "+";
								}
								String managedQuestion = question.html().replaceFirst("Q.[0-9]+", "");
								managedQuestion = managedQuestion.replaceAll("<br>", "");
								resourceList.add(new SkillResource(skill, managedQuestion, url));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
