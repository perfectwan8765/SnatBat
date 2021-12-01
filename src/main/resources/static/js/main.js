var stompClient = null;
            
function setConnected(connected) {
    
    $('#connect').attr('disabled', connected);
    $('#disconnect').attr('disabled', !connected);

    $('#conversationDiv').css('visibility', connected ? 'visible' : 'hidden');
    $('#response').text('');
}
            
function connect() {
    const socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);  
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}
            
function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}
            
function sendMessage() {
    const from = $('#from').val();
    const text = $('#text').val();

    stompClient.send("/app/chat", {}, 
        JSON.stringify({'from':from, 'text':text})
    );
}
            
function showMessageOutput(messageOutput) {
    const response = $('#response');
    let p = $('<p></p>');

    p.css('wordWrap', 'break-word');
    p.text(`${messageOutput.from}: ${messageOutput.text} (${messageOutput.time})`);

    response.append(p);
}

$( document ).ready(function() {
    disconnect();
});