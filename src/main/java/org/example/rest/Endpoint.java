package org.example.rest;

import org.example.services.QueueSenderSessionBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.logging.Logger;

@Path("/example")
public class Endpoint {

    @Inject
    private QueueSenderSessionBean sender;

    private static final Logger logger = Logger.getLogger(Endpoint.class.getName());

    @GET
    @Path("{name}")
    public void send(@PathParam("name")String payload) {
        logger.info("GET: " + payload);
        sender.sendMessage(payload);
    }

}
