package com.offcn.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducer {

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


        // 6.创建消息生产者
        MessageProducer producer = session.createProducer(queue);



        // 7.创建消息:五种消息类型的其中一种:文本信息
        TextMessage textMessage = session.createTextMessage("欢迎来优就业学习");


        // 8.发送消息
        producer.send(textMessage);

        // 9.关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("发送队列信息完毕");
    }


}
