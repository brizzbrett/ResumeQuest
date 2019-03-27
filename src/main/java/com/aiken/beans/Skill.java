package com.aiken.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "skill")
@XmlRootElement
public class Skill implements Serializable
{
	private static final long serialVersionUID = -6347102639298100293L;

	@Id
	@GeneratedValue
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

	public String getSkillType() 
	{
		return skillType;
	}

	public List<SkillResource> getResourceList() 
	{
		return resourceList;
	}

	public void setResourceList(ArrayList<SkillResource> resourceList) 
	{
		this.resourceList = resourceList;
	}

	public int getId()
	{
		return id;
	}


}
