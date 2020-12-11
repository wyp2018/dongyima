package com.offcn.test;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-*.xml")
public class TestProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue queue;
    @Autowired
    private ActiveMQTopic topic;

    @Test
    public void testQueue(){
        jmsTemplate.convertAndSend( queue,"队列信息:我是一个字符串信息");
    }

    @Test
    public void testTopic(){
        jmsTemplate.convertAndSend( topic,"主题信息:我是一个主题信息");
    }

}
