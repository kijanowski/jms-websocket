var websocketSession;

function f_onmessage(evt) {
    websocketMessages = document.getElementById('websocketMessages');
    websocketMessages.innerHTML = websocketMessages.innerHTML + evt.data + '<br/>';
}

function open() {
    if (!websocketSession) {
        websocketSession = new WebSocket('ws://' + document.location.host + document.location.pathname +'websocket');
        websocketSession.onmessage = f_onmessage;
    }
}

function close() {
    if (websocketSession) {
        websocketSession.close();
        websocketSession = null;
    }
}

function sendMessage(msg) {
    websocketSession.send(msg);
}
