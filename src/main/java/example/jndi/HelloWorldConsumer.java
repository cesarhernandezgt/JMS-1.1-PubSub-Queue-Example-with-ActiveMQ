package example.jndi;

import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.*;

public class HelloWorldConsumer implements Runnable,
        ExceptionListener {
    public void run() {
        try {

            // Create ConnectionFactory with JNDI
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.jms.TopicConnectionFactory activeMQConnectionFactory = (javax.jms.TopicConnectionFactory) ctx.lookup("ConnectionFactory");

            PooledConnectionFactory pooledActiveMQConnectionFactory = new PooledConnectionFactory();
            pooledActiveMQConnectionFactory.setConnectionFactory(activeMQConnectionFactory);

            Connection connection = pooledActiveMQConnectionFactory.createConnection();
            //            pooledActiveMQConnectionFactory.setMaxConnections(80);
            //            System.out.println(pooledActiveMQConnectionFactory.getMaxConnections());
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("JavaRocks");

            // Create a MessageConsumer from the Session to the Topic or
            // Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            Message message = consumer.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }

            consumer.close();
            session.close();
            connection.close();
            pooledActiveMQConnectionFactory.clear();
        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("ActiveMQ JMS Exception occured.  Shutting down client.");
    }
}