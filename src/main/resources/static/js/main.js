let stompClient = null;
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
    const socket = new SockJS('/secured/chat');
    stompClient = Stomp.over(socket);  
    stompClient.connect({}, function(frame) {
        setConnected(true);

        stompClient.send("/spring-security-mvc-socket/secured/join", {}, 
            JSON.stringify({'from': userName, 'text':'join'})
        );

        stompClient.subscribe('/secured/history', function(messageOutput) {
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
    const text = $('#text').val();

    stompClient.send("/spring-security-mvc-socket/secured/chat", {}, 
        JSON.stringify({'from':userName, 'text':text})
    );

    $('#text').val('');
}
            
function showMessageOutput(messageOutput) {
    const type = messageOutput.type;

    if (type == 'join') {
        const users = $('#users');
        users.empty();

        $.each(messageOutput.userList, function(index, item) {
            let userLi = $('<li></li>');
            userLi.text(item);
            users.append(userLi);
        });
        
        return;
    }

    const $messages = $('.messages');
    const message_side = messageOutput.from === userName ? 'right' : 'left';

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

    connect();

    $('#sendMessage').click(function () {
        if ($('#text').val() === "") {
            return;
        }
        sendMessage();
    });

    $('#text').keyup(function (event){
        if (event.keyCode == 13) {
            $('#sendMessage').click();
        }
    });

    $('#makeRoom').click(function (event){
        const name = $('#roomname').val();

        $.ajax({
            type: "POST",
            url: '/room',
            data: {
                "name" : name
            },
            success: function(xhr, textStatus, errorThrown) {
                console.log(xhr);
                console.log(textStatus);
            },
            error: function(xhr, textStatus, errorThrown) {
                
            }
        })
    });

    $('#logout').click(function (event){
        location.href = '/logout';
    });
});