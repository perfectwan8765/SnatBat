let stompClient = null;
let Message;
Message = function (arg) {
    this.text = arg.text;
    this.message_side = arg.message_side;

    this.draw = function (_this) {
        return function () {
            let $message;
            $message = $($('.message_template').clone().html());
            $message.addClass(_this.message_side).find('.text').html(_this.text);
            $('.messages').append($message);
            return setTimeout(function () {
                return $message.addClass('appeared');
            }, 0);
        };
    }(this);
    
    return this;
};
            
function setConnected(connected) {
    $('#connect').attr('disabled', connected);
    $('#disconnect').attr('disabled', !connected);
    $('#text').text('');
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
    let $messages, message;

    console.log(messageOutput)

    $messages = $('.messages');
    message_side = 'right';

    message = new Message({
        text: messageOutput.text,
        message_side: message_side
    });
    
    message.draw();
            
    return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
}

$(document).ready(function() {
    disconnect();
});