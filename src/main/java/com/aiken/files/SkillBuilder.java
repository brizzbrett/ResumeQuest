package com.aiken.files;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aiken.beans.Skill;
import com.aiken.beans.SkillResource;

public class SkillBuilder 
{

	public static Set<Skill> buildSkills(String skills, String contentType)
	{
		Set<Skill> skillList = new TreeSet<>();
		String[] paragraphs = null;
		String skillType = "";	
		
		if(contentType.equals("pdf"))
		{
			paragraphs = skills.trim().split("\n ");
			
			for(String s : paragraphs)
			{
				if(s.toLowerCase().strip().contains("skills​:") ||
						s.toUpperCase().contains("TECHNICAL SKILLS") ||
						s.toLowerCase().contains("languages"))
				{
					s = technicalSkillsRegex(s);
					stripSkills(s,skillType,skillList);
				}
			}
		}
		else if(contentType.equals("docx"))
		{
			paragraphs = skills.split("\n\n");
			
			for(String s : paragraphs)
			{
				if(s.toLowerCase().strip().contains("Skills:".toLowerCase()) ||
						s.toUpperCase().contains("TECHNICAL SKILLS"))
				{
					s = technicalSkillsRegex(s);
					stripSkills(s,skillType,skillList);
				}
			}
		}
		return skillList;
	}

	private static String technicalSkillsRegex(String s)
	{
		if(s.toUpperCase().contains("TECHNICAL SKILLS"))
		{
			s = s.substring(s.toUpperCase().indexOf("TECHNICAL SKILLS"),s.length());
			Pattern pattern = Pattern.compile(".+[A-Z]{4}+");
		    Matcher matcher = pattern.matcher(s);
		    
		    int startPoint = 0;
		    while (matcher.find()) 
		    {
		    	if(matcher.group().equalsIgnoreCase("technical skills"))
		    	{
		    		startPoint = matcher.end();
		    	}
		    	else if(!matcher.group().toUpperCase().equals(matcher.group()))
		    	{
		    		continue;
		    	}
		    	else
		    	{				    		
		    		s = s.substring(startPoint,matcher.start());
		    		break;
		    	}
		    }
		}
		return s;
	}
	
	private static void stripSkills(String s, String skillType, Set<Skill> skillList)
	{
		List<SkillResource> resourceList = new ArrayList<>();
		s = s.substring(s.indexOf(":")+1,s.length());
		s = s.replaceAll("and", ",");
		
		String[] skillLines = s.strip().split("\n");
		for(String skillLine : skillLines)
		{
			if(skillLine.contains("(") && skillLine.contains(")"))
			{
				if(skillLine.strip().split("\\)").length > 1 && skillLine.strip().split("\\(").length > 1)
					skillLine = skillLine.strip().split("\\(")[0] + skillLine.strip().split("\\)")[1];
			}
			if(skillLine.contains("("))
			{
				skillLine = skillLine.strip().split("\\(")[0];
			}
			if(skillLine.contains(")"))
			{
				skillLine = skillLine.strip().split("\\)")[1];
			}
			for(String skill : skillLine.strip().split(","))
			{
				if(skill.toLowerCase().equals(skill))
				{
					continue;
				}
				skill = skill.replaceAll("\\s+", "");
				if(skill.contains(":"))
				{
					skillType = skill.substring(skill.indexOf(":")+1, skill.length());
					skillType = skillType.replaceAll("\\s+", "");
					skillType = parseSkill(skillType, skillList);
				}
				else
				{
					if(skill.length() >= 15)
					{
						continue;
					}
					skillType = skill;
					skillType = parseSkill(skillType, skillList);
				}
				if(!skillType.isBlank())
				{
					Skill temp = new Skill(skillType);
					resourceList = ResourceBuilder.buildSkillResources(temp);
					skillList.add(temp);
					temp.setResourceList(resourceList);
				}
			}
		}
	}

	private static String parseSkill(String skillType, Set<Skill> skillList) {
		List<SkillResource> resourceList = new ArrayList<>();
		if(skillType.contains("/"))
		{
			for(String split : skillType.split("/"))
			{
				String newSkill = split;
				if(split.matches("[0-9]+"))
				{
					if(!split.equalsIgnoreCase(skillType.split("/")[0]))
					{
						newSkill = skillType.substring(0,1) + skillType.split("[A-Z]")[1] + split;								
					}
				}
				Skill temp = new Skill(newSkill);
				resourceList = ResourceBuilder.buildSkillResources(temp);
				skillList.add(temp);			
				temp.setResourceList(resourceList);
			}
			skillType = skillType.split("/")[0];
		}
		return skillType;
	}
}
