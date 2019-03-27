'use strict';
var uploadHeader = document.querySelector('#uploadHeader');
var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/upload");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
        	uploadHeader.style.display = "none"
        	singleUploadForm.style.display = "none"
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>" + response.filename + " Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.filedownloaduri + "' target='_blank'>" + response.filedownloaduri + "</a>";
            var i;
            for (i in response.skills) 
            {
            	singleFileUploadSuccess.innerHTML += "<br><b><a href=http://localhost:8080/skills/" + response.skills[i].skilltype + " target=_blank>" + response.skills[i].skilltype + "</a>";
            }
            singleFileUploadSuccess.innerHTML += "</p>";
            
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

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