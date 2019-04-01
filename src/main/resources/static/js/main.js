'use strict';

var uploadHeader = document.querySelector('#uploadHeader');
var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');


function deleteFile()
{
	console.log("DELETE FILES")
	var xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/deleteFileInfo");  
    xhr.send(null);
    location.reload();
}

function getFileInformation()
{
	console.log("GET FILES")
	var xhr = new XMLHttpRequest();
    xhr.open("GET", "/getFileInfo"); 
    xhr.onload = function() {
    	if(xhr.status == 200) {
    		console.log("GET UPLOAD")
	        var response = JSON.parse(xhr.responseText);
    		document.querySelector('#uploadForm').style.display = "block";
    		document.querySelector('#resumeQuest').style.display = "block";
    		document.querySelector('#skillInfo').style.display = "none"
    		document.querySelector('#uploadHeader').style.display = "none"
    		document.querySelector('#singleUploadForm').style.display = "none"
    		document.querySelector('#singleFileUploadError').style.display = "none";
    		document.querySelector('#singleFileUploadSuccess').innerHTML = "<p>" + response.filename + " Uploaded Successfully. <button id=uploadNew onclick=deleteFile()>Upload New File</button></p><p>DownloadUrl : <a href='" + response.filedownloaduri + "' target='_blank'>" + response.filedownloaduri + "</a>";
	        var i;
	        for (i in response.skills) 
	        {
	        	//singleFileUploadSuccess.innerHTML += "<br><b><a href=" + response.skills[i].resourcelist[0].url + " target=_blank>" + response.skills[i].resourcelist[0].question + "</a>";
	        	document.querySelector('#singleFileUploadSuccess').innerHTML += "<br><b><a id=skill href=javascript:getSkillURL(\"" + response.skills[i].skilltype + "\"); target=_blank>" + response.skills[i].skilltype + " resources" + "</a>";
	        }
	        document.querySelector('#singleFileUploadSuccess').innerHTML += "</p>";
	        
	        document.querySelector('#singleFileUploadSuccess').style.display = "block";
	    } else {
	    	document.querySelector('#singleFileUploadSuccess').style.display = "none";
	    	document.querySelector('#singleFileUploadError').innerHTML = (response && response.message) || "Some Error Occurred";
	    }
    };
    xhr.send(null);
}

function getSkillURL(skillType){
	var xhr = new XMLHttpRequest();
    xhr.open("GET", "/skills/" + skillType);
    
    xhr.send(null);
    xhr.onreadystatechange=(e)=>
    {
    	document.body.innerHTML = xhr.responseText;
    	document.querySelector('#uploadForm').style.display = "none";
    	document.querySelector('#resumeQuest').style.display = "none";
    }
}

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function() {
    	if(xhr.status == 200) {
    		console.log("POST UPLOAD")
	        var response = JSON.parse(xhr.responseText);
	    	uploadHeader.style.display = "none"
	    	singleUploadForm.style.display = "none"
	        singleFileUploadError.style.display = "none";
	        singleFileUploadSuccess.innerHTML = "<p>" + response.filename + " Uploaded Successfully. <button id=uploadNew onclick=deleteFile()>Upload New File</button></p><p>DownloadUrl : <a href='" + response.filedownloaduri + "' target='_blank'>" + response.filedownloaduri + "</a>";
	        var i;
	        for (i in response.skills) 
	        {
	        	//singleFileUploadSuccess.innerHTML += "<br><b><a href=" + response.skills[i].resourcelist[0].url + " target=_blank>" + response.skills[i].resourcelist[0].question + "</a>";
	        	singleFileUploadSuccess.innerHTML += "<br><b><a id=skill href=javascript:getSkillURL(\"" + response.skills[i].skilltype + "\"); target=_blank>" + response.skills[i].skilltype + " resources" + "</a>";
	        }
	        singleFileUploadSuccess.innerHTML += "</p>";
	        
	        singleFileUploadSuccess.style.display = "block";
	    } else {
	        singleFileUploadSuccess.style.display = "none";
	        singleFileUploadError.innerHTML = response.message || "Some Error Occurred";
	    }
    };

    xhr.send(formData);
}

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);