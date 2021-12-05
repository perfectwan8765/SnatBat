let stompClient = null;
let username = null;
let Message;

Message = function (arg) {
    this.from = arg.from;
    this.text = arg.text;
    this.message_side = arg.message_side;

    this.draw = function (_this) {
        return function () {
            let $message = $($('.message_template').clone().html());
            $message.addClass(_this.message_side).find('.text').html(_this.text);
            $message.find('.avatar').html(_this.from.substr(0,1).toUpperCase());

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
    // text field
    $('#text').attr('disabled', !connected);
    $('#text').val('');

    $('#sendMessage').attr('disabled', !connected);
}
            
function connect() {
    const from = $('#from').val();

    if (!from) {
        alert('Choose a nickname');
        return;
    }
    username = from;

    const socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);  
    stompClient.connect({
        name: from
    }, function(frame) {
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

    $('#text').val('');
}
            
function showMessageOutput(messageOutput) {
    const $messages = $('.messages');
    const message_side = messageOutput.from === username ? 'right' : 'left';

    const message = new Message({
        from: messageOutput.from,
        text: messageOutput.text,
        message_side: message_side
    });
    
    message.draw();
            
    return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
}

$(document).ready(function() {
    disconnect();

    $('#connect').click(function (){
        connect();
    });

    $('#disconnect').click(function (){
        disconnect();
    });

    $('#sendMessage').click(function (){
        sendMessage();
    });
});