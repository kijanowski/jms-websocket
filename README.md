Java EE 7 Example for JMS and WebSockets integration
=========================

This application demonstrates a full-duplex scenario using WebSockets and JMS, with a fully functional server-side asynchronous push, using CDI events and EJB.

Details and step-by-step were blogged here: https://blogs.oracle.com/brunoborges/entry/integrating_websockets_and_jms_with

How does it work?
-----------------

On the client side, a websocket session is opened with java script:

`websocketSession = new WebSocket('ws://' + ...);`

and messages are sent by:

`websocketSession.send(msg);`

The callback handler is implemented as `f_onmessage(evt)` which adds a message to the &lt;div&gt; element.

On the server side, there is a websocket server. When a message arrives from a websocket client, it uses the
injected `QueueSenderSessionBean` service to send a message to a JMS queue.

A MessageDrivenBean picks up that message and fires a CDI event.

The websocket server listens for CDI events and picks it up and send a message to all websocket clients.


There is a REST service, too. The Endpoint class exposes a GET method which is available under:
`http://127.0.0.1:8080/jms-websocket-1.0-SNAPSHOT/rest/example/`
The REST service uses the injected `QueueSenderSessionBean` service to send a message to a JMS queue.

A MessageDrivenBean picks up that message and fires a CDI event.

The websocket server listens for CDI events and picks it up and send a message to all websocket clients.

How to test it?
---------------

1. mvn clean install
2. cp target/jms-websocket-1.0-SNAPSHOT.war $WILDFLY_8.1_HOME/standalone/deployments/
3. $WILDFLY_8.1_HOME/bin/standalone.sh -c standalone-full.xml

4. Open two browser sessions (tabs) and navigate to http://127.0.0.1:8080/jms-websocket-1.0-SNAPSHOT/
5. Click on Open Session on both tabs and start sending messages.
6. Open another browser session and navigate to http://127.0.0.1:8080/jms-websocket-1.0-SNAPSHOT/rest/example/myMessage
7. "myMessage" should be displayed on the other two tabs.