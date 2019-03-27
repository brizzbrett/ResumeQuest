package com.aiken.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SkillResource
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int resourceid;
	
	@Column(name="question")
	private String question;
	
	@Column(name="url")
	private String url;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "skill_id")
    private Skill skill;
	
	public SkillResource()
	{
		question = "What came first, the chicken or the egg?";
		url = "https://en.wikipedia.org/wiki/Chicken_or_the_egg";
	}
	
	public SkillResource(Skill skill, String question, String url)
	{
		this.skill = skill;
		this.question = question;
		this.url = url;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question = question;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
}
