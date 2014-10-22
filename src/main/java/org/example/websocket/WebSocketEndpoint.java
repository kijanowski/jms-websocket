package org.example.websocket;

import org.example.services.QueueSenderSessionBean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@Stateless
@ServerEndpoint("/websocket")
public class WebSocketEndpoint implements Serializable {

    @Inject
    private QueueSenderSessionBean senderBean;

    private static final Logger logger = Logger.getLogger(WebSocketEndpoint.class.getName());

    private static final Set<Session> webSocketSessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        logger.info("onOpen: " + session);
        try {
            session.getBasicRemote().sendText("session opened");
            webSocketSessions.add(session);

            if (senderBean == null) {
                logger.severe("senderBean is null");
            }
        } catch (Exception ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session client) {
        logger.info("onMessage: " + client + "; Message: " + message);
        try {
            client.getBasicRemote().sendText("sending message to SessionBean...");
        } catch (IOException ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (senderBean != null) {
            senderBean.sendMessage(message);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        logger.info("onClose: " + session);
        try {
            session.getBasicRemote().sendText("WebSocket Session closed");
            webSocketSessions.remove(session);
        } catch (Exception ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onJMSMessage(@Observes @WSJMSMessage Message msg) {
        Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.INFO, "Got JMS Message at WebSocket!");
        final String message;
        try {
            message = "message from JMS: " + msg.getBody(String.class);
            webSocketSessions.parallelStream().forEach(s -> s.getAsyncRemote().sendText(message));
        } catch (JMSException ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
