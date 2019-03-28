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
    xhr.open("DELETE", "/delete");  
    xhr.send(null);
    location.reload();
}

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/upload");

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
	        	singleFileUploadSuccess.innerHTML += "<br><b><a href=localhost:8080/skills/" + response.skills[i].skilltype + " target=_blank>" + response.skills[i].skilltype + " resources" + "</a>";
	        }
	        singleFileUploadSuccess.innerHTML += "</p>";
	        
	        singleFileUploadSuccess.style.display = "block";
	    } else {
	        singleFileUploadSuccess.style.display = "none";
	        singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
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