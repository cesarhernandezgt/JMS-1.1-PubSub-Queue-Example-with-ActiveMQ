package example.jndi;


public class ActiveMQHelloWorld {
    public static void main(String[] args) throws Exception {

        (new Thread(new ActiveMQHelloWorldProducer())).start();
        (new Thread(new ActiveMQHelloWorldProducer())).start();
        (new Thread(new HelloWorldConsumer())).start();
        Thread.sleep(5000);
        (new Thread(new HelloWorldConsumer())).start();
        (new Thread(new ActiveMQHelloWorldProducer())).start();
        (new Thread(new ActiveMQHelloWorldProducer())).start();
        (new Thread(new HelloWorldConsumer())).start();

    }
}
