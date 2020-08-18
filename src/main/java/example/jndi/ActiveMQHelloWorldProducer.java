package example.jndi;

import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class ActiveMQHelloWorldProducer implements Runnable {
    public void run() {
        try {
            // Create ConnectionFactory
//            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create ConnectionFactory
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.jms.TopicConnectionFactory activeMQConnectionFactory = (javax.jms.TopicConnectionFactory)ctx.lookup("ConnectionFactory");



            PooledConnectionFactory pooledJmsConnectionFactory = new PooledConnectionFactory();
//            pooledJmsConnectionFactory.setMaxConnections(50);
            System.out.println(pooledJmsConnectionFactory.getMaxConnections());

            pooledJmsConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
//            activeMQConnectionFactory.createTopicConnection();


            // Create Connection
//            Connection connection = activeMQConnectionFactory.createConnection();
            Connection connection = pooledJmsConnectionFactory.createConnection();
            connection.start();

            // Create Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("JavaHonk");

            // Create MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create messages
            String text = "Java Honk ActiveMQ Hello world! From: "+ Thread.currentThread().getName() + " : "+ this.hashCode();
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: " + message.hashCode()+ " : " + Thread.currentThread().getName());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
            producer.close();
            pooledJmsConnectionFactory.clear();
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e);
            e.printStackTrace();
        }
    }
}