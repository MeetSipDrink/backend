'use strict';
const addr = "localhost:8443"

// create and run Web Socket connection
// 웹 소켓 연결 정보
const socket = new WebSocket("wss://" + window.location.host + "/signal");

// UI elements
const videoButtonOff = document.querySelector('#video_off');
const videoButtonOn = document.querySelector('#video_on');
const audioButtonOff = document.querySelector('#audio_off');
const audioButtonOn = document.querySelector('#audio_on');
const exitButton = document.querySelector('#exit');
const localRoom = document.querySelector('input#id').value;
const localVideo = document.getElementById('local_video');
const remoteVideo = document.getElementById('remote_video');
// const localUserName = localStorage.getItem("uuid");
const localUserName = document.querySelector("#uuid").value


document.querySelector('#view_on').addEventListener('click', startScreenShare);
document.querySelector('#view_off').addEventListener('click', stopScreenShare);


// WebRTC STUN servers
// WebRTC STUN 서버 정보
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.stunprotocol.org:3478'},
        {'urls': 'stun:stun.l.google.com:19302'},
    ]
};

// WebRTC media
const mediaConstraints = {
    video: true,
    audio: {
        echoCancellation: true,
        noiseSuppression: true,
        sampleRate: 44100
    }
};

// WebRTC variables
let localStream;
let localVideoTracks;
let myPeerConnection;

// on page load runner
$(function(){
    start();
});

function start() {
    // 페이지 시작시 실행되는 메서드 -> socket 을 통해 server 와 통신한다
    socket.onmessage = function(msg) {
        let message = JSON.parse(msg.data);
        switch (message.type) {

            case "offer":
                log('Signal OFFER received');
                handleOfferMessage(message);
                break;

            case "answer":
                log('Signal ANSWER received');
                handleAnswerMessage(message);
                break;

            case "ice":
                log('Signal ICE Candidate received');
                handleNewICECandidateMessage(message);
                break;

            case "join":
                // ajax 요청을 보내서 userList 를 다시 확인함
                message.data = chatListCount();

                log('Client is starting to ' + (message.data === "true)" ? 'negotiate' : 'wait for a peer'));
                log("messageDATA : "+message.data)
                handlePeerConnection(message);
                break;

            case "leave":
                stop();
                break;

            default:
                handleErrorMessage('Wrong type message received from server');
        }
    };


    // ICE 를 위한 chatList 인원 확인
    function chatListCount(){

        let data;

        $.ajax({
            url : "/webrtc/usercount",
            type : "POST",
            async : false,
            data : {
                "from" : localUserName,
                "type" : "findCount",
                "data" : localRoom,
                "candidate" : null,
                "sdp" : null
            },
            success(result){
                data = result;
            },
            error(result){
                console.log("error : "+result);
            }
        });

        return data;
    }
}