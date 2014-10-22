package org.example.services;

import org.example.Resources;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;
import java.util.logging.Logger;

/**
 * This bean is used by the websocket server to send JMS messages
 */
@Stateless
@JMSDestinationDefinition(
        name = "queue/myQueue",
        interfaceName = "javax.jms.Queue")
public class QueueSenderSessionBean {

    @Resource(lookup = Resources.ASYNC_QUEUE)
    private Queue myQueue;

    @Inject
    private JMSContext jmsContext;

    private static final Logger logger = Logger.getLogger(QueueSenderSessionBean.class.getName());

    public void sendMessage(String message) {
        logger.info("sendMessage: " +  message);
        jmsContext.createProducer().send(myQueue, message);
    }

}
