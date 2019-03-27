package com.aiken.files;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.aiken.beans.Skill;

public class SkillBuilder 
{
	public static Set<Skill> buildSkills(String skills, String contentType)
	{
		Set<Skill> skillList = new HashSet<>();
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
	
	public static void stripSkills(String s, String skillType, Set<Skill> skillList)
	{
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
//							System.out.println(skill.substring(skill.indexOf(":")+1, skill.length()));
				}
				else
				{
					skillType = skill;
//							System.out.println(skill);
				}
				skillList.add(new Skill(skillType));
			}
		}
	}
}
