package org.example.websocket;

import org.example.Resources;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * fire CDI events when a JMS message arrives
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = Resources.ASYNC_QUEUE) })
public class WebSocketMDB implements MessageListener {

    @Inject
    @WSJMSMessage
    Event<Message> jmsEvent;

    @Override
    public void onMessage(Message msg) {
        Logger.getLogger(WebSocketMDB.class.getName()).log(Level.INFO, "fireEvent: " +  msg);
        jmsEvent.fire(msg);
    }
}
