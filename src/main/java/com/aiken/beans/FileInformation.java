package com.aiken.beans;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class FileInformation
{
	@JsonProperty(value="filename")
    private String fileName;
	@JsonProperty(value="filedownloaduri")
    private String fileDownloadUri;
	@JsonProperty(value="filetype")
    private String fileType;
	@JsonProperty(value="skills")
	private Set<Skill> skillList;

	public FileInformation() {}
	
    public FileInformation(String fileName, String fileDownloadUri, String fileType, Set<Skill> skillList) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.skillList = skillList;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Set<Skill> getSkillList() {
		return skillList;
	}

	public void setSkillsList(Set<Skill> skillList) {
		this.skillList = skillList;
	}
}