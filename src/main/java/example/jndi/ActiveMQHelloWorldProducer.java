package example.jndi;

import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class ActiveMQHelloWorldProducer implements Runnable {
    public void run() {
        try {

            // Create ConnectionFactory
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.jms.TopicConnectionFactory activeMQConnectionFactory = (javax.jms.TopicConnectionFactory) ctx.lookup("ConnectionFactory");

            PooledConnectionFactory pooledJmsConnectionFactory = new PooledConnectionFactory();
            pooledJmsConnectionFactory.setConnectionFactory(activeMQConnectionFactory);

            Connection connection = pooledJmsConnectionFactory.createConnection();
            connection.start();

            // Create Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("JavaRocks");

            // Create MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create messages
            String text = "Java Rock ActiveMQ Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
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