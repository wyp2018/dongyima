package com.offcn.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*
* 队列消息接收
* 1.可以接收到消费者启动之前发送的消息(私聊你的时候你不在,下次上线就看到了)
* 2.一个消息只能被一个消费者接收
* */
public class QueueConsumer {

    public static void main(String[] args) throws Exception {

        // 1.创建连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.10.127.125:61616");
        // 2.获取连接
        Connection connection = connectionFactory.createConnection();
        // 3.启动连接
        connection.start();


        // 4.获取session (参数1：是否启动事务,参数2：消息确认模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


        // 5.创建队列对象
        Queue queue = session.createQueue("test-queue");


        // 6.创建消息接收者
        MessageConsumer consumer = session.createConsumer(queue);

        //7.建立消息监听: 匿名内部类
        consumer.setMessageListener(new MessageListener() {
            //接收到消息时,onMessage方法会自动触发
            public void onMessage(Message message) {
                 try {
                    TextMessage txt =(TextMessage) message;
                    String msg = txt.getText();
                     System.out.println("接收到的消息为:" + msg);
                } catch (JMSException e) {
                    e.printStackTrace();
                }


            }
        });

       /* // 9.关闭资源
        producer.close();
        session.close();
        connection.close();*/

       //让监听所在地不要退出 ,退出就听不到消息了
       Thread.sleep(100000);
    }


}
