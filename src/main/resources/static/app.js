// Initialize Websocket
var stompClient = null;
var socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, onConnected, onError);

// Subscribe to topic
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
}


function onError(error) {
    console.log('Error occured');
    console.log(error);
}

function onMessageReceived(payload) {
    var message = payload.body;
	console.log(message);
	let progressBar = document.getElementById("progressBar");
	console.log(progressBar);
	progressBar.setAttribute("value",message);
}


// Add event listener to the button element
const uploadButton = document.getElementById("uploadButton");
uploadButton.addEventListener("click", uploadFiles);

function uploadFiles(event) {
  event.preventDefault();
  const fileInput = document.getElementById("fileInput");
  const selectedFiles = fileInput.files;
  // Check if any files are selected
  if (selectedFiles.length === 0) {
    alert("Please select at least one file to upload.");
    return;
  }
  
  const formData = new FormData();
  // Append each selected file to the FormData object
  for (let i = 0; i < selectedFiles.length; i++) {
    formData.append("files[]", selectedFiles[i]);
  }
  
  // Display the FormData structure 
	console.log([...formData])
	
	const xhr = new XMLHttpRequest();
xhr.open("POST", "/upload", true);
xhr.onreadystatechange = function () {
  if (xhr.readyState === XMLHttpRequest.DONE) {
    if (xhr.status === 200) {
       // Handle successful response from the server
      console.log('Files uploaded successfully!');
      saveData();
    } else {
       // Handle error response from the server
      console.error('Failed to upload files.');
     alert("Error occurred during file upload. Please try again.");
    }
  }
};
xhr.send(formData);
}


function saveData(){
	var a = document.createElement("a");
    document.body.appendChild(a);
    a.style = "display: none";
    a.href = '/download/invoice';
    a.download = 'invoice';
    a.click();
}