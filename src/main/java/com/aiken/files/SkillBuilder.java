package com.aiken.files;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
				//System.out.println(s + " ==== ");
				if(s.toLowerCase().strip().contains("skills​:"))
				{
					stripSkills(s,skillType,skillList);
				}
			}
		}
		else if(contentType.equals("docx"))
		{
			paragraphs = skills.split("\n\n");
			
			for(String s : paragraphs)
			{
				//System.out.println(s + " ==== ");
				if(s.toLowerCase().strip().contains("Skills:".toLowerCase()))
				{
					stripSkills(s,skillType,skillList);
				}
			}
		}
		return skillList;
	}
	
	private static void stripSkills(String s, String skillType, Set<Skill> skillList)
	{
		List<SkillResource> resourceList = new ArrayList<>();
		s = s.substring(s.indexOf(":")+1,s.length());
		for(String skillLine : s.strip().split("\n"))
		{
			for(String skill : skillLine.strip().split(","))
			{
				skill = skill.replaceAll("\\s+", "");
				if(skill.contains(":"))
				{
					skillType = skill.substring(skill.indexOf(":")+1, skill.length());
					skillType = skillType.replaceAll("\\s+", "");
					parseSkill(skillType, skillList);
				}
				else
				{
					skillType = skill;
					parseSkill(skillType, skillList);
				}
				
				Skill temp = new Skill(skillType);
				resourceList = ResourceBuilder.buildSkillResources(temp,temp.getSkillType());
				skillList.add(temp);
				temp.setResourceList(resourceList);
			}
		}
	}

	private static void parseSkill(String skillType, Set<Skill> skillList) {
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
				resourceList = ResourceBuilder.buildSkillResources(temp,temp.getSkillType());
				skillList.add(temp);			
				temp.setResourceList(resourceList);
			}
			skillType = skillType.split("/")[0];
		}
	}
}
