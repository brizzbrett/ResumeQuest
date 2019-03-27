package com.aiken.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "skill")
@XmlRootElement
public class Skill implements Serializable, Comparable<Skill>
{
	private static final long serialVersionUID = -6347102639298100293L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@JsonProperty(value="skilltype")	
	@Column(name="skill")
	private String skillType;
	
	@JsonProperty(value="resourcelist")
	@OneToMany(mappedBy = "skill", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<SkillResource> resourceList;
	
	public Skill()
	{
		skillType = "";
		resourceList = new ArrayList<>();
	}
	
	public Skill(String skillType)
	{
		this.skillType = skillType;
		this.resourceList = new ArrayList<>();
		this.resourceList.add(new SkillResource(this,"What is java?", "https://docs.oracle.com/javase/8/docs/api/javax/swing/text/Document.html"));
		this.resourceList.add(new SkillResource(this,"What is C++?",""));
		this.resourceList.add(new SkillResource(this,"What is MongoDB?", ""));
		this.resourceList.add(new SkillResource(this,"Is a hotdog a sandwich?", ""));
		this.resourceList.add(new SkillResource(this,"Is Joe a human?", ""));
		this.resourceList.add(new SkillResource(this,"What's up with that?", ""));
		
	}
	public Skill(String skillType, List<SkillResource> resourceList)
	{
		this.skillType = skillType;
		this.resourceList = resourceList;
	}

	public String getSkillType() 
	{
		return skillType;
	}

	public List<SkillResource> getResourceList() 
	{
		return resourceList;
	}

	public void setResourceList(List<SkillResource> resourceList) 
	{
		this.resourceList = resourceList;
	}

	public int getId()
	{
		return id;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Skill skill = null;
		// TODO Auto-generated method stub
		if(obj instanceof Skill)
		{
			skill = (Skill)obj;
		}
		return this.skillType.equals(skill.skillType);
	}
	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public int compareTo(Skill o)
	{
		return this.skillType.compareTo(o.skillType);
	}


}
