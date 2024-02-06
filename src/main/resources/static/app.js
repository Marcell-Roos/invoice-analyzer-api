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
	
	progressBar.setAttribute("style","--value: "+message);
}




// Add event listener to the button element
const uploadButton = document.getElementById("uploadButton");
uploadButton.addEventListener("click", uploadFiles);

const fileButton = document.getElementById("fileInput");
fileButton.addEventListener("change", updatePageAfterFileSelection);

function updatePageAfterFileSelection(event){
	if(event.target.files.length > 0){
	uploadButton.classList.remove('hidden');
		let fileText = document.getElementById('fileText');
		fileText.innerHTML = event.target.files.length + ' files selected.';
		
	}
	
}

function uploadFiles(event) {
const fileInput = document.getElementById("fileInput");
  const selectedFiles = fileInput.files;
  // Check if any files are selected
  if (selectedFiles.length === 0) {
    alert("Please select at least one file to upload.");
    return;
  }
  let epiImage = document.getElementById("epiImage");
  epiImage.classList.add('hidden');
  let cardFold = document.getElementById("cardFold");
  let opacity = 0;
  let opacity2 = 1;
let fadeIn = setInterval(() => {
   cardFold.style.opacity = opacity;
   epiImage.style.opacity = opacity2;
   opacity += 0.01;
   opacity2 -= 0.03;
}, 10);
  event.preventDefault();
  
  
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