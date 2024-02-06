
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;



var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}




function onMessageReceived(payload) {
    var message = payload.body;

    var messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');

    

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
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